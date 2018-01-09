package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.ViewPagerAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.fragment.RechargeRecordFragment;
import com.ql.jcjr.fragment.TransRecordFragment;
import com.ql.jcjr.fragment.WithdrawalsRecordFragment;
import com.ql.jcjr.view.NoScrollViewPager;

import java.util.ArrayList;

public class CapitalRecordActivity extends BaseActivity {

    @ViewInject(R.id.cvw_pager)
    private NoScrollViewPager mViewPager;
    @ViewInject(R.id.rg_record)
    private RadioGroup mRadioGroup;

    private final static int IDEX_TRANSACTION = 0;//交易记录
    private final static int INDEX_WITHDRAWALS = 1;//提现记录
    private final static int INDEX_RECHARGE = 2;//充值记录
//    private final static int INDEX_BID = 3;//投标记录

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_record);

        ViewUtils.inject(this);

        mContext = this;
        init();
    }

    private void init() {
        initViewPager();
        initBottomTab();
    }

    private void initViewPager() {
        mFragmentList.clear();

        mFragmentList.add(new TransRecordFragment());
        mFragmentList.add(new WithdrawalsRecordFragment());
        mFragmentList.add(new RechargeRecordFragment());
//        mFragmentList.add(new BidRecordFragment());

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
        mViewPager.setScanScroll(false);
        mViewPager.setCurrentItem(0);
    }

    private void initBottomTab() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_transaction:
                        mViewPager.setCurrentItem(IDEX_TRANSACTION);
                        break;

                    case R.id.tab_withdrawals:
                        mViewPager.setCurrentItem(INDEX_WITHDRAWALS);
                        break;

                    case R.id.tab_recharge:
                        mViewPager.setCurrentItem(INDEX_RECHARGE);
                        break;

//                    case R.id.tab_bid:
//                        mViewPager.setCurrentItem(INDEX_BID);
//                        break;
                }
            }
        });
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
