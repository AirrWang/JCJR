package com.ql.jcjr.net;

/**
 * Created by Liuchao on 2016/9/23.
 */
public abstract class RequestCallback {

    private String mMsgCode;
    private int mPageIndex;

    public RequestCallback() {
    }

    public RequestCallback(String msgcode) {
        mMsgCode = msgcode;
    }

    public RequestCallback(String msgcode, int page) {
        mMsgCode = msgcode;
        mPageIndex = page;
    }

    public String getMsgCode() {
        return mMsgCode;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public abstract void onSuccess(String data);

    public abstract void onFailure(String erroMessageg);

    public void onCancel() {

    }
}
