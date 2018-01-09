package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.AutoBidEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.InputAmountEditText;

import java.math.BigDecimal;


public class AutoBidActivity extends BaseActivity implements
        CompoundButton.OnCheckedChangeListener{

    @ViewInject(R.id.et_bid_amt)
    private InputAmountEditText mEtBidAmt;
    @ViewInject(R.id.tv_bid_amt)
    private TextView mTvBidAmt;
    @ViewInject(R.id.btn_setting)
    private Button mBtnSetting;
    @ViewInject(R.id.cb_month)
    private CheckBox mCbMonth;
    @ViewInject(R.id.cb_day)
    private CheckBox mCbDay;
    @ViewInject(R.id.ll_month)
    private LinearLayout mLlMonth;
    @ViewInject(R.id.ll_day)
    private LinearLayout mLlDay;
    @ViewInject(R.id.et_month_start)
    private EditText mEtMonthStart;
    @ViewInject(R.id.et_month_end)
    private EditText mEtMonthEnd;
    @ViewInject(R.id.et_day_start)
    private EditText mEtDayStart;
    @ViewInject(R.id.et_day_end)
    private EditText mEtDayEnd;

    private Context mContext;

    private boolean isSetting = false;
    private String mTenderId;
    private String mTenderAmt;

    private String mMonthStart;
    private String mMonthEnd;
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
        mContext = this;
        mCbMonth.setOnCheckedChangeListener(this);
        mCbDay.setOnCheckedChangeListener(this);
        getAutoBid();
    }

    private void getAutoBid() {
        SenderResultModel resultModel = ParamsManager.senderAutoBid();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("自动投标 " + responeJson);
                AutoBidEntity entity = GsonParser.getParsedObj(responeJson, AutoBidEntity.class);
                if(Global.STATUS_AUTO_BID_SETTING.equals(entity.getStatus())) {
                    isSetting = true;
                    mTvBidAmt.setVisibility(View.VISIBLE);
                    mEtBidAmt.setVisibility(View.GONE);
                    AutoBidEntity.ResultBean resultBean = entity.getResult();
                    mTenderId = resultBean.getId();
                    mTenderAmt = resultBean.getTender_account();
                    String result = "自动投标 " + resultBean.getTender_account() + " 元";
                    SpannableString ss = StringUtils.getSpannableString(result, getResources().getColor(R.color.title_bg_color), 5, result.length() - 1);
                    mTvBidAmt.setText(ss);
                    mBtnSetting.setText("取消设置");
                    mBtnSetting.setBackgroundResource(R.drawable.btn_bg_f96503);

                    if(resultBean.getMonthSign().equals("1")) {
                        mCbMonth.setChecked(true);
                    }

                    if(resultBean.getDaySign().equals("1")) {
                        mCbDay.setChecked(true);
                    }

                    mEtMonthStart.setText(resultBean.getMonthfirst());
                    mEtMonthEnd.setText(resultBean.getMonthlast());
                    mEtDayStart.setText(resultBean.getDayfirst());
                    mEtDayEnd.setText(resultBean.getDaylast());

                    mCbMonth.setEnabled(false);
                    mCbDay.setEnabled(false);
                    mEtMonthStart.setEnabled(false);
                    mEtMonthEnd.setEnabled(false);
                    mEtDayStart.setEnabled(false);
                    mEtDayEnd.setEnabled(false);
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("自动投标 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    /**
     * 设置
     */
    private void setAutoBid(String tenderAmt,String mStart,String mEnd,String dStart,String dEnd) {
        SenderResultModel resultModel = ParamsManager.senderSetAutoBid(tenderAmt, mStart, mEnd, dStart, dEnd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("设置自动投标 " + responeJson);
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
     * 取消
     */
    private void cancelAutoBid(String tenderId) {
        SenderResultModel resultModel = ParamsManager.senderCancelAutoBid(tenderId);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("取消自动投标 " + responeJson);
//                isSetting = false;
//                mTvBidAmt.setVisibility(View.GONE);
//                mEtBidAmt.setText("");
//                mEtBidAmt.setVisibility(View.VISIBLE);
//                mBtnSetting.setText("设置");
//                mBtnSetting.setBackgroundResource(R.drawable.btn_guide_ff8f39);

                CommonToast.makeCustomText(mContext, "取消成功！");
                finish();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("取消自动投标 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private boolean checkInfo() {
        mMonthStart = mEtMonthStart.getText().toString().trim();
        mMonthEnd = mEtMonthEnd.getText().toString().trim();
        mDayStart = mEtDayStart.getText().toString().trim();
        mDayEnd = mEtDayEnd.getText().toString().trim();

        if (StringUtils.isBlank(mEtBidAmt.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "请输入自动投标金额");
            return false;
        }

        if(mCbMonth.isChecked()) {
            if((StringUtils.isNotBlank(mMonthStart) && StringUtils.isBlank(mMonthEnd)) ||
                    (StringUtils.isNotBlank(mMonthEnd) && StringUtils.isBlank(mMonthStart))) {
                CommonToast.showHintDialog(mContext, "请输入完整的借款期限");
                return false;
            }

            double start = Double.parseDouble(mEtMonthStart.getText().toString());
            double end = Double.parseDouble(mEtMonthEnd.getText().toString());
            BigDecimal data1 = new BigDecimal(start);
            BigDecimal data2 = new BigDecimal(end);
            if(data1.compareTo(data2) == 1){
                CommonToast.showHintDialog(mContext,"请输入正确的借款期限范围");
                return false;
            }
        }

        if(mCbDay.isChecked()) {
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
        }


        return true;
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.btn_setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                mCbMonth.setEnabled(true);
                mCbDay.setEnabled(true);
                mEtMonthStart.setEnabled(true);
                mEtMonthEnd.setEnabled(true);
                mEtDayStart.setEnabled(true);
                mEtDayEnd.setEnabled(true);

                mEtBidAmt.setText(mTenderAmt);
                mTvBidAmt.setVisibility(View.GONE);
                mEtBidAmt.setVisibility(View.VISIBLE);

                isSetting = false;
                mBtnSetting.setText("设置");
                mBtnSetting.setBackgroundResource(R.drawable.btn_guide_ff8f39);

                break;
            case R.id.btn_setting:
                if(isSetting) {
                    cancelAutoBid(mTenderId);
                }else{
                    if(checkInfo()) {
                        setAutoBid(mEtBidAmt.getText().toString(), mMonthStart, mMonthEnd, mDayStart, mDayEnd);
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id){
            case R.id.cb_month:
                if(isChecked) {
                    mLlMonth.setVisibility(View.VISIBLE);
                }else {
                    mLlMonth.setVisibility(View.GONE);
                }
                break;
            case R.id.cb_day:
                if(isChecked) {
                    mLlDay.setVisibility(View.VISIBLE);
                }else {
                    mLlDay.setVisibility(View.GONE);
                }
                break;
        }
    }
}
