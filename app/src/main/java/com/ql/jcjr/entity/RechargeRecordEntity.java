package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: RechargeRecordEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/17.
 */

public class RechargeRecordEntity {


    /**
     * result : [{"datetime":"2017-09-25 17:16","status":"2","money":"5000.00",
     * "trade_no":"1506331007199192"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * datetime : 2017-09-25 17:16
     * status : 2
     * money : 5000.00
     * trade_no : 1506331007199192
     */

    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String datetime;
        private String status;
        private String money;
        private String trade_no;

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }
    }
}
