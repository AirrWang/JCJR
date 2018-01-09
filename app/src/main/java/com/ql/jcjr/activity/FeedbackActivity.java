package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.LimitTheNumOfWordsEeditText;

public class FeedbackActivity extends BaseActivity {

    @ViewInject(R.id.lwet_feedback)
    private LimitTheNumOfWordsEeditText mEeditText;
    @ViewInject(R.id.btn_commit)
    private Button mBtnCommmit;

    private Context mContext;

    private static final int HANDLER_CHANGE_SUCCESS = 0;

    /**
     * Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHANGE_SUCCESS:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ViewUtils.inject(this);
        mContext = this;
    }

    private void commit(String content) {
        SenderResultModel resultModel = ParamsManager.senderFeedback(content);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("意见反馈 " + responeJson);
                CommonToast.makeCustomText(mContext, "提交成功！");
                mBtnCommmit.setEnabled(false);
                handler.sendEmptyMessageDelayed(HANDLER_CHANGE_SUCCESS, 3000);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("意见反馈 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private boolean checkInfo() {
        if (StringUtils.isBlank(mEeditText.getEditViewContent())){
            CommonToast.showHintDialog(mContext, "请输入内容");
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_left, R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_commit:
                if(checkInfo()) {
                    commit(mEeditText.getEditViewContent());
                }
                break;
        }
    }
}
