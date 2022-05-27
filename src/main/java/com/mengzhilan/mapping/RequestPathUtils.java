package com.mengzhilan.mapping;

import org.xlp.utils.XLPStringUtil;

import java.util.Objects;

/**
 * Create by xlp on 2021/12/8
 */
public class RequestPathUtils {
    /**
     * 把两个path合并成一个path
     * <ul>
     *    <li>/aaa/, /bbb -> /aaa/bbb</li>
     *    <li>/aaa, bbb -> /aaa/bbb</li>
     *    <li>/aaa, /bbb -> /aaa/bbb</li>
     *    <li>/aaa/, bbb -> /aaa/bbb</li>
     * </ul>
     *
     * @param classPath 第一个path
     * @param methodPath 第二个path
     * @return
     */
    public static String mergePath(String classPath, String methodPath){
        if (classPath == null) return methodPath;
        if (methodPath == null) return classPath;
        if (methodPath.startsWith("/") && classPath.endsWith("/"))
            return classPath + methodPath.substring(1);
        if (!methodPath.startsWith("/") && !classPath.endsWith("/"))
            return classPath + "/" + methodPath;
        String mergePath = classPath + methodPath;
        if (mergePath.endsWith("/")){
            mergePath = mergePath.substring(0, mergePath.length() -1);
        }
        return mergePath;
    }

    /**
     * 去除指定uri中ContextPath部分
     *
     * @param contextPth
     * @param uri
     * @return
     */
    public static String removeContextPathFromUri(String contextPth, String uri){
        String path = uri;
        if (contextPth.length() > 1){
            path = uri.substring(contextPth.length());
        }
        return path;
    }

    /**
     * 比较访问uri和controller中RequestMapping的路径是否相等
     *
     * @param path1 访问uri
     * @param path2 controller中RequestMapping的路径
     * @return true：相等，false：不等
     */
    static boolean comparePath(String path1, String path2){
        if (path1 == null || path2 == null){
            return false;
        }
        String[] paths1 = path1.split("/");
        String[] paths2 = path2.split("/");
        if (paths1.length != paths2.length){
            return false;
        }

        String[] names;
        int startIndex, endIndex;
        PathVariableUriCompare compare;
        out: for (int i = 0, len = paths1.length; i < len; i++) {
            endIndex = 0;
            //判断controller中RequestMapping的路径是否包含${[A-Za-z]+[A-Za-z0-9]*}值
            names = XLPStringUtil.findSubStrings(paths2[i], "\\{[A-Za-z]+[A-Za-z0-9]*\\}");
            if ( names.length > 0){
                compare = new PathVariableUriCompare();
                for (String name : names) {
                    if (name.length() == paths2[i].length()) break out;
                    startIndex = paths2[i].indexOf(name, endIndex);
                    if(!compare.compare(paths2[i].substring(endIndex, startIndex), paths1[i],
                            endIndex == 0, false)){
                        return false;
                    }
                    endIndex = startIndex + name.length();
                }
                if(!compare.compare(paths2[i].substring(endIndex), paths1[i], false, true)){
                    return false;
                }
            }else if (!Objects.equals(paths1[i], paths2[i])) {
                return false;
            }
        }
        return true;
    }
}
