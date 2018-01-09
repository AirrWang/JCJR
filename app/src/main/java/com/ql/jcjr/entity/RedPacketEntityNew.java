package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: RedPacketEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class RedPacketEntityNew {


    /**
     * result : [
     * {"money":"8","lasttime":"2017-10-13 15:09","type":"投资红包","remark":"满100可用"},
     * {"money":"30","lasttime":"2017-10-13 15:09","type":"投资红包","remark":"满5000可用"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * money : 8
     * lasttime : 2017-10-13 15:09
     * type : 投资红包
     * remark : 满100可用
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

        private List<ListBean> list;
        private int num;

        public List<ListBean> getList() {
            return list;
        }
        public void setList(List<ListBean> list) {
            this.list = list;
        }
        public int getNum() {
            return num;
        }
        public void setNum(int num) {
            this.num = num;
        }

        public static class ListBean {
            private String cashid;
            private String lasttime;
            private String money;
            private String cashMoney;
            private String cashApr;
            private String type;
            private String quanbaoname;
            private String remark;
            private String status;
            private int use_day;

            public String getCashMoney() {
                return cashMoney;
            }

            public void setCashMoney(String cashMoney) {
                this.cashMoney = cashMoney;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCashApr() {
                return cashApr;
            }

            public void setCashApr(String cashApr) {
                this.cashApr = cashApr;
            }

            public String getCashid() {
                return cashid;
            }

            public void setCashid(String cashid) {
                this.cashid = cashid;
            }

            public String getLasttime() {
                return lasttime;
            }

            public void setLasttime(String lasttime) {
                this.lasttime = lasttime;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getQuanbaoname() {
                return quanbaoname;
            }

            public void setQuanbaoname(String quanbaoname) {
                this.quanbaoname = quanbaoname;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getUse_day() {
                return use_day;
            }

            public void setUse_day(int use_day) {
                this.use_day = use_day;
            }
        }

    }
}
