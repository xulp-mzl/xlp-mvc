package com.mengzhilan.enumeration;

/**
 * Create by xlp on 2021/6/10<br/>
 * 请求类型
 *
 * @version 1.0
 * @since 1.0
 *
 */
public enum RequestMethodType {
    /**
     * POST：向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的创建和/或已有资源的修改。
     */
    POST,

    /**
     * GET：向特定的资源发出请求。
     */
    GET,

    /**
     * DELETE：请求服务器删除Request-URI所标识的资源。
     */
    DELETE,

    /**
     * PUT：向指定资源位置上传其最新内容。
     */
    PUT,

    /**
     * OPTIONS：返回服务器针对特定资源所支持的HTTP请求方法。也可以利用向web服务器发送'*'的请求来测试服务器的功能性。
     */
    OPTIONS,

    /**
     * TRACE：回显服务器收到的请求，主要用于测试或诊断。
     */
    TRACE,

    /**
     * CONNECT：HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。
     */
    CONNECT,

    /**
     * HEAD：向服务器索要与GET请求相一致的响应，只不过响应体将不会被返回。这一方法可以在不必传输整个响应内容的情况下，就可以获取包含在响应消息头中的元信息。
     */
    HEAD,

    /**
     * 所有请求方式
     */
    ALL
}
