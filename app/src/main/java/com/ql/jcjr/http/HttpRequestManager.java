package com.ql.jcjr.http;

import android.content.Context;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuchao on 2015/10/13.
 * <p/>
 * 处理请求
 */
public class HttpRequestManager {

    private static ConcurrentHashMap<Context, HttpSenderController>
            serviceControllers = new ConcurrentHashMap<Context, HttpSenderController>();

    public static HttpSenderController getRequestController(Context cls) {
        HttpSenderController serviceController = null;
        serviceController = new HttpSenderController(cls);
        serviceControllers.put(cls, serviceController);

        return serviceController;
    }

    /**
     * 根据传入参数请求服务器
     *
     * @param resultModel
     * @param.url = resultModel.url
     * @param.params = resultModel.params
     */
    public static void httpRequestService(final SenderResultModel resultModel, HttpSenderController.ViewSenderCallback callback, Context context) {
        getRequestController(context).httpService(resultModel, callback);

    }

    /**
     * 根据请求的url取消单个请求
     *
     * @param url resultmodel.requestEntity.url
     */
    public static void cancelRequest(Context cls, String url) {
        if (!serviceControllers.containsKey(cls)) return;

        HttpSenderController serviceController = serviceControllers.get(cls);
        serviceController.cancelHttpRequest(url);
        serviceControllers.remove(cls);
    }

    /**
     * 根据取消该页面的所有请求
     *
     * @param
     */
    public static void cancelAllRequest(Context cls) {
        if (!serviceControllers.containsKey(cls)) return;
        HttpSenderController serviceController = serviceControllers.get(cls);
        serviceController.cancelAllHttpRequest();
        serviceControllers.remove(cls);
    }
}
