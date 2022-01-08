package com.mengzhilan.handler;

import com.mengzhilan.annotation.*;
import com.mengzhilan.mapping.RequestMappingInfo;
import com.mengzhilan.mapping.RequestPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlp.assertion.AssertUtils;
import org.xlp.javabean.annotation.Bean;
import org.xlp.utils.XLPCharsetUtil;
import org.xlp.utils.XLPStringUtil;
import org.xlp.utils.io.XLPIOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Create by xlp on 2021/12/23
 *
 * 通过指定函数参数注解解析函数参数值
 */
public class ValueHandlerBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(ValueHandlerBuilder.class);

    /**
     * 函数参数信息
     */
    private ParameterInfoBuilder.ParameterInfo parameterInfo;

    /**
     * RequestMapping注解映射路径
     */
    private String path;

    /**
     * 存储函数参数值
     */
    private Object parameterValue;

    private HttpServletRequest request;

    /**
     * 请求字符集
     */
    private String requestCharsetName;

    private ValueHandlerBuilder(RequestMappingInfo mappingInfo, ParameterInfoBuilder.ParameterInfo parameterInfo,
                                HttpServletRequest request){
        this.parameterInfo = parameterInfo;
        this.path = RequestPathUtils.mergePath(mappingInfo.getClassPath(), mappingInfo.getMethodPath());
        this.request = request;
        this.requestCharsetName = request.getCharacterEncoding();
    }

    /**
     * 构造ValueHandlerBuilder对象
     *
     * @param mappingInfo RequestMapping注解映射路径信息
     * @param parameterInfo 函数参数信息
     * @return
     * @throws NullPointerException 假如参数为null，则抛出该异常
     */
    public static ValueHandlerBuilder builder(RequestMappingInfo mappingInfo, ParameterInfoBuilder.ParameterInfo parameterInfo,
                                              HttpServletRequest request){
        AssertUtils.isNotNull(mappingInfo, "mappingInfo parameter is null!");
        AssertUtils.isNotNull(parameterInfo, "parameterInfo parameter is null!");
        AssertUtils.isNotNull(request, "request parameter is null!");
        return new ValueHandlerBuilder(mappingInfo, parameterInfo, request);
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestParam}注解参数值
     *
     * @return
     */
    public ValueHandlerBuilder requestParam(){
        if (RequestParam.class == parameterInfo.getAnnotationClass()){
            String[] parameterValues = request.getParameterValues(parameterInfo.getName());
            parameterValue = ValueUtils.StringValuesConvert(parameterValues, parameterInfo.getParameterClass());
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestHeader}注解参数值
     *
     * @return
     */
    public ValueHandlerBuilder requestHeader(){
        if (RequestHeader.class == parameterInfo.getAnnotationClass()){
            Enumeration<String> enumeration = request.getHeaders(parameterInfo.getName());
            List<String> headerList = new ArrayList<>();
            while (enumeration.hasMoreElements()){
                headerList.add(enumeration.nextElement());
            }
            parameterValue = ValueUtils.StringValuesConvert(
                    headerList.toArray(new String[0]), parameterInfo.getParameterClass());
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.PathVariable}注解参数值
     *
     * @return
     */
    public ValueHandlerBuilder pathVariable(){
        if (PathVariable.class == parameterInfo.getAnnotationClass()){
            //项目根路经
            String contextPath = request.getContextPath();
            //访问链接uri
            String uri = request.getRequestURI();
            try {
                uri = URLDecoder.decode(uri, XLPCharsetUtil.UTF8);
            } catch (UnsupportedEncodingException e) {
                if (LOGGER.isErrorEnabled()){
                    LOGGER.error("", e);
                }
            }
            //访问路径
            uri = RequestPathUtils.removeContextPathFromUri(contextPath, uri);
            parameterValue = ValueUtils.StringValuesConvert(new String[]{parseFromUri(uri, path, parameterInfo.getName())},
                    parameterInfo.getParameterClass());
        }
        return this;
    }

    /**
     * 从给定的uri中解析出指定参数名称对应的值
     *
     * @param uri uri
     * @param path @{@link com.mengzhilan.annotation.RequestMapping}映射路径
     * @param name 参数名称
     * @return
     */
    private String parseFromUri(String uri, String path, String name) {
        String[] paths1 = uri.split("/");
        String[] paths2 = path.split("/");
        String[] names;
        int startIndex, endIndex;
        //非${}占位的字符串
        List<String> notList = new ArrayList<>();
        boolean hasName = false;
        int nameIndex = -1;
        for (int i = 0, len = paths1.length; i < len; i++) {
            endIndex = 0;
            //判断controller中RequestMapping的路径是否包含${[A-Za-z]+[A-Za-z0-9]*}值
            names = XLPStringUtil.findSubStrings(paths2[i], "\\{[A-Za-z]+[A-Za-z0-9]*\\}");
            if ( names.length == 0) {
                continue;
            }
            if (names[0].length() == paths2[i].length()) {
                if (names[0].equals("{" + name + "}"))
                    return paths1[i];
                continue;
            }
            for (int j = 0, len1 = names.length; j < len1; j++) {
                if (!hasName){
                    hasName = names[j].equals("{" + name + "}");
                    //记录{}占位符对应参数名称的位置
                    nameIndex = j;
                }
                //{}占位符对的开始位置
                startIndex = paths2[i].indexOf(names[j], endIndex);
                //存储{}占位符两边的字符
                notList.add(paths2[i].substring(endIndex, startIndex));
                //{}占位符对应的结束位置
                endIndex = startIndex + names[j].length();
            }
            notList.add(paths2[i].substring(endIndex));
            //假如该部分有指定参数名称的{}占位符，则从访问uri对应部分解析出该参数对应的值
            if (hasName){
                //记录{}占位符参数名称对应的值，在访问uri对应部分开始解析的起始位置
                int _startIndex = 0;
                String s;
                for (int i1 = 0; i1 <= nameIndex; i1++) {
                    s = notList.get(i1);
                    if (s.isEmpty()) continue;
                    _startIndex = paths1[i].indexOf(s, _startIndex);
                    if (_startIndex < 0) return null;
                    _startIndex += s.length();
                }
                //记录{}占位符参数名称对应的值，在访问uri对应部分开始解析的结束位置
                int _endIndex = 0;
                for (int i1 = nameIndex + 1; i1 < notList.size(); i1++) {
                    s = notList.get(i1);
                    if (s.isEmpty()) continue;
                    _endIndex = paths1[i].indexOf(s, _startIndex);
                    if (_endIndex < 0) return null;
                    break;
                }
                //截取{}占位符参数名称对应的值
                return paths1[i].substring(_startIndex, _endIndex == 0 ? paths1[i].length() : _endIndex);
            }
        }
        return null;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestBody}注解参数值
     *
     * @return
     * @throws ValueHandlerException 假如读取请求体中的内容出错时，抛出该异常
     */
    public ValueHandlerBuilder requestBody(){
        if (RequestBody.class == parameterInfo.getAnnotationClass()){
            String body;
            try(InputStream inputStream = request.getInputStream()){
                body = XLPIOUtil.toString(inputStream,false, requestCharsetName);
            } catch (IOException e) {
                throw new ValueHandlerException(e);
            }
            parameterValue = ValueUtils.requestBodyConvert(body, parameterInfo.getParameterClass());
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestBean}注解参数值
     *
     * @return
     */
    public ValueHandlerBuilder requestBean(){
        if (RequestBean.class == parameterInfo.getAnnotationClass()){
            Class<?> parameterClass = parameterInfo.getParameterClass();
            Bean bean = parameterClass.getAnnotation(Bean.class);
            parameterValue = null;
            if (bean != null){
                Map<String, String[]> parameterMap = request.getParameterMap();
                parameterValue = ValueUtils.parameterMapConvertBean(parameterMap, parameterInfo.getName(),
                        parameterClass);
            }
        }
        return this;
    }

    /**
     * 获取参数值
     * @return
     */
    public Object getParameterValue(){
        return parameterValue;
    }
}
