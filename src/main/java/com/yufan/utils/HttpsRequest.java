package com.yufan.utils;


import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 创建人: lirf
 * 创建时间:  2018-04-03 16:15
 * 功能介绍:
 */
public class HttpsRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpsRequest.class);
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;

    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new org.apache.http.conn.ssl.TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * // 全部信任 不做身份鉴定
     * httpclient4.5 https请求 忽略身份验证
     *
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpsPost(String url, String jsonParam) {
        log.info("开始访问：" + url);
        log.info("传入参数：" + jsonParam);
        SSLContext sslContext = null;
        String result = "";
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
            httpPost.setConfig(requestConfig);

            // 设置头信息
            Header[] headers = new Header[1];
            headers[0] = new BasicHeader("Content-type", CONTENT_TYPE);
            httpPost.setHeaders(headers);

            // 设置请求参数
            HttpEntity entity = new StringEntity(jsonParam, Charset.forName("UTF-8"));
            httpPost.setEntity(entity);


            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity);
            } else {
                result = readHttpResponse(httpResponse);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpsPost2(String url, String jsonParam) {
        log.info("开始访问：" + url);
        log.info("传入参数：" + jsonParam);
        SSLContext sslContext = null;

        try {
            TrustManager manager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            };
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{manager}, null);
            // 忽略hostname验证
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    sslContext, NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                    .register(HTTPS, socketFactory).build();

            HttpClientBuilder builder = HttpClientBuilder.create();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
            connectionManager.setDefaultMaxPerRoute(20);
            connectionManager.setMaxTotal(200);

            builder.setConnectionManager(connectionManager);

            HttpClient client = builder.build();

            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
            post.setConfig(requestConfig);
            Header[] headers = new Header[1];
            headers[0] = new BasicHeader("Content-type", CONTENT_TYPE);
            post.setHeaders(headers);

            HttpEntity entity = new StringEntity(jsonParam, Charset.forName("UTF-8"));
            post.setEntity(entity);

            HttpResponse res = client.execute(post);
            HttpEntity en = res.getEntity();

            int status = res.getStatusLine().getStatusCode();
            System.out.println("----------->status=" + status);

            if (status >= 200 && status < 300) {
                String resStr = EntityUtils.toString(en);
                log.info("返回结果：" + resStr);
                return resStr;
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        return httpClient;
    }

    public static String readHttpResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        builder.append("status:" + httpResponse.getStatusLine());
        builder.append("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            builder.append("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            builder.append("response length:" + responseString.length());
            builder.append("response content:" + responseString.replace("\r\n", ""));
        }
        return builder.toString();
    }

}
