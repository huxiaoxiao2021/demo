package com.example.demo.jsf;

import com.example.demo.utils.JsonHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * http 方式调用 jsf（rpc）
 * 
 * @date 2023-06-30 16:52
 * @author hujiping
 */
public class HttpJsfClientMain {

    /**
     * slf4j Logger for this class
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpJsfClientMain.class);

    public static void main(String[] args) {
        /*
            注意：windows下服务端若未指定绑定到所有网卡0.0.0.0，则本机客户端是不能直接使用127.0.0.1访问的。
            请查看服务端启动日志，看具体绑定的网卡和端口是哪个：Server have success bind to 10.23.11.22:11090
            例如 http://10.23.11.22:11090
         */
//        String url = "ip:port/interface/alias/method";

        // request url
        String ip = "10.253.61.10";
        String port = "22000";
        String interfaceName = "com.jd.bluedragon.external.gateway.service.JyStrandReportGatewayService";
        String alias = "123";
        String method = "queryStrandReason";
        String url = "http://" + ip + ":" + port + "/" + interfaceName + "/" + alias + "/" + method;
        // 入参
        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("curPage", 1);
        paramsMap.put("pageSize", 10);
        paramsMap.put("searchVo", "{\n" +
                "        \"provinceAgencyCode\":\"210000\"\n" +
                "    }");
        String result = sendByPost(url, new Object[]{paramsMap});
        LOGGER.info("result : {}", result);
        
        // 多个入参
        ip = "11.158.130.144";
        port = "22000";
        interfaceName = "com.jd.ql.basic.ws.BasicWareHouseWS";
        alias = "basic-ql-jsf";
        method = "query";
        url = "http://" + ip + ":" + port + "/" + interfaceName + "/" + alias + "/" + method;
        Map<String, Object> params1 = Maps.newHashMap();
        params1.put("provinceAgencyCode", "210000");
        Map<String, Object> params2 = Maps.newHashMap();
        params2.put("curPage", 1);
        params2.put("pageSize", 10);
        result = sendByPost(url, new Object[]{
                params1,
                params2
        });
        LOGGER.info("result : {}", result);

        // get 请求
        ip = "11.157.192.196";
        port = "22000";
        interfaceName = "com.jdl.basic.api.service.site.SiteQueryService";
        alias = "jy-basic-server-uat";
        method = "querySitePageByConditionFromBasicSite";
        url = "http://" + ip + ":" + port + "/" + interfaceName + "/" + alias + "/" + method;
        result = sendByGet(url);
        LOGGER.info("result : {}", result);
    }

    private static String sendByPost(String url, Object[] params){
        String response = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            // 服务端需要token
            httpost.setHeader("token", "123456");

            StringEntity entity = new StringEntity(JsonHelper.toJsonString(params), StandardCharsets.UTF_8);
            httpost.setEntity(entity);
            HttpResponse httpResponse = null;

            httpResponse = httpclient.execute(httpost);
            HttpEntity responseEntity = httpResponse.getEntity();
            LOGGER.info("response status: " + httpResponse.getStatusLine());
            response = EntityUtils.toString(responseEntity);

        } catch (Exception e) {
            LOGGER.error("服务异常!", e);
        }
        return response;
    }

    private static String sendByGet(String url){
        String response = "";
        HttpURLConnection connection = null;
        try {
            URL _url = new URL(url);
            connection = (HttpURLConnection) _url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true); // 输出，默认情况下是false;
            connection.setDoInput(true); // 读入，默认情况下是true;
            connection.setUseCaches(false); // Post 请求不能使用缓存
            connection.setRequestProperty("Content-type", "text/html; charset=UTF-8");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("token", "1qaz2wsx"); // 如果是token，在head里面加
            connection.connect();

            InputStream inStrm = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inStrm));
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            inStrm.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return response;
    }
}