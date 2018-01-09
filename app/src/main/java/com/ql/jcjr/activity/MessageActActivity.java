package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.MessageActAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.MessageActEntity;
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

public class MessageActActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;

    @ViewInject(R.id.lv_message_center)
    private XListView mLvMessageCenter;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;

    @ViewInject(R.id.ll_tip_none)
    private RelativeLayout mLlTipNone;

    // 分页加载索引
    private int mPageIndex = 1;
    private List<MessageActEntity.ResultBean> mMessageList = new ArrayList<>();

    private Context mContext;
    private MessageActAdapter mAdapter;

    private int msgType;//0 活动，，1公告

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_act);
        ViewUtils.inject(this);
        mContext = this;
        initListView();
        getMessageCneterList(String.valueOf(mPageIndex));
    }

    private void initListView() {
        msgType = getIntent().getIntExtra("msg_type",0);
        if(msgType==0){
            mActionBar.setTitle("精选活动");
        }

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);

        mAdapter = new MessageActAdapter(mContext, mMessageList, msgType);
        mLvMessageCenter.setAdapter(mAdapter);
    }

    private void getMessageCneterList(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderMessageAct(pageIndex, "10", msgType);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("消息列表 " + responeJson);
                        MessageActEntity entity = GsonParser.getParsedObj(responeJson, MessageActEntity.class);
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
}
