package com.ql.jcjr.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.ql.jcjr.constant.Global;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonLoadingDialog;

/**
 * ClassName: CommonDialogManager
 * Description:
 * Author: connorlin
 * Date: Created on 2016/9/28.
 */
public class CommonDialogManager {

    private Context mContext;
    private String mDialogText = Global.LOADING;
    private boolean mCancelable = true;
    private boolean mCancelableOutSide = false;
    private CommonLoadingDialog loadingDialog;
    private boolean mHasListener = false;

    private CommonDialogCancelListener mCommonDialogCancelListener;
    private CommonDialogParams mCommonDialogParams;

    public CommonDialogManager(Context context) {
        mContext = context;
    }

    public CommonDialogManager(Context context, String dialogText) {
        mContext = context;
        mDialogText = dialogText;
    }

    public CommonDialogManager(CommonDialogParams params) {
        mCommonDialogParams = params;
        mContext = params.getContext();
        mDialogText = params.getText();
    }

    public void show() {
        if (null == mCommonDialogParams || null == mCommonDialogParams.getContext()) {
            return;
        }

        hideLoading();
        if (loadingDialog != null) {
            loadingDialog.updateText(mCommonDialogParams.getText());
        }
        boolean cancelAble = mCommonDialogParams.isCancleAble();
        boolean cancelOutSide = mCommonDialogParams.isCancleTouchOutSideAble();
        showLoading(cancelAble, cancelOutSide, mCommonDialogParams.getText());
    }

    public void showLoading(String string) {
        hideLoading();
        reset();
        if (loadingDialog != null) {
            loadingDialog.updateText(string);
        }
        boolean cancelable = mHasListener;
        showLoading(cancelable, mCancelableOutSide, string);
    }

    public void dismiss() {
        hideLoading();
    }

    /**
     * 取消Loading View
     */
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void showLoading(final boolean cancelable, boolean outsideCancelable, String string) {
        mCancelable = cancelable;
        if (loadingDialog == null) {
            loadingDialog = new CommonLoadingDialog(mContext, string);
        }
        loadingDialog.setCancelable(mCancelable);// 需要根据是不是允许取消来设置dialog可以不可以取消
        loadingDialog.setCanceledOnTouchOutside(outsideCancelable);
        // 当取消dialog的时候，同时取消request请求
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                LogUtil.d("Load dialog is canceled !");
                if (mCommonDialogCancelListener != null) {
                    mCommonDialogCancelListener.onCommonDialogCancelListener();
                    mCommonDialogCancelListener = null;
                }
            }
        });
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode
                        && KeyEvent.ACTION_DOWN == event.getAction()) {
                    return !mCancelable;
                }
                return false;
            }
        });
        LogUtil.d("Loadding");
        // 防止因为网络加载速度比较慢，在子线程返回的时候，调用该sender的activity恰好finish，
        // 该activity还无法及时的取消request，引起该dialog无法加载
        try {
            loadingDialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    public void showLoading(boolean cancelable, boolean cancelableOutSide) {
        showLoading(cancelable, cancelableOutSide, mDialogText);
    }

    public void showLoading(String string, CommonDialogCancelListener listener) {
        mHasListener = true;
        showLoading(string);
        setOnCommonDialogCancelListener(listener);
        mHasListener = false;
    }

    public interface CommonDialogCancelListener {
        void onCommonDialogCancelListener();
    }

    public void setOnCommonDialogCancelListener(CommonDialogCancelListener listener) {
        mCommonDialogCancelListener = listener;
    }

    private void reset() {
        mCommonDialogCancelListener = null;
        mCancelable = false;
    }
}
