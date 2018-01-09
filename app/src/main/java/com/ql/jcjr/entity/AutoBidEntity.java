package com.ql.jcjr.entity;

/**
 * ClassName: AutoBidEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/17.
 */

public class AutoBidEntity {

    /**
     * id :
     * monthSign : 0
     * daySign : 0
     * monthfirst :
     * monthlast :
     * dayfirst :
     * daylast :
     * status : 1
     * tender_account :
     */

    private ResultBean result;
    /**
     * result : {"id":"","monthSign":"0","daySign":"0","monthfirst":"","monthlast":"",
     * "dayfirst":"","daylast":"","status":"1","tender_account":""}
     * RSPCODE : 00
     * RSPMSG : 成功
     * status : 1
     */

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
        private String id;
        private String monthSign;
        private String daySign;
        private String monthfirst;
        private String monthlast;
        private String dayfirst;
        private String daylast;
        private String status;
        private String tender_account;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMonthSign() {
            return monthSign;
        }

        public void setMonthSign(String monthSign) {
            this.monthSign = monthSign;
        }

        public String getDaySign() {
            return daySign;
        }

        public void setDaySign(String daySign) {
            this.daySign = daySign;
        }

        public String getMonthfirst() {
            return monthfirst;
        }

        public void setMonthfirst(String monthfirst) {
            this.monthfirst = monthfirst;
        }

        public String getMonthlast() {
            return monthlast;
        }

        public void setMonthlast(String monthlast) {
            this.monthlast = monthlast;
        }

        public String getDayfirst() {
            return dayfirst;
        }

        public void setDayfirst(String dayfirst) {
            this.dayfirst = dayfirst;
        }

        public String getDaylast() {
            return daylast;
        }

        public void setDaylast(String daylast) {
            this.daylast = daylast;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTender_account() {
            return tender_account;
        }

        public void setTender_account(String tender_account) {
            this.tender_account = tender_account;
        }
    }
}
