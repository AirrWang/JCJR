package com.ql.jcjr.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.ql.jcjr.R;
import com.ql.jcjr.utils.DisplayUnitUtils;

/**
 * 自定义没有滚动的gridview
 * <p/>
 * add by liuchao
 * <p/>
 * //增加自动设置为正方形的item的gridview的属性
 * <p/>
 * 设置gridview的item为正方形时，只要在定义该gridview的adapter布局文件时，必须用将SquareLayout作为根view；
 * 并且将R.styleable.NoScrollGridView_square_layout设置为true，默认的为false
 *
 * @date 2014-12-11
 */

public class NoScrollGridView extends GridView {

    private int numColumns;
    private int horizontalSpace = 0;
    private Context mContext;
    private int leftMargin = 0;
    private int rightMargin = 0;
    private boolean isSquareLayout = false;

    public NoScrollGridView(Context context) {
        super(context);
        this.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置按下的背景为透明色
        this.mContext = context;
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //隐藏android默认的按压效果
        this.setSelector(new ColorDrawable(Color.TRANSPARENT));
        this.mContext = context;
        if (attrs == null) {
            return;
        }
        //是否item为正方形的布局
        TypedArray typed = this.mContext.obtainStyledAttributes(attrs, R.styleable.NoScrollGridView);
        if (typed != null) {
            if (typed.hasValue(R.styleable.NoScrollGridView_square_layout)) {
                isSquareLayout = typed.getBoolean(R.styleable.NoScrollGridView_square_layout, false);
            }
            typed.recycle();
        }
        //item 的个数和间隔
        TypedArray typeAttrs = this.mContext.obtainStyledAttributes(attrs, new int[]{android.R.attr.horizontalSpacing,
                                                                                     android.R.attr.numColumns});
        if (typeAttrs != null) {
            this.horizontalSpace = typeAttrs.getDimensionPixelOffset(0, 0);
            this.numColumns = typeAttrs.getInt(1, 1);
            typeAttrs.recycle();
        }
        //是否设置了左右margin
        TypedArray typeAttrsLayout = this.mContext.obtainStyledAttributes(attrs, new int[]{
                android.R.attr.layout_marginLeft, android.R.attr.layout_marginRight});
        if (typeAttrsLayout != null) {
            this.leftMargin = typeAttrsLayout.getDimensionPixelOffset(0, 0);
            this.rightMargin = typeAttrsLayout.getDimensionPixelOffset(1, 0);
            typeAttrsLayout.recycle();
        }
        setNumColumns(numColumns);
        setHorizontalSpacing(horizontalSpace);
        setColumnWidth();
    }

    /**
     * 设置是否为正方形的item，前提是每个item的布局文件必须，必须用将SquareLayout作为根view
     *
     * @param isSquare 默认为false
     */

    public void setSquareLayout(boolean isSquare) {
        this.isSquareLayout = isSquare;
    }

    /**
     * 每行的item的个数 （同布局文件的android:numColumns属性），不用再layout文件进行设置
     *
     * @param numColumns
     */
    @Override
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        if (!isSquareLayout) {
            super.setNumColumns(numColumns);
            return;
        }
        super.setNumColumns(GridView.AUTO_FIT);
        setColumnWidth();
    }


    /**
     * 每个item之间的HorizontalSpacing（同布局文件的android:horizontalSpacing属性）
     *
     * @param padding
     */
    @Override
    public void setHorizontalSpacing(int padding) {
        this.horizontalSpace = padding;
        super.setHorizontalSpacing(horizontalSpace);
    }

    /**
     * 自动计算出每个item的width和height
     */
    private void setColumnWidth() {

        if (!isSquareLayout) {
            return;
        }
        int parentPaddingLeft = 0;
        int parentPaddingRight = 0;
        int columnWidth = 0;
        View parentView = null;
        int parentMarginLeft = 0;
        int parentMarginRight = 0;

        int screenWidth = DisplayUnitUtils.getDisplayWidth(mContext);
        parentView = ((View) getParent());
        if (parentView != null) {
            parentPaddingLeft = parentView.getPaddingLeft();
            parentPaddingRight = parentView.getPaddingRight();
            parentMarginLeft = parentView.getLeft();
            parentMarginRight = parentView.getRight();
        }

        // 计算公式如下：(屏幕宽度-gridview的item之间的间隔-gridview的paddingleft and right)/num
        columnWidth = (screenWidth - horizontalSpace * (numColumns + 1) - getPaddingLeft() - getPaddingRight()
                - leftMargin - rightMargin - parentPaddingLeft - parentPaddingRight - parentMarginLeft - parentMarginRight)
                / numColumns - 3;// -3 为了减少由于进行分辨率的转换引入的误差

        super.setColumnWidth(columnWidth);
    }
    // END:add by wenjing.liu for square gridview at 2014-7-22
}