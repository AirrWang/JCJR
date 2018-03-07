package com.ql.jcjr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    @ViewInject(R.id.tv_result_time)
    private TextView mTvTime;
    @ViewInject(R.id.tv_amount)
    private TextView mTvAmount;
    @ViewInject(R.id.tv_fee)
    private TextView mTvFee;
    @ViewInject(R.id.tv_account_amount)
    private TextView mTvAccount;
    @ViewInject(R.id.btn_finsh)
    private Button btn_finsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals_result);
        ViewUtils.inject(this);
        init();
        mActionBar.setRightText("    完成");
    }


    private void init() {
        Intent intent = getIntent();
//        String title = intent.getStringExtra("ab_title");
//        String result = intent.getStringExtra("result_text");
//        boolean isSuccess = intent.getBooleanExtra("is_success", false);
        String fee=intent.getStringExtra("fee");
        String time=intent.getStringExtra("time");
        String amount=intent.getStringExtra("amount");
        mActionBar.setTitle("提现");
        mTvTime.setText(time);
        mTvAmount.setText(amount+"元");
        mTvFee.setText(fee+"元");
        mTvAccount.setText(Integer.parseInt(amount)-Integer.parseInt(fee)+"元");
//        mTvResult.setText(result);

//        if(isSuccess) {
            mIvResult.setImageResource(R.drawable.business_success);
//        }else{
//            mIvResult.setImageResource(R.drawable.business_fail);
//        }
    }

    @OnClick({R.id.btn_left,R.id.btn_right,R.id.btn_finsh})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                finish();
                break;
            case R.id.btn_finsh:
                finish();
                break;
        }
    }
}
