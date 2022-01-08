package com.mengzhilan.mapping;

import com.mengzhilan.enumeration.RequestMethodType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Create by xlp on 2021/6/19
 *
 * 存储每个@RequestMapping的映射信息
 */
public class RequestMappingInfo {
    /**
     * @Controller注解的类
     */
    private Class<?> controllerClass;

    /**
     * 存储Controller注解类上的path
     */
    private String classPath;

    /**
     * 存储Controller注解类method上的path
     */
    private String methodPath;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法参数类型
     */
    private Class<?>[] methodParams;

    /**
     * 方法参数对象
     */
    private Parameter[] parameters;

    /**
     * 请求方式
     */
    private RequestMethodType requestMethodType;

    /**
     * 方法上的注解类型
     */
    private Annotation annotation;

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(Class<?>[] methodParams) {
        this.methodParams = methodParams;
    }

    public RequestMethodType getRequestMethodType() {
        return requestMethodType;
    }

    public void setRequestMethodType(RequestMethodType requestMethodType) {
        this.requestMethodType = requestMethodType;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RequestMappingInfo{" +
                "controllerClass=" + controllerClass +
                ", classPath='" + classPath + '\'' +
                ", methodPath='" + methodPath + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodParams=" + Arrays.toString(methodParams) +
                ", parameters=" + Arrays.toString(parameters) +
                ", requestMethodType=" + requestMethodType +
                ", annotation=" + annotation +
                '}';
    }
}
