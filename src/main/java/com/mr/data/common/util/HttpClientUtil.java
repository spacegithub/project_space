package com.mr.data.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description:
 * @Date: Created in 2018/9/28 16:39
 */
@Slf4j
public class HttpClientUtil {
    private static RequestConfig config = null;
    private static String SERVER_URL = null;
    /**
     * 设置请求超时时常
     * @return
     */
    private static RequestConfig getConfig(){
        if (config==null) {
            int timeout = 90000;
            log.debug("init  http client connection timeout:"+timeout);
            config = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        }
        return config;
    }

    /**
     * 获取请求URL
     * @return
     */
    private static String getUrl(){
        if (SERVER_URL==null) {
            SERVER_URL = "http://localhost:8080/ectcispserver/api/entcreditapi/query";
//			SERVER_URL ="http://localhost:8080/ectcispserver/api/";
            log.debug("init server url:"+SERVER_URL);
        }
        return SERVER_URL;
    }

    /**
     * 根据url创建连接
     * @param url
     * @return
     */
    private static CloseableHttpClient getHttpClient(String url){
        if (url.toLowerCase().startsWith("https")) {
            return getSingleSSLConnection();
        }else{
            return getNoSSLConnection();
        }
    }

    /**
     * 发送报文至服务端
     * @param message
     * @return
     * @throws
     */
    public static String sendMsgByPost(String message) throws Exception{
        CloseableHttpResponse response = null;
        String url = getUrl();
        long start  = System.currentTimeMillis();
        log.info("*************start request ["+start+"]**************");
        log.info("request message:");
        log.info(message);
        try {
            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("User-Agent","Apache-HttpClient/4.5.1 (Java/1.6.0_16)");
//            httpPost.addHeader(header););
//            httpPost.addHeader("Content-Type", "Apache-HttpClient/4.5.1 (Java/1.6.0_16)");
            HttpEntity reqentity = new StringEntity(message, ContentType.create("application/jason", "utf-8") );
            httpPost.setEntity(reqentity);
            CloseableHttpClient httpClient = getHttpClient(url);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity,"utf-8");
            }else{
                throw new Exception("返回结果为空!");
            }
            EntityUtils.consume(entity);
            log.info("response message:");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.error("请求通讯错误",e);

        } finally{
            log.info("*************end request  ["+(System.currentTimeMillis()- start)+"]**************");
            if(response!=null)
                try {
                    response.close();
                } catch (IOException e) {
                }
        }
        return null;
    }


    public static String sendMapByPost(Map<String,String> paramMap , String url) throws Exception{
        CloseableHttpResponse response = null;
        long start  = System.currentTimeMillis();
        log.info("*************start request ["+start+"]**************");
        log.info("request message:");
        log.info(paramMap.toString());
        try {
            //map转换为String
            ObjectMapper objmapper = new ObjectMapper();
            String message = objmapper.writeValueAsString(paramMap);
            log.info(message);

            HttpPost httpPost = new HttpPost(url);
            // 创建参数队列
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key : paramMap.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            CloseableHttpClient httpClient = getHttpClient(url);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("response statusCode:");
            log.info(Integer.toString(statusCode));
            if (statusCode != 200) {
                httpPost.abort();
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity,"utf-8");
            }else{
                throw new Exception("返回结果为空!");
            }
            EntityUtils.consume(entity);
            log.info("response message:");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.error("请求通讯错误",e);

        } finally{
            log.info("*************end request  ["+(System.currentTimeMillis()- start)+"]**************");
            if(response!=null)
                try {
                    response.close();
                } catch (IOException e) {
                }
        }
        return null;
    }

    /**
     * 发送报文至服务端
     * @param message
     * @return
     * @throws
     */
    public static String sendMsgByGet(String message) throws Exception{
        CloseableHttpResponse response = null;
        String url = getUrl();
        long start  = System.currentTimeMillis();
        log.info("*************start request ["+start+"]**************");
        log.info("request message:");
        log.info(message);
        try {
            HttpGet httpGet = new HttpGet(url+"?"+message);

            httpGet.addHeader("User-Agent","Apache-HttpClient/4.5.1 (Java/1.6.0_16)");
//            httpPost.addHeader(header););
//            httpPost.addHeader("Content-Type", "Apache-HttpClient/4.5.1 (Java/1.6.0_16)");
            HttpEntity reqentity = new StringEntity(message,ContentType.create("application/jason", "utf-8") );
            //httpGet.(message);
            CloseableHttpClient httpClient = getHttpClient(url);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity,"utf-8");
            }else{
                throw new Exception("返回结果为空!");
            }
            EntityUtils.consume(entity);
            log.info("response message:");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.error("请求通讯错误",e);

        } finally{
            log.info("*************end request  ["+(System.currentTimeMillis()- start)+"]**************");
            if(response!=null)
                try {
                    response.close();
                } catch (IOException e) {
                }
        }
        return null;
    }
    /**
     * 创建普通http连接
     * @return
     */
    private static CloseableHttpClient getNoSSLConnection(){
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(getConfig()).build();
        return httpClient;
    }

    /**
     * 创建单向ssl的连接
     * @return
     * @throws
     */
    private static CloseableHttpClient getSingleSSLConnection(){
        CloseableHttpClient httpClient = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                         String paramString) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });
            httpClient =  HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(getConfig()).build();
            return httpClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建双向ssl的连接
     * @param keyStorePath
     * @param keyStorePass
     * @return
     * @throws
     */
    private static CloseableHttpClient getDualSSLConnection(String keyStorePath,String keyStorePass){
        CloseableHttpClient httpClient = null;
        try {
            File file = new File(keyStorePath);
            URL sslJksUrl = file.toURI().toURL();
            KeyStore keyStore  = KeyStore.getInstance("jks");
            InputStream is = null;
            try {
                is = sslJksUrl.openStream();
                keyStore.load(is, keyStorePass != null ? keyStorePass.toCharArray(): null);
            } finally {
                if (is != null) is.close();
            }
            SSLContext sslContext = new SSLContextBuilder().loadKeyMaterial(keyStore, keyStorePass.toCharArray())
                    .loadTrustMaterial(null,new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                                 String paramString) throws CertificateException {
                            return true;
                        }
                    })
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClient =  HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config).build();
            return httpClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
