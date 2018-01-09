package com.ql.jcjr.manager;

import android.content.Context;

import com.ql.jcjr.constant.Global;

/**
 * ClassName: CommonDialogInfo
 * Description:
 * Author: liuchao
 * Date: Created on 202016/10/15.
 */
public class CommonDialogParams {

    private Context context;
    private String text = Global.LOADING;
    private boolean transparent = true;
    private boolean cancleAble = true;
    private boolean cancleTouchOutSideAble = false;

    public CommonDialogParams() {

    }

    public CommonDialogParams(Builder builder) {
        this.context = builder.context;
        this.text = builder.text;
        this.transparent = builder.transparent;
        this.cancleAble = builder.cancleAble;
        this.cancleTouchOutSideAble = builder.cancleTouchOutSideAble;
    }

    public CommonDialogParams(Context context, String text) {
        setContext(context);
        setText(text);
    }

    public CommonDialogParams(String text) {
        setText(text);
    }

    public boolean isCancleAble() {
        return cancleAble;
    }

    public void setCancleAble(boolean cancleAble) {
        this.cancleAble = cancleAble;
    }

    public boolean isCancleTouchOutSideAble() {
        return cancleTouchOutSideAble;
    }

    public void setCancleTouchOutSideAble(boolean cancleTouchOutSideAble) {
        this.cancleTouchOutSideAble = cancleTouchOutSideAble;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class Builder {
        private Context context;
        private String text = Global.LOADING;
        private boolean transparent = true;
        private boolean cancleAble = false;
        private boolean cancleTouchOutSideAble = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTransparent(boolean transparent) {
            this.transparent = transparent;
            return this;
        }

        public Builder setCancleAble(boolean cancleAble) {
            this.cancleAble = cancleAble;
            return this;
        }

        public Builder setCancleTouchOutSideAble(boolean cancleTouchOutSideAble) {
            this.cancleTouchOutSideAble = cancleTouchOutSideAble;
            return this;
        }

        public CommonDialogParams build() {
            return new CommonDialogParams(this);
        }
    }
}
