package com.fr.plugin.db.xml.core;


import com.fr.general.FRLogger;
import com.fr.stable.EncodeConstants;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpClientBuilder {
    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();

    private static HttpClientBuilder instance = null;

    private HttpClientBuilder() {
    }

    public static HttpClientBuilder getInstance() {
        if (instance == null) {
            instance = new HttpClientBuilder();
        }
        return instance;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        return sendHttpPost(httpUrl, maps, EncodeConstants.ENCODING_UTF_8);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, String charset) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return sendHttpPost(httpPost, charset);
    }


    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost, String charset) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseText = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseText = EntityUtils.toString(entity, charset);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } finally {
            close(httpClient, response);
        }
        return responseText;
    }


    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl) {
        return sendHttpGet(httpUrl, EncodeConstants.ENCODING_UTF_8);
    }

    public String sendHttpGet(String httpUrl, String charset) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet, charset);
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet, EncodeConstants.ENCODING_UTF_8);
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl, String charset) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet, charset);
    }


    /**
     * 发送Get请求
     *
     * @return
     */
    private String sendHttpGet(HttpGet httpGet, String charset) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseText = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseText = EntityUtils.toString(entity, charset);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        } finally {
            close(httpClient, response);
        }
        return responseText;
    }


    private void close(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            // 关闭连接,释放资源
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 发送Get请求Https
     */
    private String sendHttpsGet(HttpGet httpGet, String charset) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return responseContent;
    }


    public static void main(String... args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("gc", "测试一下");
        String text = HttpClientBuilder.getInstance().sendHttpPost("http://localhost:8075/WebReport/ReportServer?op=td_json", params);
        System.out.println(text);
    }
}

