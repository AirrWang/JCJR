package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.BidDetailActivity;
import com.ql.jcjr.adapter.YyyAdapter;
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

    private final static int IDEX_YYY = 0;//月月盈
    private final static int INDEX_TTL = 1;//天天利
    private final static int INDEX_XSZX = 2;//新手专享

    private Context mContext;
    private YyyAdapter mAdapter;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private List<BidListEntity.ResultBean> mBidList = new ArrayList<>();

    // 分页加载索引
    private int mPageIndex = 1;

    @Override
    protected int getContentView() {
        return R.layout.fragment_manage_money;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
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
        SenderResultModel resultModel = ParamsManager.senderBidList(pageIndex, "10", "");

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("理财列表 " + responeJson);
                        BidListEntity entity = GsonParser.getParsedObj(responeJson, BidListEntity.class);
                        if(pageIndex.equals("1")) {
                            mBidList.clear();
                        }
//                        LogUtil.e("entity " +entity);
//                        LogUtil.e("entity.getResult() " + entity.getResult());

                        boolean needShowAll = false;
                        int listSize = entity.getResult().size();
                        if(pageIndex.equals("1")) {
                            if(listSize < 10 && listSize >0){
                                needShowAll = true;
                            }
                        }
                        else{
                            if(listSize < 10){
                                needShowAll = true;
                            }
                        }
                        if(needShowAll){
                            mLvYyy.addFooterView();
                            //设置不在加载更多
                            mPullToRefreshView.setLoadMoreEnable(false);
                        }

                        mBidList.addAll(entity.getResult());
                        mAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(mContext, BidDetailActivity.class);
        intent.putExtra("bid_title", mBidList.get(position).getName());
        intent.putExtra("bid_id", mBidList.get(position).getId());
        mContext.startActivity(intent);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;

        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvYyy.removeFooterView();

        getYyyList(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getYyyList(String.valueOf(mPageIndex));
    }
}