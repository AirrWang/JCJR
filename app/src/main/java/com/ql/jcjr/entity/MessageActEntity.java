package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: MessageCenterEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/25.
 */

public class MessageActEntity {


    /**
     * result : [{"id":"105351","name":"您的账户成功充值10000.00元",
     * "content":"您好，您已经于2017-10-12成功充值10000.00元,流水号:1507792104203631","addtime":"2017-10-12
     * 15:08","instr":"您好，您已经于2017-10-12成功充值10000.00元"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * id : 105351
     * name : 您的账户成功充值10000.00元
     * content : 您好，您已经于2017-10-12成功充值10000.00元,流水号:1507792104203631
     * addtime : 2017-10-12 15:08
     * instr : 您好，您已经于2017-10-12成功充值10000.00元
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
        private String content;
        private String litpic;
        private String addtime;
        private String share;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getLitpic() {
            return litpic;
        }

        public void setLitpic(String litpic) {
            this.litpic = litpic;
        }

        public String getShare() {
            return share;
        }

        public void setShare(String share) {
            this.share = share;
        }
    }
}
