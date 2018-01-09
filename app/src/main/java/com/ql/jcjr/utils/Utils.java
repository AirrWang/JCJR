package com.ql.jcjr.utils;

import android.content.Context;
import android.os.Environment;

import com.ql.jcjr.R;
import com.ql.jcjr.constant.AppConfig;

import java.io.File;

public class Utils {
    public static File doPickPhotoAction(Context context, String photoName) {
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return doTakePhoto(context, photoName);
//        } else {
//            ToastUtil.showToast(context, R.string.no_sdcard);
//        }
//        return null;
    }

    public static File doTakePhoto(Context context, String photoName) {
        File file = null;
        try {
            String fileName = photoName + ".jpg";
            file = new File(getPhotoDir(), fileName);
            LogUtil.i("fileName = " + fileName);
            LogUtil.i("file = " + file);
        } catch (Exception e) {
            ToastUtil.showToast(context, R.string.no_camera_app);
        }

        return file;
    }

    private static File getPhotoDir() {
        File photoDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                + "/" + AppConfig.APP_PICTURE_FOLDER);
        if (!photoDir.exists()) {
            photoDir.mkdirs();
        }

        return photoDir;
    }

}
