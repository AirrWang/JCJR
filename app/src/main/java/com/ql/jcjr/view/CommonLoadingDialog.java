package com.ql.jcjr.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ql.jcjr.R;


public class CommonLoadingDialog extends Dialog {

    private TextView mTvDialogText;

    private Context mContext;
    private String mDialogText = " 正在加载...";

    public CommonLoadingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public CommonLoadingDialog(Context context) {
        super(context);
        mContext = context;
    }

    public CommonLoadingDialog(Context context, String text) {
        super(context);
        mContext = context;
        mDialogText = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setIndeterminate(true);
        setCancelable(true);

        // progressDialog.show()
        setContentView(R.layout.dialog_loading_progress);

        mTvDialogText = (TextView) findViewById(R.id.tv_dialog_text);
        mTvDialogText.setText(mDialogText);
    }

    public void showDialog() {
        show();
    }

    public void updateText(String string) {
        if (mTvDialogText != null) {
            mTvDialogText.setText(string);
        }
    }
}