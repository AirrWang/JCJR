package com.ql.jcjr.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.PopupWindow;

import com.ql.jcjr.activity.NetNullActivity;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.view.CommonDialog;


/**
 * 监听网络发生变化的广播
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    private Context mContext;
    private static final int HANDLER_PAGE = 1;
    private Boolean isShow=false;
    private PopupWindow popupWindow;
    /**
     * Handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_PAGE:
                    checkNet(mContext);
                    break;
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        if (intent != null && ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            LogUtils.d("Current network have changed.");
//            if (JcbApplication.getInstance().getCurrentActivity() != null) {
//                LogUtils.d("Notify current activity network changed.");
//                //true 代表网络断开 false 代表网络没有断开
//                boolean isBreak =
//                        intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//                boolean a=isBreak;
// ((BaseActivity) FreePayApplication.getInstance().getCurrentActivity())
// .handlerNetworkChanged(isBreak);
//            }
            handler.sendEmptyMessageDelayed(HANDLER_PAGE, 1500);
        }


    }

    private void checkNet(Context context) {
        if (isNetworkConnected(context)){

        }else {
            Intent intent=new Intent(context,NetNullActivity.class);

            context.startActivities(new Intent[]{intent});

//            if (isShow){
//
//            }else {
//                initPop(context);
//                showNetDialog(context);
//            }
        }
    }

    private void initPop(Context context) {



    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    private void showNetDialog(Context mContext){
        if (mContext==null){
            mContext= JcbApplication.appContext;
        }
        CommonDialog.Builder builder = new CommonDialog.Builder(mContext);
        final Context finalMContext = mContext;

        builder.setTitle("温馨提示")
                .setCancelable(false)
                .setMessage("您需要连接网络后才能继续使用服务，确认已经连接网络？")
                .setPositiveButton("立即刷新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isShow=false;
                        checkNet(finalMContext);
                    }
                })
                .setNegativeButton("解决", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isShow=false;
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        finalMContext.startActivity(intent);
                    }
                });

        builder.create().show();
        isShow=true;
    }


}
