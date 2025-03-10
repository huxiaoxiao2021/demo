package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.eclipse.jetty.util.StringUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    private final static Log logger = LogFactory.getLog(JsonHelper.class);

    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectMapper dfOneJson2ListMapper = new ObjectMapper();

    /**
     * GSON序列化器
     */
    private static final Gson GSON_COMMON=new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setPrettyPrinting().create();
    /** 不格式化JSON */
    private static final Gson GSON_NONE_PRETTY=new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static final JsonParser GSON_PARSER= new JsonParser();


    static {
        dfOneJson2ListMapper.getDeserializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @SuppressWarnings("deprecation")
    public static <T> T fromJson(String json, Class<T> responseType) {
        try {
            JsonHelper.mapper.getSerializationConfig().setSerializationInclusion(
                    JsonSerialize.Inclusion.NON_NULL);

            return JsonHelper.mapper.readValue(json, responseType);
        } catch (Exception e) {
            JsonHelper.logger.error("Jackson反序列化JSON发生异常，将使用GSON重试");
            try{
                return  GSON_COMMON.fromJson(json,responseType);
            }catch (Exception ex){
                JsonHelper.logger.error("GSON-反序列化JSON发生异常， 异常信息为：" +ex.getMessage(), ex);
            }
        }

        return null;
    }

    public static <T> T fromJsonUseGson(String json,Class<T> responseType){
        return GSON_COMMON.fromJson(json,responseType);
    }

    public static <T> T fromJsonUseGson(String json,Type responseType){
        return (T)GSON_COMMON.fromJson(json,responseType);
    }
    @SuppressWarnings("deprecation")
    public static <T> T fromJsonDateFormat(String json, Class<T> responseType) {
        try {
            JsonHelper.dfOneJson2ListMapper.getSerializationConfig().setSerializationInclusion(
                    JsonSerialize.Inclusion.NON_NULL);
            return JsonHelper.dfOneJson2ListMapper.readValue(json, responseType);
        } catch (Exception e) {
            JsonHelper.logger.error("Jackson反序列化JSON发生异常，将使用GSON重试");
            try{
                return  GSON_COMMON.fromJson(json,responseType);
            }catch (Exception ex){
                JsonHelper.logger.error("GSON-反序列化JSON发生异常， 异常信息为：" + ex.getMessage(), ex);
            }
        }

        return null;
    }

    public static <T> T fromJsonUseGsonMillisecondFormat(String json,Class<T> responseType){

        return null;
    }

    public static <T> T jsonToArray(String json, Class<T> responseType) {
        try {
            return JsonHelper.mapper.readValue(json, responseType);
        } catch (Exception e) {
            JsonHelper.logger.error("Jackson反序列化JSON发生异常，将使用GSON重试");
            try{
                return  GSON_COMMON.fromJson(json,responseType);
            }catch (Exception ex){
                JsonHelper.logger.error("GSON-反序列化JSON发生异常， 异常信息为：" + ex.getMessage(), ex);
            }
        }

        return null;
    }

    public static <T> T jsonToObject(String json, Class<T> responseType) {
        try{
            return  GSON_COMMON.fromJson(json,responseType);
        }catch (Exception ex){
            System.out.println("GSON-反序列化JSON发生异常， 异常信息为：" + ex.getMessage());
            JsonHelper.logger.error("GSON-反序列化JSON发生异常， 异常信息为：" + ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T jsonToArrayWithDateFormatOne(String json, Class<T> responseType) {
        try {
            return JsonHelper.dfOneJson2ListMapper.readValue(json, responseType);
        } catch (Exception e) {
            JsonHelper.logger.error("Jackson反序列化JSON发生异常，将使用GSON重试");
            try{
                return  GSON_COMMON.fromJson(json,responseType);
            }catch (Exception ex){
                JsonHelper.logger.error("GSON-反序列化JSON发生异常， 异常信息为：" + ex.getMessage(), ex);
            }
        }
        return null;
    }

    public static String toJson(Object obj) {
        return JsonHelper.toJson(obj, false);
    }

    /**
     *  不格式化 JSON格式
     */
    public static String toJsonString(Object obj) {
        return GSON_NONE_PRETTY.toJson(obj);
    }

    @SuppressWarnings("deprecation")
    public static String toJson(Object object, boolean prettyPrint) {
        JsonGenerator generator = null;
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            generator = JsonHelper.mapper.getJsonFactory()
                    .createJsonGenerator(writer);
            generator.useDefaultPrettyPrinter();
            JsonHelper.mapper.getSerializationConfig().setSerializationInclusion(
                    JsonSerialize.Inclusion.NON_NULL);
            JsonHelper.mapper.writeValue(generator, object);
            writer.close();
            return writer.getBuffer().toString();
        } catch (Exception e) {
            JsonHelper.logger.error("序列化JSON发生异常， 异常信息为：" + e.getMessage(), e);
        }finally {
            if(generator!=null){
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Map<String, Object>> json2Map(String jsonVal) {
        try {
            Map<String, Map<String, Object>> maps = JsonHelper.mapper.readValue(jsonVal, Map.class);
            return maps;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Boolean isJson(String json, Class<?> responseType) {
        if (StringUtil.isEmpty(json)) {
            return Boolean.FALSE;
        }

        try {
            if (JsonHelper.fromJson(json, responseType) != null) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
        }

        return Boolean.FALSE;
    }

    /*
    public static void main(String[] args) {
        System.out.printf(String.valueOf( isJsonString("201504161401010")));
        System.out.printf(String.valueOf( isJsonString("{\"test\":1}")));
        String aaaa="32322-21123";
        System.out.print(JsonHelper.toJson(aaaa));
        String test=JsonHelper.fromJson(aaaa, String.class);
        System.out.print(test);
    }
    */
    public static boolean isJsonString(String json){
        if (StringUtil.isEmpty(json)) {
            return Boolean.FALSE;
        }
        try {
            if(false==json.startsWith("{")&&false==json.startsWith("[")){
                return false;
            }
            GSON_PARSER.parse(json);
            return true;
        } catch (JsonParseException e) {
            logger.error("bad json: " + json);
            return false;
        }
    }

    /**
     * 返回的key为String value为Object
     *
     * @param jsonVal
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2MapNormal(String jsonVal) {
        try {
            Map<String, Object> maps = JsonHelper.mapper.readValue(jsonVal, Map.class);
            return maps;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * added by zhanglei  统一对时间的处理
     * @param object
     * @return
     */
    public static String toJsonUseGson(Object object) {
        return GSON_COMMON.toJson(object);
    }

    public static <T> List<T> jsonToList(String json, Class<T> elementClasses) {
        try {
            return JSONObject.parseArray(json, elementClasses);
        } catch (Exception e) {
            logger.error("GSON-反序列化JSON发生异常", e);
        }
        return null;
    }
}
