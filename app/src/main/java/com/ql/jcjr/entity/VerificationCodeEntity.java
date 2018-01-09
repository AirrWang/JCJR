package com.ql.jcjr.entity;

/**
 * ClassName: VerificationCodeEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/9/18.
 */

public class VerificationCodeEntity {

    /**
     * message : 2
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
