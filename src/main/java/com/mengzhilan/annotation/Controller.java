package com.mengzhilan.annotation;

import java.lang.annotation.*;

/**
 * 控制器
 *
 * @author 徐龙平
 *         <p>
 *         2021-06-10
 *         </p>
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.TYPE})//定义注解的作用目标**作用范围字段 （作用在类或接口上）
@Documented//说明该注解将被包含在javadoc中
public @interface Controller {
    /**
     * 描述
     */
    String description() default "";
}
