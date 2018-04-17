package com.ql.jcjr.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.ql.jcjr.R;

import java.util.List;


public class KeyboardUtil {
    private View ll_jianpan;
    private KeyboardView mKeyboardView;
    private Keyboard mNumberKeyboard; // 数字键盘
    private Keyboard mLetterKeyboard; // 字母键盘
    private Context mContext;

    private boolean isNumber = false;  // 是否数字键盘
    private boolean isUpper = false;   // 是否大写
    private EditText mEditText;

    /**
     *
     * @param context
     * @param view
     * @param editText
     * @param type  0,空白格 1，小数点  2，身份证X
     */
    public KeyboardUtil(Context context, View view, EditText editText,int type) {
        mContext=context;
        mEditText = editText;
        mLetterKeyboard = new Keyboard(context, R.xml.keyboard_qwerty);
        mKeyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
        ll_jianpan= view.findViewById(R.id.ll_jianpan);
        ImageView iv_close= (ImageView) ll_jianpan.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
        if (type==0){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers_blank);
        }else if (type==1){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers);
        }else if (type==2){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers_realname);
        }else if (type==66299){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers_blank);
            iv_close.setVisibility(View.GONE);
        }

        mKeyboardView.setKeyboard(mNumberKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);

    }
    public KeyboardUtil(Context context, Activity activity, EditText editText, int type) {
        mContext=context;
        mEditText = editText;
        if (type==0){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers_blank);
        }else if (type==1){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers);
        }else if (type==2){
            mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers_realname);
        }
        mLetterKeyboard = new Keyboard(context, R.xml.keyboard_qwerty);
        mKeyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view);
        ll_jianpan= activity.findViewById(R.id.ll_jianpan);
        mKeyboardView.setKeyboard(mNumberKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);
        ImageView iv_close= (ImageView) ll_jianpan.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }


    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL||primaryCode == 66299) { // cancel
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) { // 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) { // 大小写切换
                changeKeyboart();
                mKeyboardView.setKeyboard(mLetterKeyboard);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) { // 数字与字母键盘互换
                if (isNumber) {
                    isNumber = false;
                    mKeyboardView.setKeyboard(mLetterKeyboard);
                } else {
                    isNumber = true;
                    mKeyboardView.setKeyboard(mNumberKeyboard);
                }

            } else if (primaryCode == 57419) { // 左移
                if (start > 0) {
                    mEditText.setSelection(start - 1);
                }

            } else if (primaryCode == 57419) { // 右移
                if (start > mEditText.length()) {
                    mEditText.setSelection(start + 1);
                }
            } else { // 输入键盘值
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private void changeKeyboart() {
        List<Keyboard.Key> keyList = mLetterKeyboard.getKeys();
        if (isUpper) { // 大写切换小写
            isUpper = false;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isLetter(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else { // 小写切换成大写
            isUpper = true;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isLetter(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    /**
     * 判断是否是字母
     */
    private boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }

    public void hideKeyboard() {

        int visibility = ll_jianpan.getVisibility();
        if (visibility == View.VISIBLE) {
            ll_jianpan.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.anim_bottom_out));
            ll_jianpan.setVisibility(View.GONE);
        }
    }

    public void showKeyboard() {
        int visibility = ll_jianpan.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            ll_jianpan.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.anim_bottom_in));
            ll_jianpan.setVisibility(View.VISIBLE);
        }
    }


}
