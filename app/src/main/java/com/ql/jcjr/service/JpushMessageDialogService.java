package com.ql.jcjr.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.view.CommonToast;

public class JpushMessageDialogService extends Service{

    public JpushMessageDialogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CommonToast.showUnCancelableDialog(JcbApplication.getInstance().getCurrentActivity(), "新通知",
                intent.getStringExtra("jpush_content"), "确定", false);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
