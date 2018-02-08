package com.ql.jcjr.entity;

/**
 * ClassName: BankCardListEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/19.
 */

public class BankCardData {

//    private List<ResultBean> result;
//
//    public List<ResultBean> getResult() {
//        return result;
//    }
//
//    public void setResult(List<ResultBean> result) {
//        this.result = result;
//    }
//
//
//    public static class ResultBean {
        private String bankname;
        private String bankno;
        private String branch;
        private String imgUrl;
        private int totalMoney;


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

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }
//    }
}
