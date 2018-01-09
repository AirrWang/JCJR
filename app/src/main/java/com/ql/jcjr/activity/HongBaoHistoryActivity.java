package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.RedPacketAdapterNew;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.RedPacketEntityNew;
import com.ql.jcjr.fragment.MyRedPacketFragment;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class HongBaoHistoryActivity extends BaseActivity  implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.lv_history)
    private XListView mLvHistory;

    @ViewInject(R.id.ll_tip_none)
    private RelativeLayout mLlTipNone;


    private Context mContext;

    private RedPacketAdapterNew mAdapter;
    private List<RedPacketEntityNew.ResultBean.ListBean> mList = new ArrayList<>();

    // 分页加载索引
    private int mPageIndex = 1;

    private int typeHB = 0;
    private String typeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hb_history);
        ViewUtils.inject(this);
        mContext = this;

        typeHB = getIntent().getIntExtra("TYPE_HB",MyRedPacketFragment.TYPE_HB_DK);
        init();
    }

    private void init() {
        initListView();

        if(typeHB== MyRedPacketFragment.TYPE_HB_DK){
            mActionBar.setTitle("历史抵扣券");
            typeSend = "dikou";
        }
        else if(typeHB== MyRedPacketFragment.TYPE_HB_JX){
            mActionBar.setTitle("历史加息券");
            typeSend = "cash";
        }
        else if(typeHB== MyRedPacketFragment.TYPE_HB_FX){
            mActionBar.setTitle("历史返现券");
            typeSend = "fanxian";
        }

        getContent(String.valueOf(mPageIndex));

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
    }

    private void initListView() {
        mAdapter = new RedPacketAdapterNew(mContext, mList, Global.STATUS_USED, typeHB);
        mLvHistory.setAdapter(mAdapter);
        mLvHistory.setOnItemClickListener(this);
    }

    private void getContent(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderMyCouponNew(pageIndex, "10", "history", typeSend, null, null);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("历史红包 " + responeJson);
                RedPacketEntityNew entity = GsonParser.getParsedObj(responeJson, RedPacketEntityNew.class);

                boolean needShowAll = false;
                int listSize = entity.getResult().getList().size();
                if(pageIndex.equals("1")) {
                    mList.clear();
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
                    mLvHistory.addFooterView();
                    //设置不在加载更多
                    mPullToRefreshView.setLoadMoreEnable(false);
                }

                mList.addAll(entity.getResult().getList());
                mAdapter.notifyDataSetChanged();
                finishRefresh();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("历史红包失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                finishRefresh();
            }

        }, mContext);
    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
        if(mList.size() == 0){
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
        mLvHistory.removeFooterView();
        getContent(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getContent(String.valueOf(mPageIndex));
    }

    @OnClick({R.id.btn_left, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
