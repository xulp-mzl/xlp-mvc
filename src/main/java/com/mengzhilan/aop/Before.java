package com.mengzhilan.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>创建时间：2022年7月16日 下午1:03:09</p>
 * @author xlp
 * @version 1.0 
 * @Description 函数处理前要执行另一些操作注解
*/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Before {
	/**
	 * 函数执行前要执行的实现类
	 */
	Class<? extends IBefore> value() default IBefore.class;
	
	/**
	 *函数执行前要执行的实现类全路径
	 */
	String beforeClassName() default "";
}
