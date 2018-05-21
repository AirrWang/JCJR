package com.ql.jcjr.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.entity.BidListEntity;
import com.ql.jcjr.utils.StringUtils;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class YyyAdapter extends BaseAdapter {

    private Context mContext;
    private List<BidListEntity.ResultBean> mList;

    public YyyAdapter(Context context, List<BidListEntity.ResultBean> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_yyy, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final BidListEntity.ResultBean resultBean = mList.get(i);

        if(resultBean.getBremark().length()>0){
            viewHolder.mTvTag.setVisibility(View.VISIBLE);
            viewHolder.mTvTag.setText(resultBean.getBremark());
        }
        else{
            viewHolder.mTvTag.setVisibility(View.GONE);
        }
        if(StringUtils.isBlank(resultBean.getBremark1())){
            viewHolder.mTvTag2.setVisibility(View.GONE);
        }
        else{
            viewHolder.mTvTag2.setVisibility(View.VISIBLE);
            viewHolder.mTvTag2.setText(resultBean.getBremark1());
        }
        //年化收益
        viewHolder.mApr.setText(resultBean.getApr());
        viewHolder.mApr.setTypeface(JcbApplication.getPingFangRegularTypeFace());

        viewHolder.mAnnualizedRate.setTypeface(JcbApplication.getPingFangRegularTypeFace());
        viewHolder.mAnnualizedRate.setText(resultBean.getAprOrigin());
        //判断有无活动加成
        String cashAddition = resultBean.getCashAddition();
        if(cashAddition.equals("0") || cashAddition.equals("0.0")){
            viewHolder.mLinearLayoutGain.setVisibility(View.INVISIBLE);
        }
        else{
            viewHolder.mLinearLayoutGain.setVisibility(View.VISIBLE);
            viewHolder.mAnnualizedRateGain.setTypeface(JcbApplication.getPingFangRegularTypeFace());
            viewHolder.mAnnualizedRateGain.setText(resultBean.getCashAddition());
        }
        viewHolder.mTvTitle.setText(resultBean.getName());

        //投资期限
        switch (resultBean.getIsday()){
            case "0":
                viewHolder.mTvTerm.setText(resultBean.getTime_limit() + "个月");
                break;
            case "1":
                viewHolder.mTvTerm.setText(resultBean.getTime_limit_day() + "天");
                break;
        }
        Resources resources = mContext.getResources();
        //起投金额
//        viewHolder.mLowestAmt.setText(mContext.getString(R.string.str_lowest_account_2, resultBean.getLowest_account()));
        //总额
        String result = "项目总额" + resultBean.getAccount_format() + "元";

        //可投
        String resultRest = "剩余" + resultBean.getSurplus() + "元";


        //百分比
        viewHolder.mProgressBar.setMax(resultBean.getAccount());


        if(StringUtils.isBlank(resultBean.getSurplus()) || "0.00".equals(resultBean.getSurplus()) || "0".equals(resultBean.getSurplus())) {
//            viewHolder.mBtnBid.setText("筹款完成");
//            viewHolder.mBtnBid.setBackgroundResource(R.drawable.btn_bg_enable);
            view.setBackgroundColor(resources.getColor(R.color.item_yyy_bg_gery));
            viewHolder.mTvPercent.setText("100%");
            viewHolder.mTvPercent.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mIvIcon.setImageResource(R.drawable.icon_decorate_disable_lclb);

            viewHolder.mTvTag.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mTvTag.setBackgroundResource(R.drawable.bg_tag_grey);
            viewHolder.mTvTag2.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mTvTag2.setBackgroundResource(R.drawable.bg_tag_grey);

            viewHolder.mLinearLayoutGain.setBackgroundResource(R.drawable.bg_yyy_apr_grey);

            viewHolder.mApr.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mAprAfter.setTextColor(resources.getColor(R.color.c_cbcbcb));

            viewHolder.mAnnualizedRate.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mAnnualizedRateAfter.setTextColor(resources.getColor(R.color.c_cbcbcb));

            viewHolder.mAnnualizedRateGainBefore.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mAnnualizedRateGain.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mAnnualizedRateGainAfter.setTextColor(resources.getColor(R.color.c_cbcbcb));

            viewHolder.mTvAvailableTotal.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mTvAvailableTotal.setText(result);
            viewHolder.mTvAvailableRest.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mTvAvailableRest.setText(resultRest);
            viewHolder.mTvTerm.setTextColor(resources.getColor(R.color.c_cbcbcb));
            viewHolder.mTvTitle.setTextColor(resources.getColor(R.color.c_cbcbcb));

            viewHolder.mProgressBar.setProgress(0);
            viewHolder.mProgressBar.setSecondaryProgress(resultBean.getAccount());

        }else{
            view.setBackgroundColor(resources.getColor(R.color.white));
            //处理有人投了标，但是进度还是0的情况
            int percent = resultBean.getAccount_yes()*100/resultBean.getAccount();
            if(percent == 0 && resultBean.getAccount_yes()>0){
                percent = 1;
            }

            viewHolder.mTvPercent.setText(percent+"%");
            viewHolder.mTvPercent.setTextColor(resources.getColor(R.color.font_grey));
            viewHolder.mIvIcon.setImageResource(R.drawable.icon_decorate_lclb);

            viewHolder.mTvTag.setTextColor(resources.getColor(R.color.font_red));
            viewHolder.mTvTag.setBackgroundResource(R.drawable.bg_tag_red);
            viewHolder.mTvTag2.setTextColor(resources.getColor(R.color.font_red));
            viewHolder.mTvTag2.setBackgroundResource(R.drawable.bg_tag_red);

            viewHolder.mLinearLayoutGain.setBackgroundResource(R.drawable.bg_yyy_apr);

            viewHolder.mApr.setTextColor(resources.getColor(R.color.font_red));
            viewHolder.mAprAfter.setTextColor(resources.getColor(R.color.font_red));

            viewHolder.mAnnualizedRate.setTextColor(resources.getColor(R.color.font_red));
            viewHolder.mAnnualizedRateAfter.setTextColor(resources.getColor(R.color.font_red));

            viewHolder.mAnnualizedRateGainBefore.setTextColor(resources.getColor(R.color.font_red));
            viewHolder.mAnnualizedRateGain.setTextColor(resources.getColor(R.color.font_red));
            viewHolder.mAnnualizedRateGainAfter.setTextColor(resources.getColor(R.color.font_red));

            SpannableString ss = StringUtils.getSpannableString(result, resources.getColor(R.color.font_black), 4, result.length() - 1);
            viewHolder.mTvAvailableTotal.setText(ss);
            SpannableString ssRest = StringUtils.getSpannableString(resultRest, resources.getColor(R.color.font_black), 2, resultRest.length() - 1);
            viewHolder.mTvAvailableRest.setText(ssRest);
            viewHolder.mTvTerm.setTextColor(resources.getColor(R.color.font_black));
            viewHolder.mTvTitle.setTextColor(resources.getColor(R.color.font_black));

            viewHolder.mProgressBar.setProgress(resultBean.getAccount_yes());
            viewHolder.mProgressBar.setSecondaryProgress(0);

        }

        return view;
    }

    class ViewHolder {
//        @ViewInject(R.id.btn_bid)
//        public Button mBtnBid;

        //tag
        @ViewInject(R.id.tv_tag)
        public TextView mTvTag;
        @ViewInject(R.id.tv_tag_2)
        public TextView mTvTag2;

        //标名
        @ViewInject(R.id.iv_yyy_icon)
        public ImageView mIvIcon;

        //标名
        @ViewInject(R.id.tv_title)
        public TextView mTvTitle;
        //时间
        @ViewInject(R.id.tv_term)
        public TextView mTvTerm;

        //总年化
        @ViewInject(R.id.tv_apr)
        public TextView mApr;
        @ViewInject(R.id.tv_apr_after)
        public TextView mAprAfter;

        //原来的年化
        @ViewInject(R.id.tv_annualized_rate)
        public TextView mAnnualizedRate;
        @ViewInject(R.id.tv_annualized_rate_after)
        public TextView mAnnualizedRateAfter;
        //活动增加的年化
        @ViewInject(R.id.tv_annualized_rate_gain_before)
        public TextView mAnnualizedRateGainBefore;
        @ViewInject(R.id.tv_annualized_rate_gain)
        public TextView mAnnualizedRateGain;
        @ViewInject(R.id.tv_annualized_rate_gain_after)
        public TextView mAnnualizedRateGainAfter;
        //活动年化 ll
        @ViewInject(R.id.ll_biao_right)
        public LinearLayout mLinearLayoutGain;

//        @ViewInject(R.id.tv_lowest_amt)
//        public TextView mLowestAmt;
        //总额
        @ViewInject(R.id.tv_available_total)
        public TextView mTvAvailableTotal;
        //剩余
        @ViewInject(R.id.tv_available_rest)
        public TextView mTvAvailableRest;

        //百分比
        @ViewInject(R.id.tv_percent)
        public TextView mTvPercent;
        @ViewInject(R.id.pb_percentage)
        public ProgressBar mProgressBar;
    }
}
