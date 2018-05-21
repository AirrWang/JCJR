package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: BidListEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/9/27.
 */

public class BidListEntity {


    /**
     * RSPCODE : 00
     * RSPMSG : 成功
     * result : [{"id":"259","type":"","status":"1","award":"0","account":1000,"account_yes":300,
     * "lowest_account":"50.00","name":"存管测试1",,"apr":"12.00","isday":"0","time_limit":"1",
     * "end_time":"0","time_limit_day":"0","is_lz":"0","scales":"0.300000","biaoindex":"1",
     * "px":"5","account_format":"1,000","scale":"30.00","surplus":"700.00"},{"id":"258",
     * "type":"","status":"1","award":"0","account":1000,"account_yes":0,
     * "lowest_account":"50.00","name":"联系我们","apr":"12.00","isday":"0","time_limit":"1",
     * "end_time":"0","time_limit_day":"0","is_lz":"0","scales":"0.000000","biaoindex":"1",
     * "px":"5","account_format":"1,000","scale":"0.00","surplus":"1,000.00"}]
     * "cashAddition":"1.20"
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * id : 259
     * type :
     * status : 1
     * award : 0
     * account : 1000
     * account_yes : 300
     * lowest_account : 50.00
     * name : 存管测试1
     * apr : 12.00
     * isday : 0
     * time_limit : 1
     * end_time : 0
     * time_limit_day : 0
     * is_lz : 0
     * scales : 0.300000
     * biaoindex : 1
     * px : 5
     * account_format : 1,000
     * scale : 30.00
     * surplus : 700.00
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
        private String id;
        private String type;
        private String status;
        private String award;
        private int account;
        private int account_yes;
        private String lowest_account;
        private String name;
        private String apr;//总和年化
        private String aprOrigin;//原始年化
        private String cashAddition;//活动年化
        private String isday;
        private String time_limit;
        private String end_time;
        private String time_limit_day;
        private String is_lz;
        private String scales;
        private String biaoindex;
        private String px;
        private String account_format;
        private String scale;
        private String surplus;
        private String bremark;
        private String most_account;
        private String bremark1;


        public String getBremark() {
            return bremark;
        }

        public void setBremark(String bremark) {
            this.bremark = bremark;
        }

        private String pwd;

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getAprOrigin() {
            return aprOrigin;
        }

        public void setAprOrigin(String aprOrigin) {
            this.aprOrigin = aprOrigin;
        }

        public String getCashAddition() {
            return cashAddition;
        }

        public void setCashAddition(String cashAddition) {
            this.cashAddition = cashAddition;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAward() {
            return award;
        }

        public void setAward(String award) {
            this.award = award;
        }

        public int getAccount() {
            return account;
        }

        public void setAccount(int account) {
            this.account = account;
        }

        public int getAccount_yes() {
            return account_yes;
        }

        public void setAccount_yes(int account_yes) {
            this.account_yes = account_yes;
        }

        public String getLowest_account() {
            return lowest_account;
        }

        public void setLowest_account(String lowest_account) {
            this.lowest_account = lowest_account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApr() {
            return apr;
        }

        public void setApr(String apr) {
            this.apr = apr;
        }

        public String getIsday() {
            return isday;
        }

        public void setIsday(String isday) {
            this.isday = isday;
        }

        public String getTime_limit() {
            return time_limit;
        }

        public void setTime_limit(String time_limit) {
            this.time_limit = time_limit;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getTime_limit_day() {
            return time_limit_day;
        }

        public void setTime_limit_day(String time_limit_day) {
            this.time_limit_day = time_limit_day;
        }

        public String getIs_lz() {
            return is_lz;
        }

        public void setIs_lz(String is_lz) {
            this.is_lz = is_lz;
        }

        public String getScales() {
            return scales;
        }

        public void setScales(String scales) {
            this.scales = scales;
        }

        public String getBiaoindex() {
            return biaoindex;
        }

        public void setBiaoindex(String biaoindex) {
            this.biaoindex = biaoindex;
        }

        public String getPx() {
            return px;
        }

        public void setPx(String px) {
            this.px = px;
        }

        public String getAccount_format() {
            return account_format;
        }

        public void setAccount_format(String account_format) {
            this.account_format = account_format;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public String getSurplus() {
            return surplus;
        }

        public void setSurplus(String surplus) {
            this.surplus = surplus;
        }

        public String getMost_account() {
            return most_account;
        }

        public void setMost_account(String most_account) {
            this.most_account = most_account;
        }

        public String getBremark1() {
            return bremark1;
        }

        public void setBremark1(String bremark1) {
            this.bremark1 = bremark1;
        }
    }
}
