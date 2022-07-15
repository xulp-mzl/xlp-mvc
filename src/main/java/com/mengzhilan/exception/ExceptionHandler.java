package com.mengzhilan.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>创建时间：2022年7月15日 下午11:50:16</p>
 * @author xlp
 * @version 1.0 
 * @Description controller异常处理注解
*/

@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.TYPE})//定义注解的作用目标**作用范围字段 （作用在类或接口上）
@Documented//说明该注解将被包含在javadoc中
public @interface ExceptionHandler {
	/**
	 * 异常处理类
	 */
	Class<? extends IExceptionHandler> value() default DefaultExceptionHandler.class;
	
	/**
	 * 异常处理类全路径
	 */
	String exceptionHandlerClassName() default "";
}
