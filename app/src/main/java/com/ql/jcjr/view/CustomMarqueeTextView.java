package com.ql.jcjr.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ClassName: CustomMarqueeTextView
 * Description:
 * Author: Administrator
 * Date: Created on 202016/10/31.
 */
public class CustomMarqueeTextView extends TextView {


    public CustomMarqueeTextView(Context context) {
        super(context);
    }

    public CustomMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused) {
            super.onWindowFocusChanged(focused);
        }
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
