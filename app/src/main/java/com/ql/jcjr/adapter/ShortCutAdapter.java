package com.ql.jcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ql.jcjr.R;

import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class ShortCutAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mImgList;
    private List<String> mTitleList;

    //标识是首页的GridView（mIndex = 0） 还是 我的 页面的 GridView（mIndex = 1）
    private int mIndex;

    public ShortCutAdapter(Context context, List<Integer> imgList, List<String> titleList, int index) {
        mImgList = imgList;
        mTitleList = titleList;
        mContext = context;
        mIndex = index;
    }

    @Override
    public int getCount() {
        return mImgList == null ? 0 : mImgList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            if(mIndex == 0) {
                convertView =
                        LayoutInflater.from(mContext).inflate(R.layout.shortcut_item, null, false);
            }else {
                convertView =
                        LayoutInflater.from(mContext).inflate(R.layout.shortcut_item_me_fragemnt, null, false);
            }

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_item);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(mImgList.get(position));
        viewHolder.titleTv.setText(mTitleList.get(position));

        return convertView;
    }

    private final class ViewHolder {
        public ImageView imageView;
        public TextView titleTv;
    }
}
