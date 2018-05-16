package com.ql.jcjr.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.util.LogUtils;
import com.ql.jcjr.activity.NetNullActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonLoadingDialog;
import com.ql.jcjr.view.CommonToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuchao on 2015/10/13.
 * <p/>
 * 处理请求
 */
public class HttpSenderController {
    //请求的url地址
    public ConcurrentHashMap<String, HttpHandler>
            tasks = new ConcurrentHashMap<String, HttpHandler>();
    private Context mContext;
    private CommonLoadingDialog loadingDialog;
    /**
     * senderResult数据
     */
    //private HYSenderResultModel resultModel;
    /**
     * responseEntity
     */
    private ResponseEntity responseEntity;
    private HttpHandler mCurrentHandler;
    /**
     * 设置刷新UI的接口
     */
    private ViewSenderCallback viewCallback;

    @SuppressLint("HandlerLeak")
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            uiThreadReceiveLocalRequest(msg);
        }
    };

    public HttpSenderController(Context context) {
        this.mContext = context;
    }

    /**
     * @return the HYResponseEntity
     */
    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    /**
     * @param responseEntity the HYResponseEntity to set
     */
    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    private void setViewCallback(ViewSenderCallback callback) {
        this.viewCallback = callback;
    }


    public HttpHandler getCurrentHandler() {
        return mCurrentHandler;
    }

    /**
     * 根据传入参数请求服务器
     *
     * @param resultModel
     * @param.url = resultModel.url
     * @param.params = resultModel.params

     */
    public void httpService(final SenderResultModel resultModel, ViewSenderCallback callback) {

        if (resultModel == null) {
            return;
        }

        setViewCallback(callback);

        MyHttpUtils httpUtils = new MyHttpUtils();
        httpUtils.setHttpRequestCallBack(new MyHttpUtils.HttpRequestCallBack() {
            @Override
            public void onStart() {
                LogUtils.d("Http request url = " + resultModel.requestEntity.url);
                if (resultModel.isShowLoadding){
                    showLoading(resultModel);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {

            }

            @Override
            public void onCancelled() {
                hideLoading();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {

//                String decryptData = RSAEncrypt.decryptData(responseInfo.result.toString());
                ResponseEntity entity = new ResponseEntity();
                entity.isError = false;
                entity.url = resultModel.requestEntity.url;
//                entity.responeJson = decryptData;
                entity.responeJson = responseInfo.result.toString();
                LogUtil.i("http_result:" + entity.responeJson);
                sendMessageToUI(entity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.i("onFailure e = " + e);
                hideLoading();
                ResponseEntity entity = new ResponseEntity();
                entity.errorInfo = s;
                entity.exception = e;
                entity.isError = true;
                entity.url = resultModel.requestEntity.url;
//                LogUtil.i("请求网址 = " + resultModel.requestEntity.url);

                sendMessageToUI(entity);
                String a=e+"";
                if (a.contains("No address associated with hostname")){
                    Intent intent=new Intent(mContext,NetNullActivity.class);

                    mContext.startActivities(new Intent[]{intent});
                }
            }
        });
        mCurrentHandler = httpUtils.executeRequest(resultModel.requestEntity.httpMethod, resultModel.requestEntity);
        tasks.put(resultModel.requestEntity.url, mCurrentHandler);
    }

    /**
     * 取消请求
     *
     * @param url
     */
    public void cancelHttpRequest(String url) {
            this.hideLoading();
        if (tasks.contains(url)) {
            tasks.remove(tasks.get(url));
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelAllHttpRequest() {
        Iterator<Map.Entry<String, HttpHandler>> it = tasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, HttpHandler> map = (Map.Entry<String, HttpHandler>) it.next();
            cancelHttpRequest(map.getKey());
        }
    }

    /**
     * 显示Loading View
     *
     * @param resultModel
     */
    private void showLoading(final SenderResultModel resultModel) {

        if ((resultModel.isShowLoadding) || resultModel.isShowLoaddingAnyway) {
            loadingDialog = new CommonLoadingDialog(mContext);
            loadingDialog.setCancelable(resultModel.isShowCancel);// 需要根据是不是允许取消来设置dialog可以不可以取消
            loadingDialog.setCanceledOnTouchOutside(resultModel.isShowCancel);
            // 当取消dialog的时候，同时取消request请求
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    LogUtils.d("Load dialog is canceled !");

                }
            });
            LogUtils.d("Loadding");
            //防止因为网络加载速度比较慢，在子线程返回的时候，调用该sender的activity恰好finish，
            // 该activity还无法及时的取消request，引起该dialog无法加载
            try {
                loadingDialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 取消Loading View
     */
    private void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void uiThreadReceiveRemoteRequest(Message msg) {

    }

    public void uiThreadReceiveLocalRequest(Message msg) {
        // UI线程
        if (msg == null) {
            return;
        }
        ResponseEntity entity = (ResponseEntity) msg.obj;

        cancelHttpRequest(entity.url);

        if (loadingDialog != null && loadingDialog.isShowing()) {
            this.hideLoading();
        }
        if (!entity.isError) {

            try {
                LogUtil.i("返回参数 " + entity.responeJson);
                JSONObject object = new JSONObject(entity.responeJson);
                String code = (String) object.get(Global.RESULT_CODE);
                if (Global.RESULT_SUCCESS.equals(code)) {
                    success(entity.responeJson);
                } else if (Global.RESULT_TOKEN_WRONG.equals(code)){
                    if (!UserData.getInstance().isLogin()) {

                    }else {
                        CommonToast.showTokenWrongDialog(mContext, "您的账号在另一台设备上登陆，您已被迫下线，请重新登陆");
                    }
                }
                else {
                    entity.errorInfo = (String) object.get(Global.RESULT_MESSAGE);
                    failure(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
//            failure(entity);
        }
    }


    public void sendMessageToUI(ResponseEntity entity) {
        Message msg = new Message();
        msg.obj = entity;
        mainHandler.sendMessage(msg);
    }


    private void success(String responeJson) {
        if (viewCallback != null) {
            viewCallback.onSuccess(responeJson);
        }
    }

    private void failure(ResponseEntity entity) {
        if (viewCallback != null) {
            viewCallback.onFailure(entity);
        }
    }

    /**
     * 数据请求成功后，刷新UI的回调接口
     */
    public interface ViewSenderCallback {
        void onSuccess(String responeJson);

        void onFailure(ResponseEntity entity);
    }
}
