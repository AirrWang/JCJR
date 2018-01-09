package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.AccountAssetsActivity;
import com.ql.jcjr.activity.AutoBidActivity;
import com.ql.jcjr.activity.BindBankCardActivity;
import com.ql.jcjr.activity.CapitalRecordActivity;
import com.ql.jcjr.activity.LoginActivityCheck;
import com.ql.jcjr.activity.MessageCenterActivity;
import com.ql.jcjr.activity.MyRedPacketsActivity;
import com.ql.jcjr.activity.RealNameActivity;
import com.ql.jcjr.activity.RechargeActivity;
import com.ql.jcjr.activity.SettingActivity;
import com.ql.jcjr.activity.WithdrawalsActivity;
import com.ql.jcjr.adapter.ShortCutAdapter;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.entity.MineFragmentEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.SharedPreferencesUtils;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class MeFragment_old extends BaseFragment implements AdapterView.OnItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int INDEX_ZHZC = 0;
//    private static final int INDEX_LQB = 1;
    private static final int INDEX_WDHB = 1;
    private static final int INDEX_ZJJL = 2;
    private static final int INDEX_SMRZ = 3;
    private static final int INDEX_YHK = 4;
//    private static final int INDEX_YQHY = 5;
    private static final int INDEX_ZDTB = 5;
//    private static final int INDEX_WDJF = 7;
//    @ViewInject(R.id.gv_shortcut)
//    private NoScrollGridView mGridView;
    @ViewInject(R.id.tv_total_num)
    private TextView mTvTotalNum;
    @ViewInject(R.id.tv_balance)
    private TextView mTvBalance;
    @ViewInject(R.id.tv_accumulated_income)
    private TextView mTvAccumulatedIncome;
    @ViewInject(R.id.tv_income_received)
    private TextView mTvIncomeReceived;
    @ViewInject(R.id.tv_user_name)
    private TextView mTvUserName;
    @ViewInject(R.id.tv_phone_number)
    private TextView mTvPhoneNum;
    @ViewInject(R.id.civ_user_icon)
    private CircleImageView mUserIcon;
    @ViewInject(R.id.tv_login)
    private TextView mTvLogin;
    @ViewInject(R.id.ll_user_name)
    private LinearLayout mLlUserName;
    private Context mContext;
    private ShortCutAdapter shortCutAdapter;
    private String mRealName;
    private String mUserIconUrl;
    private String mTel;
    private String mIsSetPay;
    private List<Integer> imgList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        init();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible) {
            isLoginOrNot();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(JcbApplication.needReloadMyInfo){
            JcbApplication.needReloadMyInfo = false;
            isLoginOrNot();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();
        SharedPreferencesUtils.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void init() {
        initGridView();
//        getMineFragmentData();

//        isLoginOrNot();

        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivityCheck.class);
                startActivity(intent);
            }
        });
    }

    private void initGridView() {

        shortCutAdapter = new ShortCutAdapter(getActivity(), initImgList(), initTitleList(), 1);
//        mGridView.setAdapter(shortCutAdapter);
//        mGridView.setOnItemClickListener(this);
//        setGridViewHeightBasedOnChildren(mGridView);
    }

    private void isLoginOrNot() {
        if (UserData.getInstance().isLogin()) {
//            isLogin = true;
            mTvLogin.setVisibility(View.GONE);
            mLlUserName.setVisibility(View.VISIBLE);

            getMineFragmentData();
        } else {
            mUserIcon.setImageResource(R.drawable.default_user_icon);
            mTvLogin.setVisibility(View.VISIBLE);
            mLlUserName.setVisibility(View.GONE);

            mTvTotalNum.setText("- -");
            mTvBalance.setText("- -");
            mTvAccumulatedIncome.setText("- -");
            mTvIncomeReceived.setText("- -");
        }
    }

    private List<Integer> initImgList() {
        imgList.clear();
        imgList.add(R.drawable.my_icon_zhzc);
//        imgList.add(R.drawable.my_icon_lqb);
        imgList.add(R.drawable.my_icon_wdhb);
        imgList.add(R.drawable.my_icon_zjjl);
        imgList.add(R.drawable.my_icon_smrz);
        imgList.add(R.drawable.my_icon_yhk);
//        imgList.add(R.drawable.my_icon_yqhy);
        imgList.add(R.drawable.my_icon_zdtb);
//        imgList.add(R.drawable.my_icon_wdjf);
        return imgList;
    }

    private List<String> initTitleList() {
        titleList.clear();
        titleList.add("账户资产");
//        titleList.add("零钱宝");
        titleList.add("我的红包");
        titleList.add("资金记录");
        titleList.add("实名认证");
        titleList.add("银行卡");
//        titleList.add("邀请好友");
        titleList.add("自动投标");
//        titleList.add("我的积分");
        return titleList;
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }

        // GridView 总高度
        int totalHeight;

        // 计算GridView item 高度
        View item = adapter.getView(0, null, gridView);
        item.measure(0, 0);
        int itemHeight = item.getMeasuredHeight();

        // GridView 列数
        int column = getResources().getInteger(R.integer.home_page_grid_columms);

        // 计算GridView 行数
        int row;
        int tmpCount = gridView.getCount() / column;
        if (gridView.getCount() % gridView.getNumColumns() != 0) {
            row = tmpCount + 1;
        } else {
            row = tmpCount;
        }

        // 计算GridView总高度
        totalHeight = itemHeight * row;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 加上分割线高度
        params.height = totalHeight + (tmpCount - 1);

        // 设置GridView布局参数
        gridView.setLayoutParams(params);
    }

    public void getMineFragmentData() {
//        Toast.makeText(getActivity(),"getMineFragmentData", Toast.LENGTH_SHORT).show();
        SenderResultModel resultModel = ParamsManager.senderMineFragment();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("我的页面 " + responeJson);
                        MineFragmentEntity entity =
                                GsonParser.getParsedObj(responeJson, MineFragmentEntity.class);
                        MineFragmentEntity.ResultBean resultBean = entity.getResult();
                        mTvTotalNum.setText(StringUtils.formatMoney(resultBean.getTotal()));
                        mTvBalance.setText(StringUtils.formatMoney(resultBean.getUse_money()));
                        mTvAccumulatedIncome.setText(
                                StringUtils.formatMoney(resultBean.getCollection_interest1()));
                        mTvIncomeReceived.setText(
                                StringUtils.formatMoney(resultBean.getInterest()));

                        mUserIconUrl = resultBean.getHeadImgUrl();
                        UserData.getInstance().setUserIconUrl(mUserIconUrl);
                        GlideUtil.displayPic(mContext, mUserIconUrl, R.drawable.default_user_icon, mUserIcon);
                        mTvPhoneNum.setText(resultBean.getUsername());
                        mTel = resultBean.getUsername();
                        if (StringUtils.isBlank(resultBean.getRealname())) {
                            mTvUserName.setText("去完善身份认证");
                            mRealName = "";
                        } else {
                            mTvUserName.setText(resultBean.getRealname());
                            mRealName = resultBean.getRealname();
                        }
                        UserData.getInstance().setRealName(mRealName);

                        UserData.getInstance().setIsSetPay(resultBean.getIssetPay());
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("我的页面失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();

        if (!UserData.getInstance().isLogin()) {
            intent.setClass(mContext, LoginActivityCheck.class);
            startActivity(intent);
            return;
        }
        switch (position) {
            case INDEX_ZHZC:
                intent.setClass(mContext, AccountAssetsActivity.class);
                startActivity(intent);
                break;
//            case INDEX_LQB:
//                CommonToast.showHintDialog(mContext, "该功能尚未开发，敬请期待！");
//                break;
            case INDEX_WDHB:
                intent.setClass(mContext, MyRedPacketsActivity.class);
                intent.putExtra("use_type", MyRedPacketFragment.TYPE_MY_HB);
                startActivity(intent);
                break;
            case INDEX_ZJJL:
                intent.setClass(mContext, CapitalRecordActivity.class);
                startActivity(intent);
                break;
            case INDEX_SMRZ:
                intent.setClass(mContext, RealNameActivity.class);
                startActivity(intent);
                break;
            case INDEX_YHK:
                if (StringUtils.isNotBlank(mRealName)) {
                    intent.setClass(mContext, BindBankCardActivity.class);
                    intent.putExtra("real_name", mRealName);
                    intent.putExtra("tel", mTel);
                    startActivity(intent);
                } else {
//                    CommonToast.showHintDialog(mContext, "您还未实名认证！");
                    CommonToast.showShiMingDialog(mContext);
                }
                break;
//            case INDEX_YQHY:
//                CommonToast.showHintDialog(mContext, "该功能尚未开发，敬请期待！");
//                break;
            case INDEX_ZDTB:
                intent.setClass(mContext, AutoBidActivity.class);
                startActivity(intent);
                break;
//            case INDEX_WDJF:
//                CommonToast.showHintDialog(mContext, "该功能尚未开发，敬请期待！");
//                break;
        }
    }

    @OnClick({R.id.btn_setting, R.id.tv_withdrawals, R.id.tv_recharge, R.id.btn_notice})
    public void onClick(View v) {
        Intent intent = new Intent();
        if (!UserData.getInstance().isLogin()) {
            intent.setClass(mContext, LoginActivityCheck.class);
            startActivity(intent);
            return;
        }

        switch (v.getId()) {
            case R.id.btn_setting:
                intent.setClass(mContext, SettingActivity.class);
                intent.putExtra("user_icon_url", mUserIconUrl);
//                intent.putExtra("isSetPay", mIsSetPay);
                startActivity(intent);
                break;
            case R.id.tv_withdrawals:
                if (StringUtils.isNotBlank(mRealName)) {
                    intent.setClass(mContext, WithdrawalsActivity.class);
                    intent.putExtra("account_balance", mTvBalance.getText().toString());
                    startActivity(intent);
                } else {
//                    CommonToast.showHintDialog(mContext, "您还未实名认证！");
                    CommonToast.showShiMingDialog(mContext);
                }
                break;
            case R.id.tv_recharge:
                if (StringUtils.isNotBlank(mRealName)) {
                    intent.setClass(mContext, RechargeActivity.class);
                    startActivity(intent);
                } else {
//                    CommonToast.showHintDialog(mContext, "您还未实名认证！");
                    CommonToast.showShiMingDialog(mContext);
                }
                break;
            case R.id.btn_notice:
                intent.setClass(mContext, MessageCenterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SharedPreferencesUtils.KEY_USER_ID)
                || key.equals(SharedPreferencesUtils.KEY_USER_ICON_URL)
                || key.equals(SharedPreferencesUtils.KEY_REAL_NAME)) {

            if (isAdded()) {
//                Toast.makeText(getActivity(), "onSharedPreferenceChanged", Toast.LENGTH_SHORT).show();
                isLoginOrNot();
            }
        }

    }
}