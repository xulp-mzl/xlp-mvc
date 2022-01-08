package com.mengzhilan.annotation;

import java.lang.annotation.*;

/**
 * Create by xlp on 2021/12/11
 * 设置响应字符编码
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.METHOD})//定义注解的作用目标**作用范围字段 （作用在类或接口上）
@Documented//说明该注解将被包含在javadoc中
public @interface ResponseCharset {
    /**
     * 响应编码
     */
    String value() default "";
}
