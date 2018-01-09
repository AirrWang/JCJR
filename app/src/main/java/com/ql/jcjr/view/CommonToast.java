package com.ql.jcjr.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ql.jcjr.R;
import com.ql.jcjr.activity.RealNameActivity;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.utils.LogUtil;

/**
 * Created by liuchao on 2015/8/13.
 */
public class CommonToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CommonToast(Context context) {
        super(context);
    }

    /**
     * 默认弹出框
     *
     * @param content
     */
    public static void makeText(String content) {
        Toast toast = makeText(JcbApplication.getInstance().getApplicationContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     *自定义提示框
     * @param content
     */
    public static void makeCustomText(Context context, String content) {
        Toast toast = new Toast(context);
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        toast.setView(toastRoot);
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.toast_text);
        mTextView.setText(content);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(LENGTH_LONG);
        toast.show();
    }

    /**
     * 显示提示dialog，代替Toast
     *
     * @param context
     * @param s
     */
    public static void showHintDialog(Context context, String s) {
        try {
            CommonDialog.Builder dialog = new CommonDialog.Builder(context);
            dialog.setTitle(s);
            dialog.setMessageSize(R.dimen.f03_34);
            dialog.setButtonTextSize(R.dimen.f03_34);
            dialog.setPositiveButton(context.getString(R.string.submit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShiMingDialog(final Context context) {
        try {
            CommonDialog.Builder builder = new CommonDialog.Builder(context);
            builder.setTitle("您还未实名认证！")
                    .setMessage("立即前往实名认证？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent callIntent = new Intent(context,  RealNameActivity.class);
                            context.startActivity(callIntent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShiMingDialogNoCancel(final Context context) {
        try {
            CommonDialog.Builder builder = new CommonDialog.Builder(context);
            builder.setTitle("您还未实名认证！")
                    .setMessage("立即前往实名认证？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent callIntent = new Intent(context,  RealNameActivity.class);
                            context.startActivity(callIntent);
                        }
                    });
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showUnCancelableDialog(final Context context, String s) {
        try {
            LogUtil.i("showHintDialog");
            CommonDialog.Builder dialog = new CommonDialog.Builder(context);
            dialog.setTitle(s);
            dialog.setMessageSize(R.dimen.f03_34);
            dialog.setButtonTextSize(R.dimen.f03_34);
            dialog.setPositiveButton(context.getString(R.string.submit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(listener != null) {
                                listener.oClickEvent();
                            }
                        }
                    });
            dialog.setCancelable(false);
            dialog.create().show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(final Context context, String title, String s, String positiveText, final DialogInterface.OnClickListener listener, String negativeText, final DialogInterface.OnClickListener negativeListener) {
        try {
            LogUtil.i("showHintDialog");
            CommonDialog.Builder dialog = new CommonDialog.Builder(context);
            if(null != title){
                dialog.setTitle(title);
            }
            dialog.setMessage(s);
            dialog.setButtonTextSize(R.dimen.f03_34);
            dialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener.onClick(dialog, which);
                }
            });
            dialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if(null != negativeListener){
                        negativeListener.onClick(dialog, which);
                    }
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showUnCancelableDialog(final Context context, String title, String s, String positiveText, final boolean b) {
        try {
            LogUtil.i("showHintDialog");
            CommonDialog.Builder dialog = new CommonDialog.Builder(context);
            dialog.setTitle(title);
            dialog.setMessage(s);
            dialog.setButtonTextSize(R.dimen.f03_34);
            dialog.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(listener != null && b) {
                                listener.oClickEvent();
                            }
                        }
                    });
            dialog.setCancelable(false);
            dialog.create().show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showUpdateDialog(final Context context, String title,String message) {
        try {
            LogUtil.i("showHintDialog");
            CommonDialog.Builder dialog = new CommonDialog.Builder(context);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setButtonTextSize(R.dimen.f03_34);
            dialog.setPositiveButton(context.getString(R.string.update),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(listener != null) {
                                listener.oClickEvent();
                            }
                        }
                    });
            dialog.setNegativeButton(context.getString(R.string.cancel),
                    new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNotificationDialog(final Context context, final IPositiveButtonEvent positiveListener) {
        try {
            CommonDialog.Builder dialog = new CommonDialog.Builder(context);
            dialog.setTitle("温馨提示");
            dialog.setMessage("应用未获取到通知提醒权限！开启通知后，可第一时间知道回款时间。");
            dialog.setPositiveButton("前往开启",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(positiveListener != null) {
                                positiveListener.oClickEvent();
                            }
                        }
                    });
            dialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setCancelable(true);
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDailPrompt(final Context context, final String phoneNum) {

        CommonDialog.Builder builder = new CommonDialog.Builder(context);
        builder.setTitle("温馨提示")
                .setMessage("是否联系客服？")
                .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        Uri uri = Uri.parse("tel:" + phoneNum.replace("-", ""));
//                        callIntent.setData(uri);
//                        context.startActivity(callIntent);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.SERVICE_HOTLINE_NUM));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private static IPositiveButtonEvent  listener;

    public interface IPositiveButtonEvent {
        void oClickEvent();
    }
    public static void setIPositiveButtonEventListener(IPositiveButtonEvent positiveButtonEventListener) {
        listener = positiveButtonEventListener;
    }

    public static void unRegisteIPositiveButtonEventListener() {
        if (listener != null) {
            listener = null;
        }
    }

    private static IPositiveEvent mIPositiveEventListener;
    public interface IPositiveEvent {
        void onPositive(Object object);
    }

    public static void setOnPositiveListener(IPositiveEvent listener) {
        mIPositiveEventListener = listener;
    }

}
