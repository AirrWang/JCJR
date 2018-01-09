package com.ql.jcjr.utils;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;


/**
 * Created by Liuchao on 2016/9/25.
 */
public class ShareHelper implements ShareBoardlistener, UMShareListener{

    private Activity mActivity;
    private int shareType;
    private final int SHARE_TYPE_WEB = 1;
    private UMWeb umWeb;
    private ShareAction mShareAction;

    public ShareHelper(Activity activity){
        mActivity = activity;
        mShareAction = new ShareAction(activity).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.SMS)
                .setShareboardclickCallback(this).setCallback(this);
    }

    public void setShareWebInfo(String url, String imgUrl, String shareTitle, String shareContent){
        shareType = SHARE_TYPE_WEB;
        umWeb = new UMWeb(url);
        umWeb.setTitle(shareTitle);//标题
        UMImage image = new UMImage(mActivity, imgUrl);//网络图片
        //        UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
//        UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//        UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
//        UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
//        UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
        umWeb.setThumb(image);  //缩略图
        umWeb.setDescription(shareContent);//描述
    }

    public void setShareContent(String shareTitle, String shareContent){
        shareType = SHARE_TYPE_WEB;
        umWeb.setTitle(shareTitle);//标题
        umWeb.setDescription(shareContent);//描述
    }

    public void share(){
        mShareAction.open();
    }

    @Override
    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
        if(share_media.toString().equals("SMS")){
            String smsContent = umWeb.getTitle()+"，"+umWeb.getDescription()+",点击地址查看详情"+umWeb.toUrl();
//            UMWeb temp = new UMWeb("");
//            temp.setTitle(smsContent);
//            mShareAction.withText(smsContent).setPlatform(share_media).share();
            new ShareAction(mActivity).withText(smsContent).setPlatform(share_media).share();
        }
        else{
            mShareAction.withMedia(umWeb).setPlatform(share_media).share();
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        LogUtil.i("shareHelper share: onStart");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        LogUtil.i("shareHelper share: onResult");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        LogUtil.i("shareHelper share: onError");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        LogUtil.i("shareHelper share: onCancel");
    }
}
