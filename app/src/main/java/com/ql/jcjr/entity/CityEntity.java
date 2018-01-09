package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: CityEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/19.
 */

public class CityEntity {


    /**
     * result : [{"name":"长沙","id":"2035"},{"name":"娄底","id":"2168"},{"name":"怀化","id":"2154"},
     * {"name":"永州","id":"2141"},{"name":"郴州","id":"2128"},{"name":"益阳","id":"2120"},
     * {"name":"张家界","id":"2114"},{"name":"常德","id":"2103"},{"name":"岳阳","id":"2092"},
     * {"name":"邵阳","id":"2078"},{"name":"衡阳","id":"2064"},{"name":"湘潭","id":"2057"},
     * {"name":"株洲","id":"2046"},{"name":"湘西自治州","id":"2175"}]
     * RSPCODE : 00
     * RSPMSG : 成功
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * name : 长沙
     * id : 2035
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
