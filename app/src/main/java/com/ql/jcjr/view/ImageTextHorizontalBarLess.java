package com.ql.jcjr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;

/**
 * Created by liuchao on 2015/4/16.
 */
public class ImageTextHorizontalBarLess extends LinearLayout {

    private Context mContext;
    private LayoutInflater inflater;
    private View viewContainer;

    @ViewInject(R.id.ll_root_layout)
    private LinearLayout rootLayout;
    @ViewInject(R.id.iv_left_icon)
    private ImageView ivLeftIcon;
    @ViewInject(R.id.iv_action_go)
    private ImageView ivRightGoIcon;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitle;
    @ViewInject(R.id.tv_title_description)
    private TextView tvTitleDescription;
    @ViewInject(R.id.view_bottom_line)
    private View viewBottomLine;
    @ViewInject(R.id.tv_right_name)
    private TextView tvRightName;
    @ViewInject(R.id.tv_right_description)
    private TextView tvRightDescription;

    public ImageTextHorizontalBarLess(Context context) {
        super(context);
        // 添加此方法，在预览布局文件的时候不会出现如下错误
        // Tip: Use View.isInEditMode() in your custom views to skip code or show sample data when shown in the IDE
        if (isInEditMode()) {
            return;
        }
        this.initLayout(context, null);
    }

    public ImageTextHorizontalBarLess(Context context, AttributeSet attrs) {
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
                R.layout.image_text_horizontal_bar_less, null);

        ViewUtils.inject(this, viewContainer);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewContainer.setLayoutParams(lp);

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
                R.styleable.ImageTextHorizontalBarLess);
        if (typeArray == null) {
            return;
        }

        for (int i = 0; i < typeArray.getIndexCount(); i++) {
            int attr = typeArray.getIndex(i);
            switch (attr) {
                //下划线
                case R.styleable.ImageTextHorizontalBarLess_bottomLineVisibilityLess:
                    this.setBottomLineVisibility(typeArray.getInt(attr, View.GONE));
                    break;
                //left icon
                case R.styleable.ImageTextHorizontalBarLess_leftIconVisibilityLess:
                    this.setLeftIconVisibility(typeArray.getInt(attr, View.GONE));
                    break;
                //title 描述
                case R.styleable.ImageTextHorizontalBarLess_titleDescriptionVisibility:
                    this.setTitleDescriptionVisibility(typeArray.getInt(attr, View.GONE));
                    break;
                //right icon
                case R.styleable.ImageTextHorizontalBarLess_rightIconVisibilityLess:
                    this.setRightIconVisibility(typeArray.getInt(attr,View.GONE));
                    break;
                //right title
                case R.styleable.ImageTextHorizontalBarLess_rightTitleVisibility:
                    this.setRightTitleVisibility(typeArray.getInt(attr,View.GONE));
                    break;
                //right title Description
                case R.styleable.ImageTextHorizontalBarLess_rightTitleDescriptionVisibility:
                    this.setRightDescriptionVisibility(typeArray.getInt(attr,View.GONE));
                    break;

                //left icon
                case R.styleable.ImageTextHorizontalBarLess_leftIconLess:
                    this.setLeftIcon(typeArray.getResourceId(attr, -1));
                    break;
                //left icon size
                case R.styleable.ImageTextHorizontalBarLess_leftIconSize:
                    this.setLeftIconSize(typeArray.getDimension(attr, -1));
                    break;
                // title text
                case R.styleable.ImageTextHorizontalBarLess_titleTextLess:
                    this.setTitleText(typeArray.getString(attr));
                    break;
                // title Description text
                case R.styleable.ImageTextHorizontalBarLess_titleDescriptionText:
                    this.setTitleDescriptionText(typeArray.getString(attr));
                    break;
                //title Description color
                case R.styleable.ImageTextHorizontalBarLess_titleDescriptionColor:
                    this.setTitleDescriptionColor(typeArray.getColor(attr, Color.BLACK));
                    break;
                // right  title  color
                case R.styleable.ImageTextHorizontalBarLess_rightTitleColor:
                    this.setRightTitleColor(typeArray.getColor(attr,Color.BLACK));
                    break;
                // right  Description color
                case R.styleable.ImageTextHorizontalBarLess_rightDescriptionColor:
                    this.setRightDescriptionColor(typeArray.getColor(attr,Color.BLACK));
                    break;
                //right title text
                case R.styleable.ImageTextHorizontalBarLess_rightTitleText:
                    this.setRightTitleText(typeArray.getString(attr));
                    break;
                //right Description text
                case R.styleable.ImageTextHorizontalBarLess_rightDescriptionText:
                    this.setRightDescriptionText(typeArray.getString(attr));
                    break;

                default:
                    break;
            }
        }
        typeArray.recycle();
    }


    public void setTitleDescriptionColor(int color) {
        this.tvTitleDescription.setVisibility(View.VISIBLE);
        this.tvTitleDescription.setTextColor(color);
    }

    public void setLeftIcon(int drawableId) {
        //this.leftIconId = drawableId;
        if (drawableId < 0) {
            this.ivLeftIcon.setVisibility(View.GONE);
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.ivLeftIcon.getLayoutParams();
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
    }

    /**
     * 设置右边Icon的可见性
     *
     * @param visibility
     */
    public void setRightIconVisibility(int visibility) {
        this.ivRightGoIcon.setVisibility(visibility);
    }

    public void setRightTitleVisibility(int visibility) {
        this.tvRightName.setVisibility(visibility);
    }

    public void setRightDescriptionVisibility(int visibility) {
        this.tvRightDescription.setVisibility(visibility);
    }

    /**
     * 设置左边的文字
     *
     * @param title
     */
    public void setTitleText(String title) {
        this.tvTitle.setText(title);
    }

    public void setRightTitleText(String text) {
        this.tvRightName.setVisibility(View.VISIBLE);
        this.tvRightName.setText(text);
    }

    public void setTitleDescriptionText(String title) {
        this.tvTitleDescription.setVisibility(View.VISIBLE);
        this.tvTitleDescription.setText(title);
    }

    public void setRightDescriptionText(String text) {
        this.tvRightDescription.setVisibility(View.VISIBLE);
        this.tvRightDescription.setText(text);
    }

    public void setRightTitleColor(int color) {
        this.tvRightName.setVisibility(View.VISIBLE);
        this.tvRightName.setTextColor(color);
    }

    public void setRightDescriptionColor(int color) {
        this.tvRightDescription.setVisibility(View.VISIBLE);
        this.tvRightDescription.setTextColor(color);
    }

    /**
     * 设置底部横线(宽度不是整个屏幕宽度)的可见性
     *
     * @param visible
     */
    public void setBottomLineVisibility(int visible) {
        this.viewBottomLine.setVisibility(visible);
    }

    public void setTitleDescriptionVisibility(int visible) {
        this.tvTitleDescription.setVisibility(visible);
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
