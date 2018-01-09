package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: ProvinceEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/19.
 */

public class ProvinceEntity {


    /**
     * result : [{"name":"北京","id":"1"},{"name":"湖南省","id":"2034"},{"name":"广东省","id":"2184"},
     * {"name":"广西区","id":"2403"},{"name":"海南省","id":"2541"},{"name":"四川省","id":"2570"},
     * {"name":"贵州省","id":"2791"},{"name":"云南省","id":"2892"},{"name":"西藏区","id":"3046"},
     * {"name":"陕西省","id":"3128"},{"name":"甘肃省","id":"3256"},{"name":"青海省","id":"3369"},
     * {"name":"宁夏区","id":"3422"},{"name":"新疆区","id":"3454"},{"name":"台湾省","id":"3571"},
     * {"name":"香港特区","id":"3573"},{"name":"湖北省","id":"1905"},{"name":"河南省","id":"1711"},
     * {"name":"天津","id":"21"},{"name":"上海","id":"40"},{"name":"重庆","id":"61"},{"name":"河北省",
     * "id":"102"},{"name":"山西省","id":"297"},{"name":"内蒙古区","id":"439"},{"name":"辽宁省",
     * "id":"561"},{"name":"吉林省","id":"690"},{"name":"黑龙江省","id":"768"},{"name":"江苏省",
     * "id":"924"},{"name":"安徽省","id":"1170"},{"name":"福建省","id":"1310"},{"name":"江西省",
     * "id":"1414"},{"name":"山东省","id":"1536"},{"name":"澳门特区","id":"3575"},{"name":"浙江省",
     * "id":"1057"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * name : 北京
     * id : 1
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
    }
}
