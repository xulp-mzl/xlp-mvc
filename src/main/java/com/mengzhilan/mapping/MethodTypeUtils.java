package com.mengzhilan.mapping;

import com.mengzhilan.enumeration.RequestMethodType;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xlp on 2021/12/7
 * 处理RequestMethodType数组
 */
public class MethodTypeUtils {
    public static RequestMethodType[] convert(RequestMethodType[] requestMethodTypes){
        List<RequestMethodType> methodTypes = new ArrayList<>();
        if (requestMethodTypes != null && requestMethodTypes.length == 1
                && requestMethodTypes[0] == RequestMethodType.ALL){
            methodTypes.add(RequestMethodType.DELETE);
            methodTypes.add(RequestMethodType.POST);
            methodTypes.add(RequestMethodType.PUT);
            methodTypes.add(RequestMethodType.OPTIONS);
            methodTypes.add(RequestMethodType.GET);
            methodTypes.add(RequestMethodType.TRACE);
            methodTypes.add(RequestMethodType.HEAD);
            methodTypes.add(RequestMethodType.CONNECT);
        }else if (requestMethodTypes != null){
            for (RequestMethodType requestMethodType : requestMethodTypes) {
                if (requestMethodType != RequestMethodType.ALL){
                    methodTypes.add(requestMethodType);
                }
            }
        }
        return methodTypes.toArray(new RequestMethodType[methodTypes.size()]);
    }
}
