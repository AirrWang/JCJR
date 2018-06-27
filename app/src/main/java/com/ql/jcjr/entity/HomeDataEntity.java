package com.ql.jcjr.entity;


import java.util.List;

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
        private List<ResultBeanFour> result4;

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

        public List<ResultBeanFour> getResult4() {
            return result4;
        }

        public void setResult4(List<ResultBeanFour> result4) {
            this.result4 = result4;
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
            private List<BidBean> xinshou;
            private List<BidBean> huodong;
            private List<BidBean> tuijian;

            public List<BidBean> getXinshou() {
                return xinshou;
            }

            public void setXinshou(List<BidBean> xinshou) {
                this.xinshou = xinshou;
            }

            public List<BidBean> getHuodong() {
                return huodong;
            }

            public void setHuodong(List<BidBean> huodong) {
                this.huodong = huodong;
            }

            public List<BidBean> getTuijian() {
                return tuijian;
            }

            public void setTuijian(List<BidBean> tuijian) {
                this.tuijian = tuijian;
            }

            public class BidBean {
                private String id;
                private String apr;
                private String cashAddition;
                private String tender_times;
                private String time_limit_day;
                private String most_account;
                private String name;
                private int account; //项目总额
                private int account_yes; //已投金额
                private String bremark;
                private String bremark1;
                private String last_account;//剩余可投

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

                public String getCashAddition() {
                    return cashAddition;
                }

                public void setCashAddition(String cashAddition) {
                    this.cashAddition = cashAddition;
                }

                public String getTender_times() {
                    return tender_times;
                }

                public void setTender_times(String tender_times) {
                    this.tender_times = tender_times;
                }

                public String getTime_limit_day() {
                    return time_limit_day;
                }

                public void setTime_limit_day(String time_limit_day) {
                    this.time_limit_day = time_limit_day;
                }

                public String getMost_account() {
                    return most_account;
                }

                public void setMost_account(String most_account) {
                    this.most_account = most_account;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getAccount() {
                    return account;
                }

                public void setAccount(int account) {
                    this.account = account;
                }

                public int getAccount_yes() {
                    return account_yes;
                }

                public void setAccount_yes(int account_yes) {
                    this.account_yes = account_yes;
                }

                public String getBremark() {
                    return bremark;
                }

                public void setBremark(String bremark) {
                    this.bremark = bremark;
                }

                public String getBremark1() {
                    return bremark1;
                }

                public void setBremark1(String bremark1) {
                    this.bremark1 = bremark1;
                }

                public String getLast_account() {
                    return last_account;
                }

                public void setLast_account(String last_account) {
                    this.last_account = last_account;
                }
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

        public class ResultBeanFour {
                private String title;
                private String link;
                private String pic;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

        }
    }
}
