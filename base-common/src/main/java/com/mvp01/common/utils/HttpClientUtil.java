package com.mvp01.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by liudx on 16/1/12.
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public enum HTTPMethod {
        GET, POST, PUT, DELETE
    }

    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, null, params);
    }

    public static String doGet(String url, List<Header> headers, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            sb.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        try {
            URI uri = new URI(sb.toString());
            return sendRequest(uri, headerListToArr(headers), null, HTTPMethod.GET);
        } catch (URISyntaxException e) {
            logger.error("HTTP-Get:" + sb.toString(), e);
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url, List<Header> headers, String argsJson) {
        try {
            URI uri = new URI(url);
            StringEntity entity = null;
            if(StringUtils.isNotBlank(argsJson)){
                entity = new StringEntity(argsJson, ContentType.APPLICATION_JSON);
            }
            return sendRequest(uri, headerListToArr(headers), entity, HTTPMethod.POST);
        } catch (URISyntaxException e) {
            logger.error("HTTP-Post:" + url, e);
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url, String json) {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
        return doPost(url, headers, json);
    }

    public static String doPost(String url, List<NameValuePair> params) {
        try {
            URI uri = new URI(url);
            Header[] headers = {new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")};
            HttpEntity entity = null;
            if(params!=null){
                entity = new UrlEncodedFormEntity(params, "utf-8");
            }
            return sendRequest(uri, headers, entity, HTTPMethod.POST);
        } catch (Exception e) {
            logger.error("HTTP-Post:" + url, e);
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url, Map params) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Set<Map.Entry> entrys = params.entrySet();
        Iterator<Map.Entry> it = entrys.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            nameValuePairs.add(new BasicNameValuePair(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
        }
        return doPost(url, nameValuePairs);
    }

    public static String doPut(String url, List<Header> headers, String argsJson) {
        try {
            URI uri = new URI(url);
            StringEntity entity = null;
            if(StringUtils.isNotBlank(argsJson)){
                entity= new StringEntity(argsJson, ContentType.APPLICATION_JSON);
            }
            return sendRequest(uri, headerListToArr(headers), entity, HTTPMethod.PUT);
        } catch (URISyntaxException e) {
            logger.error("HTTP-Put:" + url, e);
            e.printStackTrace();
        }
        return null;
    }

    public static String doDelete(String url, List<Header> headers) {
        try {
            URI uri = new URI(url);
            return sendRequest(uri,headerListToArr(headers),null, HTTPMethod.DELETE);
        } catch (URISyntaxException e) {
            logger.error("HTTP-Delete:"+url,e);
            e.printStackTrace();
        }
        return null;
    }

    public static String sendRequest(URI uri, Header[] headers, HttpEntity data, HTTPMethod method) {
        HttpClient httpClient = getHttpClient(uri);
        String responseContent = null;
        try {
            HttpResponse httpResponse = null;
            switch (method) {
                case GET:
                    HttpGet httpGet = new HttpGet(uri);
                    if (null != headers && headers.length > 0)
                        httpGet.setHeaders(headers);
                    httpResponse = httpClient.execute(httpGet);
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(uri);
                    if (null != headers && headers.length > 0)
                        httpPost.setHeaders(headers);
                    if (null != data)
                        httpPost.setEntity(data);
                    httpResponse = httpClient.execute(httpPost);
                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(uri);
                    if (null != headers && headers.length > 0)
                        httpPut.setHeaders(headers);
                    if (null != data)
                        httpPut.setEntity(data);
                    httpResponse = httpClient.execute(httpPut);
                    break;
                case DELETE:
                    HttpDelete httpDelete = new HttpDelete(uri);
                    if (null != headers && headers.length > 0)
                        httpDelete.setHeaders(headers);
                    httpResponse = httpClient.execute(httpDelete);
                    break;
                default:
                    logger.error("方法参数不合法");
                    throw new RuntimeException("请求需要合法的http方法类型");
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            if (null != httpEntity) {
                responseContent = EntityUtils.toString(httpEntity, "UTF-8");
                EntityUtils.consume(httpEntity);
            }
        } catch (IOException e) {
            logger.error("HTTP请求异常,{}-{}", method, uri.toString(), e);
            e.printStackTrace();
        }
        return responseContent;
    }

    public static HttpClient getHttpClient(URI uri) {
        HttpClient httpClient = null;
        if (!"https".equals(uri.getScheme())) {
            httpClient = HttpClientBuilder.create().build();
        } else {
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            try {
                SSLContext sct = SSLContext.getInstance("TLS");
                sct.init(null, new TrustManager[]{xtm}, null);
                SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sct);
                httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpClient;
    }

    private static Header[] headerListToArr(List<Header> headerList) {
        Header[] headers = null;
        if (headerList != null && headerList.size() > 0) {
            headers = new Header[headerList.size()];
            headers = headerList.toArray(headers);
        }
        return headers;
    }
}
