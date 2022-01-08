package com.mengzhilan.handler;

import org.xlp.json.Json;
import org.xlp.json.JsonArray;
import org.xlp.json.JsonObject;
import org.xlp.utils.XLPArrayUtil;
import org.xlp.utils.XLPBooleanUtil;
import org.xlp.utils.XLPPackingTypeUtil;
import org.xlp.utils.XLPStringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by xlp on 2021/12/23
 *
 * 函数参数值处理器
 */
public class ValueUtils {
    /**
     * 获取是否启用注解映射成bean指定名称的key值
     * @see System System.getProperty()
     */
    public static final String IS_USED_ANNOTATION = "isUsedAnnotation";

    /**
     * 基本类型默认值
     */
    public static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS = new HashMap<>();

    static {
        PRIMITIVE_DEFAULTS.put(Integer.TYPE, 0);
        PRIMITIVE_DEFAULTS.put(Short.TYPE, (short) 0);
        PRIMITIVE_DEFAULTS.put(Byte.TYPE, (byte) 0);
        PRIMITIVE_DEFAULTS.put(Float.TYPE, 0f);
        PRIMITIVE_DEFAULTS.put(Double.TYPE, 0d);
        PRIMITIVE_DEFAULTS.put(Long.TYPE, 0L);
        PRIMITIVE_DEFAULTS.put(Boolean.TYPE, Boolean.FALSE);
        PRIMITIVE_DEFAULTS.put(Character.TYPE, (char) 0);
    }

    /**
     * 把字符串数组即通过
     * javax.servlet.http.HttpServletRequest.getParameterValues()获取的字符串数组转换成目标类型的值
     *
     * @param strValues
     * @param targetValueClass
     * @return 假如参数为null，则返回null。
     * <ul>
     *     <li>[],String -> ""</li>
     *     <li>["123","23"],String -> "123"</li>
     *     <li>["123"],String[] -> ["123"]</li>
     *     <li>["123","234"],int[] -> [123,234]</li>
     *     <li>["123","234"],int -> 123</li>
     *     <li>其余类型类似</li>
     * </ul>
     */
    static Object StringValuesConvert(String[] strValues, Class<?> targetValueClass){
        if (strValues == null || targetValueClass == null) {
            return null;
        }
        if (targetValueClass == String.class) {
            return strValues.length == 0 ? XLPStringUtil.EMPTY : strValues[0];
        }
        if (targetValueClass == String[].class) {
            return strValues;
        }
        if (strValues.length == 0) {
            return null;
        }
        String strVale = strValues[0];
        if (targetValueClass == Integer.TYPE || targetValueClass == Integer.class){
            return new BigDecimal(strVale).intValue();
        }
        if (targetValueClass == Long.TYPE || targetValueClass == Long.class){
            return new BigDecimal(strVale).longValue();
        }
        if (targetValueClass == Short.TYPE || targetValueClass == Short.class){
            return new BigDecimal(strVale).shortValue();
        }
        if (targetValueClass == Byte.TYPE || targetValueClass == Byte.class){
            return new BigDecimal(strVale).byteValue();
        }
        if (targetValueClass == Boolean.TYPE || targetValueClass == Boolean.class){
            return XLPBooleanUtil.valueOf(strVale);
        }
        if (targetValueClass == Double.TYPE || targetValueClass == Double.class){
            return new BigDecimal(strVale).doubleValue();
        }
        if (targetValueClass == Float.TYPE || targetValueClass == Float.class){
            return new BigDecimal(strVale).floatValue();
        }
        if (targetValueClass == Character.TYPE || targetValueClass == Character.class){
            return strVale.charAt(0);
        }
        if (targetValueClass == char[].class){
            return strVale.toCharArray();
        }
        if (targetValueClass == int[].class){
            return XLPArrayUtil.integerArrayToIntArray(XLPPackingTypeUtil.convert(strValues, Integer.class));
        }
        if (targetValueClass == Integer[].class){
            return XLPPackingTypeUtil.convert(strValues, Integer.class);
        }
        if (targetValueClass == short[].class){
            return XLPArrayUtil.shortArrayToShortArray(XLPPackingTypeUtil.convert(strValues, Short.class));
        }
        if (targetValueClass == Short[].class){
            return XLPPackingTypeUtil.convert(strValues, Short.class);
        }
        if (targetValueClass == byte[].class){
            return XLPArrayUtil.byteArrayToByteArray(XLPPackingTypeUtil.convert(strValues, Byte.class));
        }
        if (targetValueClass == Byte[].class){
            return XLPPackingTypeUtil.convert(strValues, Byte.class);
        }
        if (targetValueClass == long[].class){
            return XLPArrayUtil.longArrayToLongArray(XLPPackingTypeUtil.convert(strValues, Long.class));
        }
        if (targetValueClass == Long[].class){
            return XLPPackingTypeUtil.convert(strValues, Long.class);
        }
        if (targetValueClass == double[].class){
            return XLPArrayUtil.doubleArrayToDoubleArray(XLPPackingTypeUtil.convert(strValues, Double.class));
        }
        if (targetValueClass == Double[].class){
            return XLPPackingTypeUtil.convert(strValues, Double.class);
        }
        if (targetValueClass == float[].class){
            return XLPArrayUtil.floatArrayToFloatArray(XLPPackingTypeUtil.convert(strValues, Float.class));
        }
        if (targetValueClass == Float[].class){
            return XLPPackingTypeUtil.convert(strValues, Float.class);
        }
        if (targetValueClass == Character[].class){
            return XLPArrayUtil.charArrayToCharacterArray(strVale.toCharArray());
        }
        return null;
    }

    /**
     * 请求体内容转换成指定类型的值
     *
     * @param requestBody
     * @param targetValueClass
     * @return
     */
    static Object requestBodyConvert(String requestBody, Class<?> targetValueClass){
        if (requestBody == null || targetValueClass == null) {
            return null;
        }
        if (targetValueClass == String.class) {
            return requestBody;
        }
        if (Json.class == targetValueClass){
            return Json.parseText(requestBody);
        }
        if (JsonObject.class == targetValueClass){
            return JsonObject.fromJsonString(requestBody);
        }
        if (JsonArray.class == targetValueClass){
            return JsonArray.fromJsonString(requestBody);
        }
        return null;
    }

    /**
     * 把请求参数转换成指定类型，指定参数名称的JavaBean对象
     *
     * @param parameterMap 请求参数
     * @param beanName 参数名称
     * @param beanClass 参数类型
     * @return
     */
    static Object parameterMapConvertBean(Map<String, String[]> parameterMap, String beanName,
            Class<?> beanClass){
        String prefix = beanName + ".";
        int prefixLen = prefix.length();
        JsonObject parameterJson = new JsonObject();
        String[] values;
        String key;
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            key = entry.getKey();
            if (key.startsWith(prefix)){
                values = entry.getValue();
                parameterJson.put(key.substring(prefixLen), values.length == 0 ? null : values[0]);
            }
        }
        if (!parameterJson.isEmpty()){
            String isUsedAnnotation = System.getProperty(IS_USED_ANNOTATION);
            return parameterJson.toBeanExt(beanClass, "true".equals(isUsedAnnotation));
        }
        return null;
    }
}
