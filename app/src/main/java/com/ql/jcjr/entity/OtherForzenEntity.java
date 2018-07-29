package com.ql.jcjr.entity;


import java.util.List;

public class OtherForzenEntity {

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

    private String last_freeze_money;
    private List<AdvanceEntity.ResultBean2> result;


    public String getLast_freeze_money() {
        return last_freeze_money;
    }

    public void setLast_freeze_money(String last_freeze_money) {
        this.last_freeze_money = last_freeze_money;
    }

    public List<AdvanceEntity.ResultBean2> getResult() {
        return result;
    }

    public void setResult(List<AdvanceEntity.ResultBean2> result) {
        this.result = result;
    }
}
