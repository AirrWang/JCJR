package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.MsgHomeInfoEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;

public class MsgHomeActivity extends BaseActivity {

    @ViewInject(R.id.tv_msg_home_msg)
    private TextView mTvMsgHomeMsg;
    @ViewInject(R.id.tv_msg_home_act)
    private TextView mTvMsgHomeAct;
    @ViewInject(R.id.tv_msg_home_notice)
    private TextView mTvMsgHomeNotice;

    @ViewInject(R.id.iv_msg_home_msg)
    private ImageView mIvMsgHomeMsg;
    @ViewInject(R.id.iv_msg_home_act)
    private ImageView mIvMsgHomeAct;
    @ViewInject(R.id.iv_msg_home_notice)
    private ImageView mIvMsgHomeNotice;

    private Context mContext;

    private int msgNum;
    private int actNum;
    private int noticeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_home);

        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        getMsgInfo();
    }

    private void getMsgInfo() {
        SenderResultModel resultModel = ParamsManager.getMsgCenterInfo();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("获取消息中心信息 " + responeJson);
                        MsgHomeInfoEntity entity = GsonParser.getParsedObj(responeJson, MsgHomeInfoEntity.class);
                        MsgHomeInfoEntity.ResultBean result = entity.getResult();

                        msgNum = Integer.parseInt(result.getMessage().getNum());
                        actNum = Integer.parseInt(result.getActive().getNum());
                        noticeNum = Integer.parseInt(result.getGonggao().getNum());

                        //获取历史中的数据
                        int historyMsgNum = UserData.getInstance().getMsgNum();
                        int historyActNum = UserData.getInstance().getActNum();
                        int historyNoticeNum = UserData.getInstance().getNoticeNum();

//                        //保存新数据
//                        UserData.getInstance().setMsgNum(msgNum);
//                        UserData.getInstance().setActNum(actNum);
//                        UserData.getInstance().setNoticeNum(noticeNum);

                        //有信息时才去设置新的信息
                        if(msgNum >0){
                            mTvMsgHomeMsg.setText(result.getMessage().getName());
                        }
                        if(actNum >0){
                            mTvMsgHomeAct.setText(result.getActive().getName());
                        }
                        if(noticeNum >0){
                            mTvMsgHomeNotice.setText(result.getGonggao().getName());
                        }

                        //红点提示
                        if(msgNum >0 && msgNum>historyMsgNum){
                            mIvMsgHomeMsg.setImageResource(R.drawable.icon_msg_home_wdxx_red);
                        }
                        if(actNum >0 && actNum>historyActNum){
                            mIvMsgHomeAct.setImageResource(R.drawable.icon_msg_home_jxhd_red);
                        }
                        if(noticeNum >0 && noticeNum>historyNoticeNum){
                            mIvMsgHomeNotice.setImageResource(R.drawable.icon_msg_home_xwgg_red);
                        }

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("获取消息中心信息 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }


    @OnClick({R.id.btn_left, R.id.ll_msg_home_msg, R.id.ll_msg_home_act, R.id.ll_msg_home_notice})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;

            case R.id.ll_msg_home_msg:
                if (!UserData.getInstance().isLogin()) {
                    intent.setClass(mContext, LoginActivityCheck.class);
                    startActivity(intent);
                    return;
                }

                UserData.getInstance().setMsgNum(msgNum);
                mIvMsgHomeMsg.setImageResource(R.drawable.icon_msg_home_wdxx);

                intent.setClass(mContext, MessageCenterActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_msg_home_act:
                UserData.getInstance().setActNum(actNum);
                mIvMsgHomeAct.setImageResource(R.drawable.icon_msg_home_jxhd);

                intent.setClass(mContext, MessageActActivity.class);
                intent.putExtra("msg_type",0);
                startActivity(intent);
                break;

            case R.id.ll_msg_home_notice:
                UserData.getInstance().setNoticeNum(noticeNum);
                mIvMsgHomeNotice.setImageResource(R.drawable.icon_msg_home_xwgg);

                intent.setClass(mContext, MessageActActivity.class);
                intent.putExtra("msg_type",1);
                startActivity(intent);
                break;
        }
    }

}
