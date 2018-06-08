package com.ql.jcjr.constant;

/**
 * 请求服务器的URL工具类
 */
public class RequestURL {

    /**
     * 服务器URL
     * 线上：
     * 测试：
     */
    public static final String SERVICE_URL_BASE = "http://testing.jicaibaobao.com/";
//    public static final String SERVICE_URL_BASE = "http://www.jicaibaobao.com/";

    public static final String SERVICE_URL = SERVICE_URL_BASE+"index.action?phoneapinew&q=going/";

    //积财金融协议
    public static final String JCJR_OFFICIAL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/borrowAgreement1&show=1&borrow_id=";
    //风险提示书
    public static final String JCJR_DANGER_URL = SERVICE_URL_BASE+"addition/riskpoint.html";
    //投资合同
    public static final String JCJR_TOUZI_OFFICIAL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/borrowAgreement1&sign=1&tender_id=";

    //新手福利 立即投标链接
    public static final String INTERCEPT_XSFL_URL = SERVICE_URL_BASE+"newweixinlist/index.html";
    //充值成功H5页面 继续充值链接
    public static final String INTERCEPT_RECHARGE_URL = SERVICE_URL_BASE+"index.php?user&q=going/wxrecharge";
    //兑换中心H5
    public static final String INTERCEPT_EXCHANGE_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/exchangecenter";
    //资讯详情
    public static final String INTERCEPT_ZIXUNDETAIL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/noticecontent&isread=1&id=";
    //积财学堂 资讯更多
    public static final String INTERCEPT_ZIXUNMORE_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/jcschool";
    //我的红包跳转
    public static final String INTERCEPT_MYREDPACKETS_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/myawards";
    //
    public static final String INTERCEPT_GOINGACCOUNT_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/myaccount";
    //公告详情
    public static final String NOTICE_DETAILS_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/noticecontent&id=";
    //新手福利
    public static final String XSFL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/xshb";
    //安全保障
    public static final String AQBZ_URL = SERVICE_URL_BASE+"addition/safe/";
    //邀请有礼
    public static final String YQYL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/inviteinfo&userid=";
    //项目详情
    public static final String PROJECT_DETAIL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/getBorrowContent&id=";
    //标的详情页的安全保障
    public static final String BID_AQBZ_URL = SERVICE_URL_BASE+"addition/appsafe/";
    //消息详情
    public static final String MESSAGE_DETAIL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/getMessageContent&id=";
    //VIP详情
    public static final String VIP_DETAIL_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/vipcenter";

    //获取app信息
    public static final String GETAPP_URL = SERVICE_URL + "appDownload";
    //获取app信息
    public static final String GETAPPID_URL = SERVICE_URL + "getOauthId";
    //登录
    public static final String LOGIN_URL = SERVICE_URL + "login";
    //登录检查
    public static final String LOGIN_CHECK_URL = SERVICE_URL + "isreg";
    //注册
    public static final String REGISTER_URL = SERVICE_URL + "reg";
    //注册 获取 验证码
    public static final String REG_VERIFY_URL = SERVICE_URL + "regSms";
    //注册 验证 验证码
    public static final String VALIDATE_REG_VERIFY_URL = SERVICE_URL + "checkRegSms";
    //忘记密码 获取 验证码
    public static final String PWD_VERIFY_URL = SERVICE_URL + "findPasswordSms";
    //忘记密码 验证 验证码
    public static final String VALIDATE_PWD_VERIFY_URL = SERVICE_URL + "checkFindPasswordSms";
    //忘记支付密码 获取 验证码
    public static final String TRANS_PWD_VERIFY_URL = SERVICE_URL + "findPayPasswordSms";
    //忘记支付密码 验证 验证码
    public static final String VALIDATE_TRANS_PWD_VERIFY_URL = SERVICE_URL + "checkFindPayPasswordSms";
    //找回密码
    public static final String FIND_PSW_URL = SERVICE_URL + "findPassword";
    //待收明细
    public static final String FIND_RECENT_REPAY = SERVICE_URL + "recentRepay";
    //首页banner
    public static final String BANNER_URL = SERVICE_URL + "rollPic";
    //理财列表
    public static final String BID_LIST_URL = SERVICE_URL + "borrowList";
    //首页公告
    public static final String ROLL_NEWS_URL = SERVICE_URL + "rollNews";
    //首页公告
    public static final String HOME_DATA_URL = SERVICE_URL + "home";
    //首页年化
    public static final String NOVICE_EXCLUSIVE_URL = SERVICE_URL + "borrowForNewer";
    //我的页面
    public static final String MINE_FRAGMENT_URL = SERVICE_URL + "main";
    //标的详情
    public static final String BID_DETAIL_URL = SERVICE_URL + "borrowContent";
    //历史投资标的详情
    public static final String BID_HISTORY_DETAIL_URL = SERVICE_URL + "tenderdetail1";
    //标的红包详情
    public static final String BID_DETAIL_HB_URL = SERVICE_URL + "originAwards";
    //账户资产
    public static final String ACCT_ASSETS_URL = SERVICE_URL + "myAccount";
    //待收明细
    public static final String RECEIPT_DETAILS_URL = SERVICE_URL + "recentRepay";
    //我的红包
    public static final String MY_COUPON_URL = SERVICE_URL + "myAwards";
    //交易记录
    public static final String TRANS_RECORD_URL = SERVICE_URL + "accountLog";
    //提现记录
    public static final String WITHDRAWALS_RECORD_URL = SERVICE_URL + "myCashLog";
    //充值记录
    public static final String RECHARGE_RECORD_URL = SERVICE_URL + "myRecharge";
    //投标记录
    public static final String BID_RECORD_URL = SERVICE_URL + "myTender";
    //自动投标
    public static final String AUTO_BID_URL = SERVICE_URL + "autoBorrow";
    //设置自动投标
    public static final String SET_AUTO_BID_URL = SERVICE_URL + "autoAdd";
    //取消自动投标
    public static final String CANCEL_AUTO_BID_URL = SERVICE_URL + "autoDel";
    //是否实名认证
    public static final String CHECK_REAL_NAME_URL = SERVICE_URL + "checkRealname";
    //实名认证
    public static final String REAL_NAME_URL = SERVICE_URL + "realnameVerify";
    //提现信息
    public static final String CASH_INFO_URL = SERVICE_URL + "cashOne";
    //提现
    public static final String CASH_URL = SERVICE_URL + "cash";
    //提现
    public static final String BIND_CARD_INFO_URL = SERVICE_URL + "bankBindOne";
    //提现
    public static final String BIND_CARD_URL = SERVICE_URL + "bankBind";
    //获取银行列表
    public static final String BANK_LIST_URL = SERVICE_URL + "getBanks";

    //获取银行名字
    public static final String BANK_NAME = SERVICE_URL + "getbankname";

    //是否绑定银行卡
    public static final String CHECK_BANK_URL = SERVICE_URL + "checkBank";
    //绑定银行卡
    public static final String BIND_BANK_URL = SERVICE_URL + "bankBind";
    //获取省
    public static final String GET_PROVINCE_URL = SERVICE_URL + "getProvinces";
    //获取市
    public static final String GET_CITY_URL = SERVICE_URL + "getCitys";
    //投标
    public static final String BID_URL = SERVICE_URL + "tender";
    //修改登录密码
    public static final String REST_PWD_URL = SERVICE_URL + "resetPassword";
    //意见反馈
    public static final String FEEDBACK_URL = SERVICE_URL + "myAdvice";
    //登录密码验证
    public static final String CHECK_LOGIN_PWD_URL = SERVICE_URL + "checkPassword";
    //支付密码验证
    public static final String CHECK_PAY_PWD_URL = SERVICE_URL + "checkPayPassword";
    //交易密码设置与修改
    public static final String PAY_PWD_URL = SERVICE_URL + "resetPayPassword";
    //充值
    public static final String RECHARGE_URL = SERVICE_URL + "rechargeForApp";
    //我的消息
    public static final String MESSAGE_CENTER_URL = SERVICE_URL + "messageList";
    //精彩活动，公告
    public static final String MESSAGE_ACT_URL = SERVICE_URL + "activelist";
    //设置头像
    public static final String HEAD_IMG_URL = SERVICE_URL + "headImg";
    //设置支付密码
    public static final String AGREEMENT_URL = SERVICE_URL_BASE+"addition/agreement.html";

    //注册协议
    public static final String REGISTE_URL = SERVICE_URL_BASE+"addition/zhucexieyi.html";

    //收货地址
    public static final String Address_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/address";

    //风险测评
    public static final String RISKTEST_URL = SERVICE_URL_BASE+"index.action?newfunc&q=going/risktest";

    //获取消息中心信息
    public static final String GET_MSG_HOME_INFO_URL = SERVICE_URL + "activemodule";

    public static final String FIND_PAY_PSW_URL = SERVICE_URL + "findPayPassword";
    //提现 获取 验证码
    public static final String CASH_SMS_URL = SERVICE_URL + "cashSms";
    //提现 验证 验证码
    public static final String CHECK_CASH_SMS_URL = SERVICE_URL + "checkCashSms";
    //提现手续费信息
    public static final String GET_CASH_SERVICE = SERVICE_URL + "feeInfo1";

    //获取我的红包信息，投标时可使用的红包
    public static final String GET_MY_HB_INFO = SERVICE_URL + "awards";

    //获取我的投资记录
    public static final String GET_MY_BID_HISTORY = SERVICE_URL + "tenderlist";

    //资金统计
    public static final String GET_MYACCOUNT_URL = SERVICE_URL + "myAccount";

    //投标成功
    public static final String BID_SUCCESS_URL = SERVICE_URL_BASE + "index.action?newfunc&q=going/tender_result&sign=success";

    //提现成功
    public static final String BID_GETMONEY_URL = SERVICE_URL_BASE + "index.action?newfunc&q=going/cash_result";

    //跳转我的页面
    public static final String TO_MINE_URL = SERVICE_URL_BASE + "index.php?user&a=newweixin";

    //H5关闭当前页
    public static final String FINISH_URL = SERVICE_URL_BASE + "index.action?newfunc&q=going/setup";

    //获取外部ip
    public static final String GET_IP = SERVICE_URL + "getip";

    //风险测评结果
    public static final String GET_RISK_DATA = SERVICE_URL + "riskWarning";

    //发现
    public static final String FIND = SERVICE_URL + "find";

    //分享成功后
    public static final String SHARE_SUCCESS = SERVICE_URL + "articleOp";

    //更新用户支行和省市信息
    public static final String UPDATA_BRANCH_URL = SERVICE_URL + "updateBranch";

    //开屏页接口
    public static final String WELCOME = SERVICE_URL + "spread";
}
