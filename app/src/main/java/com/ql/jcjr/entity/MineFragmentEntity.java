package com.ql.jcjr.entity;

/**
 * ClassName: MineFragmentEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/12.
 */

public class MineFragmentEntity {


    /**
     * total : 328723.73
     * use_money : 175813.73
     * interest : 105
     * collection_interest1 : 15
     * username : 153****9675
     * realname : 张国生
     * headImgUrl : http://testing.jicaibaobao.com//data/avatar/19919_avatar_middle.jpg
     * cardId : 372901****5617
     * issetPay : 1
     */

    private ResultBean result;
    /**
     * result : {"total":"328723.73","use_money":"175813.73","interest":"105",
     * "collection_interest1":"15","username":"153****9675","realname":"张国生",
     * "headImgUrl":"http://testing.jicaibaobao.com//data/avatar/19919_avatar_middle.jpg",
     * "cardId":"372901****5617","issetPay":"1"}
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
        private String total;
        private String use_money;
        private String interest;
        private String collection_interest1;
        private String username;
        private String realname;
        private String headImgUrl;
        private String cardId;
        private String issetPay;//是否设置交易密码
        private String bank;
        private String rank;
        private String rankname;
        private String cashcount;
        private String isbindaddress;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUse_money() {
            return use_money;
        }

        public void setUse_money(String use_money) {
            this.use_money = use_money;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getCollection_interest1() {
            return collection_interest1;
        }

        public void setCollection_interest1(String collection_interest1) {
            this.collection_interest1 = collection_interest1;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getIssetPay() {
            return issetPay;
        }

        public void setIssetPay(String issetPay) {
            this.issetPay = issetPay;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRankname() {
            return rankname;
        }

        public void setRankname(String rankname) {
            this.rankname = rankname;
        }

        public String getCashcount() {
            return cashcount;
        }

        public void setCashcount(String cashcount) {
            this.cashcount = cashcount;
        }

        public String getIsbindaddress() {
            return isbindaddress;
        }

        public void setIsbindaddress(String isbindaddress) {
            this.isbindaddress = isbindaddress;
        }
    }
}
