package com.ql.jcjr.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.ql.jcjr.utils.DownloadAPK;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.NotificationUtils;
import com.ql.jcjr.utils.SharedPreferencesUtils;

import java.io.File;

public class DownloadApkService extends Service {

    private static HttpUtils httpUtils;

    private String DOWNLOAD_URL;
    private String fileSize;

    public DownloadApkService() {
    }

    public synchronized static HttpUtils getHttpUtilsInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }


    private static DownloadCallBack hyRequestCallBack;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotificationUtils();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DOWNLOAD_URL = intent.getStringExtra("download_url");
        fileSize = intent.getStringExtra("file_size");
        downloadApk();
        return START_STICKY;
    }

    private void initNotificationUtils() {
        NotificationUtils.getInstance().sendNotify(getApplicationContext());
    }

    private void downloadApk() {
        getHttpUtilsInstance().download(DOWNLOAD_URL, DownloadAPK.getInstance().getApkPath(), true, new RequestCallBack<File>() {

            @Override
            public void onStart() {
                NotificationUtils.getInstance().showStartDownloadNotify();
                hyRequestCallBack.onDownloadStart();

                saveDownLoadStatus("onStart");
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                NotificationUtils.getInstance().showDownloadFinishNotify();
                hyRequestCallBack.onDownloadSuccess(responseInfo);
                removeDownloadCallBack();
                saveDownLoadStatus("onSuccess");
                stopSelf();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                LogUtil.i("service total = " + total);
                LogUtil.i("service current = " + current);

                total = Integer.valueOf(fileSize);
                int progress = (int) ((float) current / total * 100);
                LogUtil.i("service total = " + progress);
                NotificationUtils.getInstance().showLoadingNotify(progress);
                hyRequestCallBack.onLoading(total, current, isUploading);
                saveDownLoadStatus("onLoading");
            }

            @Override
            public void onCancelled() {
                super.onCancelled();
                saveDownLoadStatus("onCancelled");
                removeDownloadCallBack();
                stopSelf();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                NotificationUtils.getInstance().showDownloadFailNotify();
                saveDownLoadStatus("onFailure");
                hyRequestCallBack.onDownloadFailure(e, s);
                removeDownloadCallBack();
                stopSelf();
            }
            });
    }

    private void saveDownLoadStatus(String value) {
        SharedPreferencesUtils.getInstance(getApplicationContext(),
                SharedPreferencesUtils.PHARMACY_CLEAR).putString(SharedPreferencesUtils.KEY_DOWNLOAD_APK_STATUS, value);
    }

    public interface DownloadCallBack {
        void onDownloadStart();

        void onDownloadSuccess(ResponseInfo<File> responseInfo);

        void onLoading(long total, long current, boolean isUploading);

        void onDownloadFailure(HttpException e, String s);
    }

    public static void setDownloadCallBack(DownloadCallBack callBack) {
        hyRequestCallBack = callBack;
    }

    public static void removeDownloadCallBack() {
        if(hyRequestCallBack != null){
            hyRequestCallBack = null;
        }
    }
}
