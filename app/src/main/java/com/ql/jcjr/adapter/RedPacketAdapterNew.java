package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.RedPacketEntityNew;
import com.ql.jcjr.fragment.MyRedPacketFragment;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class RedPacketAdapterNew extends BaseAdapter {

    private Context mContext;
    private List<RedPacketEntityNew.ResultBean.ListBean> mList;
    private String mStatus;
    private int typeHB;

    public RedPacketAdapterNew(Context context, List<RedPacketEntityNew.ResultBean.ListBean> list, String status, int typeHB) {
        mList = list;
        mContext = context;
        mStatus = status;
        this.typeHB = typeHB;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_red_packet, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mTvAmtPre.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        viewHolder.mTvAmt.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        viewHolder.mTvAmtSuf.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        viewHolder.mTvRedType.setTypeface(JcbApplication.getPingFangBoldTypeFace());

        RedPacketEntityNew.ResultBean.ListBean bean = mList.get(i);
        viewHolder.mTvRedType.setText(bean.getQuanbaoname());
        if(typeHB == MyRedPacketFragment.TYPE_HB_DK){
            viewHolder.mTvAmt.setText(bean.getMoney());
            viewHolder.mTvAmtSuf.setVisibility(View.GONE);
        }
        else if(typeHB == MyRedPacketFragment.TYPE_HB_JX){
            viewHolder.mTvAmt.setText(bean.getCashApr());
            viewHolder.mTvAmtPre.setVisibility(View.GONE);
        }
        else if(typeHB == MyRedPacketFragment.TYPE_HB_FX){
            viewHolder.mTvAmt.setText(bean.getMoney());
            viewHolder.mTvAmtSuf.setVisibility(View.GONE);
        }
        viewHolder.mTvUseCondition.setText(bean.getRemark().replace(",","\n"));
        viewHolder.mTvValidTerm.setText(bean.getLasttime());
        viewHolder.mTvLastTime.setText("剩余"+bean.getUse_day()+"天过期");
        if (bean.getNewsign().equals("1")){
            viewHolder.mIvSignNew.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mIvSignNew.setVisibility(View.GONE);
        }


        switch (mStatus){
            case Global.STATUS_AVAILABLE:
                viewHolder.ll_containerall.setBackgroundResource(R.drawable.img_hbbg1);
                break;
            case Global.STATUS_USED:
                viewHolder.ll_containerall.setBackgroundResource(R.drawable.img_hbbg2);
                int color = mContext.getResources().getColor(R.color.font_grey);
                viewHolder.mTvAmtPre.setTextColor(color);
                viewHolder.mTvAmt.setTextColor(color);
                viewHolder.mTvAmtSuf.setTextColor(color);
                viewHolder.mTvRedType.setTextColor(color);
                viewHolder.tv_1.setTextColor(color);
                viewHolder.mTvValidTerm.setTextColor(color);
                viewHolder.mTvLastTime.setTextColor(color);
                viewHolder.mTvLastTime.setVisibility(View.INVISIBLE);
                viewHolder.iv_hbtx.setImageResource(R.drawable.hbdq2);

                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                if(bean.getStatus().equals("0")){
                    viewHolder.mIvStatus.setImageResource(R.drawable.img_hb_status_past);
                }
                else{
                    viewHolder.mIvStatus.setImageResource(R.drawable.img_hb_status_used);
                }

                break;
//            case Global.STATUS_OVERDUE:
//                viewHolder.mLlContainerLeft.setBackgroundResource(R.drawable.bg_hb_unuse_l);
//                viewHolder.mLlContainerRight.setBackgroundResource(R.drawable.bg_hb_unuse_r);
//                break;
        }

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.ll_container)
        LinearLayout mLlContainer;

        @ViewInject(R.id.ll_container_left)
        LinearLayout mLlContainerLeft;

        @ViewInject(R.id.tv_red_type)
        TextView mTvRedType;

        @ViewInject(R.id.tv_amt)
        TextView mTvAmt;

        @ViewInject(R.id.tv_amt_pre)
        TextView mTvAmtPre;
        @ViewInject(R.id.tv_amt_suf)
        TextView mTvAmtSuf;

        @ViewInject(R.id.tv_use_condition)
        TextView mTvUseCondition;

        @ViewInject(R.id.tv_last_time)
        TextView mTvLastTime;

        @ViewInject(R.id.tv_valid_term)
        TextView mTvValidTerm;

        @ViewInject(R.id.iv_status)
        ImageView mIvStatus;

        @ViewInject(R.id.iv_sign_new)
        ImageView mIvSignNew;

        @ViewInject(R.id.ll_containerall)
        LinearLayout ll_containerall;

        @ViewInject(R.id.iv_hbtx)
        ImageView iv_hbtx;

        @ViewInject(R.id.tv_1)
        TextView tv_1;
    }
}
