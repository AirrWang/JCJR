package com.ql.jcjr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.utils.DisplayUnitUtils;
import com.ql.jcjr.utils.StringUtils;

/**
 * 标题栏，可以通过ActionBar_title_style属性来设置为是通常模式还是带搜索框
 */
public class ActionBar extends LinearLayout {


    /**
     * 同ActionBar_title_style属性
     * 用来设置为是通常模式还是带搜索框
     */
    public static final int NORMAL = 0;
    public static final int SEARCH = 1;

    /**
     * 同ActionBar_search_left_style属性
     * 用来设置为是搜索模式下左边显示的是第一级还是第二级目录；
     */
    public static final int SEARCH_LEFT_FIRST = 0;
    public static final int SEARCH_LEFT_SECOND = 1;
    private int currentStyle = NORMAL;
    private int currentSearchLeftStyle = SEARCH_LEFT_FIRST;
    @ViewInject(R.id.rl_actionbar)
    private RelativeLayout normalLayout;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.btn_right)
    private Button btnRight;
    @ViewInject(R.id.btn_left)
    private Button btnLeft;
    @ViewInject(R.id.tv_second_left)
    private TextView tvSecondLeft;
    @ViewInject(R.id.rl_search_layout)
    private RelativeLayout searchLayout;
    @ViewInject(R.id.et_search)
    private EditText etSearch;
    @ViewInject(R.id.iv_search_left)
    private ImageView imgSearchLeft;
    @ViewInject(R.id.btn_search_right)
    private Button btnSearchRight;
    @ViewInject(R.id.iv_right)
    private ImageView ivRight; //for H5 添加图片
    @ViewInject(R.id.iv_share)
    private ImageView ivShare; //for H5 添加图片
    @ViewInject(R.id.vw_divider)
    private View dividerView;
    @ViewInject(R.id.sp_top)
    private View sp_top;
//    @ViewInject(R.id.fl_right_cart)
//    private FrameLayout flCart;
//    @ViewInject(R.id.btn_right_cart)
//    private Button btnCart;
//    @ViewInject(R.id.tv_count)
//    private TextView tvCount;

    private LinearLayout barView;
    private Context mContext;
    private LayoutInflater inflater;
    private String titleInfo;
    private String secondLeftInfo;
    private String rightInfo;
    private String leftInfo;
    private String searchHint;
    private String searchRightText;
    private OnClickListener listener;
    private int sysDefHeight;//系统默认的标题栏高度
    private int searchHeight; //设置搜索框的高度
    private boolean searchRightImageAdjuest = false;

    public ActionBar(Context context) {
        super(context);
        initLayout(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs, defStyle);
    }

    private void initLayout(Context context) {
        this.initLayout(context, null);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        initLayout(context, attrs, 0);
    }

    private void initLayout(Context context, AttributeSet attrs, int defStyle) {
        this.mContext = context;
        if (isInEditMode()) {
            return;
        }

        sysDefHeight = DisplayUnitUtils.getActionBarHeight(mContext);
        searchHeight = 8 * sysDefHeight / 11;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        barView = (LinearLayout) inflater.inflate(R.layout.actionbar, null);
        ViewUtils.inject(this, barView);

        int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
        searchLayout.setPadding(padding, 0, padding, 0);

        RelativeLayout.LayoutParams btnRightParams = new RelativeLayout.LayoutParams(searchHeight, searchHeight);
        btnRightParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        btnRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        btnRightParams.leftMargin = padding;
        btnSearchRight.setLayoutParams(btnRightParams);
        //   btnSearchRight.setPadding(0, 0, padding, 0);
        // 根据xml配置的属性设置UI
        initAttributeFromXml(attrs);
        addView(barView);

//
    }

    private void initAttributeFromXml(AttributeSet attrs) {

        if (attrs == null) {
            return;
        }
        TypedArray typed = this.mContext.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.ActionBar_background)) {
//            setBackground(typed.getResourceId(R.styleable.ActionBar_background, -1));
            setBackgroundColor(typed.getColor(R.styleable.ActionBar_background, Color.BLACK));
        }
        if (typed.hasValue(R.styleable.ActionBar_title_style)) {
            setStyle(typed.getInt(R.styleable.ActionBar_title_style, 0));
        }

        //设置不同分辨率下的带搜索框样式

        //      if (typed.hasValue(R.styleable.ActionBar_search_right_image_adjust)) {
        //          searchRightImageAdjuest = typed.getBoolean(R.styleable.ActionBar_search_right_image_adjust, false);
        //     }
//        if (typed.hasValue(R.styleable.ActionBar_search_left_style)) {
//            setSearchLeftLayout(typed.getInt(R.styleable.ActionBar_search_left_style, 0));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_left_image)) {
//            setSearchLeftImage(typed.getResourceId(R.styleable.ActionBar_search_left_image, -1));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_right_image)) {
//            setSearchRightImage(typed.getResourceId(R.styleable.ActionBar_search_right_image, -1));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_right_text)) {
//            setSearchRightText(typed.getString(R.styleable.ActionBar_search_right_text));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_hint)) {
//            setSearchEditHint(typed.getString(R.styleable.ActionBar_search_hint));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_left_image_visibility)) {
//            setSearchLeftImageVisibility(typed.getInt(R.styleable.ActionBar_search_left_image_visibility, View.GONE));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_edittext_background)) {
//            setSearchEditTextBackground(typed.getResourceId(R.styleable.ActionBar_search_edittext_background, -1));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_hint_color)) {
//            setSearchEditHintColor(typed.getColor(R.styleable.ActionBar_search_hint_color, getResources().getColor(R.color.c05_999999)));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_left)) {
//            setSearchEditTextDrawableLeft(typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_left, R.drawable.action_bar_search_gray));
//        }
//        if (typed.hasValue(R.styleable.ActionBar_search_edittext_style)) {
//            setSearchEditTextStyle(typed.getResourceId(R.styleable.ActionBar_search_edittext_style, R.style.text_f05_28_c03_2c2c2c));
//        }

        //设置不同分辨率下的带标题样式
        if (typed.hasValue(R.styleable.ActionBar_title)) {
            setTitle(typed.getString(R.styleable.ActionBar_title));
        }
        if (typed.hasValue(R.styleable.ActionBar_titleTextColor)) {
            setTitleTextColor(typed.getColor(R.styleable.ActionBar_titleTextColor, Color.parseColor("#333333")));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_first_text)) {
            setLeftFirstText(typed.getString(R.styleable.ActionBar_left_first_text));
        }
        if (typed.hasValue(R.styleable.ActionBar_left_first_textColor)){
            setLeftFirstTextColor(typed.getColor(R.styleable.ActionBar_left_first_textColor, Color.parseColor("#333333")));
        }
        if (typed.hasValue(R.styleable.ActionBar_left_first_image)) {
            setLeftFirstImage(typed.getResourceId(R.styleable.ActionBar_left_first_image, -1));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_first_visibility)) {
            setLeftFirstVisible(typed.getInt(R.styleable.ActionBar_left_first_visibility, View.VISIBLE));
        }

        setLeftFirstImage(R.drawable.btn_action_bar_back);
        if (typed.hasValue(R.styleable.ActionBar_left_second_visibility)) {
            setLeftSecondVisible(typed.getInt(R.styleable.ActionBar_left_second_visibility, View.GONE));
        }
        if (typed.hasValue(R.styleable.ActionBar_left_second_text)) {
            setLeftSecondText(typed.getString(R.styleable.ActionBar_left_second_text));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_text)) {
            setRightText(typed.getString(R.styleable.ActionBar_right_text));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_text_textColor)) {
            setRightTextColor(typed.getColor(R.styleable.ActionBar_right_text_textColor, Color.parseColor("#333333")));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_image)) {
            setRightImage(typed.getResourceId(R.styleable.ActionBar_right_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_visibility)) {
            setRightVisible(typed.getInt(R.styleable.ActionBar_right_visibility, View.GONE));
        }

        if (typed.hasValue(R.styleable.ActionBar_bottom_divider_visibility)) {
            setBottomDividerVisible(typed.getInt(R.styleable.ActionBar_bottom_divider_visibility, View.VISIBLE));
        }

//        if (typed.hasValue(R.styleable.ActionBar_right_cart_visibility)) {
//            setRightCartVisible(typed.getInt(R.styleable.ActionBar_right_cart_visibility, View.VISIBLE));
//        }
//
//        if (typed.hasValue(R.styleable.ActionBar_right_cart_image)) {
//            setRightCartImage(typed.getResourceId(R.styleable.ActionBar_right_cart_image, -1));
//        }

        typed.recycle();
    }

    /**
     * 获取购物车的btn
     *
     * @return

    public Button getBtnCart() {
        return btnCart;
    }
     */

    /**
     * 设置标题栏的背景色，默认为#db4453
     *
     * @param colorId 颜色Id
     */
    public void setBackgroundColor(int colorId) {
//        if (colorId <= 0) {
//            return;
//        }
        barView.setBackgroundColor(colorId);
    }

    /**
     * 设置标题栏的背景，默认为#db4453
     *
     * @param drawableId 资源Id
     */
    public void setBackground(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        barView.setBackgroundResource(drawableId);
    }

    /**
     * 设置是通常模式还是搜索框模式
     *
     * @param style NORMAL、SEARCH
     */
    public void setStyle(int style) {
        this.currentStyle = style;
        switch (currentStyle) {
            case NORMAL: {
                normalLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                break;
            }
            case SEARCH: {
                normalLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    /**
     * 设置为是搜索模式下左边显示的是第一级还是第二级目录
     *
     * @param style SEARCH_LEFT_FIRST、SEARCH_LEFT_SECOND
     */
    public void setSearchLeftLayout(int style) {
        this.currentSearchLeftStyle = style;
        // setSearchLayout();
    }

    /**
     * 设置搜索模式下左边的图片是否可见
     *
     * @param visibility View.GONE,VISIBLE...
     */

    public void setSearchLeftImageVisibility(int visibility) {

        imgSearchLeft.setVisibility(visibility);

    }

    /**
     * 设置搜索框的hint
     *
     * @param hint 字符串
     */
    public void setSearchEditHint(String hint) {
        this.searchHint = hint;
        if (etSearch == null || searchHint == null) {
            return;
        }

        etSearch.setHint(searchHint);
    }

    /**
     * 设置搜索框的hint颜色
     *
     * @param color 颜色id
     */
    public void setSearchEditHintColor(int color) {
        if (etSearch == null) {
            return;
        }

        etSearch.setHintTextColor(color);
    }

    /**
     * 设置搜索模式下搜索框的左边图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchEditTextDrawableLeft(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        etSearch.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置搜索模式下搜索框的字体样式
     *
     * @param style 图片Id
     */

    public void setSearchEditTextStyle(int style) {
        if (style <= 0) {
            return;
        }
        etSearch.setTextAppearance(getContext(), style);
    }

    /**
     * 设置搜索模式下搜索框的背景
     *
     * @param drawableId 图片Id
     */

    public void setSearchEditTextBackground(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        etSearch.setBackgroundResource(drawableId);
    }

    /**
     * 得到搜索框
     */

    public EditText getSearchEditText() {
        return etSearch;
    }

    /**
     * 设置搜索模式下的右边的显示文字
     *
     * @param text 字符串
     */

    public void setSearchRightText(String text) {
        this.searchRightText = text;
        btnSearchRight.setText(searchRightText);
        btnSearchRight.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 设置搜索模式下的右边的显示图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchRightImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        btnSearchRight.setVisibility(View.VISIBLE);
        Drawable drawable = mContext.getResources().getDrawable(drawableId);

        //   if (searchRightImageAdjuest) {
        drawable.setBounds(0, 0, searchHeight, searchHeight);
        //   } else {
        //        drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
        //    }

        btnSearchRight.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置搜索模式下的左边的显示图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchLeftImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        imgSearchLeft.setVisibility(View.VISIBLE);
        imgSearchLeft.setImageResource(drawableId);
    }

    /**
     * 设置搜索模式下的右边的显示文字
     *
     * @param textId 字符串Id
     */

    public void setSearchRightText(int textId) {
        this.searchRightText = mContext.getString(textId);
        setSearchRightText(searchRightText);
    }


    /**
     * 设置最右边的文字
     *
     * @param confirm
     */
    public void setRightText(String confirm) {
        if (StringUtils.isBlank(confirm)) {
            return;
        }
        btnRight.setVisibility(View.VISIBLE);
        this.rightInfo = confirm;
        btnRight.setText(rightInfo);
        btnRight.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 获取右边按钮
     * @return
     */
    public Button getBtnRight(){
        return btnRight;
    }

    /**
     * 设置最右边的文字的颜色
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setTextColor(color);
        btnRight.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 获取最右边的文字
     *
     * @return 返回最右边的文字
     */
    public String getRightText() {
        return rightInfo;
    }

    /**
     * 设置最右边的文字
     *
     * @param confirmId 字符串的ID
     */
    public void setRightText(int confirmId) {
        this.rightInfo = mContext.getString(confirmId);
        setRightText(rightInfo);
    }

    /**
     * 设置最右边的控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setRightVisible(int visible) {
        btnRight.setVisibility(visible);
        ivRight.setVisibility(View.GONE);
    }

    /**
     * 设置最右边的图片
     *
     * @param drawableId 图片资源ID
     */

    public void setRightImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        btnRight.setVisibility(View.VISIBLE);
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
        btnRight.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置最右边的图片，just for H5添加图片
     *
     * @param url 图片资源url
     */
//    public void setRightImage(String url) {
//        btnRight.setVisibility(View.GONE);
//        ivRight.setVisibility(View.VISIBLE);
//        BitmapHelper.getSingleton().getBitmapUtils().display(ivRight, url);
//    }

    /**
     * 设置最右边的控件imageview是否可见 just for h5
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setRightH5ImageVisible(int visible) {
        ivRight.setVisibility(visible);
        btnRight.setVisibility(View.GONE);
    }

    public void showShareIcon(OnClickListener listener) {
        ivShare.setVisibility(View.VISIBLE);
        ivShare.setOnClickListener(listener);

        btnRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
    }
    public void dissShareIcon() {
        ivShare.setVisibility(View.GONE);

        btnRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param titleId 字符串对应的Id
     */
    public void setTitle(int titleId) {
        if (titleId <= 0) {
            return;
        }
        this.titleInfo = mContext.getString(titleId);
        setTitle(titleInfo);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return;
        }
        this.titleInfo = title;
        tvTitle.setText(titleInfo);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    private void setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 设置左边的文字
     *
     * @param leftText 字符串
     */
    public void setLeftFirstText(String leftText) {
        if (StringUtils.isBlank(leftText)) {
            return;
        }
        this.leftInfo = leftText;
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setText(leftText);
    }

    /**
     * 设置左边的文字的颜色
     *
     * @param color
     */
    public void setLeftFirstTextColor(int color) {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setTextColor(color);
    }

    /**
     * 设置最左边的两个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        btnLeft.setVisibility(visible);
    }


    /**
     * 设置最左边的第一个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftFirstVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        btnLeft.setVisibility(visible);
    }


    /**
     * 设置左边的是否可见
     *
     * @param drawableId 图片的资源Id
     */
    public void setLeftFirstImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnLeft.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置最左边的第二个控件的文字
     *
     * @param leftTextId 字符串
     */
    public void setLeftSecondText(int leftTextId) {
        this.secondLeftInfo = mContext.getString(leftTextId);
        setLeftSecondText(secondLeftInfo);
    }

    /**
     * 设置最左边的第二个控件的文字
     *
     * @param leftText 字符串
     */
    public void setLeftSecondText(String leftText) {
        //leftAllLayout.setVisibility(View.VISIBLE);
        tvSecondLeft.setVisibility(View.VISIBLE);
        tvSecondLeft.setText(leftText);
    }

    /**
     * 设置最左边的第二个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftSecondVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        tvSecondLeft.setVisibility(visible);
    }

    /**
     * 设置底部分割线的两个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setBottomDividerVisible(int visible) {
        dividerView.setVisibility(visible);
    }

    /**
     * 在满赠、满减页中，有在右上角的购物车图标上显示当前购物车中的
     * 商品数量，当前action bar无法满足次需求，所以又新增了控件和功能
     *
     * @param

    //public void setRightCartVisible(int visible) {
        flCart.setVisibility(visible);
    }

    public void setRightCartImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
        btnCart.setCompoundDrawables(drawable, null, null, null);
    }

    public TextView getTvCount() {
        return tvCount;
    }*/

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        tvSecondLeft.setOnClickListener(listener);
        btnRight.setOnClickListener(listener);
        btnLeft.setOnClickListener(listener);
        ivRight.setOnClickListener(listener);
    }
}
