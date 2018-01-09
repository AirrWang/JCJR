package com.ql.jcjr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.utils.StringUtils;

/**
 * 自定义带是删除功能的EditText，仿IOS效果，失去焦点时，删除icon消失
 */
public class CancelEditText extends LinearLayout {

    private Context mContext;
    private LinearLayout cancelEditView;
    private boolean aBoolean;
    private OnCancelFocusChangeListener listener;

    @ViewInject(R.id.rl_layout)
    private LinearLayout rlLayout;
    @ViewInject(R.id.iv_left_icon)
    private ImageView ivLeftIcon;
    @ViewInject(R.id.et_content)
    private EditText etContent;
    @ViewInject(R.id.iv_cancel_icon)
    private ImageView ivCancelIcon;
    @ViewInject(R.id.et_content_bottom_line)
    private View bottomLine;

    public static final int CommonStyleNone = 0;//无输入框
    public static final int CommonStylePlainTextInput = 1;//普通输入框
    public static final int CommonStyleSecureTextInput = 2;//密码输入框
    public static final int CommonStyleNumberTextInput = 3;//数字输入框
    private int isCancelIconShow;

    public CancelEditText(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public CancelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public CancelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (isInEditMode()) {
            return;
        }
        setaBoolean(false);
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cancelEditView = (LinearLayout) inflater.inflate(R.layout.cancel_edit_view, null);
        ViewUtils.inject(this, cancelEditView);
        getAttrsFromXml(attrs);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(cancelEditView, lp);

        etContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setaBoolean(hasFocus);
                if (listener != null) {
                    listener.OnCancelFocusChangeListener(hasFocus);
                }
                if (hasFocus && StringUtils.isNotBlank(etContent.getText().toString().trim())
                        && isCancelIconShow != View.GONE) {
                    ivCancelIcon.setVisibility(View.VISIBLE);
                } else {
                    ivCancelIcon.setVisibility(View.GONE);
                }
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isNotBlank(etContent.getText().toString().trim())) {
                    if (isaBoolean() && isCancelIconShow != View.GONE) {
                        ivCancelIcon.setVisibility(View.VISIBLE);
                    } else {
                        ivCancelIcon.setVisibility(View.GONE);
                    }
                } else {
                    ivCancelIcon.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ivCancelIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etContent.setText("");
            }
        });
    }

    private void getAttrsFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray =
                getContext().obtainStyledAttributes(attrs, R.styleable.CancelEditText);
        if (typedArray == null) {
            return;
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_left_icon)) {
            setLeftIcon(typedArray.getResourceId(R.styleable.CancelEditText_left_icon,
                    R.drawable.phone_number));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_left_icon_visibility)) {
            setLeftIconVisibility(typedArray.getInt(R.styleable.CancelEditText_left_icon_visibility,
                    View.VISIBLE));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_right_icon_visibility)) {
            isCancelIconShow = typedArray.getInt(R.styleable.CancelEditText_right_icon_visibility,
                    View.VISIBLE);
            setRightIconVisibility(typedArray.getInt(R.styleable.CancelEditText_right_icon_visibility,
                    View.VISIBLE));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content)) {
            setEditTextContent(typedArray.getResourceId(R.styleable.CancelEditText_content, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content_style)) {
            setEditTextContentStyle(
                    typedArray.getResourceId(R.styleable.CancelEditText_content_style, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content_hint)) {
            setEditTextHintContent(typedArray.getString(R.styleable.CancelEditText_content_hint));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_input_type)) {
            setEditTextInputType(
                    typedArray.getResourceId(R.styleable.CancelEditText_input_type, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_max_length)) {
            setEditTextMaxLength(typedArray.getInt(R.styleable.CancelEditText_max_length, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_cancel_icon)) {
            setCancelIcon(typedArray.getResourceId(R.styleable.CancelEditText_cancel_icon,
                    R.drawable.enter_cancel));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_bg_drawable)) {
            setRlLayoutBackground(typedArray.getResourceId(R.styleable.CancelEditText_bg_drawable,
                    R.drawable.bg_filled_corner_ebebeb));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_max_lines)) {
            setEditTextMaxLines(typedArray.getInt(R.styleable.CancelEditText_max_lines, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_single_line)) {
            setEditTextSingleLine(
                    typedArray.getBoolean(R.styleable.CancelEditText_single_line, false));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_common_input_style)) {
            setEditTextInputType(
                    typedArray.getInt(R.styleable.CancelEditText_common_input_style, 0));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_rl_layout_padding)) {
            setEtContentPadding(
                    typedArray.getDimension(R.styleable.CancelEditText_rl_layout_padding, 0));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_contentBottomLineVisibility)) {
            setBottomLineVisibility(
                    typedArray.getInt(R.styleable.CancelEditText_contentBottomLineVisibility,
                            View.GONE));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content_bottom_line_color)) {
            setBottomLineColor(
                    typedArray.getResourceId(R.styleable.CancelEditText_content_bottom_line_color,
                            -1));
        }

        if (typedArray.hasValue(R.styleable.CancelEditText_enabled)) {
            setEnabled(typedArray.getBoolean(R.styleable.CancelEditText_enabled, false));
        }

        typedArray.recycle();
    }

    /**
     * 设置布局的背景图片
     *
     * @param drawable
     */
    public void setRlLayoutBackground(int drawable) {
        rlLayout.setBackgroundResource(drawable);
    }

    /**
     * 文本框的左边的图片
     *
     * @param resId 图片资源Id
     */
    public void setLeftIcon(int resId) {
        ivLeftIcon.setImageResource(resId);
    }

    /**
     * 文本框的左边的图片是否可见
     *
     * @param visibility View.GONE,View.VISIBLE ...
     */
    public void setLeftIconVisibility(int visibility) {
        ivLeftIcon.setVisibility(visibility);
    }

    /**
     * 文本框的左边的图片是否可见
     *
     * @param visibility View.GONE,View.VISIBLE ...
     */
    public void setRightIconVisibility(int visibility) {
        ivCancelIcon.setVisibility(visibility);
    }

    /**
     * 文本框的文字
     *
     * @param content 文字
     */
    public void setEditTextContent(String content) {
        etContent.setText(content);
    }

    /**
     * 文本框的文字样式
     *
     * @param style 文字样式Id
     */
    public void setEditTextContentStyle(int style) {
        if (style <= 0) {
            return;
        }
        etContent.setTextAppearance(getContext(), style);
    }

    /**
     * 文本框的hint
     *
     * @param content 文字
     */
    public void setEditTextHintContent(String content) {
        etContent.setHint(content);
    }

    /**
     * 文本框的hint
     *
     * @param content 文字资源Id
     */
    public void setEditTextHintContent(int content) {
        if (content <= 0) {
            return;
        }
        setEditTextHintContent(getResources().getString(content));
        // etContent.setHint(content);
    }

    /**
     * 设置文本输入后的颜色
     *
     * @param resid 色值资源id
     */
    public void setEditTextTextColor(int resid) {
        etContent.setTextColor(resid);
    }

    /**
     * 设置输入文本的位置 Gravity.xxxx
     *
     * @param gravity
     */
    public void setEditTextGravity(int gravity) {
        etContent.setGravity(gravity);
    }

    /**
     * 文本框的inputType
     *
     * @param inputType
     */
    public void setEditTextInputType(int inputType) {
        if (inputType <= 0) {
            return;
        }

        if (inputType == CancelEditText.CommonStyleNone) {
            etContent.setInputType(inputType);
        } else if (inputType == CancelEditText.CommonStylePlainTextInput) {
            etContent.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else if (inputType == CancelEditText.CommonStyleSecureTextInput) {
            etContent.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        } else if (inputType == CancelEditText.CommonStyleNumberTextInput) {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER );
        } else {
            etContent.setInputType(inputType);
        }
    }

    /**
     * 设置内容边距
     *
     * @param padding
     */
    public void setEtContentPadding(float padding) {
        rlLayout.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
    }

    /**
     * 设置输入框的线条的是否显示
     *
     * @param visible
     */
    public void setBottomLineVisibility(int visible) {
        bottomLine.setVisibility(visible);
    }

    /**
     * 设置输入框的线条的颜色
     *
     * @param resid
     */
    public void setBottomLineColor(int resid) {
        if (resid <= 0) {
            return;
        }
        bottomLine.setBackgroundResource(resid);
    }

    /**
     * 设置edittext是否可点击
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        etContent.setFocusable(enabled);
        etContent.setFocusableInTouchMode(enabled);
        if (enabled) {
            etContent.requestFocus();
        }
    }

    /**
     * 文本框的SingleLine
     *
     * @param singleLine
     */
    public void setEditTextSingleLine(boolean singleLine) {
        etContent.setSingleLine(singleLine);
    }

    /**
     * 设置文本框最大字符数
     *
     * @param maxLength 最大字符数
     */
    public void setEditTextMaxLength(int maxLength) {
        if (maxLength <= 0) {
            return;
        }
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    /**
     * 设置文本框最大行数
     *
     * @param maxLines 最大行数
     */
    public void setEditTextMaxLines(int maxLines) {
        if (maxLines <= 0) {
            return;
        }
        etContent.setMaxLines(maxLines);
    }

    /**
     * 获取文本框的文字
     *
     * @return 文字内容
     */
    public String getEditTextContent() {
        return etContent.getText().toString().trim();
    }

    /**
     * 获取输入框
     *
     * @return 文字内容
     */
    public EditText getCancelEditText() {
        return etContent;
    }

    /**
     * 文本框的文字
     *
     * @param content 文字资源Id
     */
    public void setEditTextContent(int content) {
        if (content <= 0) {
            return;
        }
        etContent.setText(content);
    }

    /**
     * 文本框的最右边的图片
     *
     * @param resId 图片资源Id
     */
    public void setCancelIcon(int resId) {
        ivCancelIcon.setImageResource(resId);
    }

    /**
     * 设置光标位置
     *
     * @param index
     */
    public void setSelection(int index) {
        etContent.requestFocus();
        etContent.setSelection(index);
    }

    /**
     * 获取焦点事件的接口
     *
     * @param listener
     */
    public void setOnCancelFocusChangeListener(OnCancelFocusChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCancelFocusChangeListener {
        void OnCancelFocusChangeListener(boolean hasFocus);
    }

    /**
     * 监听输入事件
     *
     * @param textWatcher
     */
    public void addTextChangedListener(TextWatcher textWatcher) {
        etContent.addTextChangedListener(textWatcher);
    }
}
