package com.ql.jcjr.entity;


import java.util.List;

public class AdvanceEntity {

    /**
     * RSPCODE : 00
     * RSPMSG : 登录成功
     * result : {"user_id":"20363"}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * user_id : 20363
     */

    private List<HomeDataEntity.ResultBean.ResultBeanTwo.BidBean> result;

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

    private ResultBean1 result1;
    private List<ResultBean2> result2;

    public ResultBean1 getResult1() {
        return result1;
    }

    public void setResult1(ResultBean1 result1) {
        this.result1 = result1;
    }

    public List<ResultBean2> getResult2() {
        return result2;
    }

    public void setResult2(List<ResultBean2> result2) {
        this.result2 = result2;
    }


    public static class ResultBean1 {
        private String account;
        private String dianfuaccount;
        private String lateaccount;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getDianfuaccount() {
            return dianfuaccount;
        }

        public void setDianfuaccount(String dianfuaccount) {
            this.dianfuaccount = dianfuaccount;
        }

        public String getLateaccount() {
            return lateaccount;
        }

        public void setLateaccount(String lateaccount) {
            this.lateaccount = lateaccount;
        }
    }

    public class ResultBean2 {
        private String addtime;
        private String account;

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
