package com.ql.jcjr.entity;

/**
 * ClassName: BidDetailEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/12.
 */

public class BidDefaultHbEntity {

    private String RSPCODE;
    private String RSPMSG;

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
        private String cashid;
        private String money;
        private String cashApr;
        private String type;

        public String getCashApr() {
            return cashApr;
        }

        public void setCashApr(String cashApr) {
            this.cashApr = cashApr;
        }

        public String getCashid() {
            return cashid;
        }

        public void setCashid(String cashid) {
            this.cashid = cashid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
