package com.mengzhilan.handler;

import com.mengzhilan.annotation.RequestCharset;
import com.mengzhilan.annotation.ResponseCharset;
import com.mengzhilan.mapping.RequestMappingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlp.utils.XLPStringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Create by xlp on 2021/12/19
 * RequestMappingInfo处理器
 */
public class RequestMappingInfoHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMappingInfoHandler.class);

    /**
     * Controller请求处理
     *
     * @param request 请求
     * @param response 响应
     * @param info 请求映射信息
     * @throws RequestMappingInfoHandlerException 假如处理失败，则抛出该异常
     * @throws ParameterNotExistException 假如请求参数不存在，则抛出该异常
     * @return @Controller注解类函数返回值
     */
    @Override
    public Object handle(HttpServletRequest request, HttpServletResponse response, RequestMappingInfo info) {
        if (request == null || response == null || info == null){
            if (LOGGER.isWarnEnabled()){
                LOGGER.warn("RequestMappingInfoHandler.handle 存在为null的参数。");
            }
            return null;
        }

        Object instance;
        try {
            instance = info.getControllerClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            if (LOGGER.isErrorEnabled()){
                LOGGER.error(info + "处理异常", e);
            }
            throw new RequestMappingInfoHandlerException(info + "处理异常", e);
        }

        Method method;
        try {
            method = info.getControllerClass().getMethod(info.getMethodName(),
                    info.getMethodParams());
        } catch (NoSuchMethodException e) {
            if (LOGGER.isErrorEnabled()){
                LOGGER.error(info + "处理异常", e);
            }
            throw new RequestMappingInfoHandlerException(info + "处理异常", e);
        }
        //设置请求和响应编码
        setResAndRespCharsetName(request, response, method);

        Parameter[] parameters = info.getParameters();
        Class<?>[] parameterClass = info.getMethodParams();
        Object[] values = new Object[parameterClass.length];
        ParameterInfoBuilder.ParameterInfo parameterInfo;
        for (int i = 0, len = parameterClass.length; i < len; i++) {
            //排除HttpServletResponse和HttpServletRequest
            if (parameterClass[i].isAssignableFrom(HttpServletResponse.class)){
                values[i] = response;
                continue;
            }
            if (parameterClass[i].isAssignableFrom(HttpServletRequest.class)){
                values[i] = request;
                continue;
            }
            parameterInfo = ParameterUtils.getParameterInfo(parameters[i]);
            if (parameterInfo == null){
                values[i] = ValueUtils.PRIMITIVE_DEFAULTS.get(parameterClass[i]);
                continue;
            }

            boolean required = parameterInfo.isRequired();
            values[i] = ValueHandlerBuilder.builder(info, parameterInfo, request)
                    .requestParam().requestHeader().pathVariable()
                    .requestBody().requestBean().getParameterValue();
            //判断参数是否必须补位null
            if (required && values[i] == null){
                throw new ParameterNotExistException("[" + parameterInfo.getName() + "]该参数必须传值！");
            }
        }

        Object returnValue;
        try {
            returnValue = method.invoke(instance, values);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (LOGGER.isErrorEnabled()){
                LOGGER.error(info + "处理异常", e);
            }
            throw new RequestMappingInfoHandlerException(info + "处理异常", e);
        }
        return returnValue;
    }

    /**
     * 设置请求和响应编码
     *
     * @param request
     * @param response
     * @param method
     */
    private void setResAndRespCharsetName(HttpServletRequest request, HttpServletResponse response, Method method) {
        RequestCharset requestCharset = method.getAnnotation(RequestCharset.class);
        String charsetName;
        if (requestCharset != null && !XLPStringUtil.isEmpty(charsetName = requestCharset.value())){
            try {
                request.setCharacterEncoding(charsetName);
            } catch (UnsupportedEncodingException e) {
                if (LOGGER.isWarnEnabled()){
                    LOGGER.warn("设置请求编码失败", e);
                }
            }
        }

        ResponseCharset responseCharset = method.getAnnotation(ResponseCharset.class);
        if (responseCharset != null && !XLPStringUtil.isEmpty(charsetName = responseCharset.value())){
            response.setCharacterEncoding(charsetName);
            response.setContentType("text/html;charset=" + charsetName);
        }
    }
}
