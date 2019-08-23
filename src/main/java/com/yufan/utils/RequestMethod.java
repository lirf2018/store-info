package com.yufan.utils;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @功能名称 请求工具类
 * @作者 lirongfan
 * @时间 2016年2月19日 上午10:20:24
 */
public class RequestMethod {

    private final static Logger LOG = Logger.getLogger(RequestMethod.class);

    /**
     * get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendGet(String url, String param) {
        String result = "";
        try {
            String urlName = url + "?" + param;//
            URL U = new URL(urlName);
            URLConnection connection = U.openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
        } catch (Exception e) {
            System.out.println("没有结果！" + e);
        }
        return result;
    }

    /**
     * 微信GET请求模拟(不带参数)
     *
     * @param requestUrl
     * @return
     */
    public static String sendGetWX1(String requestUrl) {
        StringBuffer buffer = null;

        try {
            // 建立连接
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");

            // 获取输入流
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 读取返回结果
            buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 微信GET请求模拟(带参数)
     *
     * @param url
     * @param param表示json格式的请求参数
     * @return
     */
    public static String sendGetWX2(String url, Object param) {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setDoOutput(true);
            // 建立实际的连接
            connection.connect();
            out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(param);
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * 微信POST请求模拟
     *
     * @param url
     * @param param表示json格式的请求参数
     * @return
     */
    public static String sendPostWX(String url, Object param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 注意Authorization生成
            // conn.setRequestProperty("Content-Type",
            // "application/x-www-form-urlencoded");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
//			System.out.println(result);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post 请求
     *
     * @param httpUrl
     * @param httpJson
     * @return
     */
    public static String httpPost(String httpUrl, String httpJson) {

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setRequestProperty("content-type", "text/xml");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("contentType", "UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
            out.write(httpJson);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else {
                    result.append(line);
                }
            }

            return result.toString();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 表单请求post
     *
     * @param url
     * @param nvps
     * @return //请求信息封装
     * NameValuePair[] data = {
     * new NameValuePair("orderNo",orderNo),//订单号
     * new NameValuePair("mobile",mobile),//充值手机号
     * new NameValuePair("userName",userName),//公司管理员账号admin@2000703175
     * new NameValuePair("userPwd",userPwd),//公司管理员密码
     * new NameValuePair("proKey",proKey),//充值的流量套餐编码
     * new NameValuePair("bcallbackUrl",bcallbackUrl),//充值成功回调地址
     * new NameValuePair("flowType","AF"),//充值成功回调地址 AF全网  SF 省网   RF红包
     * new NameValuePair("sign",sign),//签名认证
     * new NameValuePair("businessType",businessType),//1话费  2流量
     * new NameValuePair("f","recharge")// 方法
     * };
     */
    public static String httpPostForm(String url, List<NameValuePair> nvps) {
        try {
            LOG.info("url=" + url);
            LOG.info("nvps=" + com.alibaba.fastjson.JSONObject.toJSONString(nvps));
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            // 执行请求
            HttpResponse response = httpclient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 网易短信接口
     *
     * @param nvps
     * @param url
     * @param appSecret
     * @param appkey
     * @return
     */
    public static String httpPostNetease(List<NameValuePair> nvps, String url, String appSecret, String appkey) {
        try {
            LOG.info("url=" + url);
            LOG.info("nvps=" + com.alibaba.fastjson.JSONObject.toJSONString(nvps));
            LOG.info("appkey=" + appkey);
            LOG.info("appSecret=" + appSecret);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            Random random = new Random();
            int nonce = random.nextInt(10000);
            //当前UTC时间戳
            String curTime = String.valueOf((new Date()).getTime() / 1000L);
            /*
             * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
             */
            String checkStr = appSecret + nonce + curTime;// 开发者平台分配的appkey+随机数+当前UTC时间戳
            String checkSum = SHA1.encode(checkStr);
            // 设置请求的header
            httpPost.addHeader("AppKey", appkey);
            httpPost.addHeader("Nonce", nonce + "");
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("CheckSum", checkSum);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = httpclient.execute(httpPost);
            /*
             * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
             * 2.具体的code有问题的可以参考官网的Code状态表
             */
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
