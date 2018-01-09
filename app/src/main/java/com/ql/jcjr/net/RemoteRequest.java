package com.ql.jcjr.net;

import java.util.Map;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class RemoteRequest {

    private RemoteRequest() {
    }

    public static RemoteRequest getInstance() {
        return Singleton.instance;
    }

    public void invoke(Map jsonObject, RequestCallback requestCallback) {
        RequestRunnable request = RequestManager.getInstance()
                .createRequest(jsonObject, requestCallback);

        FreeThreadPool.getInstance().execute(request);
    }

    public void invoke(RequestRunnable request) {
        RequestManager.getInstance().addRequest(request);
        FreeThreadPool.getInstance().execute(request);
    }

    private static class Singleton {
        private static RemoteRequest instance = new RemoteRequest();
    }
}
