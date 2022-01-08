package com.mengzhilan.mapping;

/**
 * Create by xlp on 2021/12/9
 * 路径重复加载异常
 */
public class RepeatLoadPathException extends RuntimeException {
    private static final long serialVersionUID = -9109679086869772398L;

    public RepeatLoadPathException(String message) {
        super(message);
    }

    public RepeatLoadPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
