package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: RedPacketEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class RedPacketEntity {


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
        private String money;
        private String lasttime;
        private String type;
        private String remark;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getLasttime() {
            return lasttime;
        }

        public void setLasttime(String lasttime) {
            this.lasttime = lasttime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
