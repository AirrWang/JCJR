package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.RedPacketEntity;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class RedPacketAdapter extends BaseAdapter {

    private Context mContext;
    private List<RedPacketEntity.ResultBean> mList;
    private String mStatus;

    public RedPacketAdapter(Context context, List<RedPacketEntity.ResultBean> list, String status) {
        mList = list;
        mContext = context;
        mStatus = status;
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

        RedPacketEntity.ResultBean bean = mList.get(i);
        viewHolder.mTvRedType.setText(bean.getType());
        viewHolder.mTvAmt.setText("¥ " + bean.getMoney());
        viewHolder.mTvUseCondition.setText(bean.getRemark());
        viewHolder.mTvValidTerm.setText(bean.getLasttime());

        switch (mStatus){
            case Global.STATUS_AVAILABLE:
                viewHolder.mLlContainerLeft.setBackgroundResource(R.drawable.bg_hb_active_l);
                viewHolder.mLlContainerRight.setBackgroundResource(R.drawable.bg_hb_active_r);
                break;
            case Global.STATUS_USED:
                viewHolder.mLlContainerLeft.setBackgroundResource(R.drawable.bg_hb_unuse_l);
                viewHolder.mLlContainerRight.setBackgroundResource(R.drawable.bg_hb_unuse_r);
                break;
            case Global.STATUS_OVERDUE:
                viewHolder.mLlContainerLeft.setBackgroundResource(R.drawable.bg_hb_unuse_l);
                viewHolder.mLlContainerRight.setBackgroundResource(R.drawable.bg_hb_unuse_r);
                break;
        }

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.ll_container)
        LinearLayout mLlContainer;

        @ViewInject(R.id.ll_container_left)
        LinearLayout mLlContainerLeft;
        @ViewInject(R.id.ll_container_right)
        LinearLayout mLlContainerRight;

        @ViewInject(R.id.tv_red_type)
        TextView mTvRedType;
        @ViewInject(R.id.tv_amt)
        TextView mTvAmt;
        @ViewInject(R.id.tv_use_condition)
        TextView mTvUseCondition;

        @ViewInject(R.id.tv_last_time)
        TextView mTvLastTime;

        @ViewInject(R.id.tv_valid_term)
        TextView mTvValidTerm;
    }
}
