package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: BankListEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/18.
 */

public class BankListEntity {

    /**
     * result : [{"name":"中国工商银行","id":"300"},{"name":"中国农业银行","id":"303"},{"name":"中国银行",
     * "id":"301"},{"name":"中国建设银行","id":"302"},{"name":"交通银行","id":"463"},{"name":"中国邮政储蓄银行",
     * "id":"538"},{"name":"招商银行","id":"466"},{"name":"广发银行","id":"465"},{"name":"平安银行",
     * "id":"467"},{"name":"广东发展银行","id":"577"},{"name":"中国农业银行","id":"569"},{"name":"中国银行",
     * "id":"570"},{"name":"杭州银行","id":"566"},{"name":"中国建设银行","id":"571"},{"name":"交通银行",
     * "id":"572"},{"name":"中信银行","id":"573"},{"name":"中国光大银行","id":"574"},{"name":"华夏银行",
     * "id":"575"},{"name":"平安银行股份有限公司","id":"578"},{"name":"中国工商银行","id":"568"},{"name":"招商银行",
     * "id":"567"},{"name":"兴业银行","id":"579"},{"name":"上海浦东发展银行","id":"580"},{"name":"中国民生银行",
     * "id":"576"},{"name":"民生银行","id":"469"},{"name":"华夏银行","id":"470"},{"name":"上海浦东发展银行",
     * "id":"471"},{"name":"中信银行","id":"472"},{"name":"中国光大银行","id":"473"},{"name":"恒丰银行",
     * "id":"555"},{"name":"兴业银行","id":"468"},{"name":"杭州银行","id":"561"},{"name":"宁波银行",
     * "id":"531"},{"name":"上海银行","id":"560"},{"name":"北京银行","id":"554"},{"name":"深圳发展银行",
     * "id":"556"},{"name":"广州银行","id":"557"},{"name":"天津银行","id":"559"},{"name":"温州市商业银行",
     * "id":"558"},{"name":"渤海银行","id":"562"},{"name":"城市信用合作社","id":"564"},{"name":"农村信用合作社",
     * "id":"565"},{"name":"农村合作银行","id":"563"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * name : 中国工商银行
     * id : 300
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
        private String name;
        private String id;
        private String imgUrl;
        private String oneorder;
        private String oneday;
        private String order;
        private String onemonth;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getOneorder() {
            return oneorder;
        }

        public void setOneorder(String oneorder) {
            this.oneorder = oneorder;
        }

        public String getOneday() {
            return oneday;
        }

        public void setOneday(String oneday) {
            this.oneday = oneday;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getOnemonth() {
            return onemonth;
        }

        public void setOnemonth(String onemonth) {
            this.onemonth = onemonth;
        }
    }
}
