package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: TransRecordEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class WithdrawalsRecordEntity {

    /**
     * result : [{"datetime":"2017-10-16 14:09","status":"审核中","money":"100","bankinfo":"招商银行(6)
     * "},{"datetime":"2017-09-25 17:20","status":"审核中","money":"100","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-09-25 16:13","status":"审核中","money":"111","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-09-25 15:59","status":"审核中","money":"100","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-09-25 15:46","status":"审核中","money":"111","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-09-25 15:22","status":"审核中","money":"111","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-09-25 15:08","status":"审核中","money":"111","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-03-10 16:44","status":"银行处理中","money":"5110","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-03-05 22:18","status":"提现失败","money":"100","bankinfo":"招商银行(6)"},
     * {"datetime":"2017-02-19 00:23","status":"银行处理中","money":"202","bankinfo":"招商银行(6)"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * datetime : 2017-10-16 14:09
     * status : 审核中
     * money : 100
     * bankinfo : 招商银行(6)
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
        private String bankinfo;

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

        public String getBankinfo() {
            return bankinfo;
        }

        public void setBankinfo(String bankinfo) {
            this.bankinfo = bankinfo;
        }
    }
}
