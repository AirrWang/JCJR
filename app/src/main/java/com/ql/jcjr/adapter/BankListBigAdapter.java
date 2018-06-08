package com.ql.jcjr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.entity.BankListEntity;
import com.ql.jcjr.utils.GlideUtil;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class BankListBigAdapter extends BaseAdapter {

    private Context mContext;
    private List<BankListEntity.ResultBean> mList;
    private String sOneOrder;
    private String sOneDay;
    private String sOneMonth;

    public BankListBigAdapter(Context context, List<BankListEntity.ResultBean> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card_1, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }



        BankListEntity.ResultBean data = mList.get(i);


        if (data.getOneorder().equals("无")){
            sOneOrder=data.getOneorder();
        }else {
            float oneOrder=Float.valueOf(data.getOneorder())/10000;
            sOneOrder=subZeroAndDot(oneOrder+"");

        }
        if (data.getOneday().equals("无")){
            sOneDay=data.getOneday();
        }else {
            float oneDay=Float.valueOf(data.getOneday())/10000;
            sOneDay=subZeroAndDot(oneDay+"");
        }
        if (data.getOnemonth().equals("无")){
            sOneMonth=data.getOnemonth();
        }else {
            float oneMonth=Float.valueOf(data.getOnemonth())/10000;
            sOneMonth=subZeroAndDot(oneMonth+"");
        }

        viewHolder.tv_bank_info.setText(sOneOrder+"万/"+sOneDay+"万/"+sOneMonth+"万");

        viewHolder.mTvBankName.setText(data.getName());
        GlideUtil.displayPic(mContext, data.getImgUrl(), -1, viewHolder.mCivIcon);

        if (data.getOrder().equals("1")){  //推荐
            viewHolder.tv_bank_bq.setText("推荐");
            viewHolder.tv_bank_bq.setBackgroundResource(R.drawable.btn_rectangle_circl_btn);
        }else if (data.getOrder().equals("2")){
            viewHolder.tv_bank_bq.setText("额度低");
            viewHolder.tv_bank_bq.setBackgroundResource(R.drawable.bg_filled_corner_b74f4f);
        }else if (data.getOrder().equals("4")){
            viewHolder.tv_bank_bq.setText("维护中");
            viewHolder.tv_bank_bq.setBackgroundResource(R.drawable.bg_filled_corner_grey);
        }else {
            viewHolder.tv_bank_bq.setText("");
            viewHolder.tv_bank_bq.setBackgroundColor(Color.TRANSPARENT);
        }

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
        TextView tv_bank_info;

        @ViewInject(R.id.tv_bank_bq)
        TextView tv_bank_bq;
    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
