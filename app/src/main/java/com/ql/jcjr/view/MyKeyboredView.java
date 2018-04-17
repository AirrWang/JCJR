package com.ql.jcjr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;

import java.util.List;

/**
 * Created by Airr on 2018/4/10.
 */

public class MyKeyboredView extends KeyboardView {


    public MyKeyboredView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyKeyboredView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyKeyboredView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == 66299) {
                Log.e("KEY", "Drawing key with code " + key.codes[0]);
                Drawable dr = (Drawable) JcbApplication.getInstance().getResources().getDrawable(R.drawable.jianpan_sure);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            } else {

            }
        }
    }
}
