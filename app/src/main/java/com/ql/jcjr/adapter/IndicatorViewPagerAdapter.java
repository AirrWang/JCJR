package com.ql.jcjr.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by liuchao.
 * <p/>
 * Viewpager适配器
 */
public class IndicatorViewPagerAdapter extends PagerAdapter {
    private List<View> pagerViewList;

    public IndicatorViewPagerAdapter(List<View> pager) {
        this.pagerViewList = pager;
    }

    @Override
    public int getCount() {
        return pagerViewList.size();
    }

    @Override  //销毁position位置的界面
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView(pagerViewList.get(position));
    }

    @Override //初始化position的界面
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(pagerViewList.get(position));
        return pagerViewList.get(position);

    }

    @Override //判断是否有对象生成界面
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }
}
