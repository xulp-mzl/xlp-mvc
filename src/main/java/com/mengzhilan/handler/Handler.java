package com.mengzhilan.handler;

import com.mengzhilan.mapping.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by xlp on 2021/7/4
 * 请求处理器接口
 */
public interface Handler {
    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param info
     * @return @Controller注解类函数返回值
     */
    Object handle(HttpServletRequest request, HttpServletResponse response, RequestMappingInfo info);
}
