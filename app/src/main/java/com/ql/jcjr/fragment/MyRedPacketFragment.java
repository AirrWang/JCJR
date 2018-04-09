package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.HongBaoHistoryActivity;
import com.ql.jcjr.activity.WebViewActivity;
import com.ql.jcjr.adapter.RedPacketAdapterNew;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.RedPacketEntityNew;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.model.H5Request;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class MyRedPacketFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.tv_hb_use_num)
    private TextView mTVHBUseNum;
    @ViewInject(R.id.tv_hb_unuse)
    private TextView mTVHBUnuse;

    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.lv_available)
    private XListView mLvAvailable;

    @ViewInject(R.id.ll_tip_none)
    private LinearLayout mLlTipNone;

    private Context mContext;
    private RedPacketAdapterNew mAdapter;
    private List<RedPacketEntityNew.ResultBean.ListBean> mList = new ArrayList<>();

    private int typeUsage = 0;
    public static final int TYPE_MY_HB = 0;
    public static final int TYPE_USE_HB = 1;

    private int typeHB = 0;
    public static final int TYPE_HB_DK = 0;
    public static final int TYPE_HB_JX = 1;
    public static final int TYPE_HB_FX = 2;

    private String sign;
    private String typeSend;
    private String borrowid;
    private String money;

    private RedPacketClickListener clickListener;

    // 分页加载索引
    private int mPageIndex = 1;

    public void setType(int typeUsage, int typeHB, String borrowid, String money, RedPacketClickListener clickListener){
        this.typeUsage = typeUsage;
        this.typeHB = typeHB;
        this.borrowid = borrowid;
        this.money = money;
        this.clickListener = clickListener;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_my_hb;
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
        initListView();

        if(typeUsage == TYPE_MY_HB){
            mTVHBUseNum.setVisibility(View.VISIBLE);

            sign = "mine";
            if(typeHB== TYPE_HB_DK){
                mTVHBUnuse.setText("查看历史抵扣券");
                typeSend = "dikou";
            }
            else if(typeHB== TYPE_HB_JX){
                mTVHBUnuse.setText("查看历史加息券");
                typeSend = "cash";
            }
            else if(typeHB== TYPE_HB_FX){
                mTVHBUnuse.setText("查看历史返现券");
                typeSend = "fanxian";
            }
        }
        else if(typeUsage == TYPE_USE_HB){
            mTVHBUseNum.setVisibility(View.GONE);

            sign = "tender";
            if(typeHB== TYPE_HB_DK){
                mTVHBUnuse.setText("不使用抵扣券");
                typeSend = "dikou";
            }
            else if(typeHB== TYPE_HB_JX){
                mTVHBUnuse.setText("不使用加息券");
                typeSend = "cash";
            }
            else if(typeHB== TYPE_HB_FX){
                mTVHBUnuse.setText("不使用返现券");
                typeSend = "fanxian";
            }
        }

        getAvailableCoupon(String.valueOf(mPageIndex));

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
    }

    private void initListView() {
        mAdapter = new RedPacketAdapterNew(mContext, mList, Global.STATUS_AVAILABLE, typeHB);
        mLvAvailable.setAdapter(mAdapter);
        mLvAvailable.setOnItemClickListener(this);
    }

    private void getAvailableCoupon(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderMyCouponNew(pageIndex, "10", sign, typeSend, borrowid, money);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("我的红包 " + responeJson);
                RedPacketEntityNew entity = GsonParser.getParsedObj(responeJson, RedPacketEntityNew.class);

                mTVHBUseNum.setText("可用数量："+entity.getResult().getNum());

                if(pageIndex.equals("1")) {
                    mList.clear();
                }

                boolean needShowAll = false;
                int listSize = entity.getResult().getList().size();
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
                    mLvAvailable.addFooterView();
                    //设置不在加载更多
                    mPullToRefreshView.setLoadMoreEnable(false);
                }

                mList.addAll(entity.getResult().getList());
                mAdapter.notifyDataSetChanged();
                finishRefresh();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("我的红包失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                finishRefresh();
            }

        }, mContext);
    }

    @OnClick({R.id.tv_hb_unuse,R.id.tv_jump_tip})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_hb_unuse:
                if(typeUsage == TYPE_MY_HB){
                    //历史红包
                    Intent intent = new Intent(getActivity(), HongBaoHistoryActivity.class);
                    intent.putExtra("TYPE_HB",typeHB);
                    getActivity().startActivity(intent);
                }
                else if(typeUsage == TYPE_USE_HB){
                    //不使用红包
                    if(null != clickListener){
                        clickListener.clickNoUse();
                    }
                }
                break;
            case R.id.tv_jump_tip:
                H5Request h5Request = new H5Request();
                h5Request.setTitle("兑换中心");
                h5Request.setUrl(RequestURL.INTERCEPT_EXCHANGE_URL);
                h5Request.setIsLeftable(true);
                h5Request.setIsShowActionBar(false);
                Intent intent = new Intent(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("h5Request", h5Request);
                intent.putExtras(bundle);
                intent.putExtra("isShare",true);
                startActivityForResult(intent,1);
                break;
        }
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
        if(null != clickListener){
            RedPacketEntityNew.ResultBean.ListBean bean = mList.get(position);
            String cashid = bean.getCashid();
            String type = bean.getType();
            String money = null;
            String cashApr = null;
            if(type.equals(Global.HB_TYPE_JX)){
                money = bean.getCashMoney();
                cashApr = bean.getCashApr();
            }
            else{
                money = bean.getMoney();
            }

            clickListener.clickUseHb(cashid, type, money, cashApr);
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;

        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvAvailable.removeFooterView();

        getAvailableCoupon(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getAvailableCoupon(String.valueOf(mPageIndex));
    }

    public interface RedPacketClickListener{
        void clickNoUse();
        void clickUseHb(String cashid, String hbType, String hbMoney, String cashApr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            init();
        }
    }

}