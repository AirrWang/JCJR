package com.ql.jcjr.entity;

/**
 * ClassName: UploadPicEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/27.
 */

public class UploadPicEntity {


    /**
     * headImgUrl : http://testing.jicaibaobao.com//data/avatar/19919_avatar_middle.jpg
     */

    private ResultBean result;
    /**
     * result : {"headImgUrl":"http://testing.jicaibaobao.com//data/avatar/19919_avatar_middle.jpg"}
     * RSPCODE : 00
     * RSPMSG : 成功
     */

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

    public static class ResultBean {
        private String headImgUrl;

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }
    }
}
