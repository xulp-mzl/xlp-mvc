package com.mengzhilan.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>创建时间：2022年7月16日 下午4:07:00</p>
 * @author xlp
 * @version 1.0 
 * @Description 标记是否增强类方法执行功能注解
*/
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
@Documented
public @interface EnableMethodEnhanceService {
}
