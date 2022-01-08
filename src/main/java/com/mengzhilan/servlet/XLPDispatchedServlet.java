package com.mengzhilan.servlet;

import com.mengzhilan.enumeration.RequestMethodType;
import com.mengzhilan.handler.ParameterNotExistException;
import com.mengzhilan.handler.RequestMappingInfoHandler;
import com.mengzhilan.mapping.RequestMappingInfo;
import com.mengzhilan.mapping.RequestMappingMap;
import com.mengzhilan.mapping.RequestPathUtils;
import org.xlp.utils.XLPCharsetUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * Create by xlp on 2022/01/01
 *
 * 对@{@link com.mengzhilan.annotation.RequestMapping}路径映射拦截，并进行相应的处理
 */
public class XLPDispatchedServlet extends HttpServlet {
    private static final long serialVersionUID = -8887930386546188053L;

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
                    httpServletResponse.getWriter().write(value.toString());
                }
            } catch (ParameterNotExistException e){
                httpServletResponse.sendError(400, e.getMessage());
            }
        } else {
            httpServletResponse.sendError(404);
        }
    }
}
