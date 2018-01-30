package com.ql.jcjr.http;

import android.os.Bundle;

import com.lidroid.xutils.http.RequestParams;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.DeviceInfoUtil;
import com.ql.jcjr.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName: SenderManager
 * Description:
 * Author: liuchao
 * Date: Created on 202017/6/26.
 */

public class ParamsManager {

    /**
     * 获取app分享信息，新版本信息
     */
    public static SenderResultModel senderGetAppInfo() {
        JSONObject object = new JSONObject();
        try {
            object.put("imei", DeviceInfoUtil.imei);
            object.put("phone_model", DeviceInfoUtil.phone_model);
            object.put("os_version", DeviceInfoUtil.os_version);
            object.put("channel", DeviceInfoUtil.channel);
            object.put("version_code", CommonUtils.getAppVersionCode()+"");
            object.put("account", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GETAPP_URL);

        return resultModel;
    }

    /**
     * 获取验证码
     */
    public static SenderResultModel senderGetVerifyCode(String phone, String url) {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, url);

        return resultModel;
    }
    /**
     * 获取验证码
     */
    public static SenderResultModel senderGetVerifyCodeFirst(String phone, String url,String type) {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
            object.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, url);

        return resultModel;
    }
    /**
     * 注册 验证 验证码
     */
    public static SenderResultModel senderRegValidateCode(String phone, String code, String url) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("phonecode", code);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, url);

        return resultModel;
    }

    /**
     * 忘记密码 验证 验证码
     */
    public static SenderResultModel senderPwdValidateCode(String phone, String code, String url) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("code", code);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, url);

        return resultModel;
    }

    /**
     * 注册
     */
    public static SenderResultModel senderRegister(String phone, String psw, String inviter, String appSign) {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
            object.put("password", psw);
            object.put("invite_username", inviter);
            object.put("appSign", appSign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.REGISTER_URL);

        return resultModel;
    }

    /**
     * 登录
     */
    public static SenderResultModel senderLogin(String phone, String psw) {
        JSONObject object = new JSONObject();
        try {
            object.put("username", phone);
            object.put("password", psw);
            object.put("serialnumber", StringUtils.getSerialNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.LOGIN_URL);

        return resultModel;
    }

    /**
     * 手机号检查
     */
    public static SenderResultModel senderLoginCheck(String phone) {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.LOGIN_CHECK_URL);
        return resultModel;
    }


    /**
     * 找回密码
     */
    public static SenderResultModel senderFindPsw(String phone, String psw) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", phone);
        params.addBodyParameter("password", psw);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.FIND_PSW_URL);

        return resultModel;
    }

    /**
     * 找回密码
     */
    public static SenderResultModel senderRecentRepay() {
        RequestParams params = new RequestParams();

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.FIND_RECENT_REPAY);

        return resultModel;
    }

    /**
     * banner
     */
    public static SenderResultModel senderBanner() {
        SenderResultModel resultModel = SenderManager.buildResultModel(null, RequestURL.BANNER_URL, false);
        return resultModel;
    }

    /**
     * 首页公告
     */
    public static SenderResultModel senderRollNews() {
        SenderResultModel resultModel = SenderManager.buildResultModel(null, RequestURL.ROLL_NEWS_URL, false);
        return resultModel;
    }

    /**
     * 首页数据
     */
    public static SenderResultModel getHomeData() {
        SenderResultModel resultModel = SenderManager.buildResultModel(null, RequestURL.HOME_DATA_URL, false);
        return resultModel;
    }

    /**
     * 首页年化
     */
    public static SenderResultModel senderNoviceExclusive() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.NOVICE_EXCLUSIVE_URL, false);
        return resultModel;
    }

    /**
     * 理财列表
     */
    public static SenderResultModel senderBidList(String page, String epage, String type) {
        RequestParams params = new RequestParams();
//        params.addBodyParameter("biaoSing", biaoSing);
        params.addBodyParameter("page", page);
        params.addBodyParameter("epage", epage);
        params.addBodyParameter("type", type);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_LIST_URL, false);

        return resultModel;
    }

    /**
     * 我的页面
     */
    public static SenderResultModel senderMineFragment() {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.MINE_FRAGMENT_URL, false);

        return resultModel;
    }

    /**
     * 标的详情
     */
    public static SenderResultModel senderBidDetail(String bidId) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", bidId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_DETAIL_URL);

        return resultModel;
    }

    /**
     * 历史投资标的详情
     */
    public static SenderResultModel senderBidHistoryDetail(String bidId, String tenderId) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", bidId);
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("tender_id", tenderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_HISTORY_DETAIL_URL);

        return resultModel;
    }

    /**
     * 标的红包详情
     */
    public static SenderResultModel senderBidHBDetail(String bidId, String money) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", bidId);
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("money", money);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_DETAIL_HB_URL);

        return resultModel;
    }


    /**
     * 账户资产
     */
    public static SenderResultModel senderAccountAssets() {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.ACCT_ASSETS_URL);

        return resultModel;
    }

    /**
     * 手续费详情
     */
    public static SenderResultModel senderServiceCash() {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GET_CASH_SERVICE);

        return resultModel;
    }

    /**
     * 待收明细
     */
    public static SenderResultModel senderReceiptDetails(String page) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.RECEIPT_DETAILS_URL);

        return resultModel;
    }

    /**
     * 我的红包
     */
    public static SenderResultModel senderMyCoupon(String page, String epage, String status) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
            object.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.MY_COUPON_URL);

        return resultModel;
    }

    /**
     * 我的红包，包含抵扣、加息、返现
     */
    public static SenderResultModel senderMyCouponNew(String page, String epage, String sign, String type, String borrowid, String money) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
            object.put("sign", sign);
            object.put("type", type);
            if(null != borrowid){
                object.put("borrowid", borrowid);
            }
            if(null != money){
                object.put("money", money);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GET_MY_HB_INFO);

        return resultModel;
    }

    /**
     * 我的投资
     */
    public static SenderResultModel senderMyBidHistory(String page, String epage, String status) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
            if(null != status){
                object.put("status", status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GET_MY_BID_HISTORY);
        return resultModel;
    }

    /**
     * 交易记录
     */
    public static SenderResultModel senderTransRecord(String page, String epage) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.TRANS_RECORD_URL);

        return resultModel;
    }

    /**
     * 提现记录
     */
    public static SenderResultModel senderWithdrawalsRecord(String page, String epage) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.WITHDRAWALS_RECORD_URL);

        return resultModel;
    }

    /**
     * 充值记录
     */
    public static SenderResultModel senderRechargeRecord(String page, String epage) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.RECHARGE_RECORD_URL);

        return resultModel;
    }

    /**
     * 投标记录
     */
    public static SenderResultModel senderBidRecord(String page, String epage) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", page);
            object.put("epage", epage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_RECORD_URL);

        return resultModel;
    }

    /**
     * 自动投标
     */
    public static SenderResultModel senderAutoBid() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.AUTO_BID_URL);

        return resultModel;
    }

    /**
     * 设置自动投标
     */
    public static SenderResultModel senderSetAutoBid(String tenderAmt, String dayfirst, String daylast) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("tenderaccount", tenderAmt);

            object.put("monthfirst", "");
            object.put("monthlast", "");
            object.put("dayfirst", dayfirst);
            object.put("daylast", daylast);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.SET_AUTO_BID_URL);

        return resultModel;
    }
    public static SenderResultModel senderSetAutoBid(String tenderAmt, String monthfirst, String monthlast, String dayfirst, String daylast) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("tenderaccount", tenderAmt);

            object.put("monthfirst", monthfirst);
            object.put("monthlast", monthlast);
            object.put("dayfirst", dayfirst);
            object.put("daylast", daylast);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.SET_AUTO_BID_URL);

        return resultModel;
    }

    /**
     * 取消自动投标
     */
    public static SenderResultModel senderCancelAutoBid(String tenderId) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("id", tenderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CANCEL_AUTO_BID_URL);

        return resultModel;
    }

    /**
     * 是否实名认证
     */
    public static SenderResultModel senderCheckRealName() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CHECK_REAL_NAME_URL);

        return resultModel;
    }

    /**
     * 实名认证
     */
    public static SenderResultModel senderRealName(String pid, String pname) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("pid", pid);
            object.put("payer_name", pname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.REAL_NAME_URL);

        return resultModel;
    }

    /**
     * 提现信息
     */
    public static SenderResultModel senderCashInfo() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CASH_INFO_URL);

        return resultModel;
    }

    /**
     * 提现
     */
    public static SenderResultModel senderCash(String money, String paypassword) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("money", money);
            object.put("paypassword", paypassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CASH_URL);

        return resultModel;
    }

    /**
     * 提现
     */
    public static SenderResultModel senderSmsCash(String money, String smsCode) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("money", money);
            object.put("code", smsCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CASH_URL);

        return resultModel;
    }

    /**
     * 提现信息
     */
    public static SenderResultModel senderBindBankInfo() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BIND_CARD_INFO_URL);

        return resultModel;
    }

    /**
     * 获取银行列表
     */
    public static SenderResultModel senderGetBankList() {

        SenderResultModel resultModel = SenderManager.buildResultModel(null, RequestURL.BANK_LIST_URL);

        return resultModel;
    }

    /**
     * 根据卡号检查银行名字
     */
    public static SenderResultModel senderGetBankName(String cardNum) {
        JSONObject object = new JSONObject();
        try {
            object.put("card", cardNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);

        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BANK_NAME);
        return resultModel;
    }

    /**
     * 是否绑定银行卡
     */
    public static SenderResultModel senderCheckBank() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CHECK_BANK_URL);

        return resultModel;
    }

    /**
     * 绑定银行卡
     */
    public static SenderResultModel senderBindBank(Bundle bundle) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("name", bundle.get("real_name"));
            object.put("bankCard", bundle.get("card_num"));
            object.put("mobile", bundle.get("mobile"));
            object.put("bank", bundle.get("bank_id"));
            object.put("branch", bundle.get("branch_name"));
            object.put("province", bundle.get("province_id"));
            object.put("city", bundle.get("city_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BIND_BANK_URL);

        return resultModel;
    }

    /**
     * 获取省
     */
    public static SenderResultModel senderGetprovince() {

        SenderResultModel resultModel = SenderManager.buildResultModel(null, RequestURL.GET_PROVINCE_URL);

        return resultModel;
    }

    /**
     * 获取市
     */
    public static SenderResultModel senderGetCity(String provinceId) {

        JSONObject object = new JSONObject();
        try {
            object.put("id", provinceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GET_CITY_URL);

        return resultModel;
    }

    /**
     * 投标
     */
    public static SenderResultModel senderBid(String tenderId, String amt, String dxbPWD, String lcb, String cashid, String hbType) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("id", tenderId);
            object.put("money", amt);
            object.put("dxbPWD", dxbPWD);
            object.put("lcb", lcb);
            if(null != cashid){
                object.put("cashid", cashid);
            }
            if(null != cashid){
                object.put("type", hbType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_URL);

        return resultModel;
    }
    public static SenderResultModel senderBid(String tenderId, String amt, String dxbPWD, String lcb) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("id", tenderId);
            object.put("money", amt);
            object.put("dxbPWD", dxbPWD);
            object.put("lcb", lcb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.BID_URL);

        return resultModel;
    }

    /**
     * 修改登录密码
     */
    public static SenderResultModel senderChangeLoginPwd(String oldPwd, String newOne, String newTwo) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("oldpassword", oldPwd);
            object.put("newpasswordOne", newOne);
            object.put("newpasswordTwo", newTwo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.REST_PWD_URL);

        return resultModel;
    }

    /**
     * 意见反馈
     */
    public static SenderResultModel senderFeedback(String content) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.FEEDBACK_URL);

        return resultModel;
    }

    /**
     * 登录密码验证
     */
    public static SenderResultModel senderCheckLoginPwd(String pwd) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("oldpassword", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CHECK_LOGIN_PWD_URL);

        return resultModel;
    }

    /**
     * 支付密码验证
     */
    public static SenderResultModel senderCheckPayPwd(String pwd) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("oldpassword", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CHECK_PAY_PWD_URL);

        return resultModel;
    }

    /**
     * 支付密码设置或修改
     */
    public static SenderResultModel sendeSetPayPwd(String pwd, String confirmPwd, String oldPwd) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("newpasswordOne", pwd);
            object.put("newpasswordTwo", confirmPwd);

            if (StringUtils.isNotBlank(oldPwd)) {
                object.put("oldpassword", oldPwd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.PAY_PWD_URL);

        return resultModel;
    }

    /**
     * 充值
     */
    public static SenderResultModel sendeRecharge(String amt) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("tradeamount", amt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.RECHARGE_URL);

        return resultModel;
    }

    /**
     * 我的消息
     */
    public static SenderResultModel sendeMessageCenter(String pageIndex, String epage) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", pageIndex);
            object.put("epage", epage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.MESSAGE_CENTER_URL);

        return resultModel;
    }

    /**
     * 精彩活动  公告
     */
    public static SenderResultModel senderMessageAct(String pageIndex, String epage, int msgType) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("page", pageIndex);
            object.put("epage", epage);
            object.put("from", "app");
            if(msgType == 0){
                object.put("type", "active");
            }
            else{
                object.put("type", "gonggao");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.MESSAGE_ACT_URL);

        return resultModel;
    }

    /**
     * 上传头像
     */
    public static SenderResultModel sendeHeaderImg(String photoData) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("headImg", photoData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.HEAD_IMG_URL);

        return resultModel;
    }

    /**
     * 找回交易秘密
     */
    public static SenderResultModel sendeFindPayPsw(String psw) {

        JSONObject object = new JSONObject();
        try {
            object.put("username", UserData.getInstance().getPhoneNumber());
            object.put("password", psw);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.FIND_PAY_PSW_URL);

        return resultModel;
    }

    /**
     * 提现获取验证码
     */
    public static SenderResultModel sendeCashSms() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CASH_SMS_URL, false);

        return resultModel;
    }

    /**
     * 提现验证验证码
     */
    public static SenderResultModel sendeCashCheckSms(String code) {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("code", code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.CHECK_CASH_SMS_URL);

        return resultModel;
    }


    /**
     * 获取消息中心信息
     */
    public static SenderResultModel getMsgCenterInfo() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
            object.put("from", "app");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GET_MSG_HOME_INFO_URL);

        return resultModel;
    }

    /**
     * 资金统计
     */
    public static SenderResultModel getMyAccount() {

        JSONObject object = new JSONObject();
        try {
            object.put("userid", UserData.getInstance().getUSERID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params, RequestURL.GET_MYACCOUNT_URL);

        return resultModel;
    }

    /**
     * 头条激活
     */
    public static SenderResultModel postIMEI(String imei) {

        JSONObject object = new JSONObject();
        try {
            object.put("imei", imei);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = SenderManager.buildRequestParams(object);
        SenderResultModel resultModel = SenderManager.buildResultModel(params,"http://www.jicaibaobao.com/export.php?sign=deviceinfocheck");
        return resultModel;
    }
}
