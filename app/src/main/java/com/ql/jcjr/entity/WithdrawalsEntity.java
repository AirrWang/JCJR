package com.ql.jcjr.entity;



public class WithdrawalsEntity {


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
        private String fee;
        private String time;
        private String account;
        private String credited;


        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getCredited() {
            return credited;
        }

        public void setCredited(String credited) {
            this.credited = credited;
        }
    }
}
