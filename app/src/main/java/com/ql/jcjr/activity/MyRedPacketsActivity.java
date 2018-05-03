package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.ViewPagerAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.fragment.MyRedPacketFragment;
import com.ql.jcjr.view.NoScrollViewPager;

import java.util.ArrayList;


public class MyRedPacketsActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener{

    @ViewInject(R.id.cvw_pager)
    private NoScrollViewPager mViewPager;
    @ViewInject(R.id.tab_tv0)
    private TextView mTVTab0;
    @ViewInject(R.id.tab_tv1)
    private TextView mTVTab1;
    @ViewInject(R.id.tab_tv2)
    private TextView mTVTab2;
    @ViewInject(R.id.bottom_line)
    private FrameLayout mBottomLine;

    @ViewInject(R.id.ll_hb_title)
    private FrameLayout mLLHbTitle;

    @ViewInject(R.id.btn_to_bid)
    private Button btn_to_bid;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private int screenWidth = 0;
    private final static int IDEX_TAB0 = 0;
    private final static int IDEX_TAB1 = 1;
    private final static int IDEX_TAB2 = 2;

    public static final int RESULT_CODE_NOUSE = 1;
    public static final int RESULT_CODE_USEHB = 2;

    private int hbUseType;
    private String borrowid;
    private String money;

    private MyRedPacketClickListener myRedPacketClickListener =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_packets);
        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        hbUseType = getIntent().getIntExtra("use_type",0);
        btn_to_bid.setVisibility(View.VISIBLE);
        if(hbUseType == MyRedPacketFragment.TYPE_USE_HB){
            borrowid = getIntent().getStringExtra("borrowid");
            money = getIntent().getStringExtra("money");
            myRedPacketClickListener = new MyRedPacketClickListener();
            btn_to_bid.setVisibility(View.GONE);
        }
        initViewPager();
        ViewTreeObserver viewTreeObserver = mLLHbTitle.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);
//        ViewTreeObserver.OnPreDrawListener mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
////                int height = mLLHbTitle.getHeight();
//                if(screenWidth==0){
//                    screenWidth = mLLHbTitle.getWidth();
//                    changeIndicatorIndex(0);
//                }
//                return true;
//            }
//        };
//        viewTreeObserver.addOnPreDrawListener(mPreDrawListener);
    }

    private void initViewPager() {
        mFragmentList.clear();

        MyRedPacketFragment myRedPacketFragment = new MyRedPacketFragment();
        myRedPacketFragment.setType(hbUseType, MyRedPacketFragment.TYPE_HB_DK, borrowid, money, myRedPacketClickListener);
        mFragmentList.add(myRedPacketFragment);

        myRedPacketFragment = new MyRedPacketFragment();
        myRedPacketFragment.setType(hbUseType, MyRedPacketFragment.TYPE_HB_JX, borrowid, money, myRedPacketClickListener);
        mFragmentList.add(myRedPacketFragment);

        myRedPacketFragment = new MyRedPacketFragment();
        myRedPacketFragment.setType(hbUseType, MyRedPacketFragment.TYPE_HB_FX, borrowid, money, myRedPacketClickListener);
        mFragmentList.add(myRedPacketFragment);

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
//        mViewPager.setScanScroll(false);
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

    class MyRedPacketClickListener implements MyRedPacketFragment.RedPacketClickListener {
        @Override
        public void clickNoUse() {
            setResult(RESULT_CODE_NOUSE);
            finish();
        }

        @Override
        public void clickUseHb(String cashid, String hbType, String hbMoney, String cashApr) {
            Intent intent = new Intent();
            intent.putExtra("cashid",cashid);
            intent.putExtra("hbType",hbType);
            intent.putExtra("hbMoney",hbMoney);
            intent.putExtra("cashApr",cashApr);
            setResult(RESULT_CODE_USEHB, intent);
            finish();
        }
    }

    private void changeIndicatorIndex(int index) {
        mBottomLine.layout((int) (index * screenWidth / 3), 0,(int) ((index + 1) * screenWidth / 3), mBottomLine.getWidth());
    }

    @OnClick({R.id.iv_back, R.id.tab_tv0, R.id.tab_tv1, R.id.tab_tv2,R.id.btn_to_bid})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tab_tv0:
                mViewPager.setCurrentItem(IDEX_TAB0);
                break;

            case R.id.tab_tv1:
                mViewPager.setCurrentItem(IDEX_TAB1);
                break;

            case R.id.tab_tv2:
                mViewPager.setCurrentItem(IDEX_TAB2);
                break;
            case R.id.btn_to_bid:
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("main_index",1);
                startActivity(intent);
                finish();
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
