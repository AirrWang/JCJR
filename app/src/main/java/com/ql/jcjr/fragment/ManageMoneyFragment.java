package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.BidDetailActivity;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.adapter.YyyAdapter;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.entity.BidListEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.NoScrollViewPager;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

import static com.ql.jcjr.utils.StatusBarCompat.getStatusBarHeight;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class ManageMoneyFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.cvw_pager)
    private NoScrollViewPager mViewPager;
    @ViewInject(R.id.rg__manage_money)
    private RadioGroup mRadioGroup;
    @ViewInject(R.id.lv_yyy)
    private XListView mLvYyy;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.tv_moren)
    private TextView tv_moren;
    @ViewInject(R.id.tv_apr)
    private TextView tv_apr;
    @ViewInject(R.id.iv_apr_up)
    private ImageView iv_apr_up;
    @ViewInject(R.id.iv_apr_down)
    private ImageView iv_apr_down;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.iv_time_down)
    private ImageView iv_time_down;
    @ViewInject(R.id.iv_time_up)
    private ImageView iv_time_up;
    @ViewInject(R.id.tv_jindu)
    private TextView tv_jindu;
    @ViewInject(R.id.iv_jindu_down)
    private ImageView iv_jindu_down;
    @ViewInject(R.id.iv_jindu_up)
    private ImageView iv_jindu_up;

    @ViewInject(R.id.v_status)
    private View v_status;


    private final static int IDEX_YYY = 0;//月月盈
    private final static int INDEX_TTL = 1;//天天利
    private final static int INDEX_XSZX = 2;//新手专享

    private Context mContext;
    private YyyAdapter mAdapter;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private List<BidListEntity.ResultBean> mBidList = new ArrayList<>();
    private List<BidListEntity.ResultBean> mBidAll = new ArrayList<>();

    private String order="";
    private boolean a=true;
    private boolean b=true;
    private boolean c=true;
    private Boolean isTouch=false;//判断是否是排序跳转
    // 分页加载索引
    private int mPageIndex = 1;
    private View headerView;
    private LinearLayout ll_biao_right;
    private TextView tv_annualized_rate_gain;
    private TextView tv_apr1;
    private TextView tv_annualized_rate;
    private TextView tv_xsb_day;
    private TextView tv_xsb_most;
    private LinearLayout ll_head_xsb;
    private Button btn_bid;

    @Override
    protected int getContentView() {
        return R.layout.fragment_manage_money;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v_status.getLayoutParams();
        layoutParams.height = getStatusBarHeight(getActivity());
        v_status.setLayoutParams(layoutParams);

        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
        headerView = LayoutInflater.from(mContext).inflate(R.layout.head_first,null);
        mLvYyy.addHeaderView(headerView);
        initId();
    }

    private void initId() {
        ll_biao_right = (LinearLayout) headerView.findViewById(R.id.ll_biao_right);
        tv_annualized_rate_gain = (TextView) headerView.findViewById(R.id.tv_annualized_rate_gain);
        tv_apr1 = (TextView) headerView.findViewById(R.id.tv_apr);
        tv_annualized_rate = (TextView) headerView.findViewById(R.id.tv_annualized_rate);
        tv_xsb_day = (TextView) headerView.findViewById(R.id.tv_xsb_day);
        tv_xsb_most = (TextView) headerView.findViewById(R.id.tv_xsb_most);
        ll_head_xsb = (LinearLayout) headerView.findViewById(R.id.ll_head_xsb);
        btn_bid = (Button) headerView.findViewById(R.id.btn_bid);
        btn_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(mContext, NoviceExclusiveActivity.class);
                    intent.putExtra("bid_id", mBidAll.get(0).getId());
                    intent.putExtra("bid_title",  mBidAll.get(0).getName());
                    startActivity(intent);

            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        init();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible) {
            getYyyList(String.valueOf(mPageIndex));
        }
    }

    //    public void setCurrentItem(int index){
//        switch (index){
//            case 0:
//                mRadioGroup.check(R.id.tab_yyy);
//                break;
//            case 1:
//                mRadioGroup.check(R.id.tab_ttl);
//                break;
//            case 2:
//                mRadioGroup.check(R.id.tab_xszx);
//                break;
//        }
//        mViewPager.setCurrentItem(index);
//    }

    private void init() {
//        initViewPager();
//        initBottomTab();

        initListView();
    }

    private void initListView() {
        mAdapter = new YyyAdapter(mContext, mBidList);

        mLvYyy.setAdapter(mAdapter);
        mLvYyy.setOnItemClickListener(this);
    }

    private void getYyyList(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderBidList(pageIndex, "10", "",order);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        initUI();
                        LogUtil.i("理财列表 " + responeJson);
                        BidListEntity entity = GsonParser.getParsedObj(responeJson, BidListEntity.class);
                        if(pageIndex.equals("1")) {
                            mBidList.clear();
                            mBidAll.clear();
                        }


                        boolean needShowAll = false;
//                        int listSize = entity.getResult().size();
//                        if(pageIndex.equals("1")) {
//                            if(listSize < 10 && listSize >0){
//                                needShowAll = true;
//                            }
//                        }
//                        else{
//                            if(listSize < 10){
//                                needShowAll = true;
//                            }
//                        }
                        if (entity.getResult()==null||entity.getResult().size()==0){
                            needShowAll = true;
                        }
                        if(needShowAll){
                            mLvYyy.addFooterView();
                            //设置不在加载更多
                            mPullToRefreshView.setLoadMoreEnable(false);
                        }

                        mBidList.addAll(entity.getResult());
                        mBidAll.addAll(entity.getResult());
                        checkXsb(pageIndex);
                        mAdapter.notifyDataSetChanged();
                        if (isTouch){
                            mLvYyy.smoothScrollToPosition(0);
                            mAdapter.notifyDataSetChanged();
                        }

                        finishRefresh();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("理财列表 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        finishRefresh();
                    }

                }, mContext);
    }

    private void checkXsb(String pageIndex) {

        if (mBidAll.get(0).getType().equals("xsb")){
            ll_head_xsb.setVisibility(View.VISIBLE);
        }else {
            ll_head_xsb.setVisibility(View.GONE);
        }

        if (pageIndex.equals("1")&&mBidList.get(0).getType().equals("xsb")){

            BidListEntity.ResultBean bean=mBidList.get(0);
            //年化收益
            tv_apr1.setText(bean.getApr());
            tv_apr1.setTypeface(JcbApplication.getPingFangRegularTypeFace());

            tv_annualized_rate.setTypeface(JcbApplication.getPingFangRegularTypeFace());
            tv_annualized_rate.setText(bean.getAprOrigin());

            //判断有无活动加成
            String cashAddition = bean.getCashAddition();
            if(cashAddition.equals("0") || cashAddition.equals("0.0")){
                ll_biao_right.setVisibility(View.INVISIBLE);
            }else{
                ll_biao_right.setVisibility(View.VISIBLE);
                tv_annualized_rate_gain.setTypeface(JcbApplication.getPingFangRegularTypeFace());
                tv_annualized_rate_gain.setText(bean.getCashAddition());
            }
            //投资期限
            switch (bean.getIsday()) {
                case "0":
                    tv_xsb_day.setText(bean.getTime_limit() + "个月");
                    break;
                case "1":
                    tv_xsb_day.setText(bean.getTime_limit_day() + "天");
                    break;
            }

            tv_xsb_most.setText(bean.getMost_account()+"元");

            mBidList.remove(0);
        }

    }

    private void initUI() {
        if (order.equals("apr_down")){
            b=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.btn_main));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

        }else if (order.equals("apr_up")){
            b=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.btn_main));
            iv_apr_up.setImageResource(R.drawable.arrow_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

        }else if (order.equals("time_down")){
            a=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.btn_main));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);
        }else if (order.equals("time_up")){
            a=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.btn_main));
            iv_time_up.setImageResource(R.drawable.arrow_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

        }else if (order.equals("jindu_down")){
            a=true;
            b=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.btn_main));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_yellow_down_wdtz);

        }else if (order.equals("jindu_up")){
            a=true;
            b=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.btn_main));
            iv_jindu_up.setImageResource(R.drawable.arrow_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

        }else {  //默认
            tv_moren.setTextColor(getResources().getColor(R.color.btn_main));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);
            b=true;
            c=true;
            a=true;
        }



    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
    }

//    private void initViewPager() {
//        mFragmentList.clear();
//
//        mFragmentList.add(new YYYFragment());
//
////        mFragmentList.add(new TTLFragment());
////
////        mFragmentList.add(new XSZXFragment());
//
//        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(), mFragmentList));
//        mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
//        mViewPager.setScanScroll(false);
//        mViewPager.setCurrentItem(0);
//    }

//    private void initBottomTab() {
//        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.tab_yyy:
//                        mViewPager.setCurrentItem(IDEX_YYY);
//                        break;
//
//                    case R.id.tab_ttl:
//                        mViewPager.setCurrentItem(INDEX_TTL);
//                        break;
//
//                    case R.id.tab_xszx:
//                        mViewPager.setCurrentItem(INDEX_XSZX);
//                        break;
//                }
//            }
//        });
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if(mBidList.get(position).getPwd().equals("1")){
//            gotoBidDetail(position);
//        }
//        else{
            gotoBidDetail(position);
//        }
    }

    private void gotoBidDetail(int position){
        position--;
        if (position==mBidList.size())return;
        Intent intent = new Intent(mContext, BidDetailActivity.class);
        intent.putExtra("bid_title", mBidList.get(position).getName());
        intent.putExtra("bid_id", mBidList.get(position).getId());
        mContext.startActivity(intent);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isTouch=false;
        mPageIndex = 1;

        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvYyy.removeFooterView();

        getYyyList(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        isTouch=false;
        mPageIndex++;
        getYyyList(String.valueOf(mPageIndex));
    }

    @OnClick({R.id.tv_moren,R.id.rl_apr,R.id.ll_time,R.id.ll_jindu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_moren:
                isTouch=true;
                order="";
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.rl_apr:
                isTouch=true;
                if (a){
                    order="apr_down";
                }else {
                    order="apr_up";
                }
                a=!a;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;

            case R.id.ll_time:
                isTouch=true;
                if (b){
                    order="time_down";
                }else {
                    order="time_up";
                }
                b=!b;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.ll_jindu:
                isTouch=true;
                if (c){
                    order="jindu_down";
                }else {
                    order="jindu_up";
                }
                c=!c;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;

        }
    }
}