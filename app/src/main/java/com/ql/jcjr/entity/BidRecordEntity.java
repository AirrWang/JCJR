package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: BidRecordEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/17.
 */

public class BidRecordEntity {


    /**
     * result : [{"status":"0","type":"月","capital":"111","interest":"1","number":"2017-10-09
     * 17:56:03_256","repay_account":"112"},{"status":"0","type":"天","capital":"5000",
     * "interest":"10.5","number":"2016-12-22 11:40:21_113","repay_account":"5010.5"},
     * {"status":"1","type":"天","capital":"5000","interest":"10.5","number":"2016-12-30
     * 13:52:09_117","repay_account":"5010.5"},{"status":"1","type":"月","capital":"100",
     * "interest":"0.9","number":"2017-01-09 13:17:27_141","repay_account":"100.9"},
     * {"status":"1","type":"月","capital":"100","interest":"0.9","number":"2016-12-16
     * 14:33:35_104","repay_account":"100.9"},{"status":"1","type":"月","capital":"100",
     * "interest":"0.9","number":"2016-11-07 15:06:24_29","repay_account":"100.9"},{"status":"1",
     * "type":"月","capital":"100","interest":"0.9","number":"2016-11-02 15:25:31_25",
     * "repay_account":"100.9"},{"status":"1","type":"月","capital":"100","interest":"0.9",
     * "number":"2016-11-01 17:03:31_24","repay_account":"100.9"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * status : 0
     * type : 月
     * capital : 111
     * interest : 1
     * number : 2017-10-09 17:56:03_256
     * repay_account : 112
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
        private String status;
        private String type;
        private String capital;
        private String interest;
        private String number;
        private String repay_account;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getRepay_account() {
            return repay_account;
        }

        public void setRepay_account(String repay_account) {
            this.repay_account = repay_account;
        }
    }
}
