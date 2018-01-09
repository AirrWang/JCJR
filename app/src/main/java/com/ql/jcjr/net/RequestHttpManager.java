package com.ql.jcjr.net;

import com.ql.jcjr.constant.Global;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.JsonUtils;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.crypt.AesUtil;
import com.ql.jcjr.utils.crypt.RSAEncrypt;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class RequestHttpManager {

    private HttpPost httpPost;
    private DefaultHttpClient httpClient;

    public String accessNetworkByPost(Map paramsMap, RequestCallback callback) {
        String result = Global.RESULT_EXCEPTION;
        String requestParam = JsonUtils.setJson(paramsMap);
        requestParam = requestParam.replace(" ", "");

        LogUtil.i("requestParam ==== " + requestParam);

        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("code", callback.getMsgCode()));
        paramList.add(new BasicNameValuePair("content", RSAEncrypt.encryptData(requestParam)));

        httpClient = new DefaultHttpClient();
        // 链接超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                Global.NET_TIMEOUT_MILLIS);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                Global.SOCKET_TIMEOUT_MILLIS);
        try {
            httpPost = new HttpPost(AppConfigCommon.REQUEST_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (Global.RESULT_CODE_200 == statusCode) {
                String resultString = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                result = RSAEncrypt.decryptData(resultString);
            }
        } catch (ClientProtocolException e) {
            LogUtil.e("ClientProtocolException = " + e);
        } catch (HttpHostConnectException e) {
            LogUtil.e("HttpHostConnectException = " + e);
            result = Global.RESULT_CONNECT_EXCEPTION;
        } catch (SocketTimeoutException e) {
            LogUtil.e("SocketTimeoutException = " + e);
            result = Global.SOCKET_CONNECT_TIMEOUT;
        } catch (ConnectTimeoutException e) {
            LogUtil.e("ConnectTimeoutException = " + e);
            result = Global.RESULT_CONNECT_TIMEOUT;
        } catch (IOException e) {
            LogUtil.e("IOException = " + e);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Exception = " + e);
        } finally {
            disConnect();
        }
        return result.trim();
    }

    public void disConnect() {
        LogUtil.d("disConnect");
        try {
            if (null != httpClient) {
                httpClient.getConnectionManager().shutdown();
            }

            if (null != httpPost) {
                httpPost.abort();
            }
        } catch (Exception e) {
            LogUtil.e("abort exception = " + e);
        }
    }

    public String accessOfflineServer(Map paramsMap, RequestCallback callback) {
        String result = Global.RESULT_EXCEPTION;
        String requestParam = JsonUtils.setJson(paramsMap);
        requestParam = requestParam.replace(" ", "");
        LogUtil.i("requestParam ==== " + requestParam);

        List<NameValuePair> paramList = new ArrayList<>();
        requestParam =
                AesUtil.encryptString1(requestParam, AesUtil.encryptToMD5(AppConfigCommon.AES_Password));
        paramList.add(new BasicNameValuePair("requestParam", callback.getMsgCode() + requestParam));

        httpClient = new DefaultHttpClient();
        // 链接超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                Global.NET_TIMEOUT_MILLIS);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                Global.SOCKET_TIMEOUT_MILLIS);
        try {
            httpPost = new HttpPost(AppConfigCommon.OFFLINE_TRANSACTION_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            LogUtil.i("statusCode = " + statusCode);
            if (Global.RESULT_CODE_200 == statusCode) {
                String resultString = EntityUtils.toString(httpResponse.getEntity());
                result = AesUtil.decryptString1(resultString, AesUtil.encryptToMD5(AppConfigCommon.AES_Password));
            }
        } catch (ClientProtocolException e) {
            LogUtil.i("ClientProtocolException = " + e);
        } catch (HttpHostConnectException e) {
            LogUtil.i("HttpHostConnectException = " + e);
            result = Global.RESULT_CONNECT_EXCEPTION;
        } catch (SocketTimeoutException e) {
            LogUtil.i("SocketTimeoutException = " + e);
            result = Global.SOCKET_CONNECT_TIMEOUT;
        } catch (ConnectTimeoutException e) {
            LogUtil.i("ConnectTimeoutException = " + e);
            result = Global.RESULT_CONNECT_TIMEOUT;
        } catch (IOException e) {
            LogUtil.i("IOException = " + e);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("Exception = " + e);
        } finally {
            disConnect();
        }
        return result.trim();
    }
}

