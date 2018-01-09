package com.ql.jcjr.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ql.jcjr.constant.Global;
import com.ql.jcjr.manager.CommonDialogManager;
import com.ql.jcjr.manager.CommonDialogParams;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class RequestRunnable implements Runnable {

    private static final int MSG_SHOW_PROGRESS = 10;
    private static final int MSG_DISMISS_PROGRESS = 11;
    private static final int MSG_REQUEST_SUCCESS = 100;
    private static final int MSG_REQUEST_FAILED = 101;

    private Map mParamsMap;
    private RequestCallback mCallback;
    private String mResult = "";
    private boolean mOfflineServer = false;
    private boolean mAbort = false;

    private RequestHttpManager mHttpManager;
    private CommonDialogManager mCommonDialogManager;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    if (mCommonDialogManager != null) {
                        mCommonDialogManager.show();
                    }
                    break;

                case MSG_DISMISS_PROGRESS:
                    dismiss();
                    break;

                case MSG_REQUEST_SUCCESS:
                    if (mCallback != null) {
                        mCallback.onSuccess((String) msg.obj);
                    }
                    break;

                case MSG_REQUEST_FAILED:
                    if (mCallback != null) {
                        mCallback.onFailure((String) msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public RequestRunnable(Context context, Map paramsMap, RequestCallback callback) {
        this(paramsMap, callback);
        if (context != null) {
            CommonDialogParams params = new CommonDialogParams.Builder(context)
                    .setCancleAble(true)
                    .setCancleTouchOutSideAble(false)
                    .build();
            initProgress(params);
        }
    }

    /**
     *  没有加载框
     * @param paramsMap
     * @param callback
     */
    public RequestRunnable(Map paramsMap, RequestCallback callback) {
        mParamsMap = paramsMap;
        mCallback = callback;
        mHttpManager = new RequestHttpManager();

        mOfflineServer = mCallback.getMsgCode().equals(AppConfigCommon.DEVICE_SIGN_REQUEST_CODE)
                || mCallback.getMsgCode().equals(AppConfigCommon.OFFLINE_TRANSACTION_REQUEST_CODE);
    }

    private void initProgress(CommonDialogParams params) {
        mCommonDialogManager = new CommonDialogManager(params);
        mCommonDialogManager.setOnCommonDialogCancelListener(
                new CommonDialogManager.CommonDialogCancelListener() {
                    @Override
                    public void onCommonDialogCancelListener() {
                        LogUtil.d("cancel connect");
                        abort();
                        dismiss();

                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                });
    }

    public void abort() {
        LogUtil.d("abort");
        mAbort = true;
        if (mHttpManager != null) {
            mHttpManager.disConnect();
        }
    }

    private void dismiss() {
        if (mCommonDialogManager != null) {
            mCommonDialogManager.dismiss();
        }
    }

    public RequestRunnable(CommonDialogParams params, Map paramsMap, RequestCallback callback) {
        this(paramsMap, callback);
        if (params.getContext() != null) {
            initProgress(params);
        }
    }

    @Override
    public void run() {
        showProgress();

        if (mCallback == null) {
            dismissProgress();
            return;
        }

        try {
            if (mOfflineServer) {
                mResult = mHttpManager.accessOfflineServer(mParamsMap, mCallback);
            } else {
                mResult = mHttpManager.accessNetworkByPost(mParamsMap, mCallback);
            }
            if (mAbort) {
                mAbort = false;
                dismissProgress();
                return;
            }

            if (dealErr()) {
                dismissProgress();
                return;
            }

            JSONObject object = new JSONObject(mResult);
            String code = (String) object.get(Global.RESULT_CODE);
            if (Global.RESULT_SUCCESS.equals(code)) {
                success(mResult);
            } else {
                failed((String) object.get(Global.RESULT_MESSAGE));
            }
            dismissProgress();
        } catch (JSONException e) {
            exceptioin(e);
            return;
        }
    }

    private void showProgress() {
        mHandler.sendEmptyMessage(MSG_SHOW_PROGRESS);
    }

    private void dismissProgress() {
        mHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS);
    }

    private boolean dealErr() {
        if (Global.RESULT_CONNECT_EXCEPTION.equals(mResult)) {
            failed(Global.RESULT_CONNECT_EXCEPTION);
            return true;
        } else if (Global.RESULT_CONNECT_TIMEOUT.equals(mResult)) {
            failed(Global.RESULT_CONNECT_TIMEOUT);
            return true;
        }else if (Global.SOCKET_CONNECT_TIMEOUT.equals(mResult)) {
            failed(Global.SOCKET_CONNECT_TIMEOUT);
            return true;
        }else if (Global.RESULT_EXCEPTION.equals(mResult)) {
            failed(Global.RESULT_EXCEPTION);
            return true;
        }

        return false;
    }

    private void success(String result) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_REQUEST_SUCCESS;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    private void failed(String failedMsg) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_REQUEST_FAILED;
        msg.obj = failedMsg;
        mHandler.sendMessage(msg);
    }

    private void exceptioin(Exception e) {
        e.printStackTrace();
        LogUtil.i("doInBackground exception:" + e.toString());
        failed(Global.RESULT_CONNECT_EXCEPTION);
        dismissProgress();
    }

}
