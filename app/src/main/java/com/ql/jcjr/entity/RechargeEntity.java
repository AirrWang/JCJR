package com.ql.jcjr.entity;

/**
 * ClassName: RechargeEntity
 * Description:
 * Author: Administrator
 * Date: Created on 202017/10/23.
 */

public class RechargeEntity {


    /**
     * RSPCODE : 00
     * RSPMSG : 成功
     * result : {"fuioupayurl":"https://mpay.fuiou.com:16128/h5pay/payAction.pay","ENCTP":"1",
     * "VERSION":"2.0","MCHNTCD":"0003310F0398811","LOGOTP":"0",
     * "FM":"ha7tzFxRSAjF+kFTBI+/M9ka1qjGcHuYP9pBGTJXhpIypCegEubuR2sEJqIawt3D
     * +FnHhQvDPRkfzhuS4x6FpPgEpp/VvF77cb2D6AOSFWX4j0oZMzUHxqKpWBjGCbOPcRbKlozxFwuDmEklrQOfeqMX55
     * /gxCVPxp90OlViSxCiFmHvXqsaZvFimMupKVlmEnRF2XU0cv9Lg71C
     * +TJcrTj9aOXkhsBNfImRdFQFQmr7tAi9YJlrnJKQQGiHGO7k4bKI2UTPDabKjTVYEIey6GItYn
     * /s923XdUYD7hIIRQ90h5OCWMwlhR5l74nnXJzKLWZhhvSYh6kU39wNiAaH0Q3luvBjRQwzkMTdKmhNdOcx4byTMLrEtRWeUSip+U1j4mSCgh+OaEA+M6nYAr/pyznay/LzTVMzq52YBNxAu7GiU9v7kLzit5jHP6ui/WjCt9a6Z8w6709xN5RGlrdPuF0Tvlsnz9udAoGY0jkU4urNFiqDkz/ELTcJnLm3kpwtc4o6hGgMc2kGID1EQBCDeumnO4THIKSrdoHWG7KFzVXJrA3/zivSpKH6Smckf53tkXumFOWNAn+gd4IqqB6ZeCy6J1wQCpUQiRp2vo5bo/0C8aQHNjDkBupf2NCX9/T+W7+OZgnff0mfHsPDLe9L2mhrbXy72Nc6GZQeJdk4UnrRJ0dMD/hYn8HUsz7KlacnaNGsFgp64vE0YSdd/t0Ba/n5l7cAbQiJN9QUQ1rmDME6z3hDuDvw3tbhxJjPUB8UdjDLBDJ1cvahLpjZR+YhZogkTBrlWWBpWg/gMEq7mVCS9b+l0ZIY19XTDOxZ+uHPxc/XKbyPn7yBWVPnNssqYbp1Kn+k/hU9qbkHiDTMAP8="}
     */

    private String RSPCODE;
    private String RSPMSG;
    /**
     * fuioupayurl : https://mpay.fuiou.com:16128/h5pay/payAction.pay
     * ENCTP : 1
     * VERSION : 2.0
     * MCHNTCD : 0003310F0398811
     * LOGOTP : 0
     * FM : ha7tzFxRSAjF+kFTBI+/M9ka1qjGcHuYP9pBGTJXhpIypCegEubuR2sEJqIawt3D
     * +FnHhQvDPRkfzhuS4x6FpPgEpp/VvF77cb2D6AOSFWX4j0oZMzUHxqKpWBjGCbOPcRbKlozxFwuDmEklrQOfeqMX55
     * /gxCVPxp90OlViSxCiFmHvXqsaZvFimMupKVlmEnRF2XU0cv9Lg71C
     * +TJcrTj9aOXkhsBNfImRdFQFQmr7tAi9YJlrnJKQQGiHGO7k4bKI2UTPDabKjTVYEIey6GItYn
     * /s923XdUYD7hIIRQ90h5OCWMwlhR5l74nnXJzKLWZhhvSYh6kU39wNiAaH0Q3luvBjRQwzkMTdKmhNdOcx4byTMLrEtRWeUSip+U1j4mSCgh+OaEA+M6nYAr/pyznay/LzTVMzq52YBNxAu7GiU9v7kLzit5jHP6ui/WjCt9a6Z8w6709xN5RGlrdPuF0Tvlsnz9udAoGY0jkU4urNFiqDkz/ELTcJnLm3kpwtc4o6hGgMc2kGID1EQBCDeumnO4THIKSrdoHWG7KFzVXJrA3/zivSpKH6Smckf53tkXumFOWNAn+gd4IqqB6ZeCy6J1wQCpUQiRp2vo5bo/0C8aQHNjDkBupf2NCX9/T+W7+OZgnff0mfHsPDLe9L2mhrbXy72Nc6GZQeJdk4UnrRJ0dMD/hYn8HUsz7KlacnaNGsFgp64vE0YSdd/t0Ba/n5l7cAbQiJN9QUQ1rmDME6z3hDuDvw3tbhxJjPUB8UdjDLBDJ1cvahLpjZR+YhZogkTBrlWWBpWg/gMEq7mVCS9b+l0ZIY19XTDOxZ+uHPxc/XKbyPn7yBWVPnNssqYbp1Kn+k/hU9qbkHiDTMAP8=
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
        private String fuioupayurl;
        private String ENCTP;
        private String VERSION;
        private String MCHNTCD;
        private String LOGOTP;
        private String FM;

        public String getFuioupayurl() {
            return fuioupayurl;
        }

        public void setFuioupayurl(String fuioupayurl) {
            this.fuioupayurl = fuioupayurl;
        }

        public String getENCTP() {
            return ENCTP;
        }

        public void setENCTP(String ENCTP) {
            this.ENCTP = ENCTP;
        }

        public String getVERSION() {
            return VERSION;
        }

        public void setVERSION(String VERSION) {
            this.VERSION = VERSION;
        }

        public String getMCHNTCD() {
            return MCHNTCD;
        }

        public void setMCHNTCD(String MCHNTCD) {
            this.MCHNTCD = MCHNTCD;
        }

        public String getLOGOTP() {
            return LOGOTP;
        }

        public void setLOGOTP(String LOGOTP) {
            this.LOGOTP = LOGOTP;
        }

        public String getFM() {
            return FM;
        }

        public void setFM(String FM) {
            this.FM = FM;
        }
    }
}
