package com.ql.jcjr.timer;

import android.os.Handler;
import android.os.Message;

import com.ql.jcjr.constant.Global;
import com.ql.jcjr.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ClassName: TimerHandler
 * Description:
 * Author: Administrator
 * Date: Created on 202017/8/14.
 */

public class TimerHandler extends Handler {

    private int mSecond;
    private Timer timer;
    private TimerTask task;

    public TimerHandler(int second) {
        mSecond = second;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Global.WHATE_TIMER_TASK:
                LogUtil.i("TimerHandler  " + mSecond);
                mTimerListener.timing(mSecond);

                if (0 == mSecond) {
                    stopTask();
                    mTimerListener.finishTimer();
                    unRegisteITimerListener();

                }
                break;
        }
    }

    /**
     * 停止计时器
     */
    public void stopTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
        mSecond = 60;
    }

    /**
     * 开始任务
     */
    public void startTask() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    mSecond--;
                    Message message = new Message();
                    message.what = Global.WHATE_TIMER_TASK;
                    sendMessage(message);
                }
            };
        }
        if (timer != null && task != null) {
            timer.schedule(task, 0, 1000);
        }
        mTimerListener.startTimer();
    }


    private ITimerListener mTimerListener;

    public interface ITimerListener {
        void startTimer();
        void timing(int second);
        void finishTimer();
    }
    public void setITimerListener(ITimerListener TimerListener) {
        mTimerListener = TimerListener;
    }

    public void unRegisteITimerListener() {
        if (mTimerListener != null) {
            mTimerListener = null;
        }
    }
}
