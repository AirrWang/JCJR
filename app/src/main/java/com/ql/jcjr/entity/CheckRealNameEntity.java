package com.ql.jcjr.entity;

/**
 * ClassName: CheckRealNameEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/18.
 */

public class CheckRealNameEntity {


    /**
     * RSPCODE : 00
     * RSPMSG : 成功
     * result : {"status":"1"}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * status : 1
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
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
