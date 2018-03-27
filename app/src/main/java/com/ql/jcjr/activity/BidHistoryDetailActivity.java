package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.BidDetailEntity;
import com.ql.jcjr.entity.BidHistoryDetailEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;


public class BidHistoryDetailActivity extends BaseActivity {

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;

    @ViewInject(R.id.tv_history_detail_name)
    private TextView mTvHistoryDetailName;
    @ViewInject(R.id.tv_history_detail_status)
    private TextView mTvHistoryDetailStatus;
    @ViewInject(R.id.tv_history_detail_id)
    private TextView mTvHistoryDetailID;
    @ViewInject(R.id.tv_history_detail_apr)
    private TextView mTvHistoryDetailApr;
    @ViewInject(R.id.tv_history_detail_duration)
    private TextView mTvHistoryDetailDuration;
    @ViewInject(R.id.tv_history_detail_way)
    private TextView mTvHistoryDetailWay;
    @ViewInject(R.id.tv_history_detail_money)
    private TextView mTvHistoryDetailMoney;
    @ViewInject(R.id.tv_history_detail_get)
    private TextView mTvHistoryDetailGet;

    @ViewInject(R.id.ll_history_detail_hongbao)
    private RelativeLayout mLlHistoryDetailHongbao;
    @ViewInject(R.id.tv_history_detail_hongbao)
    private TextView mTvHistoryDetailHongbao;

    @ViewInject(R.id.ll_history_detail_date)
    private RelativeLayout mLlHistoryDetailDate;
    @ViewInject(R.id.tv_history_detail_date)
    private TextView mTvHistoryDetailDate;

    @ViewInject(R.id.tv_history_detail_success_time)
    private TextView mTvHistoryDetailSuccessTime;
    @ViewInject(R.id.ll_history_detail_start_time)
    private RelativeLayout mLlHistoryDetailStartTime;
    @ViewInject(R.id.tv_history_detail_start_time)
    private TextView mTvHistoryDetailStartTime;
    @ViewInject(R.id.ll_history_detail_complete_time)
    private RelativeLayout mLlHistoryDetailCompleteTime;
    @ViewInject(R.id.tv_history_detail_complete_time)
    private TextView mTvHistoryDetailCompleteTime;

    @ViewInject(R.id.tv_history_detail_ht)
    private TextView mTvHistoryDetailHT;

    @ViewInject(R.id.tv_history_detail_origin)
    private TextView mTvHistoryDetailOrigin;

    private Context mContext;
    private String mBidId;
    private String mBidName;
    private String mTenderId;

    private BidDetailEntity.ResultBean resultBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_history_detail);
        ViewUtils.inject(this);

        mContext = this;
        getIntentData();
    }

    private void getIntentData() {
        mBidId = getIntent().getStringExtra("bid_id");
        mBidName = getIntent().getStringExtra("bid_name");
        mTenderId = getIntent().getStringExtra("bid_tender");
        String bid_status = getIntent().getStringExtra("bid_status");
        String bid_status_name = getIntent().getStringExtra("bid_status_name");
        mTvHistoryDetailName.setText(mBidName);
        mTvHistoryDetailStatus.setText(bid_status_name);

        mActionBar.setTitle("投资详情");
        getBidDetailData(mBidId);
    }

    private void getBidDetailData(String bidId) {
        SenderResultModel resultModel = ParamsManager.senderBidHistoryDetail(bidId, mTenderId);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("标的详情 " + responeJson);
                        BidHistoryDetailEntity entity = GsonParser.getParsedObj(responeJson, BidHistoryDetailEntity.class);
                        BidHistoryDetailEntity.ResultBean resultBean = entity.getResult();

                        mTvHistoryDetailID.setText(resultBean.getBianhao());
                        mTvHistoryDetailApr.setText(resultBean.getNianhua());
                        mTvHistoryDetailDuration.setText(resultBean.getQixian());
                        mTvHistoryDetailWay.setText(resultBean.getHuankuan());
                        mTvHistoryDetailMoney.setText(resultBean.getBenjin()+"元");
                        mTvHistoryDetailGet.setText(resultBean.getShouyi()+"元");

                        if(null != resultBean.getDaoqi() && resultBean.getDaoqi().length()>0){
                            mTvHistoryDetailDate.setText(resultBean.getDaoqi());
                        }
                        else{
                            mLlHistoryDetailDate.setVisibility(View.GONE);
                        }

                        if(null != resultBean.getHongbao() && resultBean.getHongbao().length()>0){
                            mTvHistoryDetailHongbao.setText(resultBean.getHongbao());
                        }
                        else{
                            mLlHistoryDetailHongbao.setVisibility(View.GONE);
                        }

                        mTvHistoryDetailSuccessTime.setText(resultBean.getChenggong());
                        if(null != resultBean.getJixi() && resultBean.getJixi().length()>0){
                            mTvHistoryDetailStartTime.setText(resultBean.getJixi());
                        }
                        else{
                            mLlHistoryDetailStartTime.setVisibility(View.GONE);
                        }
                        if(null != resultBean.getHuikuan() && resultBean.getHuikuan().length()>0){
                            mTvHistoryDetailCompleteTime.setText(resultBean.getHuikuan());
                        }
                        else{
                            mLlHistoryDetailCompleteTime.setVisibility(View.GONE);
                        }
                        if(resultBean.getXieyi()==1){
                            mTvHistoryDetailHT.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("标的详情失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }


    @OnClick({R.id.btn_left, R.id.tv_history_detail_origin, R.id.tv_history_detail_ht})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;

            case R.id.tv_history_detail_origin:
                Intent intent = new Intent(mContext, BidDetailActivity.class);
                intent.putExtra("bid_title", mBidName);
                intent.putExtra("bid_id", mBidId);
                intent.putExtra("show_btn", false);
                mContext.startActivity(intent);
                break;

            case R.id.tv_history_detail_ht:
//                Intent urlIntent= new Intent();
//                urlIntent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(RequestURL.JCJR_TOUZI_OFFICIAL_URL+mTenderId+"&show=1");
//                urlIntent.setData(content_url);
//                startActivity(urlIntent);
                UrlUtil.showHtmlPage(mContext,"积财金融服务协议", RequestURL.JCJR_TOUZI_OFFICIAL_URL+mTenderId+"&show=1",true);
//                Log.d("积财金融服务协议", RequestURL.JCJR_TOUZI_OFFICIAL_URL+mTenderId+"&show=1");
                break;
        }
    }
}
