package com.example.demo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

    /**
     * 默认的Gson序列化工具，配置全部默认，后续有需求需要配置相应得方法
     */
    public static final Gson gson = new GsonBuilder().create();

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromGson(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }


}
