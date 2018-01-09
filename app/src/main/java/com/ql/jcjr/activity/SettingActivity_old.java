package com.ql.jcjr.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.CheckRealNameEntity;
import com.ql.jcjr.entity.UploadPicEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.FileUtil;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.ImageUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.utils.crypt.DesUtil;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBar;

import java.io.File;

public class SettingActivity_old extends BaseActivity {

    public static final int PICK_CODE = 2;
    public static final int TAKE_PHOTO_CODE = 3;
    private static final int CODE_CHANGE_LOGIN_PWD = 0;
    private static final int CODE_GESTURE = 1;
    @ViewInject(R.id.ithb_avatar)
    private ImageTextHorizontalBar mIthbAvatar;
    @ViewInject(R.id.ithb_bind_mobile)
    private ImageTextHorizontalBar mIthbMobile;
    @ViewInject(R.id.ithb_real_name)
    private ImageTextHorizontalBar mTthbRealName;
    private Context mContext;
    private String mUserIconUrl;
    private String mIsSetPay;
    private CircleImageView mCircleImageView;
    private String mTakePhotoPath;
    private Bitmap mPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(UserData.getInstance().getRealName().length()==0){
            mTthbRealName.setDescriptionText("未认证");
        }
        else{
            mTthbRealName.setDescriptionText("已认证");
        }
    }

    private void init() {
        mUserIconUrl = getIntent().getStringExtra("user_icon_url");
//        mIsSetPay = getIntent().getStringExtra("isSetPay");
        GlideUtil.displayPic(mContext, mUserIconUrl, R.drawable.ic_setting_default_icon,
                mIthbAvatar.circleImageView);
        mIthbMobile.setDescriptionText(UserData.getInstance().getPhoneNumber());
        initCircleImageView();
        checkRealName();
    }

    private void initCircleImageView() {
        mCircleImageView = mIthbAvatar.getCircleImageView();
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void checkRealName() {
        SenderResultModel resultModel = ParamsManager.senderCheckRealName();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("检查是否实名认证 " + responeJson);
                        CheckRealNameEntity
                                entity =
                                GsonParser.getParsedObj(responeJson, CheckRealNameEntity.class);
                        String status = entity.getResult().getStatus();
                        switch (status) {
                            case Global.STATUS_PASS:
                                mTthbRealName.setDescriptionText("已认证");
                                break;
                            case Global.STATUS_UN_PASS:
                                mTthbRealName.setDescriptionText("未认证");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("检查是否实名认证 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    /**
     * 选择头像
     */
    private void showDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.choice_face_dialog, null);
        Button btn1 = (Button) view.findViewById(R.id.btn1_dialog);
        Button btn2 = (Button) view.findViewById(R.id.btn2_dialog);
        Button btnCancel = (Button) view.findViewById(R.id.bt_cancel);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
        dialog.setContentView(view);
        dialog.show();

        //拍照
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                createPath();
                takePicture();
            }
        });

        //从相册选择
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fromGallery();
            }
        });

        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void createPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/"
                    + AppConfig.APP_PICTURE_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            mTakePhotoPath = folder.getAbsolutePath() + "/" + "header" + ".jpg";

        } else {
            Toast.makeText(mContext, "您还没有安装SD卡无法进行图片上传功能，请确保已经插入SD卡", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTakePhotoPath)));
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    /**
     * 从相册选择
     */
    private void fromGallery() {
        Intent intent = new Intent();
        //开启Pictures画面Type设定为image
        intent.setType("image/*");
        //使用Intent.ACTION_PICK这个Action
        intent.setAction(Intent.ACTION_PICK);//Intent.ACTION_GET_CONTENT
        //取得相片后返回本画面
        startActivityForResult(intent, PICK_CODE);
    }

    private void handlePickPic(Intent data) {
        Uri uri = data.getData();
        if (uri == null) {
            return;
        }
        ContentResolver cr = getContentResolver();
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor == null) {
            return;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        //最后根据索引值获取图片路径
        String path = cursor.getString(column_index);
        if (!cursor.isClosed()) {
            cursor.close();
        }
        setPhotoBitmap(path);
    }

    private void setPhotoBitmap(String path) {
        if (StringUtils.isNotBlank(path)) {
            File mFile = new File(path);
            try {
                // 此处可调节bitmap大小，越大上传越久
                mPhotoBitmap = FileUtil.getBitmapFromSD(mFile, ImageUtil.SCALEIMG, 320, 480);
                if (mPhotoBitmap == null) {
                    ToastUtil.showToast(mContext, R.string.cannot_find_image);
                    return;
                }

            } catch (Exception e) {
                ToastUtil.showToast(mContext, R.string.cannot_find_image);
                return;
            }

            upload();

        }
    }

    private void upload() {

        String base64Bitmap = ImageUtil.Bitmap2StrByBase64(mPhotoBitmap);

        SenderResultModel resultModel = ParamsManager.sendeHeaderImg(base64Bitmap);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        //{"RSPCOD":"00","RSPMSG":"上传成功","BINDID":"37651"}
                        LogUtil.i("上传图片成功 " + responeJson);
                        UploadPicEntity entity = GsonParser.getParsedObj(responeJson, UploadPicEntity.class);
                        UserData.getInstance().setUserIconUrl(entity.getResult().getHeadImgUrl());
                        mCircleImageView.setImageBitmap(mPhotoBitmap);
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("上传图片失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);

                    }

                }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_CHANGE_LOGIN_PWD:
                finish();
                break;
            case CODE_GESTURE:
                finish();
                break;
            case PICK_CODE:
                handlePickPic(data);
                break;
            case TAKE_PHOTO_CODE:
                setPhotoBitmap(mTakePhotoPath);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({
            R.id.btn_left, R.id.ithb_real_name, R.id.ithb_login_psw, R.id.ithb_trans_psw, R.id.ithb_bank, R.id.tv_exit, R.id.ithb_gesture_psw, R.id.ithb_avatar, R.id.ithb_about_us
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ithb_real_name:
                Intent realNameIntent = new Intent(mContext, RealNameActivity.class);
                startActivity(realNameIntent);
                break;
            case R.id.ithb_login_psw:
                Intent loginPswIntent = new Intent(mContext, ChangePasswordActivity.class);
                startActivityForResult(loginPswIntent, CODE_CHANGE_LOGIN_PWD);
                break;
            case R.id.ithb_trans_psw:
                Intent transPswIntent = new Intent();
                switch (UserData.getInstance().getIsSetPay()) {
                    case "0":
                        transPswIntent.setClass(mContext, VerifyLoginPwdActivity.class);
                        startActivity(transPswIntent);
                        break;
                    case "1":
                        transPswIntent.setClass(mContext, VerifyPayPswActivity.class);
                        startActivity(transPswIntent);
                        break;
                }
                break;
//            case R.id.ithb_auto_bid:
//                Intent autoBidIntent = new Intent(mContext, AutoBidActivity.class);
//                startActivity(autoBidIntent);
//                break;
            case R.id.ithb_bank:
                if(UserData.getInstance().getRealName().length()==0){
                    CommonToast.showShiMingDialog(mContext);
                }
                else{
                    Intent bankIntent = new Intent(mContext, BindBankCardActivity.class);
                    startActivity(bankIntent);
                }
                break;
            case R.id.tv_exit:
                showExitDialog();
                break;
            case R.id.ithb_gesture_psw:
                Intent gestureIntent = new Intent(mContext, SettingGestureActivity.class);
                gestureIntent.putExtra("user_icon_url", mUserIconUrl);
                startActivityForResult(gestureIntent, CODE_GESTURE);
                break;

            case R.id.ithb_about_us:
                Intent aboutUsIntent = new Intent(mContext, AboutUsActivity.class);
                startActivity(aboutUsIntent);
                break;
        }
    }

    private final String PSW_FILE_NAME = "wjthnfkghj";
    private void savePswString(String password) {
        String encrypt = "";
        try {
            byte[] encrypted =
                    DesUtil.encrypt(password.getBytes("utf-8"), AppConfigCommon.ENCRYPT_KEY.getBytes());
            encrypt = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtil.writeObjectToDataFile(mContext, encrypt, PSW_FILE_NAME);
    }

    private void showExitDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exit_dialog, null);
        Button btnExit = (Button) view.findViewById(R.id.btn_exit);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
        dialog.setContentView(view);
        dialog.show();

        //退出
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData.getInstance().setUSERID("");
                savePswString("");
                finish();
            }
        });

        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
