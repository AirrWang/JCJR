package com.ql.jcjr.http;

import com.lidroid.xutils.http.RequestParams;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * ClassName: Sender
 * Description:
 * Author: liuchao
 * Date: Created on 202017/6/26.
 */

public class SenderManager {

    private static void httpSenderService(SenderResultModel resultModel, HttpRequestEntity entity) {
        resultModel.setHttpRequestEntity(entity);
    }

    private static SenderResultModel createHttpRequestModel() {
        SenderResultModel resultModel = new SenderResultModel();
        return resultModel;
    }

    public static SenderResultModel buildResultModel(RequestParams params, String url) {
        HttpRequestEntity entity = new HttpRequestEntity();
        entity.url = url;
        entity.params = params;

        SenderResultModel resultModel = createHttpRequestModel();

        httpSenderService(resultModel, entity);

        return resultModel;
    }

    public static SenderResultModel buildResultModel(RequestParams params, String url, boolean isShowLoading) {
        HttpRequestEntity entity = new HttpRequestEntity();
        entity.url = url;
        entity.params=params;

        SenderResultModel resultModel = createHttpRequestModel();
        resultModel.isShowLoadding = isShowLoading;

        httpSenderService(resultModel, entity);

        return resultModel;
    }

    public static RequestParams buildRequestParams(JSONObject jsonObject) {
        String token= UserData.getInstance().getUSERID();
        try {
            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.i("请求参数 " + jsonObject.toString());
        RequestParams params = new RequestParams();
//        String content = RSAEncrypt.encryptData(jsonObject.toString());
        buildRequestParams(jsonObject, params);
        return params;
    }

    private static void buildRequestParams(JSONObject jsonObject, RequestParams params) {
        for (Iterator<String> keyStr = jsonObject.keys(); keyStr.hasNext(); ) {
            try {
                String key = keyStr.next().trim();
                Object value = jsonObject.get(key);
                params.addBodyParameter(key, (String) value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

//    public static RequestParams buildRequestParams(String code, String method,JSONObject jsonObject) {
//        LogUtil.i("请求参数 " + jsonObject);
//        RequestParams params = new RequestParams();
////        String content = RSAEncrypt.encryptData(jsonObject.toString());
//        params.addBodyParameter(Global.REQUEST_CODE, code);
//        params.addBodyParameter(Global.REQUEST_METHOD, method);
////        params.addBodyParameter(Global.REQUEST_BIZ_CONTENT, content);
//        params.addBodyParameter(Global.REQUEST_BIZ_CONTENT, jsonObject.toString());
//        return params;
//    }
//
//    public static RequestParams buildRequestParams(String code, String method, String jsonObject) {
//        LogUtil.i("请求参数 " + jsonObject.toString());
//        RequestParams params = new RequestParams();
////        String content = RSAEncrypt.encryptData(jsonObject.toString());
//        params.addBodyParameter(Global.REQUEST_CODE, code);
//        params.addBodyParameter(Global.REQUEST_METHOD, method);
////        params.addBodyParameter(Global.REQUEST_BIZ_CONTENT, content);
//        params.addBodyParameter(Global.REQUEST_BIZ_CONTENT, jsonObject);
//        return params;
//    }
}
