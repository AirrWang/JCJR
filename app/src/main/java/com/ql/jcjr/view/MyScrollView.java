package com.ql.jcjr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;


public class MyScrollView extends ScrollView {

    public int watch=0;

    ViewGroup showLayout;
    ViewGroup hintLayout;

    int i=0;

    int[] location = new int[2];
    int[] location2 = new int[2];

    boolean isMoving;

    private int downX;
    private int downY;
    private int mTouchSlop;

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    private boolean isTop = false;//是不是滑动到了最低端 ；使用这个方法，解决了上拉加载的问题
    private OnScrollToBottomListener onScrollToBottom;

    public MyScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setShowAndHintLayout(ViewGroup showLayout, ViewGroup hintLayout){
        this.showLayout = showLayout;
        this.hintLayout = hintLayout;

    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY != 0 &&onScrollToBottom!=null&&clampedY&&isTop()){//&&!isMoving &&isTop()这里需要再划一下开始加载更多
            i++;
            onScrollToBottom.onScrollBottomListener(clampedY);
        }

    }

    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }



    public interface OnScrollToBottomListener{
        void onScrollBottomListener(boolean isBottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                setTop(false);
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                /****判断是向下滑动，才设置为true****/
                if(downY-moveY>0){
                    setTop(true);
                }else{
                    setTop(false);
                }
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }
        return super.onInterceptTouchEvent(e);
    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


        showLayout.getLocationOnScreen(location);
        hintLayout.getLocationOnScreen(location2);
        if(this.showLayout != null && this.hintLayout != null) {
            if (t >= this.showLayout.getHeight()) {
                this.hintLayout.setVisibility(View.VISIBLE);
            } else {
                this.hintLayout.setVisibility(View.GONE);
            }}

    }

}
