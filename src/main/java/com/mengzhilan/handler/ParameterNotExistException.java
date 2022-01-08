package com.mengzhilan.handler;

/**
 * Create by xlp on 2021/12/26
 *
 * 请求参数不存在，可以抛出该异常
 */
public class ParameterNotExistException extends RuntimeException{
    private static final long serialVersionUID = -20968942241600709L;

    public ParameterNotExistException(String message) {
        super(message);
    }

    public ParameterNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
