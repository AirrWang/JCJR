package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.entity.BidHistoryEntity;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class BidHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<BidHistoryEntity.ResultBean.ListBean> mList;

    public BidHistoryAdapter(Context context, List<BidHistoryEntity.ResultBean.ListBean> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bid_history, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BidHistoryEntity.ResultBean.ListBean bean = mList.get(i);
        viewHolder.mIvBidHistoryName.setText(bean.getName());
        viewHolder.mIvBidHistoryPay.setText(bean.getAccount());
        viewHolder.mIvBidHistoryGet.setText(bean.getInterest());
        viewHolder.mIvBidHistoryStatus.setText(bean.getStatusname());

        String status = bean.getStatus();


        if(status.equals("0")){
            viewHolder.mIvBidHistoryIcon.setImageResource(R.drawable.icon_decorate_lclb);
            viewHolder.mIvBidHistoryStatus.setBackgroundResource(R.drawable.icon_bid_history_mj);
            viewHolder.mIvBidHistoryDate.setVisibility(View.GONE);
            viewHolder.mIvBidHistoryDivider.setVisibility(View.GONE);
        }
        else if(status.equals("1")){
            viewHolder.mIvBidHistoryIcon.setImageResource(R.drawable.icon_decorate_lclb);
            viewHolder.mIvBidHistoryStatus.setBackgroundResource(R.drawable.icon_bid_history_hk);
            viewHolder.mIvBidHistoryDivider.setVisibility(View.VISIBLE);

            String endTime = bean.getEnd_time();
            if(null != endTime && endTime.length()>0){
                viewHolder.mIvBidHistoryDate.setVisibility(View.VISIBLE);
                viewHolder.mIvBidHistoryDate.setText("到期时间  "+bean.getEnd_time());
            }
            else{
                viewHolder.mIvBidHistoryDate.setVisibility(View.GONE);
            }
        } else if(status.equals("2")){
            viewHolder.mIvBidHistoryIcon.setImageResource(R.drawable.icon_decorate_disable_lclb);
            viewHolder.mIvBidHistoryStatus.setBackgroundResource(R.drawable.icon_bid_history_ls);
            viewHolder.mIvBidHistoryDivider.setVisibility(View.VISIBLE);

            String endTime = bean.getEnd_time();
            if(null != endTime && endTime.length()>0){
                viewHolder.mIvBidHistoryDate.setVisibility(View.VISIBLE);
                viewHolder.mIvBidHistoryDate.setText("到期时间  "+bean.getEnd_time());
            }
            else{
                viewHolder.mIvBidHistoryDate.setVisibility(View.GONE);
            }
        }

        if(bean.getOvertime().equals("1")){
            viewHolder.mIvBidHistoryIcon.setImageResource(R.drawable.icon_decorate_lclb);
            viewHolder.mIvBidHistoryStatus.setBackgroundResource(R.drawable.icon_djz_wdtz);
            viewHolder.mIvBidHistoryDivider.setVisibility(View.VISIBLE);

            String endTime = bean.getEnd_time();
            if(null != endTime && endTime.length()>0){
                viewHolder.mIvBidHistoryDate.setVisibility(View.VISIBLE);
                viewHolder.mIvBidHistoryDate.setText("到期时间  "+bean.getEnd_time());
            }
            else{
                viewHolder.mIvBidHistoryDate.setVisibility(View.GONE);
            }
        }
        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.iv_bid_history_icon)
        ImageView mIvBidHistoryIcon;
        @ViewInject(R.id.tv_bid_history_name)
        TextView mIvBidHistoryName;
        @ViewInject(R.id.tv_bid_history_pay)
        TextView mIvBidHistoryPay;
        @ViewInject(R.id.tv_bid_history_status)
        TextView mIvBidHistoryStatus;
        @ViewInject(R.id.tv_bid_history_get)
        TextView mIvBidHistoryGet;
        @ViewInject(R.id.tv_bid_history_date)
        TextView mIvBidHistoryDate;
        @ViewInject(R.id.tv_bid_history_divider)
        View mIvBidHistoryDivider;
    }
}
