package com.ql.jcjr.entity;

/**
 * ClassName: BidDetailEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/12.
 */

public class BidHistoryDetailEntity {

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

        private String bianhao;
        private String nianhua;
        private String qixian;
        private String huankuan;
        private String benjin;
        private String shouyi;
        private String daoqi;
        private String chenggong;
        private String jixi;
        private String huikuan;
        private int xieyi;
        private String hongbao;

        public String getHongbao() {
            return hongbao;
        }

        public void setHongbao(String hongbao) {
            this.hongbao = hongbao;
        }

        public int getXieyi() {
            return xieyi;
        }

        public void setXieyi(int xieyi) {
            this.xieyi = xieyi;
        }

        public String getBianhao() {
            return bianhao;
        }

        public void setBianhao(String bianhao) {
            this.bianhao = bianhao;
        }

        public String getNianhua() {
            return nianhua;
        }

        public void setNianhua(String nianhua) {
            this.nianhua = nianhua;
        }

        public String getQixian() {
            return qixian;
        }

        public void setQixian(String qixian) {
            this.qixian = qixian;
        }

        public String getHuankuan() {
            return huankuan;
        }

        public void setHuankuan(String huankuan) {
            this.huankuan = huankuan;
        }

        public String getBenjin() {
            return benjin;
        }

        public void setBenjin(String benjin) {
            this.benjin = benjin;
        }

        public String getShouyi() {
            return shouyi;
        }

        public void setShouyi(String shouyi) {
            this.shouyi = shouyi;
        }

        public String getDaoqi() {
            return daoqi;
        }

        public void setDaoqi(String daoqi) {
            this.daoqi = daoqi;
        }

        public String getChenggong() {
            return chenggong;
        }

        public void setChenggong(String chenggong) {
            this.chenggong = chenggong;
        }

        public String getJixi() {
            return jixi;
        }

        public void setJixi(String jixi) {
            this.jixi = jixi;
        }

        public String getHuikuan() {
            return huikuan;
        }

        public void setHuikuan(String huikuan) {
            this.huikuan = huikuan;
        }
    }
}
