package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: RollNewsEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/11.
 */

public class RollNewsEntity {

    /**
     * result : [{"id":"707","name":"关于微信端快捷支付信用卡暂停公告"},{"id":"705","name":"关于春节假期结算工作调整公告"},
     * {"id":"704","name":"鸡年大吉，积财有礼\u2014\u2014 话费红包抢不停"},{"id":"702","name":"积财网银联支付正式开通"},
     * {"id":"700","name":"关于iphone7抽奖时间变更的公告"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * id : 707
     * name : 关于微信端快捷支付信用卡暂停公告
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
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
