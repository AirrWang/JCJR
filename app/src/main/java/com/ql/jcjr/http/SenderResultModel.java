package com.ql.jcjr.http;

public class SenderResultModel {
    public int token;

    public boolean isShowError = true;
    public boolean isShowCancel = true;
    public boolean isShowLoadding = true;
    //有些操作会较慢，所以需要在任何网络环境下都显示loading状态
    public boolean isShowLoaddingAnyway = false;
    public String textLoadding = "正在加载中..";
    public String textLoaddingTitle = "";

    /**
     * 用来传递请求参数
     */
    public HttpRequestEntity requestEntity;

    /**
     * 用来设置请求参数
     */
    public void setHttpRequestEntity(HttpRequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

}
