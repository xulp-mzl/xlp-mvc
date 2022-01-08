package com.mengzhilan.handler;

import com.mengzhilan.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * Create by xlp on 2021/12/21
 *
 * 获取函数参数信息，即@{@link com.mengzhilan.annotation.RequestParam}等注解标注的参数信息构建类
 */
public class ParameterInfoBuilder {
    /**
     * 函数参数对象信息
     */
    private ParameterInfo parameterInfo;

    /**
     * 标记是否已经创建了ParameterInfo对象
     */
    private boolean hasCreateParameterInfo;

    /**
     * 参数对象
     */
    private Parameter parameter;

    /**
     * @param parameter
     */
    private ParameterInfoBuilder(Parameter parameter){
        hasCreateParameterInfo = (parameter == null);
        this.parameter = parameter;
    }

    /**
     * 创建<code>ParameterInfoBuilder</code>对象
     *
     * @param parameter
     * @return
     */
    public static ParameterInfoBuilder builder(Parameter parameter){
        return new ParameterInfoBuilder(parameter);
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestParam}注解参数信息
     *
     * @return
     */
    public ParameterInfoBuilder requestParam(){
        if (!hasCreateParameterInfo){
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestParam != null){
                String parameterName = requestParam.value().trim();
                parameterName = parameterName.isEmpty() ? parameter.getName() : parameterName;
                parameterInfo = new ParameterInfo(RequestParam.class, parameterName, requestParam.required(),
                        parameter.getType());
                hasCreateParameterInfo = true;
            }
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestHeader}注解参数信息
     *
     * @return
     */
    public ParameterInfoBuilder requestHeader(){
        if (!hasCreateParameterInfo){
            RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
            if (requestHeader != null){
                String parameterName = requestHeader.value().trim();
                parameterName = parameterName.isEmpty() ? parameter.getName() : parameterName;
                parameterInfo = new ParameterInfo(RequestHeader.class, parameterName, requestHeader.required(),
                        parameter.getType());
                hasCreateParameterInfo = true;
            }
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.PathVariable}注解参数信息
     *
     * @return
     */
    public ParameterInfoBuilder pathVariable(){
        if (!hasCreateParameterInfo){
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (pathVariable != null){
                String parameterName = pathVariable.value().trim();
                parameterName = parameterName.isEmpty() ? parameter.getName() : parameterName;
                parameterInfo = new ParameterInfo(PathVariable.class, parameterName, pathVariable.required(),
                        parameter.getType());
                hasCreateParameterInfo = true;
            }
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestBody}注解参数信息
     *
     * @return
     */
    public ParameterInfoBuilder requestBody(){
        if (!hasCreateParameterInfo){
            RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
            if (requestBody != null){
                String parameterName = parameter.getName();
                parameterInfo = new ParameterInfo(RequestBody.class, parameterName, requestBody.required(),
                        parameter.getType());
                hasCreateParameterInfo = true;
            }
        }
        return this;
    }

    /**
     * 解析@{@link com.mengzhilan.annotation.RequestBean}注解参数信息
     *
     * @return
     */
    public ParameterInfoBuilder requestBean(){
        if (!hasCreateParameterInfo){
            RequestBean requestBean = parameter.getAnnotation(RequestBean.class);
            if (requestBean != null){
                String parameterName = requestBean.value().trim();
                parameterName = parameterName.isEmpty() ? parameter.getName() : parameterName;
                parameterInfo = new ParameterInfo(RequestBean.class, parameterName, requestBean.required(),
                        parameter.getType());
                hasCreateParameterInfo = true;
            }
        }
        return this;
    }

    /**
     * 获取函数参数信息对象
     *
     * @return
     */
    public ParameterInfo getParameterInfo(){
        return parameterInfo;
    }

    /**
     * 函数参数信息
     */
    public static class ParameterInfo{
        /**
         * 函数参数注解对象
         */
        private Class<? extends Annotation> annotationClass;
        /**
         * 函数参数名称
         */
        private String name;

        /**
         * 该参数对应的值是否不能为空
         */
        private boolean required = false;

        /**
         * 函数参数类型
         */
        private Class<?> parameterClass;

        public ParameterInfo(){}

        /**
         * 构造函数
         *
         * @param annotationClass
         * @param name
         * @param required
         */
        public ParameterInfo(Class<? extends Annotation> annotationClass, String name, boolean required, Class<?> parameterClass) {
            this.annotationClass = annotationClass;
            this.name = name;
            this.required = required;
            this.parameterClass = parameterClass;
        }

        public Class<? extends Annotation> getAnnotationClass() {
            return annotationClass;
        }

        public void setAnnotation(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public Class<?> getParameterClass() {
            return parameterClass;
        }

        public void setParameterClass(Class<?> parameterClass) {
            this.parameterClass = parameterClass;
        }
    }
}
