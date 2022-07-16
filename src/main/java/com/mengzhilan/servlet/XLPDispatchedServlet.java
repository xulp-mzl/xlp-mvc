package com.mengzhilan.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xlp.json.Json;
import org.xlp.utils.XLPCharsetUtil;
import org.xlp.utils.XLPPackingTypeUtil;

import com.mengzhilan.enumeration.RequestMethodType;
import com.mengzhilan.handler.ParameterNotExistException;
import com.mengzhilan.handler.RequestMappingInfoHandler;
import com.mengzhilan.mapping.RequestMappingInfo;
import com.mengzhilan.mapping.RequestMappingMap;
import com.mengzhilan.mapping.RequestPathUtils;

/**
 * Create by xlp on 2022/01/01
 *
 * 对@{@link com.mengzhilan.annotation.RequestMapping}路径映射拦截，并进行相应的处理
 */
public class XLPDispatchedServlet extends HttpServlet {
    private static final long serialVersionUID = -8887930386546188053L;

    /**
     * Tomcat的默认servlet名称
     */
    public static final String COMMON_DEFAULT_SERVLET_NAME = "default";

    /**
     * 标记是否在web.xml中配置了用默认servlet处理静态资源参数名称
     */
    public static final String HAS_CONFIGURE_DDEFAULT_SERVLET_PARAETER = "configDefault";

    /**
     * 标记是否在web.xml中配置了用默认servlet处理静态资源参数,值为true时，配置了，否则没有配置
     */
    public boolean hasConfiguredDefaultServlet = false;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String initParameter = config.getInitParameter(HAS_CONFIGURE_DDEFAULT_SERVLET_PARAETER);
        hasConfiguredDefaultServlet = "true".equals(initParameter);
    }

    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        //获取请求方式
        String requestMethod = httpServletRequest.getMethod().toUpperCase(Locale.ENGLISH);
        //把请求方式转换成枚举类型
        RequestMethodType requestMethodType = RequestMethodType.valueOf(requestMethod);
        //项目根路经
        String contextPath = httpServletRequest.getContextPath();
        //访问链接uri
        String uri = httpServletRequest.getRequestURI();
        uri = URLDecoder.decode(uri, XLPCharsetUtil.UTF8);
        //访问路径key
        String path = RequestPathUtils.removeContextPathFromUri(contextPath, uri);
        if (path.endsWith("/")){
            path = path.substring(0, path.length() -1);
        }
        String pathKey = path +  "_" + requestMethodType.name();
        RequestMappingInfo info = RequestMappingMap.getRequestMappingInfo(pathKey);
        if(info == null){
            if (RequestMappingMap.getPathSet().contains(path)) {
                httpServletResponse.sendError(403, "不支持[" + requestMethod + "]请求方式！");
                return;
            }

            RequestMappingMap.PathVariableFindInfo findInfo = RequestMappingMap.findPathVariableMapping(path, requestMethodType);
            int flag = findInfo.getFlag();
            if (flag == RequestMappingMap.PATH_EQUAL_NO_USE){
                httpServletResponse.sendError(403, "不支持[" + requestMethod + "]请求方式！");
                return;
            }
            if (flag == RequestMappingMap.PATH_EQUAL) {
                info = findInfo.getRequestMappingInfo();
            }
        }

        if (info != null){
            try {
                Object value = new RequestMappingInfoHandler().handle(httpServletRequest, httpServletResponse, info);
                if (value != null){
                	 String response;
                     Class<?> cs = value.getClass();
                     // 判断返回值是否是数字类型，或基础类型，或包装类型，或字符串类型
                     if (XLPPackingTypeUtil.isNumber(value)
                         || XLPPackingTypeUtil.isOtherRawOrPackingType(cs)
                         || value instanceof CharSequence
                         || value instanceof Json){
                         response = value.toString();
                     } else {
                         response = Json.toJsonString(value);
                     }
                     httpServletResponse.getWriter().write(response);
                }
            } catch (ParameterNotExistException e){
                httpServletResponse.sendError(400, e.getMessage());
            }
        } else {
            //配置了用默认servlet处理静态资源参数, 则未找到所要访问的资源，直接返回404
            if (hasConfiguredDefaultServlet){
                httpServletResponse.sendError(404);
            } else {
                //未配置了用默认servlet处理静态资源参数, 则未找到所要访问的资源，则用默认servlet处理
                httpServletRequest.getServletContext().getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME)
                        .forward(httpServletRequest, httpServletResponse);
            }
        }
    }
}
