package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.AutoBidEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;
import com.ql.jcjr.view.InputAmountEditText;

import java.math.BigDecimal;


public class AutoBidActivityNew extends BaseActivity implements
        CompoundButton.OnCheckedChangeListener, TextWatcher{

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;

    @ViewInject(R.id.ll_auto_bid_info)
    private LinearLayout mLlAutoBidInfo;
    @ViewInject(R.id.tv_bid_status)
    private ImageTextHorizontalBarLess mTvBidStatus;
    //设置好的投资金额
    @ViewInject(R.id.tv_bid_amt)
    private ImageTextHorizontalBarLess mTvBidAmt;
    //设置好的投资天数
    @ViewInject(R.id.tv_bid_days)
    private ImageTextHorizontalBarLess mTvBidDays;

    @ViewInject(R.id.ll_auto_bid_set)
    private LinearLayout mLlAutoBidSet;
    @ViewInject(R.id.switch_auto_bid)
    private Switch mSwitchAutoBid;
    //输入的投资金额
    @ViewInject(R.id.et_bid_amt)
    private InputAmountEditText mEtBidAmt;
    //输入天数
    @ViewInject(R.id.et_day_start)
    private EditText mEtDayStart;
    //输入天数
    @ViewInject(R.id.et_day_end)
    private EditText mEtDayEnd;

    @ViewInject(R.id.ll_bid_amt)
    private RelativeLayout mLlBidAmt;
    @ViewInject(R.id.ll_bid_date)
    private RelativeLayout mLlBidDate;

    //设置按钮
    @ViewInject(R.id.btn_setting)
    private Button mBtnSetting;

    private Context mContext;

    private boolean isOnStatusScreen = true;
    private boolean isSetting = false;
    private String mTenderId = null;

    private String mDayStart;
    private String mDayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_bid);

        ViewUtils.inject(this);
        init();
    }

    private void init() {
        mActionBar.setRightText("规则说明");

        mContext = this;
        mSwitchAutoBid.setOnCheckedChangeListener(this);
        mEtBidAmt.addTextChangedListener(this);
        mEtDayStart.addTextChangedListener(this);
        mEtDayEnd.addTextChangedListener(this);
        getAutoBid();
    }

    private void getAutoBid() {
        SenderResultModel resultModel = ParamsManager.senderAutoBid();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("自动投标 " + responeJson);
                AutoBidEntity entity = GsonParser.getParsedObj(responeJson, AutoBidEntity.class);
                AutoBidEntity.ResultBean resultBean = entity.getResult();

                isOnStatusScreen = true;
                if(Global.STATUS_AUTO_BID_SETTING.equals(entity.getStatus()) && resultBean.getDaySign().equals("1")) {
                    isSetting = true;
                    mLlAutoBidInfo.setVisibility(View.VISIBLE);
                    mLlAutoBidSet.setVisibility(View.GONE);

                    mTvBidStatus.setRightTitleText("开启");
//                    mTvBidStatus.setRightTitleColor(getResources().getColor(R.color.font_black));

                    mTenderId = resultBean.getId();

                    mSwitchAutoBid.setChecked(true);
                    //投资金额
                    setInfo(resultBean.getTender_account(), resultBean.getDayfirst(), resultBean.getDaylast());

                    UserData.getInstance().setAutoBidMoney(resultBean.getTender_account());
                    UserData.getInstance().setAutoBidDayStart(resultBean.getDayfirst());
                    UserData.getInstance().setAutoBidDayEnd(resultBean.getDaylast());

//                    mTvBidAmt.setText("￥ "+resultBean.getTender_account());
//                    mEtBidAmt.setText(resultBean.getTender_account());

                    mBtnSetting.setText("修改设置");

//                    mEtDayStart.setText(resultBean.getDayfirst());
//                    mEtDayEnd.setText(resultBean.getDaylast());

//                    mTvBidDays.setText(resultBean.getDayfirst()+"-"+resultBean.getDaylast()+"天");
                }
                else{
                    isSetting = false;
                    mLlAutoBidInfo.setVisibility(View.VISIBLE);
                    mLlAutoBidSet.setVisibility(View.GONE);

                    mTvBidStatus.setRightTitleText("关闭");
//                    mTvBidStatus.setRightTitleColor(getResources().getColor(R.color.font_grey));

                    mTenderId = null;

                    mSwitchAutoBid.setChecked(false);
                    mBtnSetting.setText("修改设置");
                    //投资金额
                    setInfo(UserData.getInstance().getAutoBidMoney(), UserData.getInstance().getAutoBidDayStart(), UserData.getInstance().getAutoBidDayEnd());
//                    mTvBidAmt.setText("￥ "+resultBean.getTender_account());
//                    mEtBidAmt.setText(resultBean.getTender_account());


//                    mEtDayStart.setText(resultBean.getDayfirst());
//                    mEtDayEnd.setText(resultBean.getDaylast());
//
//                    mTvBidDays.setText(resultBean.getDayfirst()+"-"+resultBean.getDaylast()+"天");
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("自动投标 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private void setInfo(String money, String dayStart, String dayEnd){
        if(money.length()==0){
            mTvBidAmt.setRightTitleText("未设置");
//            mTvBidAmt.setRightTitleColor(getResources().getColor(R.color.font_grey));
        }
        else{
            mTvBidAmt.setRightTitleText("￥ "+money);
//            mTvBidAmt.setRightTitleColor(getResources().getColor(R.color.font_black));

            mEtBidAmt.setText(money);
            mEtBidAmt.setSelection(money.length());
        }

        if(dayStart.length()==0){
            mTvBidDays.setRightTitleText("未设置");
//            mTvBidDays.setRightTitleColor(getResources().getColor(R.color.font_grey));
        }
        else{
            mEtDayStart.setText(dayStart);
            mEtDayEnd.setText(dayEnd);

            mTvBidDays.setRightTitleText(dayStart+"-"+dayEnd+"天");
//            mTvBidDays.setRightTitleColor(getResources().getColor(R.color.font_black));
        }
    }

    /**
     * 上传自动投标信息
     */
    private void setAutoBid(String tenderAmt,String dStart,String dEnd) {
        SenderResultModel resultModel = ParamsManager.senderSetAutoBid(tenderAmt, dStart, dEnd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("设置自动投标 " + responeJson);
                ToastUtil.showToast(mContext, "自动投标设置成功！");
                getAutoBid();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("设置自动投标 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    /**
     * 取消自动
     */
    private void cancelAutoBid(String tenderId, final boolean hasNext) {
        SenderResultModel resultModel = ParamsManager.senderCancelAutoBid(tenderId);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("取消自动投标 " + responeJson);

                mTenderId = null;
                if(hasNext){
                    setAutoBid(mEtBidAmt.getText().toString(), mDayStart, mDayEnd);
                }
                else{
                    ToastUtil.showToast(mContext, "取消成功！");
                    getAutoBid();
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("取消自动投标 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private boolean checkInfo() {
        mDayStart = mEtDayStart.getText().toString().trim();
        mDayEnd = mEtDayEnd.getText().toString().trim();

        if (StringUtils.isBlank(mEtBidAmt.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "请输入自动投标金额");
            return false;
        }

        if((StringUtils.isNotBlank(mDayStart) && StringUtils.isBlank(mDayEnd)) ||
                (StringUtils.isNotBlank(mDayEnd) && StringUtils.isBlank(mDayStart))) {
            CommonToast.showHintDialog(mContext, "请输入完整的借款期限");
            return false;
        }

        double start = Double.parseDouble(mEtDayStart.getText().toString());
        double end = Double.parseDouble(mEtDayEnd.getText().toString());
        BigDecimal data1 = new BigDecimal(start);
        BigDecimal data2 = new BigDecimal(end);
        if(data1.compareTo(data2) == 1){
            CommonToast.showHintDialog(mContext,"请输入正确的借款期限范围");
            return false;
        }
        return true;
    }

    private void showCommonDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.common_new_dialog, null);
        Button dialog_btn_cancel = (Button)view.findViewById(R.id.dialog_btn_cancel);
        Button dialog_btn_ok = (Button)view.findViewById(R.id.dialog_btn_ok);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_CENTER);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelAutoBid(mTenderId, false);
            }
        });

        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mSwitchAutoBid.setChecked(true);
            }
        });
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.btn_setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                CommonToast.showHintDialog(AutoBidActivityNew.this, "每次新标发布后，系统将按照设定金额自动投标，如实际可用余额少于设定金额则按实际金额投标。\n自动投标暂不支持红包的自动使用！\n");
                break;
            case R.id.btn_setting:
                if(isOnStatusScreen) {
                    //修改按钮文字
                    mBtnSetting.setText("确认修改");
                    //修改界面显示
                    mLlAutoBidInfo.setVisibility(View.GONE);
                    mLlAutoBidSet.setVisibility(View.VISIBLE);
                    isOnStatusScreen = false;
                }else{
                    boolean check = mSwitchAutoBid.isChecked();
                    if(check){
                        if(checkInfo()) {
                            if(null != mTenderId){
                                cancelAutoBid(mTenderId, true);
                            }
                            else{
                                setAutoBid(mEtBidAmt.getText().toString(), mDayStart, mDayEnd);
                            }
                        }
                    }
                    else{
                        if(null != mTenderId){
                            showCommonDialog();
//                            CommonToast.showDialog(mContext, "确定关闭自动投标吗？", "您的资金可能会闲置哦", "确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    cancelAutoBid(mTenderId, false);
//                                }
//                            }, "取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mSwitchAutoBid.setChecked(true);
//                                }
//                            });
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(isSetting){
//            //已经设置了自动
//            if(!isChecked){
//                //弹出弹框提示
//            }
//        }
//        else{
            //没有设置自动
            if(isChecked){
                mLlBidAmt.setVisibility(View.VISIBLE);
                mLlBidDate.setVisibility(View.VISIBLE);
            }
            else{
                mLlBidAmt.setVisibility(View.GONE);
                mLlBidDate.setVisibility(View.GONE);
            }
//        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                s.delete(0, 1);
            }
        }

        String text = s.toString();
        if(text.indexOf(".")!=-1){
            s.delete(s.length()-1, s.length());
        }
    }
}
