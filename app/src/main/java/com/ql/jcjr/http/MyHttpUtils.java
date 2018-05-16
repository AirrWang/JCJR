package com.ql.jcjr.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by liuchao on 2015/10/14.
 * <p/>
 * 处理Http的类
 */
public class MyHttpUtils {

    private int token;

    private static MyHttpUtils hyHttpUtils;
    private static HttpUtils httpUtils;
    private HttpRequestCallBack requestCallBack;

    public synchronized static MyHttpUtils shareInstance() {
        if (hyHttpUtils == null) {
            hyHttpUtils = new MyHttpUtils();
        }
        return hyHttpUtils;
    }

    /**
     * 单例HttpUtils类
     *
     * @return
     */
    public synchronized static HttpUtils getHttpUtilsInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }


    /**
     * @return the token
     */
    public int getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(int token) {
        this.token = token;
    }

    public void setHttpRequestCallBack(HttpRequestCallBack callBack) {
        this.requestCallBack = callBack;
    }

    /**
     * 请求服务器
     *
     * @param method        请求方式
     * @param requestEntity 请求的参数
     */
    public HttpHandler executeRequest(HttpRequest.HttpMethod method, HttpRequestEntity requestEntity) {


        getHttpUtilsInstance().configCurrentHttpCacheExpiry(1000 * 60);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        getHttpUtilsInstance().configTimeout(60 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        getHttpUtilsInstance().configSoTimeout(60 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间

        HttpHandler handler = getHttpUtilsInstance().send(method, requestEntity.url, requestEntity.params, new RequestCallBack<Object>() {
            @Override
            public void onStart() {
                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onStart();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onLoading(total, current, isUploading);
            }

            @Override
            public void onCancelled() {
                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onCancelled();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onSuccess(responseInfo);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onFailure(e, s);
            }

        });
        return handler;
    }

    public interface HttpRequestCallBack {

        void onStart();

        void onLoading(long total, long current, boolean isUploading);

        void onCancelled();

        void onSuccess(ResponseInfo<Object> responseInfo);

        void onFailure(HttpException e, String s);

    }
}
