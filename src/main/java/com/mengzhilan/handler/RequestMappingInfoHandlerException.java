package com.mengzhilan.handler;

/**
 * Create by xlp on 2021/12/19
 *
 * RequestMappingInfoHandler处理异常
 */
public class RequestMappingInfoHandlerException extends RuntimeException{
    private static final long serialVersionUID = 2397699277552083676L;

    public RequestMappingInfoHandlerException(String message) {
        super(message);
    }

    public RequestMappingInfoHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestMappingInfoHandlerException(Throwable cause) {
        super(cause);
    }
}
