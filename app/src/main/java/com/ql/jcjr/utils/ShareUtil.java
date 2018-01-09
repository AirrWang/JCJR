package com.ql.jcjr.utils;

import android.app.Activity;
import android.os.Environment;

import com.ql.jcjr.constant.AppConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.media.UMWeb;

import java.io.File;


/**
 * Created by Liuchao on 2016/9/25.
 */
public class ShareUtil {

    private static String path = Environment.getExternalStorageDirectory().getAbsoluteFile() +
            File.separator + AppConfig.APP_PICTURE_FOLDER;

    public static void shareWebLink(Activity activity){

//        UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
//        UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//        UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
//        UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
//        UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流

        UMWeb web = new UMWeb("");
        web.setTitle("This is music title");//标题
//        web.setThumb(new UMImage());  //缩略图
        web.setDescription("my description");//描述

        new ShareAction(activity).withMedia(web).share();
    }


//    public static void showShare(Context context, String url) {
//        ImageUtil.saveImageFromDrawable(context, path, AppConfigCommon.SHARE_IMAGE_NAME, R.mipmap.ic_launcher);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("标题");
//        // titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl("http://fanyi.baidu.com/");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("内容");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数 确保SDcard下面存在此张图片
//        oks.setImagePath(path + File.separator + AppConfigCommon.SHARE_IMAGE_NAME);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
////        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
////        oks.setComment("");
////        // site是分享此内容的网站名称，仅在QQ空间使用
////        oks.setSite(context.getString(R.string.app_name));
////        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
////        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.show(context);
//    }

    //我的二维码
//    public static void showMyQrShare(Context context, String url) {
//        ImageUtil.saveImageFromDrawable(context, path, AppConfigCommon.SHARE_QR_NAME, R.drawable.share_qr_img);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("二维码");
//        // titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("使用音频刷卡器、蓝牙刷卡器等新兴的支付方式，可以实现在手机上进行便捷付款");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数 确保SDcard下面存在此张图片
//        oks.setImagePath(path + File.separator + AppConfigCommon.SHARE_QR_NAME);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.show(context);
//    }

    //付款码
//    public static void sharePayQr(Context context, String url) {
//        ImageUtil.saveImageFromDrawable(context, path, AppConfigCommon.SHARE_QR_NAME, R.drawable.share_qr_img);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("二维码");
//        // titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("使用音频刷卡器、蓝牙刷卡器等新兴的支付方式，可以实现在手机上便捷进行付款");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数 确保SDcard下面存在此张图片
//        oks.setImagePath(path + File.separator + AppConfigCommon.SHARE_QR_NAME);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.show(context);
//    }

    //付款码
//    public static void sharePayQr(Context context, String fileName, Bitmap bitmap) {
//        FileUtil.writeBitmapToSD(path + File.separator + fileName, bitmap, true);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("");
//        // titleUrl是标题的网络链接，QQ和QQ空间等使用
////        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数 确保SDcard下面存在此张图片
//        oks.setImagePath(path + File.separator + fileName);
//        // url仅在微信（包括好友和朋友圈）中使用
////        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
////        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.show(context);
//    }

    //公众号
//    public static void sharePublicNUm(Context context) {
//        ImageUtil.saveImageFromDrawable(context, path, AppConfigCommon.SHARE_PUBLIC_NAME, R.drawable.public_num_img);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("");
//        // titleUrl是标题的网络链接，QQ和QQ空间等使用
////        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数 确保SDcard下面存在此张图片
//        oks.setImagePath(path + File.separator + AppConfigCommon.SHARE_PUBLIC_NAME);
//        // url仅在微信（包括好友和朋友圈）中使用
////        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
////        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.show(context);
//    }
}
