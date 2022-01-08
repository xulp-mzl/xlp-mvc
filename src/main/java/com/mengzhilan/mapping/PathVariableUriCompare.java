package com.mengzhilan.mapping;

/**
 * Create by xlp on 2021/12/28
 *
 * 对比uri和@{@link com.mengzhilan.annotation.PathVariable}注解映射
 */
class PathVariableUriCompare {
    /**
     * 非${}占位的起始位置
     */
    private int startIndex = 0;

    /**
     * 判断uri指定的位置与@{@link com.mengzhilan.annotation.PathVariable}注解映射非${}占位字符串是否相同
     *
     * @param partStr
     * @param uriCorrespondingStr
     * @param isStart
     * @param isEnd
     * @return
     */
    public boolean compare(String partStr, String uriCorrespondingStr, boolean isStart, boolean isEnd){
        if (partStr == null || uriCorrespondingStr == null){
            return false;
        }
        if (partStr.isEmpty()){
            return true;
        }
        if (isStart){
            startIndex += partStr.length();
            return uriCorrespondingStr.startsWith(partStr);
        }
        if (isEnd){
            startIndex += partStr.length();
            return uriCorrespondingStr.endsWith(partStr);
        }
        int index =  uriCorrespondingStr.indexOf(partStr, startIndex);
        if (index < 0){
            return false;
        }
        startIndex = index + partStr.length();
        return true;
    }
}
