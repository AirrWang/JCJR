package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.ContactUsActivity;
import com.ql.jcjr.activity.MessageActActivity;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.entity.FindEntity;
import com.ql.jcjr.entity.MessageActEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;


public class FindFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener {

    @ViewInject(R.id.tv_get_more)
    TextView tv_get_more;
    @ViewInject(R.id.iv_find_1)
    ImageView iv_find_1;
    @ViewInject(R.id.iv_find_2)
    ImageView iv_find_2;
    @ViewInject(R.id.iv_find_3)
    ImageView iv_find_3;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullRefresh;
    @ViewInject(R.id.iv_0)
    private ImageView iv_0;
    @ViewInject(R.id.iv_1)
    private ImageView iv_1;
    @ViewInject(R.id.iv_2)
    private ImageView iv_2;
    @ViewInject(R.id.iv_3)
    private ImageView iv_3;
    @ViewInject(R.id.tv_0)
    private TextView tv_0;
    @ViewInject(R.id.tv_1)
    private TextView tv_1;
    @ViewInject(R.id.tv_2)
    private TextView tv_2;
    @ViewInject(R.id.tv_3)
    private TextView tv_3;

    @ViewInject(R.id.ab_header)
    private ActionBar mHeader;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private MessageActEntity entity;
    private String iv1="";
    private String iv2="";
    private String iv3="";
    private Boolean b1=true;
    private Boolean b2=true;
    private Boolean b3=true;
    private String url0="";
    private String url1="";
    private String url2="";
    private String url3="";


    @Override
    protected int getContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPullRefresh.setOnHeaderRefreshListener(this);
        mPullRefresh.setOnFooterLoadListener(this);
        mPullRefresh.removeFootView();
        mHeader.setRightText("帮助中心");
    }

    @Override
    protected void onFragmentFirstVisible() {
        init();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible) {
            getYyyList();
            getMessageCneterList();
        }
    }

    private void init() {


    }


    private void getYyyList() {
        SenderResultModel resultModel = ParamsManager.getFind();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("发现 " + responeJson);
                        mPullRefresh.onHeaderRefreshFinish();
                        FindEntity entity = GsonParser.getParsedObj(responeJson, FindEntity.class);
                        if (entity.getResult().size()<4){
                            return;
                        }
                        initFour(entity.getResult());
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("发现 " + entity.errorInfo);
                        mPullRefresh.onHeaderRefreshFinish();
                    }

                }, mContext);

    }

    private void initFour(List<FindEntity.ResultBean> entity) {
        tv_0.setText(entity.get(0).getTitle());
        tv_1.setText(entity.get(1).getTitle());
        tv_2.setText(entity.get(2).getTitle());
        tv_3.setText(entity.get(3).getTitle());
        GlideUtil.displayPic(mContext,entity.get(0).getPic(),R.drawable.ptjs_sy,iv_0);
        GlideUtil.displayPic(mContext,entity.get(1).getPic(),R.drawable.yqyl_sy,iv_1);
        GlideUtil.displayPic(mContext,entity.get(2).getPic(),R.drawable.dhzx_sy,iv_2);
        GlideUtil.displayPic(mContext,entity.get(3).getPic(),R.drawable.mrqd_sy,iv_3);
        url0 = entity.get(0).getLink();
        url1 = entity.get(1).getLink();
        url2 = entity.get(2).getLink();
        url3 = entity.get(3).getLink();
    }


    private void getMessageCneterList() {
        SenderResultModel resultModel = ParamsManager.senderMessageAct("1", "3", 0);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("精选活动 " + responeJson);
                        mPullRefresh.onHeaderRefreshFinish();
                        entity = GsonParser.getParsedObj(responeJson, MessageActEntity.class);
                        GlideUtil.displayPic(mContext, entity.getResult().get(0).getLitpic(), -1, iv_find_1);
                        GlideUtil.displayPic(mContext, entity.getResult().get(1).getLitpic(), -1, iv_find_2);
                        GlideUtil.displayPic(mContext, entity.getResult().get(2).getLitpic(), -1, iv_find_3);
                        iv1 = entity.getResult().get(0).getId();
                        iv2 = entity.getResult().get(1).getId();
                        iv3 = entity.getResult().get(2).getId();
                        if (entity.getResult().get(0).getShare().equals("0")){
                            b1=true;
                        }else {
                            b1=false;
                        }
                        if (entity.getResult().get(1).getShare().equals("0")){
                            b2=true;
                        }else {
                            b2=false;
                        }
                        if (entity.getResult().get(2).getShare().equals("0")){
                            b3=true;
                        }else {
                            b3=false;
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("精选活动 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        mPullRefresh.onHeaderRefreshFinish();
                    }

                }, mContext);
    }


    @OnClick({R.id.iv_find_1,R.id.iv_find_2,R.id.iv_find_3,R.id.tv_get_more,R.id.ll_ptjs, R.id.ll_yqyl,R.id.ll_dhzx,R.id.ll_mrqd,R.id.btn_right})
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.tv_get_more:
                intent.setClass(mContext, MessageActActivity.class);
                intent.putExtra("msg_type",0);
                startActivity(intent);
                break;
            case R.id.iv_find_1:
                UrlUtil.showHtmlPage(mContext,"精选活动", iv1,b1);
                break;
            case R.id.iv_find_2:
                UrlUtil.showHtmlPage(mContext,"精选活动", iv2,b2);
                break;
            case R.id.iv_find_3:
                UrlUtil.showHtmlPage(mContext,"精选活动", iv3,b3);
                break;

            case R.id.ll_ptjs:

                UrlUtil.showHtmlPage(mContext,tv_0.getText().toString(), url0,true);

                break;

            case R.id.ll_yqyl:

                UrlUtil.showHtmlPage(mContext,tv_1.getText().toString(), url1,true);

                break;

            case R.id.ll_dhzx:

                UrlUtil.showHtmlPage(mContext,tv_2.getText().toString(), url2,true);

                break;

            case R.id.ll_mrqd:

                UrlUtil.showHtmlPage(mContext,tv_3.getText().toString(), url3,true);

                break;
            case R.id.btn_right:
                intent.setClass(mContext, ContactUsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        getYyyList();
        getMessageCneterList();
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPullRefresh.onFooterLoadFinish();
        mPullRefresh.setOnHeaderRefreshListener(this);
        mPullRefresh.setOnFooterLoadListener(this);
        mPullRefresh.removeFootView();
    }
}