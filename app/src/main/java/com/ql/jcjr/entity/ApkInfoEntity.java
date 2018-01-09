package com.ql.jcjr.entity;

/**
 * ClassName: LoginEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/11.
 */

public class ApkInfoEntity {

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
        private String version_name;
        private int version_code;
        private String version_desc;
        private String download_url;
        private int necessity;
        private String icon_url;
        private String home_url;
        private String share_title;
        private String share_content;

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            try{
                this.version_code = Integer.parseInt(version_code);
            }catch(Exception e){
                this.version_code = 1;
            }
        }

        public String getVersion_desc() {
            return version_desc;
        }

        public void setVersion_desc(String version_desc) {
            this.version_desc = version_desc;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public int getNecessity() {
            return necessity;
        }

        public void setNecessity(String necessity) {
            try{
                this.necessity = Integer.parseInt(necessity);
            }catch(Exception e){
                this.necessity = 0;
            }
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getHome_url() {
            return home_url;
        }

        public void setHome_url(String home_url) {
            this.home_url = home_url;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_content() {
            return share_content;
        }

        public void setShare_content(String share_content) {
            this.share_content = share_content;
        }
    }
}
