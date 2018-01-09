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
import com.ql.jcjr.entity.WithdrawalsRecordEntity;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class WithdrawalsRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<WithdrawalsRecordEntity.ResultBean> mList;

    public WithdrawalsRecordAdapter(Context context, List<WithdrawalsRecordEntity.ResultBean> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_trans_record, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        WithdrawalsRecordEntity.ResultBean bean = mList.get(i);

        viewHolder.mTvTitle.setText(bean.getStatus() + "("  + bean.getBankinfo() + ")");
        viewHolder.mTvDate.setText(bean.getDatetime());
        viewHolder.mTvAmt.setText(bean.getMoney());

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.iv_type)
        private ImageView mIvType;
        @ViewInject(R.id.tv_title)
        private TextView mTvTitle;
        @ViewInject(R.id.tv_date)
        private TextView mTvDate;
        @ViewInject(R.id.tv_amt)
        private TextView mTvAmt;
    }
}
