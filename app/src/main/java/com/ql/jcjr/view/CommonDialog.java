package com.ql.jcjr.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.utils.StringUtils;

/**
 * 自定义对话框
 */
public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 实现该接口，即可改变Message内容
     */
    public interface onChangeMessage {
        void updateMessageContent();
    }

    //所有的方法执行完都会返回一个Builder使得后面可以直接create和show
    public static class Builder implements onChangeMessage {
        private Context mContext;
        private String title;
        private String message;
        private int messageSize;
        private int buttonSize;
        private boolean isCancelable = true;//点击其他地方是否能取消
        private int color;
        private String positiveButtonText;//确定按钮
        private int positiveButtonTextColor = 0;//确定按钮颜色
        private String negativeButtonText;//取消按钮
        private View contentView;
        private OnClickListener positiveButtonClickListener; //确定按钮事件
        private OnClickListener negativeButtonClickListener; //取消按钮事件
        private CommonDialog dialog;
        private boolean isShowInputView = false;//是否显示密码输入框，默认不显示
        @ViewInject(R.id.ll_top_layout)
        private LinearLayout topLayout;
        @ViewInject(R.id.tv_title)
        private TextView titleView;
        @ViewInject(R.id.ll_message_layout)
        private LinearLayout messageLayout;
        @ViewInject(R.id.tv_message)
        private TextView messageView;
        @ViewInject(R.id.ll_button_layout)
        private LinearLayout buttonLayout;
        @ViewInject(R.id.btn_ok)
        private Button okBtn;
        @ViewInject(R.id.btn_cancel)
        private Button cancelBtn;
        @ViewInject(R.id.vw_divider)
        private View dividerView;
        @ViewInject(R.id.et_user_input)
        private CancelEditText userInputView;
        @ViewInject(R.id.vw_ver_divider)
        private View vwVerDivider;

        private TextWatcher mTextWatcher;

        public Builder(Context context) {
            this.mContext = context;
        }


        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) mContext.getText(title);
            setTitle(this.title);
            return this;
        }

        /**
         * 设置标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) mContext.getText(message);
            return this;
        }

        /**
         * 设置标题
         *
         * @param message 消息
         * @param color   颜色 R.color.xx
         * @return
         */
        public Builder setMessage(String message, int color) {
            this.message = message;
            this.color = color;
            return this;
        }

        /**
         * 设置标题
         *
         * @param message 消息
         * @param color   颜色 R.color.xx
         * @return
         */
        public Builder setMessage(int message, int color) {
            this.message = (String) mContext.getText(message);
            this.color = color;
            return this;
        }

        /**
         * 设置消息字体大小
         *
         * @param size 颜色 R.dimen.xx
         * @return
         */
        public Builder setMessageSize(int size) {
            this.messageSize = size;
            return this;
        }

        /**
         * 设置按钮字体大小
         *
         * @param size 字体大小 R.dimen.xx
         * @return
         */
        public Builder setButtonTextSize(int size) {
            this.buttonSize = size;
            return this;
        }

        /**
         * 设置整个背景
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 是否显示密码输入框
         *
         * @param isShowInputView
         */
        public Builder setIsShowInputView(boolean isShowInputView,TextWatcher watcher) {
            this.isShowInputView = isShowInputView;
            mTextWatcher = watcher;
            return this;
        }

        /**
         * 是否显示可输入的EditText
         */
        private void isNeedShowInputView() {
            if (this.isShowInputView) {
                userInputView.setVisibility(View.VISIBLE);
                userInputView.setCancelIcon(R.drawable.enter_cance_dialog);
                if(mTextWatcher != null){
                    userInputView.getCancelEditText().addTextChangedListener(mTextWatcher);
                }

            } else {
                userInputView.setVisibility(View.GONE);
            }

            changeDividerMarginTop();
        }


        /**
         * 得到输入框的内容
         */
        public final String getInputViewText() {
            if (userInputView != null && isShowInputView) {
                return userInputView.getEditTextContent();
            } else {
                return "";
            }
        }

        /**
         * 点击其他地方是否能取消
         *
         * @param isCancelable
         * @return
         */
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置输入框的Hint
         *
         * @param hintText
         */
        //TODO 次方法必须在 create() 调用之后再调用
        public void setHintText(String hintText) {
            if (this.isShowInputView) {
                userInputView.setEditTextHintContent(hintText);
            }
        }

        /**
         * 设置输入框的输入类型
         *
         * @param inputType
         */
        //TODO 次方法必须在 create() 调用之后再调用
        public void setInputType(int inputType) {
            if (this.isShowInputView) {
                userInputView.setEditTextInputType(inputType);
            }
        }

        public CancelEditText getEdittext() {
            return userInputView;
        }

        public Button getOkBtn() {
            return okBtn;
        }

        private TextWatcher watcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                okBtn.setTextColor(mContext.getResources().getColor(R.color.gray));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(userInputView.getCancelEditText().getText().length() >= 6){
                   okBtn.setEnabled(true);
                   okBtn.setTextColor(mContext.getResources().getColor(R.color.c14_0071d1));
               }else {
                   okBtn.setEnabled(false);
                   okBtn.setTextColor(mContext.getResources().getColor(R.color.gray));
               }
            }
        };

        /**
         * 当有密码输入框的时候，需要将id-> @+id/vw_ver_divider的
         * 控件的layout_marginTop设置为0
         */
        private void changeDividerMarginTop() {
            LinearLayout.LayoutParams lp =
                    (LinearLayout.LayoutParams) vwVerDivider.getLayoutParams();
            if (this.isShowInputView) {
                lp.topMargin = 0;
            } else {
                lp.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_40px);
            }
            vwVerDivider.setLayoutParams(lp);
        }

        /**
         * 设置确定按钮和其点击事件
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonText = (String) mContext
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButtonTextColor(int colorId) {
            this.positiveButtonTextColor = colorId;

            return this;
        }

        //设置取消按钮和其事件
        public Builder setNegativeButton(int negativeButtonText,
                OnClickListener listener) {
            this.negativeButtonText = (String) mContext
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }


        public CommonDialog create() {
            dialog = new CommonDialog(mContext, R.style.CommonDialog);
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(R.layout.common_dialog, null);
            ViewUtils.inject(this, layoutView);

            dialog.addContentView(layoutView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            if (!StringUtils.isBlank(this.title)) {
                titleView.setText(this.title);
                titleView.setVisibility(View.VISIBLE);
            } else {
                titleView.setVisibility(View.GONE);
            }
            if (!StringUtils.isBlank(this.message)) {
                messageView.setText(this.message);
                if (messageSize != 0) {
                    messageView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mContext.getResources().getDimension(messageSize));
                }
                messageLayout.setVisibility(View.VISIBLE);
            } else {
                messageLayout.setVisibility(View.GONE);
            }
            //如果设置了contentView，而不是设置message
            if (contentView != null) {
                //将vwVerDivider隐藏
                vwVerDivider.setVisibility(View.GONE);

                // if no message set add the contentView to the dialog body
                messageLayout.setVisibility(View.VISIBLE);
                messageLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                messageView.setVisibility(View.GONE);
                topLayout.setBackgroundColor(Color.TRANSPARENT);
                // messageLayout.removeAllViews();
                messageLayout.addView(contentView,
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
            } else {
                topLayout.setBackgroundResource(R.drawable.bg_common_dialog_top);
            }
            //根据设置是不是设置Button，来确定是显示几个button
            //取消
            if (StringUtils.isNotBlank(negativeButtonText) && StringUtils.isBlank(
                    positiveButtonText)) {
                cancelBtn.setVisibility(View.VISIBLE);
                okBtn.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
                buttonLayout.setBackgroundColor(Color.TRANSPARENT);
                cancelBtn.setBackgroundResource(R.drawable.bg_common_dialog_bottom);
                if (buttonSize != 0) {
                    cancelBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mContext.getResources().getDimension(buttonSize));
                }
            }
            //Ok
            if (StringUtils.isNotBlank(positiveButtonText) && StringUtils.isBlank(
                    negativeButtonText)) {
                okBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
                buttonLayout.setBackgroundColor(Color.TRANSPARENT);
                okBtn.setBackgroundResource(R.drawable.bg_common_dialog_bottom);
                if (buttonSize != 0) {
                    okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mContext.getResources().getDimension(buttonSize));
                }
            }
            //如果有两个Button
            if (StringUtils.isBlank(negativeButtonText) && StringUtils.isBlank(
                    positiveButtonText)) {
                buttonLayout.setVisibility(View.GONE);
            } else {
                buttonLayout.setVisibility(View.VISIBLE);
                if (buttonSize != 0) {
                    okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mContext.getResources().getDimension(buttonSize));
                    cancelBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mContext.getResources().getDimension(buttonSize));
                }
            }
            //设置监听事件
            if (StringUtils.isNotBlank(negativeButtonText)) {
                cancelBtn.setText(negativeButtonText);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (negativeButtonClickListener == null) {
                            dialog.dismiss();
                            return;
                        }
                        negativeButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                });
                cancelBtn.setVisibility(View.VISIBLE);
            }

            if (StringUtils.isNotBlank(positiveButtonText)) {
                okBtn.setText(positiveButtonText);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (positiveButtonClickListener == null) {
                            dialog.dismiss();
                            return;
                        }
                        positiveButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });

                if(positiveButtonTextColor != 0) {
                    okBtn.setTextColor(positiveButtonTextColor);
                    okBtn.setEnabled(false);
                }
                okBtn.setVisibility(View.VISIBLE);
            }

            //根据是否有Edittext，设置底部分割线的下间距
            isNeedShowInputView();
            dialog.setCancelable(isCancelable);
            dialog.setCanceledOnTouchOutside(isCancelable);

            dialog.setContentView(layoutView);

            return dialog;
        }

        @Override
        public void updateMessageContent() {
            if (!StringUtils.isBlank(this.message)) {
                messageView.setText(this.message);
                if (color != 0) {
                    //TODO mContext.getResources().getColor(color)的返回值: -1885108 , 目前还不清楚为啥是负值
                    messageView.setTextColor(mContext.getResources().getColor(color));
                } else {
                    messageView.setTextColor(mContext.getResources().getColor(R.color.c03_2c2c2c));
                }
                messageLayout.setVisibility(View.VISIBLE);
            } else {
                messageLayout.setVisibility(View.GONE);
            }
        }
    }
}
