package com.mengzhilan.annotation;

import java.lang.annotation.*;

/**
 * Create by xlp on 2021/6/12
 *
 * 请求头注解
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.PARAMETER})//定义注解的作用目标**作用范围字段 （作用在类或接口上或函数上）
@Documented//说明该注解将被包含在javadoc中
public @interface RequestHeader {
    /**
     * 描述
     */
    String description() default "";

    /**
     * 参数名称
     */
    String value() default "";

    /**
     * 标记参数是否必须有，默认不是必须有
     */
    boolean required() default false;
}
