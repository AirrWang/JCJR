package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.MessageCenterAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.MessageCenterEntity;
import com.ql.jcjr.fragment.MyRedPacketFragment;
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

public class MessageCenterActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.lv_message_center)
    private XListView mLvMessageCenter;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;

    @ViewInject(R.id.ll_tip_none)
    private RelativeLayout mLlTipNone;

    // 分页加载索引
    private int mPageIndex = 1;
    private List<MessageCenterEntity.ResultBean> mMessageList = new ArrayList<>();

    private Context mContext;
    private MessageCenterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ViewUtils.inject(this);
        mContext = this;
        initListView();
        getMessageCneterList(String.valueOf(mPageIndex));
        mLvMessageCenter.setOnItemClickListener(this);
    }

    private void initListView() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);

        mAdapter = new MessageCenterAdapter(mContext, mMessageList);
        mLvMessageCenter.setAdapter(mAdapter);
    }

    private void getMessageCneterList(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.sendeMessageCenter(pageIndex, "10");

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("消息列表 " + responeJson);
                        MessageCenterEntity entity = GsonParser.getParsedObj(responeJson, MessageCenterEntity.class);
                        if(pageIndex.equals("1")) {
                            mMessageList.clear();
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
                            mLvMessageCenter.addFooterView();
                            //设置不在加载更多
                            mPullToRefreshView.setLoadMoreEnable(false);
                        }

                        mMessageList.addAll(entity.getResult());
                        mAdapter.notifyDataSetChanged();
                        finishRefresh();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("消息列表 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        finishRefresh();
                    }

                }, mContext);
    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
        if(mMessageList.size() == 0){
            mPullToRefreshView.setVisibility(View.GONE);
            mLlTipNone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;

        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvMessageCenter.removeFooterView();

        getMessageCneterList(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getMessageCneterList(String.valueOf(mPageIndex));
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       String type= mMessageList.get(i).getType();
        Intent intent=new Intent();
       if (type.contains("award")){ //红包
           intent.setClass(mContext, MyRedPacketsActivity.class);
           intent.putExtra("use_type", MyRedPacketFragment.TYPE_MY_HB);
           startActivity(intent);
       }else if (type.equals("loan_yes_account")||type.equals("borrow_review_yes")){  //我的投资
           intent.setClass(mContext, BidHistoryActivity.class);
           startActivity(intent);

       }else if (type.equals("loan_pay")){  //历史投资
           intent = new Intent(mContext, BidHistoryActivity.class);
           intent.putExtra("tag_history",true);
           startActivity(intent);
       }else if (type.equals("loan_no_account")||type.equals("recharge")||type.equals("withdraw_yes")||type.equals("withdraw_no")){ //交易记录
           intent.setClass(mContext, CapitalRecordActivity.class);
           startActivity(intent);
       }else {
           return;
       }

    }
}
