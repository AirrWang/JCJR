package com.ql.jcjr.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.RedPacketAdapter;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.RedPacketEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class UsedRedPacketFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.lv_available)
    private ListView mLvAvailable;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;

    private Context mContext;
    private RedPacketAdapter mAdapter;
    private List<RedPacketEntity.ResultBean> mList = new ArrayList<>();

    // 分页加载索引
    private int mPageIndex = 1;

    @Override
    protected int getContentView() {
        return R.layout.fragment_available;
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
        getUsedCoupon(String.valueOf(mPageIndex));

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
    }

    private void initListVieww() {
        mAdapter = new RedPacketAdapter(mContext, mList, Global.STATUS_USED);
        mLvAvailable.setAdapter(mAdapter);
        mLvAvailable.setOnItemClickListener(this);
    }

    private void getUsedCoupon(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderMyCoupon(pageIndex, "10", Global.STATUS_USED);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("已用红包 " + responeJson);
                RedPacketEntity entity = GsonParser.getParsedObj(responeJson, RedPacketEntity.class);
                if(pageIndex.equals("1")) {
                    mList.clear();
                }
                mList.addAll(entity.getResult());
                mAdapter.notifyDataSetChanged();

                finishRefresh();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("已用红包失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                finishRefresh();
            }

        }, mContext);
    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;
        getUsedCoupon(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getUsedCoupon(String.valueOf(mPageIndex));
    }
}