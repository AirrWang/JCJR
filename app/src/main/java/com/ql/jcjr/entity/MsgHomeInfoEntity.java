package com.ql.jcjr.entity;

import java.io.Serializable;

/**
 * ClassName: AutoBidEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/17.
 */

public class MsgHomeInfoEntity {


    private ResultBean result;
    private String RSPCODE;
    private String RSPMSG;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean implements Serializable{

        private StatusBean message;
        private StatusBean active;
        private StatusBean gonggao;

        public StatusBean getMessage() {
            return message;
        }

        public void setMessage(StatusBean message) {
            this.message = message;
        }

        public StatusBean getActive() {
            return active;
        }

        public void setActive(StatusBean active) {
            this.active = active;
        }

        public StatusBean getGonggao() {
            return gonggao;
        }

        public void setGonggao(StatusBean gonggao) {
            this.gonggao = gonggao;
        }

        public static class StatusBean {
            private String num;
            private String name;

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
