package com.ql.jcjr.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ql.jcjr.R;

/**
 * 自定义对话框
 */
public class NetDialog extends Dialog {
    private Context mContext;

    public NetDialog(Context context) {
        super(context);
        mContext=context;
    }

    public NetDialog(Context context, int theme) {
        super(context, theme);
        mContext=context;
    }

    protected NetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_net,null);
        setContentView(view);
    }


}
