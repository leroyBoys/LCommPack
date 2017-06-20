package com.lgame.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2017/6/19.
 */
public class HttpTool {

    public static void main(String[] args){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        List<NameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("name","admin"));
        formparams.add(new BasicNameValuePair("password","admin"));

        String retData =httpPost(url,formparams,"utf-8");

        System.out.println("===========retData:");
        System.out.println(retData);

        System.out.println("==========-------");
        System.out.println(httpGet("http://47.92.115.60:8080/LGameLogin/main"));
/*
        Map<String,String> ps = new HashMap<>();
        ps.put("name","admin");
        ps.put("password","admin");
        retData = httpClient(url,"utf-8",ps);
        System.out.println("========>");
        System.out.println(retData);*/
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     * @param url
     * @param formparams 参数队列
     * @return
     */
    public static String httpPost(String url,List<NameValuePair> formparams,String encode) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, encode==null?"UTF-8":encode);
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送 get请求
     */
    public static String httpGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                   /* // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));*/
                    return EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String httpClient(String path, String encode, Map<String, String> paramMap) {
        StringBuffer sb = new StringBuffer();
        // 默认连接
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = null;
        try {
            httppost = new HttpPost(path);
            String param = convertParam(paramMap);
            if (param != null) {
                StringEntity reqEntity = new StringEntity(param, encode);
                httppost.setEntity(reqEntity);
                reqEntity.setContentType("application/x-www-form-urlencoded");
            }

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent(), encode));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.getMessage());
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static String httpClient(String path, Map<String, String> paramMap){
        return httpClient(path,"utf-8",paramMap);
    }

    private static String convertParam(Map<String, String> paramMap) {
        if (paramMap == null || paramMap.size() == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (String s : paramMap.keySet()) {
            sb.append(s).append("=").append(paramMap.get(s)).append("&");
        }

        return sb.substring(0, sb.length() - 1);
    }
}
