package com.ql.jcjr.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;


/**
 * Created by Liuchao on 2016/9/25.
 */
public class ShareHelper implements ShareBoardlistener, UMShareListener{

    private Activity mActivity;
    private int shareType;
    private final int SHARE_TYPE_WEB = 1;
    private final int SHARE_TYPE_IMAGE = 2;
    private UMWeb umWeb;
    private ShareAction mShareAction;
    private UMImage image;
    private final ShareBoardConfig config;

    public ShareHelper(Activity activity){
        mActivity = activity;
        mShareAction = new ShareAction(activity).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.SMS)
                .setShareboardclickCallback(this).setCallback(this);

        //新建ShareBoardConfig               config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);//设置位置
        config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        config.setTitleVisibility(false);
        config.setCancelButtonVisibility(true);
        config.setCancelButtonText("取消");
        config.setShareboardBackgroundColor(Color.parseColor("#FFFFFF"));
        config.setIndicatorVisibility(false);
    }

    public void setShareWebInfo(String url, String imgUrl, String shareTitle, String shareContent){
        shareType = SHARE_TYPE_WEB;
        umWeb = new UMWeb(url);
        umWeb.setTitle(shareTitle);//标题
        image = new UMImage(mActivity, imgUrl);//网络图片
        umWeb.setThumb(image);  //缩略图
        umWeb.setDescription(shareContent);//描述
    }

    public void setShareContent(String shareTitle, String shareContent){
        shareType = SHARE_TYPE_WEB;
        umWeb.setTitle(shareTitle);//标题
        umWeb.setDescription(shareContent);//描述
    }
    public void setShareImageView(String imageUrl){
        shareType = SHARE_TYPE_IMAGE;

        image = new UMImage(mActivity, imageUrl);
        image.setThumb(image);
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
    }
    public void setShareImageView(Bitmap bitmap){
        shareType = SHARE_TYPE_IMAGE;

        image = new UMImage(mActivity, bitmap);
        image.setThumb(image);
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
    }
    public void share(){
        mShareAction.open(config);
    }

    @Override
    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
        if(share_media.toString().equals("SMS")){
            if (shareType==SHARE_TYPE_WEB) {
                String smsContent = umWeb.getTitle()+"，"+umWeb.getDescription()+",点击地址查看详情"+umWeb.toUrl();
                new ShareAction(mActivity).withText(smsContent).setPlatform(share_media).share();
            }else {
                new ShareAction(mActivity).withMedia(image).setPlatform(share_media).share();
            }

        }
        else{
            if (shareType==SHARE_TYPE_WEB) {
                mShareAction.withMedia(umWeb).setPlatform(share_media).share();
            }else {
                mShareAction.withMedia(image).setPlatform(share_media).share();
            }
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
        LogUtil.i("shareHelper share: onError"+throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        LogUtil.i("shareHelper share: onCancel");
    }
}
