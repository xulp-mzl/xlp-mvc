package com.mengzhilan.handler;

import java.lang.reflect.Parameter;

/**
 * Create by xlp on 2021/12/20
 *
 * 函数参数操作工具类
 */
class ParameterUtils {
    /**
     * 获取函数参数信息，即@{@link com.mengzhilan.annotation.RequestParam}等注解标注的参数信息，
     * 为接下来解析@{@link com.mengzhilan.annotation.RequestMapping}映射做准备
     *
     * @param parameter
     * @return ParameterInfo对象
     */
    static ParameterInfoBuilder.ParameterInfo getParameterInfo(Parameter parameter){
       return ParameterInfoBuilder.builder(parameter).requestParam()
               .requestHeader().pathVariable()
               .requestBody().requestBean().getParameterInfo();
    }


}
