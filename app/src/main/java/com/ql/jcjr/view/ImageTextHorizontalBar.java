package com.ql.jcjr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.utils.DisplayUnitUtils;

/**
 * Created by liuchao on 2015/4/16.
 */
public class ImageTextHorizontalBar extends LinearLayout {
    private Context mContext;
    private LayoutInflater inflater;
    private View viewContainer;
    @ViewInject(R.id.iv_left_icon)
    private ImageView ivLeftIcon;
    @ViewInject(R.id.iv_action_go)
    private ImageView ivRightGoIcon;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitle;
    @ViewInject(R.id.tv_description)
    private TextView tvDescription;
    @ViewInject(R.id.view_bottom_line)
    private View viewBottomLine;
    @ViewInject(R.id.tv_tip)
    private TextView tvTip;
    @ViewInject(R.id.view_top_line)
    private View viewTopLine;
    @ViewInject(R.id.view_bottom_full_line)
    private View viewBottomFullLine;
    @ViewInject(R.id.ll_root_layout)
    private RelativeLayout rootLayout;
    @ViewInject(R.id.rl_content_layout)
    private RelativeLayout contentLayout;
    @ViewInject(R.id.tv_additional_info)
    private TextView additional;
    @ViewInject(R.id.civ_photo)
    public CircleImageView circleImageView;
    @ViewInject(R.id.iv_normal_img)
    private ImageView normalImageView;

    public ImageTextHorizontalBar(Context context) {
        super(context);
        // 添加此方法，在预览布局文件的时候不会出现如下错误
        // Tip: Use View.isInEditMode() in your custom views to skip code or show sample data when shown in the IDE
        if (isInEditMode()) {
            return;
        }
        this.initLayout(context, null);
    }

    public ImageTextHorizontalBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        this.initLayout(context, attrs);
    }

    // 初始化控件
    private void initLayout(Context context, AttributeSet attrs) {
        this.mContext = context;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewContainer = inflater.inflate(
                R.layout.image_text_horizontal_bar, null);

        ViewUtils.inject(this, viewContainer);
        addView(viewContainer);
        try {
            this.getAttrsValue(attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 获取控件的属性值
    private void getAttrsValue(AttributeSet attrs) {

        if (attrs == null) {
            return;
        }
        //设置Id，用来设置控件的点击事件
        TypedArray typed = mContext.obtainStyledAttributes(attrs, new int[]{android.R.attr.id});
        if (typed == null) {
            throw new IllegalArgumentException("Miss click because of not set id");
        }
        int id = typed.getResourceId(0, -1);
        if (id <= 0) {
            throw new IllegalArgumentException("Set id is not right value,the value <= 0");
        }
        rootLayout.setId(id);

        TypedArray typeArray = this.mContext.obtainStyledAttributes(attrs,
                R.styleable.ImageTextHorizontalBar);
        if (typeArray == null) {
            return;
        }

        for (int i = 0; i < typeArray.getIndexCount(); i++) {
            int attr = typeArray.getIndex(i);
            switch (attr) {
                case R.styleable.ImageTextHorizontalBar_paddingLeft:
                    this.setPaddingLeft(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_paddingLeft, 0));
                    break;
                case R.styleable.ImageTextHorizontalBar_rightGoIconPaddingRight:
                    this.setIvRightGoIconPaddingRight(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_rightGoIconPaddingRight, 0));
                    break;
                case R.styleable.ImageTextHorizontalBar_leftIconVisibility:
                    this.setLeftIconVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_leftIconVisibility,
                            View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_bottomLineVisibility:
                    this.setBottomLineVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_bottomLineVisibility,
                            View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_topLineVisibility:
                    this.setTopLineVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_topLineVisibility,
                            View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_bottomFullLineVisibility:
                    this.setBottomFullLineVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_bottomFullLineVisibility,
                            View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionFontColor:
                    this.setDescriptionFontColor(typeArray
                            .getColor(
                                    R.styleable.ImageTextHorizontalBar_descriptionFontColor,
                                    Color.GRAY));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionFontSize:
                    this.setDescriptionFontSize(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_descriptionFontSize,
                            DisplayUnitUtils.sp2px(12, mContext)));

                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionText:
                    this.setDescriptionText(typeArray
                            .getString(R.styleable.ImageTextHorizontalBar_descriptionText));
                    break;
                case R.styleable.ImageTextHorizontalBar_leftIcon:
                    this.setLeftIcon(typeArray.getResourceId(
                            R.styleable.ImageTextHorizontalBar_leftIcon, -1));
                    break;
                case R.styleable.ImageTextHorizontalBar_leftIcon_size:
                    this.setLeftIconSize(typeArray.getDimension(R.styleable.ImageTextHorizontalBar_leftIcon_size, -1));
                    break;
                case R.styleable.ImageTextHorizontalBar_rightGoIcon:
                    this.setRightGoIcon(typeArray.getResourceId(
                            R.styleable.ImageTextHorizontalBar_rightGoIcon, -1));
                    break;
                case R.styleable.ImageTextHorizontalBar_titleText:
                    this.setTitleText(typeArray
                            .getString(R.styleable.ImageTextHorizontalBar_titleText));
                    break;
                case R.styleable.ImageTextHorizontalBar_additionalText:
                    this.setAdditionalText(typeArray
                            .getString(R.styleable.ImageTextHorizontalBar_additionalText));
                    break;
                case R.styleable.ImageTextHorizontalBar_titleFontColor:
                    this.setTitleFontColor(typeArray.getColor(
                            R.styleable.ImageTextHorizontalBar_titleFontColor,
                            Color.BLACK));
                    break;
                case R.styleable.ImageTextHorizontalBar_titleFontSize:
                    this.setTitleFontSize(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_titleFontSize, 17));
                    break;
//                case R.styleable.ImageTextHorizontalBar_tipsBackground:
//                    this.setTipsBackground(typeArray.getResourceId(
//                            R.styleable.ImageTextHorizontalBar_tipsBackground,
//                            R.drawable.dot_24b279_15px));
//                    break;
                case R.styleable.ImageTextHorizontalBar_tipsText:
                    this.setTipsText(typeArray
                            .getString(R.styleable.ImageTextHorizontalBar_tipsText));
                    break;
                case R.styleable.ImageTextHorizontalBar_tipsFontColor:
                    this.setTipsFontColor(typeArray.getColor(
                            R.styleable.ImageTextHorizontalBar_tipsFontColor,
                            Color.WHITE));
                    break;
                case R.styleable.ImageTextHorizontalBar_tipsFontSize:
                    this.setTipsFontSize(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_tipsFontSize,
                            DisplayUnitUtils.sp2px(5, mContext)));
                    break;
                case R.styleable.ImageTextHorizontalBar_tipsVisibility:
                    this.setTipsVisility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_tipsVisibility, View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionVisibility:
                    this.setDescriptionVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_descriptionVisibility, View.INVISIBLE));
                    break;
//                case R.styleable.ImageTextHorizontalBar_descriptionBackground:
//                    this.setDescriptionBackground(typeArray.getResourceId(
//                            R.styleable.ImageTextHorizontalBar_descriptionBackground, R.drawable.dot_24b279_15px));
//                    break;
                case R.styleable.ImageTextHorizontalBar_rightIconVisibility:
                    this.setRightIconVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_rightIconVisibility, View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_additionalTextVisibility:
                    this.setAdditionalTextVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_additionalTextVisibility, View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_circleImageViewVisibility:
                    this.setCircleImageViewVisibility(typeArray.getInt(
                            R.styleable.ImageTextHorizontalBar_circleImageViewVisibility, View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_srcNormalImage:
                    this.setNormalImage(typeArray.getResourceId(
                            R.styleable.ImageTextHorizontalBar_srcNormalImage, -1));
                    break;
                case R.styleable.ImageTextHorizontalBar_normalImageViewVisibility:
                    this.setNormalImageViewVisibility(typeArray.getInt(R.styleable.ImageTextHorizontalBar_normalImageViewVisibility, View.GONE));
                    break;
                case R.styleable.ImageTextHorizontalBar_tipsStyle:
                    setTipsStyle(typeArray.getResourceId(
                            R.styleable.ImageTextHorizontalBar_tipsStyle, 0));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionStyle:
                    setDescriptionStyle(typeArray.getResourceId(
                            R.styleable.ImageTextHorizontalBar_descriptionStyle, 0));
                    break;
                case R.styleable.ImageTextHorizontalBar_titleStyle:
                    setTitleStyle(typeArray.getResourceId(
                            R.styleable.ImageTextHorizontalBar_titleStyle, R.style.text_f03_34_c03_2c2c2c));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionPaddingRight:
                    setRightDescriptionPaddingRight(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_descriptionPaddingRight, 0));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionMarginRight:
                    setRightDescriptionMarginRight(typeArray.getDimension(
                            R.styleable.ImageTextHorizontalBar_descriptionMarginRight, 0));
                    break;
                case R.styleable.ImageTextHorizontalBar_descriptionMaxLength:
                    setMaxLength(typeArray.getInt(R.styleable.ImageTextHorizontalBar_descriptionMaxLength, -1));
                    break;
                default:
                    break;
            }
        }
        typeArray.recycle();
    }


    /**
     * 设置右边tip的背景图片
     *
     * @param drawableId
     */
    public void setDescriptionBackground(int drawableId) {
        this.tvDescription.setBackgroundResource(drawableId);
    }

    /**
     * 设置右边tip的可见性
     *
     * @param visibility
     */
    public void setDescriptionVisibility(int visibility) {
        this.tvDescription.setVisibility(visibility);
    }

    /**
     * 设置左边字体样式
     *
     * @param style
     */
    public void setTitleStyle(int style) {
        this.tvTitle.setTextAppearance(mContext, style);
    }

    /**
     * 设置左边字体的大小
     *
     * @param size
     */
    public void setTitleFontSize(float size) {
        this.tvTitle.setTextSize(size);
    }

    /**
     * 设置左边字体颜色
     *
     * @param color
     */
    public void setTitleFontColor(int color) {
        this.tvTitle.setTextColor(color);
    }

    /**
     * 设置左边的资源图标
     *
     * @param drawableId
     */
    public void setLeftIcon(int drawableId) {
        //this.leftIconId = drawableId;
        if (drawableId < 0) {
            this.ivLeftIcon.setVisibility(View.GONE);
            this.tvTip.setVisibility(View.GONE);
        } else {
            this.ivLeftIcon.setVisibility(View.VISIBLE);
            this.ivLeftIcon.setImageResource(drawableId);
        }
    }

    /**
     * 设置左边的Icon的显示大小(宽、高)
     *
     * @param size
     */
    public void setLeftIconSize(float size) {
        if (size <= 0) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.ivLeftIcon.getLayoutParams();
        lp.width = (int) size;
        lp.height = (int) size;
        this.ivLeftIcon.setLayoutParams(lp);
    }

    /**
     * 设置左边Icon的可见性
     *
     * @param visibility
     */
    public void setLeftIconVisibility(int visibility) {
        this.ivLeftIcon.setVisibility(visibility);
        this.tvTip.setVisibility(visibility);
    }

    /**
     * 设置右边跳转按钮的资源图片
     *
     * @param drawableId
     */
    public void setRightGoIcon(int drawableId) {
        if (drawableId < 0) {
            this.ivRightGoIcon.setVisibility(View.INVISIBLE);
        } else {
            this.ivRightGoIcon.setVisibility(View.VISIBLE);
            this.ivRightGoIcon.setImageResource(drawableId);
        }
    }

    /**
     * 设置右边Icon的可见性
     *
     * @param visibility
     */
    public void setRightIconVisibility(int visibility) {
        this.ivRightGoIcon.setVisibility(visibility);
    }

    /**
     * 设置附加说明的可见性
     *
     * @param visibility
     */
    public void setAdditionalTextVisibility(int visibility) {
        this.additional.setVisibility(visibility);
    }

    /**
     * 设置CircleImageView的可见性
     *
     * @param visibility
     */
    public void setCircleImageViewVisibility(int visibility) {
        this.circleImageView.setVisibility(visibility);
        this.normalImageView.setVisibility(View.GONE);
    }

    /**
     * 返回CircleImageView
     *
     * @return
     */
    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    /**
     * 设置左边的资源图标
     *
     * @param drawableId
     */
    public void setNormalImage(int drawableId) {
        if (drawableId < 0) {
            this.normalImageView.setVisibility(View.GONE);
        } else {
            this.circleImageView.setVisibility(View.GONE);
            this.normalImageView.setVisibility(View.VISIBLE);
            this.normalImageView.setImageResource(drawableId);
        }
    }

    /**
     * 设置普通ImageView的可见性
     *
     * @param visibility
     */
    public void setNormalImageViewVisibility(int visibility) {
        this.normalImageView.setVisibility(visibility);
        this.circleImageView.setVisibility(View.GONE);
    }

    /**
     * 设置左边的文字，通过资源Id设置
     *
     * @param titleId
     */
    public void setTitleText(int titleId) {
        this.setTitleText(this.mContext.getString(titleId));
    }

    /**
     * 设置左边的文字
     *
     * @param title
     */
    public void setTitleText(String title) {
        this.tvTitle.setText(title);
    }

    /**
     * 设置附加说明的文字，通过资源Id设置
     *
     * @param additionalId
     */
    public void setAdditionalText(int additionalId) {
        this.setAdditionalText(this.mContext.getString(additionalId));
    }

    /**
     * 设置附加说明的文字
     *
     * @param additional
     */
    public void setAdditionalText(String additional) {
        this.additional.setText(additional);
    }

    /**
     * 设置小红点内部文字样式，通过资源Id设置
     *
     * @param style
     */
    public void setTipsStyle(int style) {
        this.tvTip.setTextAppearance(mContext, style);
    }

    /**
     * 设置右边的备注文字，通过资源Id设置
     *
     * @param titleId
     */
    public void setDescriptionText(int titleId) {
        this.setDescriptionText(this.mContext.getString(titleId));
    }

    /**
     * 设置右边的备注文字样式，通过资源Id设置
     *
     * @param style
     */
    public void setDescriptionStyle(int style) {
        this.tvDescription.setTextAppearance(mContext, style);
    }

    /**
     * 设置右边的额备注文字
     *
     * @param title
     */
    public void setDescriptionText(String title) {
        // this.descriptionText = title;
        this.tvDescription.setVisibility(View.VISIBLE);
//        if (!title.isEmpty()) {
//            this.tvDescription.setVisibility(View.VISIBLE);
        this.tvDescription.setText(title);
//        } else {
//            this.tvDescription.setVisibility(View.INVISIBLE);
//        }
    }

    public String getDescriptionText() {
        return this.tvDescription.getText().toString();
    }

    /**
     * 设置小红点是否可见
     *
     * @param visible
     */
    public void setTipsVisility(int visible) {
        //this.isTipVisible = isVisible;
        this.tvTip.setVisibility(visible);
    }

    /**
     * 设置右边备注文字的字体颜色
     *
     * @param color
     */
    public void setDescriptionFontColor(int color) {
        this.tvDescription.setTextColor(color);
    }

    /**
     * 设置右边的备注文字字体大小
     *
     * @param size
     */
    public void setDescriptionFontSize(float size) {
        this.tvDescription.setTextSize(size);
    }

    /**
     * 设置小红点内部文字
     *
     * @param text
     */
    public void setTipsText(String text) {
        this.tvTip.setText(text);
    }

    /**
     * 设置小红点内部字体颜色
     *
     * @param color
     */
    public void setTipsFontColor(int color) {
        this.tvTip.setTextColor(color);
    }

    /**
     * 设置小红点内部文字的字体大小
     *
     * @param size
     */
    public void setTipsFontSize(float size) {
        this.tvTip.setTextSize(size);
    }

    /**
     * 设置小红点的显示背景图
     *
     * @param drawableId
     */
    public void setTipsBackground(int drawableId) {
        if (this.ivLeftIcon.getVisibility() != View.GONE) {
            this.tvTip.setBackgroundResource(drawableId);
        }
    }

    /**
     * 设置底部横线(宽度不是整个屏幕宽度)的可见性
     *
     * @param visible
     */
    public void setBottomLineVisibility(int visible) {
        //TODO  this.isBottomLineVisible = isVisible;
        this.viewBottomLine.setVisibility(visible);
    }

//    public void setBackGround(int drawableId) {
//        this.viewContainer.setBackgroundResource(drawableId);
//    }

    /**
     * 设置顶部横线的可见性
     *
     * @param visible
     */
    public void setTopLineVisibility(int visible) {
        this.viewTopLine.setVisibility(visible);
    }

    /**
     * 设置底部横线(宽度和屏幕宽度一样)的可见性
     *
     * @param visible
     */
    public void setBottomFullLineVisibility(int visible) {
        this.viewBottomFullLine.setVisibility(visible);
    }

    /**
     * 到左边框的距离
     *
     * @param space
     */
    public void setPaddingLeft(float space) {
        this.contentLayout.setPadding((int) space, 0, 0, 0);
    }

    /**
     * 设置右边Icon距离右边的距离
     *
     * @param paddingRight
     */
    public void setIvRightGoIconPaddingRight(float paddingRight) {
        this.ivRightGoIcon.setPadding(0, 0, (int) paddingRight, 0);
    }

    /**
     * 设置右边描述文字的内容距离右边的距离
     *
     * @param paddingRight
     */
    public void setRightDescriptionPaddingRight(float paddingRight) {
        if (this.tvDescription == null) {
            return;
        }
        this.tvDescription.setPadding(0, 0, (int) paddingRight, 0);
    }

    /**
     * 设置右边描述文字距离右边的距离
     *
     * @param marginRight
     */
    public void setRightDescriptionMarginRight(float marginRight) {
        if (this.tvDescription == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.tvDescription.getLayoutParams();
        lp.rightMargin = (int) marginRight;
        this.tvDescription.setLayoutParams(lp);
    }

    /**
     * 设置文本框最大字符数
     *
     * @param maxLength 最大字符数
     */
    public void setMaxLength(int maxLength) {
        if (maxLength <= 0) {
            return;
        }
        tvDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        tvDescription.setSingleLine(true);
        tvDescription.setEllipsize(TextUtils.TruncateAt.END);
    }

    /**
     * 添加点击事件
     *
     * @param listener
     */
    public void setOnClickListener(OnClickListener listener) {
        this.viewContainer.setOnClickListener(listener);
    }

}
