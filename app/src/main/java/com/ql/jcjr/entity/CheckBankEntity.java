package com.ql.jcjr.entity;

/**
 * ClassName: CheckBankEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/19.
 */

public class CheckBankEntity {


    /**
     * RSPCODE : 00
     * RSPMSG : 成功
     * result : {"status":"1","bankname":"招商银行","bankno":"621483******0956","branch":"高新支行",
     * "imgUrl":"http://testing.jicaibaobao
     * .com//themes/newfunc/newfont/bankcardbind/images/466.png","shortNo":"6","totalMoney":10000}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * status : 1
     * bankname : 招商银行
     * bankno : 621483******0956
     * branch : 高新支行
     * imgUrl : http://testing.jicaibaobao.com//themes/newfunc/newfont/bankcardbind/images/466.png
     * shortNo : 6
     * totalMoney : 10000
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
        private String status;
        private String bankname;
        private String bankno;
        private String branch;
        private String imgUrl;
        private String shortNo;
        private int totalMoney;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getBankno() {
            return bankno;
        }

        public void setBankno(String bankno) {
            this.bankno = bankno;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getShortNo() {
            return shortNo;
        }

        public void setShortNo(String shortNo) {
            this.shortNo = shortNo;
        }

        public int getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(int totalMoney) {
            this.totalMoney = totalMoney;
        }
    }
}
