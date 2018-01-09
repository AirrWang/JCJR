package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: ReceiptDetailsEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class ReceiptDetailsEntity {

    /**
     * result : [{"number":"20170211_190","repay_time":"2017-03-19","repay_account":"5010.5"},
     * {"number":"20170211_190","repay_time":"2017-03-19","repay_account":"10021"},
     * {"number":"20170211_190","repay_time":"2017-03-19","repay_account":"10021"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * number : 20170211_190
     * repay_time : 2017-03-19
     * repay_account : 5010.5
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
        private String number;
        private String repay_time;
        private String repay_account;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getRepay_time() {
            return repay_time;
        }

        public void setRepay_time(String repay_time) {
            this.repay_time = repay_time;
        }

        public String getRepay_account() {
            return repay_account;
        }

        public void setRepay_account(String repay_account) {
            this.repay_account = repay_account;
        }
    }
}
