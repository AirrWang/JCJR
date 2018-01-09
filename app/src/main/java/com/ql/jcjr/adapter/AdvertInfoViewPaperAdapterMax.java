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


public class AdvertInfoViewPaperAdapterMax extends PagerAdapter {
    public View[] mListViews;
    private Context mContext;
    private ArrayList<BannerEntity.ResultBean> advertInfoList;
    private int count;

    public AdvertInfoViewPaperAdapterMax(Context context, ArrayList<BannerEntity.ResultBean> advertInfoList, View[] mListViews, int count) {
        this.mListViews = mListViews;
        mContext = context;
        this.advertInfoList = advertInfoList;
        this.count = count;
    }

    public void setCount(int count){
        this.count = count;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        arg0.removeView((View)arg2);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        final int index = arg1 % mListViews.length;

        ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.item_for_advert, null);
        arg0.addView(imageView);

        final BannerEntity.ResultBean advertInfo = advertInfoList.get(index);

//        imageView.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                UrlUtil.showHtmlPage(mContext,"详情", advertInfo.getUrl());
//            }
//        });

        GlideUtil.displayPic(mContext, advertInfo.getPic(), -1, imageView);
        return imageView;
    }

    private View findViewByPosition(ViewGroup container, int position){
        View view = mListViews[position];
        if(view.getParent()==null){
            container.addView(view);
        }
        return view;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}