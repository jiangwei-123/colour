package com.jw.colour.common;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http request
 *
 * @author jw on 2021/2/4
 */
public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);


    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_FROM = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * @param url url
     * @author jw
     * @date 2021/2/7
     * @return: java.lang.String
     **/
    public String doGet(URI url) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);

        // 响应模型
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return result;
    }

    /**
     * get request have params
     *
     * @param url       url
     * @param paramsMap paramsMap
     * @author jw
     * @date 2021/2/7
     * @return: java.lang.String
     **/
    public String doGetByParam(String url, Map<String, Object> paramsMap) {
        // 参数
        URI uri = null;
        try {
            // 将参数放入键值对类NameValuePair中,再放入集合中
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> map : paramsMap.entrySet()) {
                params.add(new BasicNameValuePair(map.getKey(), String.valueOf(map.getValue())));
            }
            uri = new URIBuilder().setPath(url).setParameters(params).build();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        return doGet(uri);
    }


    public String doPost(URI uri) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost(uri);
        // 响应模型
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return result;
    }




    private static void close(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}