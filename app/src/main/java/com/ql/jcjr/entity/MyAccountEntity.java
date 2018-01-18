package com.ql.jcjr.entity;

/**
 * ClassName: MyAccountEntity
 * Description:
 * Author: Administrator
 */

public class MyAccountEntity {


    private ResultBean result;


    private String RSPCODE;
    private String RSPMSG;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean {
        private String total;
        private String allInterest;
        private String interest;
        private String use_money;
        private String tenderFrozen;
        private String cashFrozen;
        private String interestGet;
        private String awards;
        private String capital;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getAllInterest() {
            return allInterest;
        }

        public void setAllInterest(String allInterest) {
            this.allInterest = allInterest;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getUse_money() {
            return use_money;
        }

        public void setUse_money(String use_money) {
            this.use_money = use_money;
        }

        public String getTenderFrozen() {
            return tenderFrozen;
        }

        public void setTenderFrozen(String tenderFrozen) {
            this.tenderFrozen = tenderFrozen;
        }

        public String getCashFrozen() {
            return cashFrozen;
        }

        public void setCashFrozen(String cashFrozen) {
            this.cashFrozen = cashFrozen;
        }

        public String getInterestGet() {
            return interestGet;
        }

        public void setInterestGet(String interestGet) {
            this.interestGet = interestGet;
        }

        public String getAwards() {
            return awards;
        }

        public void setAwards(String awards) {
            this.awards = awards;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }
    }
}
