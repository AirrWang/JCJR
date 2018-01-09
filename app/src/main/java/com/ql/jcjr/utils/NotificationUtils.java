package com.ql.jcjr.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.ql.jcjr.R;

import java.io.File;

/**
 * Created by LIUCHAO on 2016/4/19.
 */
public class NotificationUtils {

    private Context mContext;
    private static NotificationUtils instance;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private int notificationId = 108;

    private NotificationUtils(){
    }

    public static NotificationUtils getInstance(){
        if(instance == null) {
            instance = new NotificationUtils();
        }
        return instance;
    }

    public void sendNotify(Context context) {
        mContext = context;
        initNotification();
        //updateDownloadStatus();
    }

    private void initNotification() {
        notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setTicker("正在下载")
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    private PendingIntent getPendingIntent(int flag) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(DownloadAPK.getInstance().getApkPath()));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return PendingIntent.getActivity(mContext, 0, intent, flag);
    }

    /**
     * 开始下载
     */
    public void showStartDownloadNotify(){
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.notification_update_progress_loading) + 0 + "%")
                .setProgress(100, 0, false)
//                .setColor(mContext.getResources().getColor(R.color.c29_2cc490))
                .setShowWhen(false);
        Notification notification = mBuilder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        notificationManager.notify(notificationId, notification);
    }

    /**
     * 正在下载
     */
    public void showLoadingNotify(int value) {
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.notification_update_progress_loading) + value + "%")
                .setProgress(100,value,false)
//                .setColor(mContext.getResources().getColor(R.color.c29_2cc490))
                .setShowWhen(false);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        notificationManager.notify(notificationId, notification);
    }

    /**
     * 显示更新完毕通知栏，点击打开安装Apk
     */
    public void showDownloadFinishNotify() {
        mBuilder.setTicker("下载完成")
                .setContentTitle(mContext.getResources().getString(R.string.notification_update_finish_title))
                .setContentText(mContext.getResources().getString(R.string.notification_update_finish))
                .setContentIntent(getPendingIntent(Notification.FLAG_AUTO_CANCEL))
                .setProgress(0, 0, false)
                .setShowWhen(true)
                .setAutoCancel(true);
        Notification notification = mBuilder.build();
        notificationManager.notify(notificationId, notification);
    }

    /**
     * 下载失败的通知提醒
     */
    public void showDownloadFailNotify() {
        mBuilder.setTicker("下载失败")
                .setContentTitle(mContext.getResources().getString(R.string.notification_download_fail))
                .setContentText(mContext.getResources().getString(R.string.notification_download_fail))
                .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(), PendingIntent.FLAG_ONE_SHOT))
                .setProgress(0, 0, false)
                .setShowWhen(true);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationId, notification);
    }
}
