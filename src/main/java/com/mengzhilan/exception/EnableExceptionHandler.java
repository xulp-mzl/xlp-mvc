package com.mengzhilan.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>创建时间：2022年7月16日 下午3:45:28</p>
 * @author xlp
 * @version 1.0 
 * @Description 标记是否开启Controller异常处理功能
*/
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
@Documented
public @interface EnableExceptionHandler {
}
