package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

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

public class CapitalRecordActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener{

    @ViewInject(R.id.cvw_pager)
    private NoScrollViewPager mViewPager;
    @ViewInject(R.id.bottom_line)
    private FrameLayout mBottomLine;

    @ViewInject(R.id.ll_hb_title)
    private FrameLayout mLLHbTitle;

    private int screenWidth = 0;
    private final static int IDEX_TRANSACTION = 0;//交易记录
    private final static int INDEX_WITHDRAWALS = 2;//提现记录
    private final static int INDEX_RECHARGE = 1;//充值记录

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
        ViewTreeObserver viewTreeObserver = mLLHbTitle.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);
    }

    private void initViewPager() {
        mFragmentList.clear();

        mFragmentList.add(new TransRecordFragment());
        mFragmentList.add(new RechargeRecordFragment());
        mFragmentList.add(new WithdrawalsRecordFragment());

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
        mViewPager.setScanScroll(false);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int index, float positionOffset, int pixes) {
                if (pixes != 0) {
                    mBottomLine.layout(
                            (int) ((index + positionOffset) * screenWidth / 3), 0,
                            (int) ((index + 1 + positionOffset) * screenWidth / 3),
                            mBottomLine.getWidth());
                }
            }

            @Override
            public void onPageSelected(int position) {
                changeIndicatorIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeIndicatorIndex(int index) {
        mBottomLine.layout((int) (index * screenWidth / 3), 0,(int) ((index + 1) * screenWidth / 3), mBottomLine.getWidth());
    }

    @OnClick({R.id.iv_back, R.id.tab_tv0, R.id.tab_tv1, R.id.tab_tv2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tab_tv0:
                mViewPager.setCurrentItem(IDEX_TRANSACTION);
                break;

            case R.id.tab_tv1:
                mViewPager.setCurrentItem(INDEX_RECHARGE);
                break;

            case R.id.tab_tv2:
                mViewPager.setCurrentItem(INDEX_WITHDRAWALS);
                break;
        }
    }


    @Override
    public void onGlobalLayout() {
        screenWidth = mLLHbTitle.getWidth();
        changeIndicatorIndex(0);

        mLLHbTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
