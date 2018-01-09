package com.ql.jcjr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.lidroid.xutils.util.LogUtils;
import com.ql.jcjr.application.JcbApplication;

/**
 * 监听网络发生变化的广播
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            LogUtils.d("Current network have changed.");
            if (JcbApplication.getInstance().getCurrentActivity() != null) {
                LogUtils.d("Notify current activity network changed.");
                //true 代表网络断开 false 代表网络没有断开
                boolean isBreak =
                        intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//                ((BaseActivity) FreePayApplication.getInstance().getCurrentActivity())
// .handlerNetworkChanged(isBreak);
            }
        }

    }
}
