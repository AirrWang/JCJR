package com.ql.jcjr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;

/**
 * Created by LIUCHAO on 2016/2/29.
 */
public class LimitTheNumOfWordsEeditText extends LinearLayout {

    @ViewInject(R.id.et_signature)
    private EditText etContent;
    @ViewInject(R.id.tv_signature_num)
    private TextView tvNum;

    private Context mContext;
    private int limitCount;

    public LimitTheNumOfWordsEeditText(Context context) {
        this(context, null);
    }

    public LimitTheNumOfWordsEeditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitTheNumOfWordsEeditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.limit_the_num_of_words, null);
        ViewUtils.inject(this, view);
        getAttrsFromXml(attrs);
        addView(view);

        initEditText();
    }

    private void initEditText() {
        etContent.addTextChangedListener(textWatcher);
        String content = etContent.getText().toString().trim();
    }

    public void setSelection(int index) {
        etContent.setSelection(index);
    }

    private void getAttrsFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LimitTheNumOfWordsEeditText);
        if (typedArray == null) {
            return;
        }

        if (typedArray.hasValue(R.styleable.LimitTheNumOfWordsEeditText_hint)) {
            setEditTextHint(typedArray.getString(R.styleable.LimitTheNumOfWordsEeditText_hint));
        }
        if (typedArray.hasValue(R.styleable.LimitTheNumOfWordsEeditText_limitCount)) {
            limitCount = typedArray.getInt(R.styleable.LimitTheNumOfWordsEeditText_limitCount, 140);
            setTextViewContent(limitCount);
        }
        if (typedArray.hasValue(R.styleable.LimitTheNumOfWordsEeditText_inputTextStyle)) {
            setInputTextStyle(typedArray.getResourceId(R.styleable.LimitTheNumOfWordsEeditText_inputTextStyle, R.style.text_f05_24_c33_333333));
        }
        if (typedArray.hasValue(R.styleable.LimitTheNumOfWordsEeditText_TextViewStyle)) {
            setTextViewStyle(typedArray.getResourceId(R.styleable.LimitTheNumOfWordsEeditText_TextViewStyle, R.style.text_f04_30_c05_999999));
        }
    }

    private void setEditTextHint(String hint) {
        etContent.setHint(hint);
    }

    private void setTextViewContent(int count) {
        tvNum.setText(getResources().getString(R.string.max_ems, etContent.length(), count));
    }
    /**
     * 设置文字的样式
     * @param styleId
     */
    private void setInputTextStyle(int styleId) {
        if (styleId <= 0) {
            return;
        }
        etContent.setTextAppearance(mContext, styleId);
    }

    private void setTextViewStyle(int styleId) {
        if (styleId <= 0) {
            return;
        }
        tvNum.setTextAppearance(mContext, styleId);
    }

    public void setEditViewContent(String content) {
        etContent.setText(content);
    }

    public String getEditViewContent() {
        return etContent.getText().toString().trim();
    }

    TextWatcher textWatcher = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            tvNum.setText(getResources().getString(R.string.max_ems, s.length(), limitCount));
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

    };
}
