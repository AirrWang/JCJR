package com.ql.jcjr.entity;


import java.util.List;

public class XSBEntity {

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


    public List<HomeDataEntity.ResultBean.ResultBeanTwo.BidBean> getResult() {
        return result;
    }

    public void setResult(List<HomeDataEntity.ResultBean.ResultBeanTwo.BidBean> result) {
        this.result = result;
    }
}
