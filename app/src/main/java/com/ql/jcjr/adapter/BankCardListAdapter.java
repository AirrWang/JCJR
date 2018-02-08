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
import com.ql.jcjr.entity.BankCardData;
import com.ql.jcjr.utils.GlideUtil;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class BankCardListAdapter extends BaseAdapter {

    private Context mContext;
    private List<BankCardData> mList;

    public BankCardListAdapter(Context context, List<BankCardData> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BankCardData data = mList.get(i);

        String bankNo = data.getBankno();
        bankNo = bankNo.substring(bankNo.length()-4);
        viewHolder.mTvBankName.setText(data.getBankname()+" ("+bankNo+")");
//        viewHolder.mTvBankBranch.setText(data.getBranch());
//        viewHolder.mTvCardNum.setText(data.getBankno());
        viewHolder.mTvBankInfo.setText("单笔限额"+data.getTotalMoney());
        GlideUtil.displayPic(mContext, data.getImgUrl(), -1, viewHolder.mCivIcon);

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_bank_name)
        TextView mTvBankName;
//        @ViewInject(R.id.tv_bank_branch)
//        TextView mTvBankBranch;
//        @ViewInject(R.id.tv_card_num)
//        TextView mTvCardNum;
        @ViewInject(R.id.civ_icon)
        ImageView mCivIcon;
        @ViewInject(R.id.tv_bank_info)
        TextView mTvBankInfo;
    }
}
