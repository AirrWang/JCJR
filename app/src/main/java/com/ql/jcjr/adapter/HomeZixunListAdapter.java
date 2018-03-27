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
import com.ql.jcjr.entity.MessageActEntity;
import com.ql.jcjr.utils.GlideUtil;

import java.util.List;


/**
 * ClassName: CarStoreListAdapter
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/19.
 */

public class HomeZixunListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MessageActEntity.ResultBean> mList;

    public HomeZixunListAdapter(Context context, List<MessageActEntity.ResultBean> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_home_zixun, null);
            viewHolder = new ViewHolder();
            ViewUtils.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_zixun_title.setText(mList.get(i).getName());
        viewHolder.tv_zixun_time.setText(mList.get(i).getAddtime());
        GlideUtil.displayPic(mContext, mList.get(i).getLitpic(), -1, viewHolder.im_zixun_item);

        return view;
    }

    class ViewHolder {
        @ViewInject(R.id.im_zixun_item)
        ImageView im_zixun_item;
        @ViewInject(R.id.tv_zixun_title)
        TextView tv_zixun_title;
        @ViewInject(R.id.tv_zixun_time)
        TextView tv_zixun_time;
    }
}
