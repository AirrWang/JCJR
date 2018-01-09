package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: CashServiceEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/11.
 */

public class CashServiceEntity {

    /**
     * RSPCODE : 00
     * RSPMSG : 登录成功
     * result : {"user_id":"20363"}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * user_id : 20363
     */

    private ResultBean result;

    public String getRSPCODE() {
        return RSPCODE;
    }

    public void setRSPCODE(String RSPCODE) {
        this.RSPCODE = RSPCODE;
    }

    public String getRSPMSG() {
        return RSPMSG;
    }

    public void setRSPMSG(String RSPMSG) {
        this.RSPMSG = RSPMSG;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int last_num;
        private int fee;
        private List<String> remind;
        private int type;

        public int getLast_num() {
            return last_num;
        }

        public void setLast_num(int last_num) {
            this.last_num = last_num;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public List<String> getRemind() {
            return remind;
        }

        public void setRemind(List<String> remind) {
            this.remind = remind;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
