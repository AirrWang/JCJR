package com.ql.jcjr.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
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

    @OnClick({R.id.btn_left, R.id.ithb_customer_service_hotline, R.id.ithb_customer_service_qa})
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
        }
    }
}
