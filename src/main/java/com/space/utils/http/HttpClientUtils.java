package com.space.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClient工具类
 *
 * @author zhuzhe
 * @date 2017/12/11
 * @email zhe.zhu1@outlook.com
 */
@Slf4j
public class HttpClientUtils {

    /**
     * 以jsonString形式发送HttpPost的Json请求，String形式返回响应结果
     * 可使用 {@link HttpUtils#doPost(java.lang.String, java.lang.String, java.util.Map, java.util.Map, java.lang.String)} 代替
     *
     * @param url
     * @param jsonString
     * @return
     */
    public static String sendPostJsonStr(String url, String jsonString) throws IOException {
        if (jsonString == null || jsonString.isEmpty()) {
            return sendPost(url);
        }
        String resp = "";
        StringEntity entityStr = new StringEntity(jsonString,
                ContentType.create("text/plain", "UTF-8"));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entityStr);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (resp == null || resp.equals("")) {
            return "";
        }
        return resp;
    }

    /**
     * 发送不带参数的HttpPost请求
     *
     * @param url
     * @return
     */
    public static String sendPost(String url) throws IOException {
        // 1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2.生成一个post请求
        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            // 3.执行get请求并返回结果
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        // 4.处理结果，这里将结果返回为字符串
        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            log.error(e.getMessage());
        }
        return result;
    }
}

