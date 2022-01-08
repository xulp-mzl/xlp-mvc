package com.mengzhilan.mapping;

import com.mengzhilan.annotation.Controller;
import com.mengzhilan.annotation.RequestMapping;
import com.mengzhilan.enumeration.RequestMethodType;
import org.xlp.scanner.pkg.ClassPathPkgScanner;
import org.xlp.utils.XLPStringUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Create by xlp on 2021/6/19
 *
 * 存储@RequestMapping的映射信息
 */
public class RequestMappingMap {
    /**
     * 存储control信息
     */
    private static Map<String, RequestMappingInfo> requestMappingInfoMap = new HashMap<>();

    /**
     * 存储所有@{@link RequestMapping}的路径
     */
    private static Set<String> pathSet = new HashSet<>();

    /**
     * 相等
     */
    public static int PATH_EQUAL = 0;

    /**
     * 不等
     */
    public static int PATH_NO_EQUAL = 1;

    /**
     * 不可访问
     */
    public static int PATH_EQUAL_NO_USE = 2;

    /**
     * 获取所有的@{@link RequestMapping}的路径
     *
     * @return
     */
    public static Set<String> getPathSet() {
        return pathSet;
    }

    /**
     * 添加RequestMapping的映射信息
     *
     * @param key path路径
     * @param info <code>RequestMappingInfo</code>对象
     * @return
     * @throws RepeatLoadPathException 假如路径被重复加载，则抛出该异常
     */
    static void addRequestMappingInfo(String key, RequestMappingInfo info){
        if (!XLPStringUtil.isEmpty(key) && info != null){
            RequestMappingInfo tempInfo = requestMappingInfoMap.get(key);
            if (tempInfo != null){
                Class<?> cs = tempInfo.getControllerClass();
                Class<?> cs1 = info.getControllerClass();
                if (cs.isAssignableFrom(cs1)){
                    requestMappingInfoMap.put(key, info);
                } else if (!cs1.isAssignableFrom(cs)){
                    int index = key.lastIndexOf("_");
                    String path = index >= 0 ? key.substring(0, index) : key;
                    throw new RepeatLoadPathException(path + "该路径被重复加载！");
                }
            } else {
                requestMappingInfoMap.put(key, info);
                pathSet.add(RequestPathUtils.mergePath(info.getClassPath(), info.getMethodPath()));
            }
        }
    }

    /**
     * 加载所有的controller注解类信息
     *
     * @param classLoader
     * @param packagePath
     */
    public static void loadAllController(ClassLoader classLoader, String packagePath) throws IOException {
        String[] packagePaths = XLPStringUtil.emptyTrim(packagePath).split(",");
        ClassPathPkgScanner scanner = new ClassPathPkgScanner(classLoader);
        Set<Class<?>> classes = new HashSet<>();
        for (String path : packagePaths) {
            classes.addAll(scanner.scannerToClass(path));
        }

        for (Class<?> cs : classes) {
            createRequestMappingInfo(cs);
        }
    }

    /**
     * 创建RequestMappingInfo对象
     *
     * @param cs
     */
    private static  void createRequestMappingInfo(Class<?> cs) {
        //把controller注解类解析成RequestMappingInfo对象
        Controller controller = cs.getAnnotation(Controller.class);
        //是否是抽象类
        boolean isAbs = Modifier.isAbstract(cs.getModifiers());
        //包含Controller注解并且该类不是接口或抽象类，则进行处理
        if (controller != null && !isAbs){
            RequestMapping requestMapping = cs.getAnnotation(RequestMapping.class);
            String classPath = "";
            RequestMethodType[] methodTypes = {RequestMethodType.ALL};
            if (requestMapping != null){
                classPath = requestMapping.value();
                methodTypes = requestMapping.method();
            }
            methodTypes = MethodTypeUtils.convert(methodTypes);
            Method[] methods = cs.getMethods();
            RequestMappingInfo requestMappingInfo;
            String methodPath, pathKey, completedPath;
            RequestMethodType[] methodTypes1;
            for (Method method : methods) {
                //获取RequestMapping注解函数
                requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null){
                    String methdName = method.getName();
                    Class<?>[] paramClass = method.getParameterTypes();
                    Parameter[] parameters = method.getParameters();
                    methodPath = requestMapping.value();
                    completedPath = RequestPathUtils.mergePath(classPath, methodPath);
                    methodTypes1 = requestMapping.method();
                    methodTypes1 = methodTypes1[0] == RequestMethodType.ALL ? methodTypes
                           : MethodTypeUtils.convert(methodTypes1);
                    for (RequestMethodType requestMethodType : methodTypes1) {
                        pathKey = completedPath + "_" + requestMethodType.name();
                        requestMappingInfo = new RequestMappingInfo();
                        requestMappingInfo.setAnnotation(requestMapping);
                        requestMappingInfo.setClassPath(classPath);
                        requestMappingInfo.setControllerClass(cs);
                        requestMappingInfo.setMethodPath(methodPath);
                        requestMappingInfo.setRequestMethodType(requestMethodType);
                        requestMappingInfo.setMethodName(methdName);
                        requestMappingInfo.setMethodParams(paramClass);
                        requestMappingInfo.setParameters(parameters);
                        addRequestMappingInfo(pathKey, requestMappingInfo);
                    }
                }
            }
        }
    }

    /**
     * 获取RequestMappingInfo对象
     *
     * @param key
     * @return
     */
    public static RequestMappingInfo getRequestMappingInfo(String key){
        return requestMappingInfoMap.get(key);
    }

    /**
     * 查找指定uri对应的PathVariableMapping信息
     *
     * @param requestUri 格式是uri_RequestType
     * @param requestMethodType 请求方式
     * @return 返回PathVariableFindInfo对象，记录了@PathVariable注解映射信息，该函数不会返回null
     */
    public static PathVariableFindInfo findPathVariableMapping(String requestUri, RequestMethodType requestMethodType){
        if (XLPStringUtil.isEmpty(requestUri) || requestMethodType == null){
            return new PathVariableFindInfo(PATH_NO_EQUAL, null);
        }

        RequestMappingInfo requestMappingInfo;
        String prefixKey;
        for (String key : requestMappingInfoMap.keySet()) {
            int index = key.lastIndexOf("_");
            prefixKey = key.substring(0, index);
            boolean equals = RequestPathUtils.comparePath(requestUri, prefixKey);
            requestMappingInfo = requestMappingInfoMap.get(key);
            if (equals){
                if (requestMethodType == requestMappingInfo.getRequestMethodType()) {
                    return new PathVariableFindInfo(PATH_EQUAL, requestMappingInfo);
                }
                requestMappingInfo = requestMappingInfoMap.get(prefixKey + "_" + requestMethodType.name());
                if (requestMappingInfo == null){
                    return new PathVariableFindInfo(PATH_EQUAL_NO_USE, null);
                }
                return new PathVariableFindInfo(PATH_EQUAL, requestMappingInfo);
            }
        }
        //判断是否找到对应的
        return new PathVariableFindInfo(PATH_NO_EQUAL, null);
    }

    /**
     * 处理@PathVariable参数映射
     */
    public static class PathVariableFindInfo{
        /**
         * 记录@PathVariable与@RequestMapping是否可匹配，默认不匹配
         */
        private int flag = PATH_NO_EQUAL;

        /**
         * 映射信息
         */
        private RequestMappingInfo requestMappingInfo;

        public PathVariableFindInfo(){

        }

        public PathVariableFindInfo(int flag, RequestMappingInfo requestMappingInfo) {
            this.flag = flag;
            this.requestMappingInfo = requestMappingInfo;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public RequestMappingInfo getRequestMappingInfo() {
            return requestMappingInfo;
        }

        public void setRequestMappingInfo(RequestMappingInfo requestMappingInfo) {
            this.requestMappingInfo = requestMappingInfo;
        }
    }
}
