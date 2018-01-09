package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: BannerEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/9/26.
 */

public class BannerEntity {

    /**
     * url :
     * pic : http://www.jicaibaobao
     * .comdata/upfiles/images/2017-09/11/11980_scrollpic_15051132406.jpg
     */

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String url;
        private String pic;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
