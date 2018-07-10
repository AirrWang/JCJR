package com.ql.jcjr.entity;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: BidDetailEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/12.
 */

public class BidDetailEntity {
    /**
     * RSPCODE : 00
     * RSPMSG : 成功
     * result : {"id":"46","type":"xsb","apr":"18.00","is_lz":"1","isday":"1",
     * "time_limit_day":"3","time_limit":"1","lowest_account":"100.00","most_account":"10000.00",
     * "account":"50000.00","account_yes":"25000.00","is_mb":"0","style":"0","tender_times":"85",
     * "content":null,"award":"","part_account":"0.00","funds":"","pwd":"","scale":"50",
     * "surplus":"25000","repaytype":"到期自动回购","tenders":[{"account":"9100",
     * "username":"13868065499","addtime":"1479284045"}]}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * id : 46
     * type : xsb
     * apr : 18.00
     * is_lz : 1
     * isday : 1
     * time_limit_day : 3
     * time_limit : 1
     * lowest_account : 100.00
     * most_account : 10000.00
     * account : 50000.00
     * account_yes : 25000.00
     * is_mb : 0
     * style : 0
     * tender_times : 85
     * content : null
     * award :
     * part_account : 0.00
     * funds :
     * pwd :
     * scale : 50
     * surplus : 25000
     * repaytype : 到期自动回购
     * tenders : [{"account":"9100","username":"13868065499","addtime":"1479284045"}]
     * tenderNum : 6
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
        private String id;
        private String type;
        private String apr;
        private String aprOrigin;//原始年化
        private String cashAddition;//活动年化

        private String is_lz;
        private String isday;
        private String time_limit_day;
        private String time_limit;
        private int lowest_account;
        private int most_account;
        private int account;
        private String account_yes;
        private String is_mb;
        private String style;
        private String tender_times;
//        private Object content;
        private String award;
        private String part_account;
        private String funds;
        private String pwd;
        private String scale;
        private String surplus;
        private String repaytype;
        private int tenderNum;
        private String bremark;
        private String url;
        private String bremark1;
        private String url1;
        private int  lasttime;

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

        /**
         * account : 9100
         * username : 13868065499
         * addtime : 1479284045
         */



        private List<TendersBean> tenders;

        public int getTenderNum() {
            return tenderNum;
        }

        public void setTenderNum(int tenderNum) {
            this.tenderNum = tenderNum;
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

        public String getApr() {
            return apr;
        }

        public void setApr(String apr) {
            this.apr = apr;
        }

        public String getIs_lz() {
            return is_lz;
        }

        public void setIs_lz(String is_lz) {
            this.is_lz = is_lz;
        }

        public String getIsday() {
            return isday;
        }

        public void setIsday(String isday) {
            this.isday = isday;
        }

        public String getTime_limit_day() {
            return time_limit_day;
        }

        public void setTime_limit_day(String time_limit_day) {
            this.time_limit_day = time_limit_day;
        }

        public String getTime_limit() {
            return time_limit;
        }

        public void setTime_limit(String time_limit) {
            this.time_limit = time_limit;
        }



        public String getAccount_yes() {
            return account_yes;
        }

        public void setAccount_yes(String account_yes) {
            this.account_yes = account_yes;
        }

        public String getIs_mb() {
            return is_mb;
        }

        public void setIs_mb(String is_mb) {
            this.is_mb = is_mb;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getTender_times() {
            return tender_times;
        }

        public void setTender_times(String tender_times) {
            this.tender_times = tender_times;
        }

//        public Object getContent() {
//            return content;
//        }
//
//        public void setContent(Object content) {
//            this.content = content;
//        }

        public String getAward() {
            return award;
        }

        public void setAward(String award) {
            this.award = award;
        }

        public String getPart_account() {
            return part_account;
        }

        public void setPart_account(String part_account) {
            this.part_account = part_account;
        }

        public String getFunds() {
            return funds;
        }

        public void setFunds(String funds) {
            this.funds = funds;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
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

        public String getRepaytype() {
            return repaytype;
        }

        public void setRepaytype(String repaytype) {
            this.repaytype = repaytype;
        }

        public List<TendersBean> getTenders() {
            return tenders;
        }

        public void setTenders(List<TendersBean> tenders) {
            this.tenders = tenders;
        }

        public String getBremark() {
            return bremark;
        }

        public void setBremark(String bremark) {
            this.bremark = bremark;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBremark1() {
            return bremark1;
        }

        public void setBremark1(String bremark1) {
            this.bremark1 = bremark1;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public int getLasttime() {
            return lasttime;
        }

        public void setLasttime(int lasttime) {
            this.lasttime = lasttime;
        }

        public int getAccount() {
            return account;
        }

        public void setAccount(int account) {
            this.account = account;
        }

        public int getMost_account() {
            return most_account;
        }

        public void setMost_account(int most_account) {
            this.most_account = most_account;
        }

        public int getLowest_account() {
            return lowest_account;
        }

        public void setLowest_account(int lowest_account) {
            this.lowest_account = lowest_account;
        }

        public static class TendersBean implements Serializable{
            private String account;
            private String username;
            private String addtime;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }
    }
}
