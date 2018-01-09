package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.entity.ReceiptDetailsEntity;
import com.ql.jcjr.utils.StringUtils;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class ReceiptDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReceiptDetailsEntity.ResultBean> mList;

    public ReceiptDetailsAdapter(Context context, List<ReceiptDetailsEntity.ResultBean> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_receipt_details, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ReceiptDetailsEntity.ResultBean resultBean = mList.get(i);
        viewHolder.mTvNum.setText(resultBean.getNumber());
        viewHolder.mTvAmt.setText(StringUtils.formatMoney(resultBean.getRepay_account()));
        viewHolder.mTvDate.setText(resultBean.getRepay_time());

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_number)
        TextView mTvNum;
        @ViewInject(R.id.tv_amt)
        TextView mTvAmt;
        @ViewInject(R.id.tv_date)
        TextView mTvDate;
    }
}
