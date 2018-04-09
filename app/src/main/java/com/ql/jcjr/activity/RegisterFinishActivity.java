package com.ql.jcjr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Airr on 2018/1/8.
 */

public class RegisterFinishActivity extends BaseActivity{

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;

    @ViewInject(R.id.btn_to_real_name)
    private Button btn_to_real_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finish);
        ViewUtils.inject(this);
        mActionBar.setRightText("完成");
        Map<String, String> datas = new HashMap<String, String>();
        MobclickAgent.onEventValue(this, "register_sucess", datas, 1);

    }
    @OnClick({R.id.btn_to_real_name, R.id.btn_right})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_to_real_name:
                intent = new Intent(RegisterFinishActivity.this, RealNameActivity.class);
                intent.putExtra("tag_history",true);
                startActivity(intent);
                break;

            case R.id.btn_right:
                finish();
                break;


        }
    }
}
