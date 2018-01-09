package com.ql.jcjr.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ql.jcjr.R;
import com.ql.jcjr.entity.BannerEntity;
import com.ql.jcjr.utils.GlideUtil;

import java.util.ArrayList;


public class AdvertInfoViewPaperAdapterLoop extends PagerAdapter {
    public View[] mListViews;
    private Context mContext;
    private ArrayList<BannerEntity.ResultBean> advertInfoList;
    private int count;
    LayoutInflater mLayoutInflater;

    public AdvertInfoViewPaperAdapterLoop(Context context, ArrayList<BannerEntity.ResultBean> advertInfoList, View[] mListViews, int count) {
        this.mListViews = mListViews;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.advertInfoList = advertInfoList;
        this.count = count;
    }

    public void setCount(int count){
        this.count = count;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        arg0.removeView(mListViews[arg1]);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
//        View view = mListViews[arg1];
        ImageView view = (ImageView)mLayoutInflater.inflate(R.layout.item_for_advert, null);

        arg0.addView(view, 0);
//        ImageView imageView = (ImageView) view;

        final BannerEntity.ResultBean advertInfo = advertInfoList.get(arg1);

//        imageView.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                UrlUtil.showHtmlPage(mContext,"详情", advertInfo.getUrl());
//            }
//        });

        GlideUtil.displayPic(mContext, advertInfo.getPic(), -1, view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}