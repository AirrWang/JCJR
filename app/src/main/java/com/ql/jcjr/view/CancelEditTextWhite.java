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
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.utils.StringUtils;

/**
 * 自定义带是删除功能的EditText，仿IOS效果，失去焦点时，删除icon消失
 */
public class CancelEditTextWhite extends LinearLayout {

    private Context mContext;
    private LinearLayout cancelEditView;
    private boolean aBoolean;
    private CancelEditEventListener listener;

//    @ViewInject(R.id.rl_layout)
//    private LinearLayout rlLayout;
    @ViewInject(R.id.iv_left_icon)
    private ImageView ivLeftIcon;
    @ViewInject(R.id.et_content)
    private EditText etContent;
    @ViewInject(R.id.iv_cancel_icon)
    private ImageView ivCancelIcon;
    @ViewInject(R.id.tv_extra_operate)
    private TextView tvExtraOperate;
    @ViewInject(R.id.iv_extra_operate)
    private ImageView ivExtraOperate;

    public static final int CommonStyleNone = 0;//无输入框
    public static final int CommonStylePlainTextInput = 1;//普通输入框
    public static final int CommonStyleSecureTextInput = 2;//密码输入框
    public static final int CommonStyleNumberTextInput = 3;//数字输入框
    private int isCancelIconShow;

    public CancelEditTextWhite(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public CancelEditTextWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public CancelEditTextWhite(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cancelEditView = (LinearLayout) inflater.inflate(R.layout.cancel_edit_view_white, null);
        ViewUtils.inject(this, cancelEditView);
        getAttrsFromXml(attrs);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(cancelEditView, lp);

        etContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setaBoolean(hasFocus);
                if (listener != null) {
                    listener.onCancelFocusChange(hasFocus);
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

        tvExtraOperate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickRightExtraTextView();
                }
            }
        });

        ivExtraOperate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickRightExtraImageView();
                }
            }
        });
    }

    private void getAttrsFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CancelEditText);
        if (typedArray == null) {
            return;
        }
        //left  icon
        if (typedArray.hasValue(R.styleable.CancelEditText_left_icon)) {
            setLeftIcon(typedArray.getResourceId(R.styleable.CancelEditText_left_icon,
                    R.drawable.phone_number));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_left_icon_visibility)) {
            setLeftIconVisibility(typedArray.getInt(R.styleable.CancelEditText_left_icon_visibility, View.VISIBLE));
        }
        //cancel icon
        if (typedArray.hasValue(R.styleable.CancelEditText_cancel_icon)) {
            setCancelIcon(typedArray.getResourceId(R.styleable.CancelEditText_cancel_icon,
                    R.drawable.enter_cancel));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_right_icon_visibility)) {
            isCancelIconShow = typedArray.getInt(R.styleable.CancelEditText_right_icon_visibility, View.VISIBLE);
            setRightIconVisibility(typedArray.getInt(R.styleable.CancelEditText_right_icon_visibility, View.VISIBLE));
        }
        //extra_tv
        if (typedArray.hasValue(R.styleable.CancelEditText_right_extra_tv_visibility)) {
            setRightExtraTextViewVisibility(typedArray.getInt(R.styleable.CancelEditText_right_extra_tv_visibility, View.GONE));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_right_extra_tv_content)) {
            setRightExtraTextViewContent(typedArray.getString(R.styleable.CancelEditText_right_extra_tv_content));
        }
        //extra_iv
        if (typedArray.hasValue(R.styleable.CancelEditText_right_extra_iv_visibility)) {
            setRightExtraImageViewVisibility(typedArray.getInt(R.styleable.CancelEditText_right_extra_iv_visibility, View.GONE));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_right_extra_iv_icon)) {
            setRightExtraImageIcon(typedArray.getResourceId(R.styleable.CancelEditText_right_extra_iv_icon, R.drawable.phone_number));
        }
        //EditText
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
            setEditTextInputType(typedArray.getResourceId(R.styleable.CancelEditText_input_type, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_max_length)) {
            setEditTextMaxLength(typedArray.getInt(R.styleable.CancelEditText_max_length, -1));
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
        if (typedArray.hasValue(R.styleable.CancelEditText_enabled)) {
            setEnabled(typedArray.getBoolean(R.styleable.CancelEditText_enabled, false));
        }

        typedArray.recycle();
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
     * 文本框的右边的图片是否可见
     *
     * @param visibility View.GONE,View.VISIBLE ...
     */
    public void setRightIconVisibility(int visibility) {
        ivCancelIcon.setVisibility(visibility);
    }

    /**
     * 文本框的右边 额外文字操作按钮 是否可见
     *
     * @param visibility View.GONE,View.VISIBLE ...
     */
    public void setRightExtraTextViewVisibility(int visibility) {
        tvExtraOperate.setVisibility(visibility);
    }
    public void setRightExtraTextViewContent(String content) {
        tvExtraOperate.setText(content);
    }
    public void setRightExtraTextViewEnable(boolean enable) {
        tvExtraOperate.setEnabled(enable);
    }
    /**
     * 文本框的右边 额外图片操作按钮 是否可见
     *
     * @param visibility View.GONE,View.VISIBLE ...
     */
    public void setRightExtraImageViewVisibility(int visibility) {
        ivExtraOperate.setVisibility(visibility);
    }
    public void setRightExtraImageIcon(int resId) {
        ivExtraOperate.setImageResource(resId);
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

        if (inputType == CancelEditTextWhite.CommonStyleNone) {
            etContent.setInputType(inputType);
        } else if (inputType == CancelEditTextWhite.CommonStylePlainTextInput) {
            etContent.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else if (inputType == CancelEditTextWhite.CommonStyleSecureTextInput) {
            etContent.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        } else if (inputType == CancelEditTextWhite.CommonStyleNumberTextInput) {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER );
        } else {
            etContent.setInputType(inputType);
        }
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
    public void setOnCancelEditEventListener(CancelEditEventListener listener) {
        this.listener = listener;
    }

    public interface CancelEditEventListener {
        void onCancelFocusChange(boolean hasFocus);
        void onClickRightExtraTextView();
        void onClickRightExtraImageView();
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
