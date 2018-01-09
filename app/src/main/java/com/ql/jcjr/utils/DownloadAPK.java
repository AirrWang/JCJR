package com.ql.jcjr.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.lidroid.xutils.util.LogUtils;
import com.ql.jcjr.constant.AppConfig;

import java.io.File;

/**
 * 版本更新
 *
 * @author lz
 * @version 2014年11月12日 下午5:20:15
 */
public class DownloadAPK {
    public static final int DOWN_ERROR = 0;

    public static final int DOWNLAODSUCCESS = 10;
    private static final int DOWNLAODERROR = 11;
    private static final int DOWNLAODING = 12;
    private static final int DOWNLAODCANCEL = 13;
    private static final int DOWNLAODPAUSE = 12;


    private boolean downloadPause = false;
    private boolean downloadTaskCancel = false;

//    private List<InterfaceManager.DownLoadApkCallBack> downLoadApkCallBacks = new ArrayList<>();
//
//    public Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (downLoadApkCallBacks.isEmpty()) {
//                return;
//            }
//
//            switch (msg.what) {
//                case DOWNLAODSUCCESS:
//                    Object obj = msg.obj;
//                    String path = (String) obj;
//                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
//                        downLoadApkCallBacks.get(i).onLoadSuccess(path);
//                    }
//                    removeAllListener();
//                    break;
//                case DOWNLAODERROR:
//                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
//                        downLoadApkCallBacks.get(i).onLoadFail();
//                    }
//                    removeAllListener();
//                    break;
//                case DOWNLAODING:
//                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
//                        downLoadApkCallBacks.get(i).onLoading(msg.arg1);
//                    }
//                    break;
//                case DOWNLAODCANCEL:
//                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
//                        downLoadApkCallBacks.get(i).onLoadFail();
//                    }
//                    break;
//            }
//        }
//    };


    private static DownloadAPK downloadAPK;

    private DownloadAPK() {
    }

    public static DownloadAPK getInstance() {
        if (downloadAPK == null) {
            downloadAPK = new DownloadAPK();
        }
        return downloadAPK;
    }

//    public void registerDownloadListener(InterfaceManager.DownLoadApkCallBack callBack) {
//        downLoadApkCallBacks.add(callBack);
//    }
//
//    public void unRegisterDownLoadListener(InterfaceManager.DownLoadApkCallBack callBack) {
//        downLoadApkCallBacks.remove(callBack);
//    }
//
//    private void removeAllListener() {
//        downLoadApkCallBacks.clear();
//    }


    public void setDownlaodTaskCancel(){
        downloadTaskCancel = false;
    }

    public String getApkPath(){
        return Environment.getExternalStorageDirectory().getAbsoluteFile() + "/"
                + AppConfig.APP_UPDATE_FOLDER+"/"+ AppConfig.APP_UPDATE_NAME;
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public boolean isApkExists(String path) {
        try {
            File file = new File(path);
            if(!file.exists()){
                return false;
            }
        }catch(Exception e) {
            return false;
        }

        return true;
    }

    public boolean readApkInfoIsLaster(Context mcontext){
        String archiveFilePath=getApkPath();//安装包路径
        PackageManager pm = mcontext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info == null) {
            return false;
        }
        ApplicationInfo appInfo = info.applicationInfo;
        String packageName = appInfo.packageName;  //得到安装包名称
        String version= String.valueOf(info.versionCode);       //得到版本信息
        if(version.equals(CommonUtils.getAppVersionCode())){
            return false;
        }
        return true;
    }

    // 安装apk
    public void installApk(Context mContext, String apkPath) {
        if(apkPath == null){
            LogUtils.d("apkpath");
            return;
        }
        try{
//            SharedPreferencesUtils.getInstance(mContext).putBoolean("isUpdateSelf", true);
            Intent intent = new Intent();
            // 执行动作
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 执行的数据类型
            Uri uri = Uri.fromFile(new File(apkPath));
            //intent.setDataAndType(Uri.parse(apkPath), "application/vnd.android.package-archive");
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
            LogUtils.e("安装失败");
        }

    }
}
