package com.ql.jcjr.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.ql.jcjr.R;

/**
 * Created by liuwenjing at 2015/08/24.
 * <p/>
 * 只要将弹出框的View设置成width = MATCH_PARENT height ＝ WRAP_CONTENT即可，
 * 若要是弹出框的View有高度，则在传入UI之前就将高度设置好即可。
 */
public class ActionSheet extends Dialog {
    private Context context;
    private boolean mCloseOnTouchOutside = true;
    private boolean mCancelable = true;

    public static final int GRAVITY_TOP = Gravity.TOP;//显示在窗口的顶部
    public static final int GRAVITY_BOTTOM = Gravity.BOTTOM;//显示在窗口的底部
    public static final int GRAVITY_CENTER = Gravity.CENTER;//显示在窗口的中间
    public static final int GRAVITY_RIGHT = Gravity.LEFT;//显示在窗口的顶部
    private int gravity = GRAVITY_BOTTOM;//默认显示底部
    private WindowManager.LayoutParams windowParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public ActionSheet(Context context) {
        super(context);
        this.context = context;
        initWindowLayoutParam();
    }

    /**
     *
     * @param context
     * @param gravity 窗口显示的位置(GRAVITY_TOP, GRAVITY_BOTTOM)
     */
    public ActionSheet(Context context, int gravity) {
        super(context);
        this.context = context;
        this.gravity = gravity;
        initWindowLayoutParam();
    }

    private void initWindowLayoutParam() {
        Window window = getWindow();
        windowParam = window.getAttributes();
//        windowParam.softInputMode= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
        // 设置显示动画
        int resId = 0;
        if (this.gravity == ActionSheet.GRAVITY_TOP) {
            resId = R.style.ActionSheetStyle_Top;
        } else if (this.gravity == ActionSheet.GRAVITY_BOTTOM) {
            resId = R.style.ActionSheetStyle_Bottom;
        }
        else if (this.gravity == ActionSheet.GRAVITY_RIGHT) {
            resId = R.style.ActionSheetStyle_RIGHT;
        }

        if (this.gravity != ActionSheet.GRAVITY_CENTER) {//除了中间显示不需要窗口动画
            window.setWindowAnimations(resId);
        }

        //设置没有标题
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void show() {
        super.show();
        if (this.gravity == ActionSheet.GRAVITY_TOP) {
            windowParam.gravity = Gravity.TOP;
        } else if (this.gravity == ActionSheet.GRAVITY_BOTTOM) {
            windowParam.gravity = Gravity.BOTTOM;
        }else if (this.gravity == ActionSheet.GRAVITY_RIGHT) {
            windowParam.gravity = Gravity.RIGHT;
        } else if (this.gravity == ActionSheet.GRAVITY_CENTER) {
            windowParam.gravity = Gravity.CENTER;
        }

        // 以下这两句是为了保证按钮可以水平满屏
        windowParam.width = ViewGroup.LayoutParams.MATCH_PARENT;
        windowParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        onWindowAttributesChanged(windowParam);
    }

    /**
     * 参数为0时，使用默认
     * @param width
     * @param height
     */
    public void show(int width, int height) {
        show();

        windowParam.width = width > 0 ? width : ViewGroup.LayoutParams.MATCH_PARENT;
        windowParam.height = height > 0 ? height : ViewGroup.LayoutParams.WRAP_CONTENT;
        onWindowAttributesChanged(windowParam);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        this.mCloseOnTouchOutside = cancel;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.mCancelable = flag;
    }



    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation tranAnimation = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        tranAnimation.setDuration(500);
        tranAnimation.setFillAfter(true);
        return tranAnimation;
    }


    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation tranAnimation = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        tranAnimation.setDuration(500);
        tranAnimation.setFillAfter(true);
        return tranAnimation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCancelable && mCloseOnTouchOutside && isOutOfBounds(event)) {
            this.dismiss();
            return false;// 向上传递
        }
        return false;// 向上传递
    }

    /**
     * 判断是不是menu之外的点击区域
     *
     * @param event 点击的event事件
     */
    private boolean isOutOfBounds(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

}
