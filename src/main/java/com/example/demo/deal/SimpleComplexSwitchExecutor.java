package com.example.demo.deal;

import com.example.demo.utils.JsonHelper;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 简繁转换处理器
 *
 * @author hujiping
 * @date 2023/7/27 8:01 PM
 */
@Component("simpleComplexSwitchExecutor")
public class SimpleComplexSwitchExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleComplexSwitchExecutor.class);

    public void recursiveDeal(Object result, Integer switchType) {
        try {
            if(result instanceof String){
                Field field = result.getClass().getDeclaredField("value");
                field.setAccessible(true);
                field.set(result, Objects.equals(switchType, 1)
                        ? complexToSimple((String) result).toCharArray()
                        : simpleToComplex((String) result).toCharArray());
            }
            else if(result instanceof List){
                extractedList((List) result, switchType);
            }
            else if(result instanceof Map){
                extractedMap((Map) result, switchType);
            }
            else {
                String str = JsonHelper.toJson(result);
                if (str.startsWith("{") && str.endsWith("}")){
                    // 判断是对象
                    recursiveDealOfVO(result, switchType);
                }
            }
        }catch (Exception e){
            logger.error("转换异常!", e);
        }
    }

    void recursiveDealOfVO(Object result, Integer switchType) throws Exception {
        List<Field> declaredFields = getAllFiled(result);
        Class<?> clz = result.getClass();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object fieldValue = field.get(result);
            if ("serialVersionUID".equals(field.getName())){
                continue;
            }
            if(fieldValue instanceof String){
                // 字符串则简繁切换
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                clz.getMethod("set" + captureName(field.getName()), String.class)
                        .invoke(result, Objects.equals(switchType, 1)
                                ? complexToSimple((String) field.get(result))
                                : simpleToComplex((String) field.get(result)));
            }
            else if(fieldValue instanceof List){
                // list
//                extractedList((List) fieldValue, switchType);
                String str = JsonHelper.toJson(fieldValue);

                clz.getMethod("set" + captureName(field.getName()), field.getType())
                        .invoke(result, Objects.equals(switchType, 1)
                                ? JsonHelper.fromJson(complexToSimple(str), field.getType())
                                : JsonHelper.fromJson(simpleToComplex(str), field.getType()));
            }
            else if(fieldValue instanceof Map){
                // map
//                extractedMap((Map<?, ?>) fieldValue, switchType);
                String str = JsonHelper.toJson(fieldValue);

                clz.getMethod("set" + captureName(field.getName()), field.getType())
                        .invoke(result, Objects.equals(switchType, 1)
                                ? JsonHelper.fromJson(complexToSimple(str), field.getType())
                                : JsonHelper.fromJson(simpleToComplex(str), field.getType()));
            }
            else {
                String str = JsonHelper.toJson(fieldValue);
                
                if (str.startsWith("{") && str.endsWith("}")){
                    // 判断是对象
//                    recursiveDealOfVO(fieldValue, switchType);
                    clz.getMethod("set" + captureName(field.getName()), field.getType())
                            .invoke(result, Objects.equals(switchType, 1)
                                    ? complexToSimple(str)
                                    : JsonHelper.fromJson(simpleToComplex(str), field.getType()));
                }
            }
        }
    }

    private List<Field> getAllFiled(Object result) {
        List<Field> fieldList = Lists.newArrayList();
        Class<?> tempClass = result.getClass();
        // 当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }

    private void extractedList(List<?> fieldValue, Integer switchType) {
        fieldValue.forEach(item -> {
            try {
                recursiveDeal(item, switchType);
            } catch (Exception e) {
                logger.error("转换异常!", e);
            }
        });
    }

    private void extractedMap(Map<?, ?> fieldValue, Integer switchType) {
        fieldValue.forEach((k, v) -> {
            try {
                // k的值判断
                recursiveDeal(k, switchType);
                // v的值判断
                recursiveDeal(v, switchType);
            } catch (Exception e) {
                logger.error("转换异常!", e);
            }
        });
    }

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }

    String simpleToComplex(String oldStr){
        // 简体转换为繁体
        return ZhConverterUtil.convertToTraditional(oldStr);
    }
    String complexToSimple(String oldStr){
        // 繁体转换为简体
        return ZhConverterUtil.convertToSimple(oldStr);
    }
    
}
