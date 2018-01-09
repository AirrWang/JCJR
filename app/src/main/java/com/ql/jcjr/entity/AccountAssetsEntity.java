package com.ql.jcjr.entity;

/**
 * ClassName: AccountAssetsEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class AccountAssetsEntity {

    /**
     * total : 10000
     * use_money : 10000.00
     * current_money :
     * interest : 0
     * collection : 0
     * no_use_money : 0
     */

    private ResultBean result;
    /**
     * result : {"total":"10000","use_money":"10000.00","current_money":"","interest":"0",
     * "collection":"0","no_use_money":"0"}
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        private String total;
        private String use_money;
        private String current_money;
        private String interest;
        private String collection;
        private String no_use_money;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUse_money() {
            return use_money;
        }

        public void setUse_money(String use_money) {
            this.use_money = use_money;
        }

        public String getCurrent_money() {
            return current_money;
        }

        public void setCurrent_money(String current_money) {
            this.current_money = current_money;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public String getNo_use_money() {
            return no_use_money;
        }

        public void setNo_use_money(String no_use_money) {
            this.no_use_money = no_use_money;
        }
    }
}
