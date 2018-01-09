package com.ql.jcjr.http;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST;

public class HttpRequestEntity {
    /**
     * 请求的url
     */
    public String url;
    /**
     * 请求的参数
     */
    public RequestParams params;
    /**
     * 请求的类型
     */
    public HttpRequest.HttpMethod httpMethod = POST;
}
