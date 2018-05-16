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
        private int oneday;
        private String iscomplete;
        private String province;
        private String city;
        private String province_id;
        private String city_id;

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

        public int getOneday() {
            return oneday;
        }

        public void setOneday(int oneday) {
            this.oneday = oneday;
        }

        public String getIscomplete() {
            return iscomplete;
        }

        public void setIscomplete(String iscomplete) {
            this.iscomplete = iscomplete;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }
    }
}
