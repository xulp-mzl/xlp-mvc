package com.mengzhilan.handler;

/**
 * Create by xlp on 2021/12/26
 */
public class ValueHandlerException extends RuntimeException {
    private static final long serialVersionUID = -8939064312461098448L;

    public ValueHandlerException(String message) {
        super(message);
    }

    public ValueHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueHandlerException(Throwable cause) {
        super(cause);
    }
}
