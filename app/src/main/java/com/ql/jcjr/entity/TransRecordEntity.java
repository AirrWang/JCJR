package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: TransRecordEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class TransRecordEntity {

    /**
     * result : [{"remark":"鐢ㄦ埛鎻愮幇鐢宠","money":"100","addtime":"2017-10-16 14:09:11","type":""}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * remark : 鐢ㄦ埛鎻愮幇鐢宠
     * money : 100
     * addtime : 2017-10-16 14:09:11
     * type :
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
        private String remark;
        private String money;
        private String addtime;
        private String type;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
