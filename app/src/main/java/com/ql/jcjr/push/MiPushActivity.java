package com.ql.jcjr.push;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ql.jcjr.R;
import com.ql.jcjr.activity.MainActivity;
import com.ql.jcjr.utils.LogUtil;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

public class MiPushActivity extends UmengNotifyClickActivity {

    private static String TAG = MiPushActivity.class.getName();

    private Handler handler;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LogUtil.i("---MiPushActivity  onCreate----");
//        setContentView(R.layout.activity_mipush);
        setContentView(R.layout.activity_welcome);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String body = (String)msg.obj;
                Intent goIntent = new Intent(MiPushActivity.this, MainActivity.class);
                goIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(null != body){
                    try{
                        JSONObject obj = new JSONObject(body);
                        JSONObject bodyObk = obj.optJSONObject("body");

                        Object customObj = bodyObk.get("custom");
                        String custom=null;
                        if (custom instanceof String){
                            custom=(String) customObj;
                        }else{
                            custom=customObj.toString();
                        }

                        goIntent.putExtra("push_tag",true);
                        goIntent.putExtra("push_msg",custom);
                        goIntent.putExtra("main_index",0);
                    }catch(Exception e){

                    }
                }
                startActivity(goIntent);
                finish();
            }
        };
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        final String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        LogUtil.i("---MiPushActivity  onCreate----"+body);

//        {
//            "msg_id": "us23814151433719427401",
//                "random_min": 0,
//                "display_type": "notification",
//                "body": {
//                    "ticker": "新闻公告",
//                    "title": "1123123",
//                    "text": "1123123",
//                    "icon": "xx",
//                    "largeIcon": "xx",
//                    "builder_id": 1,
//                    "play_vibrate": "true",
//                    "play_lights": "false",
//                    "play_sound": "true",
//                    "after_open": "go_custom",
//                    "custom": {
//                        "type": "xwgg",
//                        "value": 740
//                    }
//                }
//        }
//        UmLog.i(TAG, body);
        Message message = Message.obtain();
        message.obj = body;
        handler.sendMessageDelayed(message, 1000);
    }
}