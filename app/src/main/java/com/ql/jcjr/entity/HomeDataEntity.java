package com.ql.jcjr.entity;



public class HomeDataEntity {

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }


    public static class ResultBean {
        private ResultBeanOne result1;
        private ResultBeanTwo result2;
        private ResultBeanThree result3;

        public ResultBeanOne getResult1() {
            return result1;
        }

        public void setResult1(ResultBeanOne result1) {
            this.result1 = result1;
        }

        public ResultBeanTwo getResult2() {
            return result2;
        }

        public void setResult2(ResultBeanTwo result2) {
            this.result2 = result2;
        }

        public ResultBeanThree getResult3() {
            return result3;
        }

        public void setResult3(ResultBeanThree result3) {
            this.result3 = result3;
        }

        public class ResultBeanOne {
            private String code;
            private String msg;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }

        public class ResultBeanTwo {
            private String id;
            private String name;
            private String apr;
            private String time_limit_day;
            private String tender_times;
            private String isselled;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getApr() {
                return apr;
            }

            public void setApr(String apr) {
                this.apr = apr;
            }

            public String getTime_limit_day() {
                return time_limit_day;
            }

            public void setTime_limit_day(String time_limit_day) {
                this.time_limit_day = time_limit_day;
            }

            public String getTender_times() {
                return tender_times;
            }

            public void setTender_times(String tender_times) {
                this.tender_times = tender_times;
            }

            public String getIsselled() {
                return isselled;
            }

            public void setIsselled(String isselled) {
                this.isselled = isselled;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public class ResultBeanThree {
            private String account;
            private String count;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }
        }
    }
}
