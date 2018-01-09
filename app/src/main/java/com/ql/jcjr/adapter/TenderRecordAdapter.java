package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.entity.BidDetailEntity;
import com.ql.jcjr.utils.StringUtils;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class TenderRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<BidDetailEntity.ResultBean.TendersBean> mList;

    public TenderRecordAdapter(Context context, List<BidDetailEntity.ResultBean.TendersBean> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_tender_record, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BidDetailEntity.ResultBean.TendersBean bean = mList.get(i);

//        if(i%2==1){
//            viewHolder.mLlRecord.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }

        viewHolder.mTvTel.setText(StringUtils.getHidePhoneNum(bean.getUsername()));
        String addTime = bean.getAddtime();
        addTime = addTime.substring(5);
        viewHolder.mTvTime.setText(addTime);
        viewHolder.mTvAmt.setText(StringUtils.formatMoney(bean.getAccount()));

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.ll_record)
        private RelativeLayout mLlRecord;
        @ViewInject(R.id.tv_tel)
        private TextView mTvTel;
        @ViewInject(R.id.tv_time)
        private TextView mTvTime;
        @ViewInject(R.id.tv_amt)
        private TextView mTvAmt;
    }
}
