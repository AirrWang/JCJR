package com.ql.jcjr.view;

/**
 * ClassName: PwdEditText
 * Description:
 * Author: Administrator
 * Date: Created on 202017/6/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.ql.jcjr.R;

/**
 *
 * 自定义密码输入框
 *
 * @author zhangke
 *
 */
public class PwdEditText extends android.support.v7.widget.AppCompatEditText {

    /**
     * 间隔
     */
    private final int PWD_SPACING = 5;
    /**
     * 密码大小
     */
    private final int PWD_SIZE = 5;
    /**
     * 密码长度
     */
    private final int PWD_LENGTH = 6;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 密码框
     */
    private Rect mRect;

    /**
     * 密码画笔
     */
    private Paint mPwdPaint;

    /**
     * 密码框画笔
     */
    private Paint mRectPaint;
    /**
     * 输入的密码长度
     */
    private int mInputLength;

    /**
     * 输入结束监听
     */
    private OnInputFinishListener mOnInputFinishListener;

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化密码画笔
        mPwdPaint = new Paint();
        mPwdPaint.setColor(getResources().getColor(R.color.indicator_color_normal));
        mPwdPaint.setStyle(Paint.Style.FILL);
        mPwdPaint.setAntiAlias(true);
        // 初始化密码框
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setColor(getResources().getColor(R.color.c_et_hint));
        mRectPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        // 外边框
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        mRectPaint.setColor(getResources().getColor(R.color.c_et_hint));
        canvas.drawRoundRect(rect, 8, 8, mRectPaint);

        // 内容区
        RectF rectIn = new RectF(rect.left + 1, rect.top + 1,
                rect.right - 1, rect.bottom - 1);
        mRectPaint.setColor(getResources().getColor(R.color.white));
        mRectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectIn, 8, 8, mRectPaint);

        // 分割线
        mRectPaint.setStrokeWidth(1);
        mRectPaint.setColor(getResources().getColor(R.color.c_et_hint));
        for (int i = 1; i < PWD_LENGTH; i++) {
            float x = mWidth * i / PWD_LENGTH;
            canvas.drawLine(x, 0, x, mHeight, mRectPaint);
        }

        // 密码
        float cx, cy = mHeight/ 2;
        float half = mWidth / PWD_LENGTH / 2;
        for(int i = 0; i < mInputLength; i++) {
            cx = mWidth * i / PWD_LENGTH + half;
                canvas.drawCircle(cx, cy, 6, mPwdPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
            int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mInputLength = text.toString().length();
        invalidate();
        if (mInputLength == PWD_LENGTH && mOnInputFinishListener != null) {
            mOnInputFinishListener.onInputFinish(text.toString());
        }
    }

    public interface OnInputFinishListener {
        /**
         * 密码输入结束监听
         *
         * @param password
         */
        void onInputFinish(String password);
    }

    /**
     * 设置输入完成监听
     *
     * @param onInputFinishListener
     */
    public void setOnInputFinishListener(OnInputFinishListener onInputFinishListener) {
        this.mOnInputFinishListener = onInputFinishListener;
    }

}
