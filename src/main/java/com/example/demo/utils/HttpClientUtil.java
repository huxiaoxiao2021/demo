package com.example.demo.utils;

import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpClientUtil {

    private static final String cookie = "__jda=92483395.1659592303992810932468.1659592304.1663149615.1664437749.9; sso.jd.com=BJ.BF4EC349B22296A062E55F6695869AEE.0220220929183331; dynamic_code_verified=pTeatqpWAfSxyEL1/3tRcAxjf+RZe6jx";

    public static void main(String[] args) {

        String url = "http://dms-w.jdl.com/center/reprintRecord/queryPageList";
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Cookie", cookie);
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("x-proxy-opts", "{\"target\":\"http://origin.jmq.jd.com\",\"pathRewrite\":{\"^/api/jmqApi\":\"/\"}}");

        Map<String, Object> param = Maps.newHashMap();
        param.put("pageNumber", 1);
        param.put("pageSize", 10);
        param.put("orgId", 6);
        param.put("siteCode", 333833);
        param.put("barCode", "JDVA16121398389");
        param.put("rowKeyStart", "");
        param.put("operateTimeFromStr", "2022-09-29 00:00:00");
        param.put("operateTimeToStr", "2022-09-30 00:00:00");

        String response = postJson(url, param, headers, String.class);
    }

    // Create a custom response handler
    private static ResponseHandler<String> responseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    };

    public static <T> T get(String url, Map<String, Object> param, Map<String, String> headers, Class<T> clazz) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            if (!Objects.isNull(param) && !param.isEmpty()) {
                StringBuilder urlParam = new StringBuilder();
                urlParam.append("?");
                for (Map.Entry entry : param.entrySet()) {
                    urlParam.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                url = url + urlParam.deleteCharAt(urlParam.lastIndexOf("&"));
            }

            HttpGet httpget = new HttpGet(url);
//            // 配置请求参数实例
//            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
//                    .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
//                    .setSocketTimeout(60000)// 设置读取数据连接超时时间
//                    .build();
//            // 为httpPost实例设置配置
//            httpget.setConfig(requestConfig);

            //设置headers
            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpget.setHeader(entry.getKey(), entry.getValue());
                }
            }

            String responseBody = httpclient.execute(httpget, responseHandler);
            return JsonUtil.fromGson(responseBody, clazz);
        } catch (IOException e) {
            System.out.println("调用{}发生IO异常:" + url + e.getMessage());
            return null;
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    System.out.println("关闭httpClient发生IO异常:" + e.getMessage());
                }
            }
        }
    }

    public static <T> T postJson(String url, Map<String, Object> param, Map<String, String> headers, Class<T> clazz) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(url);
            // 配置请求参数实例
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)// 设置连接主机服务超时时间
                    .setConnectionRequestTimeout(10000)// 设置连接请求超时时间
                    .setSocketTimeout(10000)// 设置读取数据连接超时时间
                    .build();
            // 为httpPost实例设置配置
            httpPost.setConfig(requestConfig);

            // 封装post请求参数
            if (null != param && param.size() > 0) {
                // 为httpPost设置封装好的请求参数
                httpPost.setEntity(new StringEntity(JsonUtil.toJsonString(param), "UTF-8"));
            }

            //设置headers
            if (null != headers) {
                httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            String responseBody = httpclient.execute(httpPost, responseHandler);
            return clazz.getName().equals(String.class.getName()) ? (T) responseBody : JsonUtil.fromGson(responseBody, clazz);
        } catch (IOException e) {
            System.out.println("调用{}发生IO异常:" + url + e.getMessage());
            return null;
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    System.out.println("关闭httpClient发生IO异常:" + e.getMessage());
                }
            }
        }
    }

    public static <T> T postFormText(String url, String param, Map<String, String> headers, Class<T> clazz) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(url);
            // 配置请求参数实例
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)// 设置连接主机服务超时时间
                    .setConnectionRequestTimeout(10000)// 设置连接请求超时时间
                    .setSocketTimeout(10000)// 设置读取数据连接超时时间
                    .build();
            // 为httpPost实例设置配置
            httpPost.setConfig(requestConfig);

            // 封装post请求参数
            // 为httpPost设置封装好的请求参数
            httpPost.setEntity(new StringEntity(param, "UTF-8"));

            //设置headers
            if (null != headers) {
                httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            String responseBody = httpclient.execute(httpPost, responseHandler);
            return clazz.getName().equals(String.class.getName()) ? (T) responseBody : JsonUtil.fromGson(responseBody, clazz);
        } catch (IOException e) {
            System.out.println("调用{}发生IO异常:" + url + e.getMessage());
            return null;
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    System.out.println("关闭httpClient发生IO异常:" + e.getMessage());
                }
            }
        }
    }

    public static <T> T postWithFormUrlEnCoding(String url, Map<String, String> param, Map<String, String> headers, Class<T> clazz) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(url);
            // 配置请求参数实例
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)// 设置连接主机服务超时时间
                    .setConnectionRequestTimeout(10000)// 设置连接请求超时时间
                    .setSocketTimeout(10000)// 设置读取数据连接超时时间
                    .build();
            // 为httpPost实例设置配置
            httpPost.setConfig(requestConfig);

            // 封装post请求参数
            if (null != param && param.size() > 0) {
                // 为httpPost设置封装好的请求参数
                List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
                for (Map.Entry<String, String> item : param.entrySet()) {
                    basicNameValuePairs.add(new BasicNameValuePair(item.getKey(), item.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(basicNameValuePairs, StandardCharsets.UTF_8));
            }

            //设置headers
            if (null != headers) {
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            String responseBody = httpclient.execute(httpPost, responseHandler);
            return clazz.getName().equals(String.class.getName()) ? (T) responseBody : JsonUtil.fromGson(responseBody, clazz);
        } catch (IOException e) {
            System.out.println("调用{}发生IO异常:" + url + e.getMessage());
            return null;
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    System.out.println("关闭httpClient发生IO异常:" + e.getMessage());
                }
            }
        }
    }

    public static <T> T put(String url, Map<String, Object> param, Map<String, String> headers, Class<T> clazz) {
        String encode = "utf-8";
        //HttpClients.createDefault()等价于 HttpClientBuilder.create().build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut httpput = new HttpPut(url);

        //设置header
        httpput.setHeader("Content-type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(), entry.getValue());
            }
        }
        //组织请求参数
        StringEntity stringEntity = new StringEntity(JsonUtil.toJsonString(param), encode);
        httpput.setEntity(stringEntity);

        try {
            String responseBody = httpclient.execute(httpput, responseHandler);
            return clazz.getName().equals(String.class.getName()) ? (T) responseBody : JsonUtil.fromGson(responseBody, clazz);
        } catch (IOException e) {
            System.out.println("调用{}发生IO异常:" + url + e.getMessage());
            return null;
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    System.out.println("关闭httpClient发生IO异常:" + e.getMessage());
                }
            }
        }
    }
}
