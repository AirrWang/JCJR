package com.ql.jcjr.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义没有滚动的ListView
 * add by wenjing.liu
 * @date 2015/07/22
 */

public class NoScrollListView extends ListView {


    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置不滚动
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}