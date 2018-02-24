package com.ql.jcjr.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CommonDialog;

public class ContactUsActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ViewUtils.inject(this);

        mContext = this;
    }

    private void showDailPrompt() {

        CommonDialog.Builder builder = new CommonDialog.Builder(mContext);
        builder.setTitle("温馨提示")
                .setMessage("是否联系客服？")
                .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        Uri uri = Uri.parse("tel:" + AppConfig.SERVICE_HOTLINE_NUM);
//                        callIntent.setData(uri);
//                        startActivity(callIntent);

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.SERVICE_HOTLINE_NUM));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    @OnClick({R.id.btn_left, R.id.ithb_customer_service_hotline, R.id.ithb_customer_service_qa,R.id.ithb_customer_service_online})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ithb_customer_service_qa:
                UrlUtil.showHtmlPage(mContext,"常见问题", AppConfig.COMMON_PROBLEM_URL);
                break;
            case R.id.ithb_customer_service_hotline:
                showDailPrompt();
                break;
            case R.id.ithb_customer_service_online:
                String title = "积财客服";
                /**
                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（可自由定义）。
                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                 */
                ConsultSource source = new ConsultSource(AppConfig.OFFICIAL_WEBSITE_URL, "积财金融", "Android");
                source.productDetail = null;
                /**
                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                 * 如果返回为false，该接口不会有任何动作
                 *
                 * @param context 上下文
                 * @param title   聊天窗口的标题
                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                 */
//                CommonToast.showHintDialog(mContext,Unicorn.isServiceAvailable()+"");
                Unicorn.openServiceActivity(mContext, title, source);

//                ConsultSource source = new ConsultSource(uri, title, null);
//                source.productDetail = productDetail;
//                Unicorn.openServiceActivity(context, staffName(), source);
                break;
        }
    }
}
