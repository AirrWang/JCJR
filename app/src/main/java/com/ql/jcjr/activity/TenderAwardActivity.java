package com.ql.jcjr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;


public class TenderAwardActivity extends BaseActivity {

    @ViewInject(R.id.tv_award)
    private TextView mTvAward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_award);

        ViewUtils.inject(this);
        mTvAward.setText(getIntent().getStringExtra("tender_reward"));
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
