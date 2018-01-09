package com.ql.jcjr.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liuchao on 2016/1/20.
 * 禁止滑动的ViewPager
 */
public class NoScrollViewPager extends ViewPager {
    private boolean isScrollable = true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否可滑动
     * @param isScrollable
     */
    public void setScanScroll(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }
}
