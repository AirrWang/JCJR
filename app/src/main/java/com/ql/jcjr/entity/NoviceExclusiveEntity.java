package com.ql.jcjr.entity;

/**
 * ClassName: NoviceExclusiveEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/11.
 */

public class NoviceExclusiveEntity {


    /**
     * RSPCODE : 00
     * RSPMSG : 成功
     * result : {"id":"257","name":null,"apr":"10.80","isday":"1","borrowTime":"7天",
     * "lowestaccount":"5000.00"}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * id : 257
     * name : null
     * apr : 10.80
     * isday : 1
     * borrowTime : 7天
     * lowestaccount : 5000.00
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
        private Object name;
        private String apr;
        private String isday;
        private String borrowTime;
        private String lowestaccount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
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

        public String getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }

        public String getLowestaccount() {
            return lowestaccount;
        }

        public void setLowestaccount(String lowestaccount) {
            this.lowestaccount = lowestaccount;
        }
    }
}
