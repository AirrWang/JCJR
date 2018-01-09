package com.ql.jcjr.entity;

/**
 * ClassName: LoginEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/11.
 */

public class LoginEntity {

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
        private String user_id;

        public String getUserId() {
            return user_id;
        }

        public void setUserId(String user_id) {
            this.user_id = user_id;
        }
    }
}
