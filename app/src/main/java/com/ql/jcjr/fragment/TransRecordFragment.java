package com.ql.jcjr.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.TransRecordAdapter;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.entity.TransRecordEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易记录
 * Created by Liuchao on 2016/9/23.
 */
public class TransRecordFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.lv_transaction_record)
    private XListView mLvTransRecord;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;

    @ViewInject(R.id.ll_tip_none)
    private RelativeLayout mLlTipNone;

    private Context mContext;
    private TransRecordAdapter mAdapter;
    List<TransRecordEntity.ResultBean> mRecordList = new ArrayList<>();

    // 分页加载索引
    private int mPageIndex = 1;

    @Override
    protected int getContentView() {
        return R.layout.fragment_transaction_record;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();

    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        init();
    }

    private void init() {
        initListVieww();
        getTransRecord(String.valueOf(mPageIndex));

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
    }

    private void initListVieww() {
        mAdapter = new TransRecordAdapter(mContext, mRecordList);
        mLvTransRecord.setAdapter(mAdapter);
        mLvTransRecord.setOnItemClickListener(this);
    }

    private void getTransRecord(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderTransRecord(pageIndex, "10");

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("交易记录 " + responeJson);
                TransRecordEntity entity = GsonParser.getParsedObj(responeJson, TransRecordEntity.class);
                if(pageIndex.equals("1")) {
                    mRecordList.clear();
                }

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
                    mLvTransRecord.addFooterView();
                    //设置不在加载更多
                    mPullToRefreshView.setLoadMoreEnable(false);
                }

                mRecordList.addAll(entity.getResult());
                mAdapter.notifyDataSetChanged();
                finishRefresh();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("交易记录失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                finishRefresh();
            }

        }, mContext);
    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
        if(mRecordList.size() == 0){
            mPullToRefreshView.setVisibility(View.GONE);
            mLlTipNone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;

        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvTransRecord.removeFooterView();

        getTransRecord(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getTransRecord(String.valueOf(mPageIndex));
    }
}