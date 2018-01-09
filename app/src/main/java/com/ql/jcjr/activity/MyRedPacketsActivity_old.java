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
import com.ql.jcjr.fragment.AvailableRedPacketFragment;
import com.ql.jcjr.fragment.OverdueRedPacketFragment;
import com.ql.jcjr.fragment.UsedRedPacketFragment;
import com.ql.jcjr.view.NoScrollViewPager;

import java.util.ArrayList;


public class MyRedPacketsActivity_old extends BaseActivity {

    @ViewInject(R.id.cvw_pager)
    private NoScrollViewPager mViewPager;
    @ViewInject(R.id.radioGroup)
    private RadioGroup mRadioGroup;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private final static int IDEX_AVAILABLE = 0;//可用
    private final static int INDEX_USED = 1;//已用
    private final static int INDEX_OVERDUE = 2;//过期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_packets);
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

        mFragmentList.add(new AvailableRedPacketFragment());

        mFragmentList.add(new UsedRedPacketFragment());

        mFragmentList.add(new OverdueRedPacketFragment());

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
                    case R.id.tab_available:
                        mViewPager.setCurrentItem(IDEX_AVAILABLE);
                        break;

                    case R.id.tab_used:
                        mViewPager.setCurrentItem(INDEX_USED);
                        break;

                    case R.id.tab_overdue:
                        mViewPager.setCurrentItem(INDEX_OVERDUE);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
