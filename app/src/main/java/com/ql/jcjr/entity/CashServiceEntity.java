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
        private int min;
        private int max;
        private float available_amount;
        private float fee_apr;
        private List<String> cash_remind;

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

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public float getAvailable_amount() {
            return available_amount;
        }

        public void setAvailable_amount(float available_amount) {
            this.available_amount = available_amount;
        }

        public float getFee_apr() {
            return fee_apr;
        }

        public void setFee_apr(float fee_apr) {
            this.fee_apr = fee_apr;
        }

        public List<String> getCash_remind() {
            return cash_remind;
        }

        public void setCash_remind(List<String> cash_remind) {
            this.cash_remind = cash_remind;
        }
    }
}
