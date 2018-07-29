package com.ql.jcjr.entity;

import java.util.List;

/**
 * ClassName: RedPacketEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/13.
 */

public class BidHistoryEntity {



    private String RSPCODE;
    private String RSPMSG;

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

        private List<ListBean> list;
        private String account;
        private String wait_interest;
        private String all_account;
        private String late_account;

        public List<ListBean> getList() {
            return list;
        }
        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getWait_interest() {
            return wait_interest;
        }

        public void setWait_interest(String wait_interest) {
            this.wait_interest = wait_interest;
        }

        public String getAll_account() {
            return all_account;
        }

        public void setAll_account(String all_account) {
            this.all_account = all_account;
        }

        public String getLate_account() {
            return late_account;
        }

        public void setLate_account(String late_account) {
            this.late_account = late_account;
        }

        public static class ListBean {
            private String borrow_id;
            private String account;
            private String name;
            private String end_time;
            private String status;
            private String interest;
            private String statusname;
            private String tender_id;
            private String overtime;

            public String getTender_id() {
                return tender_id;
            }
            public void setTender_id(String tender_id) {
                this.tender_id = tender_id;
            }
            public String getBorrow_id() {
                return borrow_id;
            }

            public void setBorrow_id(String borrow_id) {
                this.borrow_id = borrow_id;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getInterest() {
                return interest;
            }

            public void setInterest(String interest) {
                this.interest = interest;
            }

            public String getStatusname() {
                return statusname;
            }

            public void setStatusname(String statusname) {
                this.statusname = statusname;
            }

            public String getOvertime() {
                return overtime;
            }

            public void setOvertime(String overtime) {
                this.overtime = overtime;
            }
        }

    }
}
