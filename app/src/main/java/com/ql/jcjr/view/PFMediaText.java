package com.ql.jcjr.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ql.jcjr.application.JcbApplication;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class PFMediaText extends android.support.v7.widget.AppCompatTextView {
    public PFMediaText(Context context) {
        super(context);
        init();
    }



    public PFMediaText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PFMediaText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(JcbApplication.getPingFangBoldTypeFace());
    }
}
