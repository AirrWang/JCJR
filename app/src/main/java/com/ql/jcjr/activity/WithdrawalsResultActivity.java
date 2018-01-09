package com.ql.jcjr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.view.ActionBar;


public class WithdrawalsResultActivity extends BaseActivity {

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;
    @ViewInject(R.id.iv_result)
    private ImageView mIvResult;
    @ViewInject(R.id.tv_result)
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals_result);
        ViewUtils.inject(this);
        init();
    }


    private void init() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("ab_title");
        String result = intent.getStringExtra("result_text");
        boolean isSuccess = intent.getBooleanExtra("is_success", false);
        mActionBar.setTitle(title);
        mTvResult.setText(result);

        if(isSuccess) {
            mIvResult.setImageResource(R.drawable.business_success);
        }else{
            mIvResult.setImageResource(R.drawable.business_fail);
        }
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
