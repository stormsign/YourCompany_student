package com.miuhouse.yourcompany.student.utils;

/**
 * Created by khb on 2016/5/13.
 */
public class Constants {

    //  短信验证码
    public final static String SMSSDK_COUNTRYCODE = "86";
    public final static String SMSSDK_APP_KEY = "1603fc79f3380";
    public final static String SMSSDK_APP_SECRET = "7686ac0ec95238edb38935d6ad52c2a9";
    //    服务器地址
    public final static String URL_HEAD = "http://192.168.1.235:8080";
//    public final static String URL_HEAD = "http://192.168.1.185";
    //        public final static String URL_HEAD = "http://www.peibanxue.com";
//    public final static String URL_HEAD = "http://www.exuepei.com";
    public final static String URL_VALUE = URL_HEAD + "/appparent/";
    public final static String IMAGE_URL_UPLOAD = "http://upload.miuhouse.com/app/";
    // 内网测试地址
    public final static String IMEI = "imei";
    public final static String DEVICETYPE = "deviceType";
    public final static String VERSIONCODE = "version_code";
    //    请求超时时间
    public final static int TIMEOUT = 10;
    //    最大请求重试次数
    public static final int MAX_RETRIES = 0;

    public final static String DEVICETYPE_VALUE = "3";
    public final static String VERSIONCODE_VALUE = "1";
    public final static String APPKEY = "peibanxue";
    public static String IMEI_VALUE = "";
    public final static String MD5 = "md5";
    public final static String TRANSDATA = "transData";
    //最多几张图片
    public static final int MAX_NUM = 9;
    //    从服务器返回的图片url前缀，图片服务器地址
    public final static String IMGURL_HEAD = "http://img.miuhouse.com";
    //    百度推送
    public final static String PUSH_APIKEY = "GTC1zjQQIPlnXf9ZvPCCrxnG";
    //  未读系统消息
    public static final String UNREAD_SYSMSG_COUNT = "unread_sysmsg_count";
    public static final String LATEST_SYSMSG = "latest_sysmsg";
    //    未读订单消息
    public static final String UNREAD_ORDERMSG_COUNT = "unread_pursemsg_count";
    public static final String LATEST_ORDERMSG = "latest_pursemsg";
    //  新评论或赞
    public static final String UNREAD_DONGTAI = "unread_dongtai";
    public static final String LATEST_DONGTAI = "latest_dongtai";

  public static final String INTENT_ACTION_RECEIVE = "com.miuhouse.yourcompany.student.RECEIVE";

  public static final int PAGE_SIZE = 20;

  public static final String SUBJECT_TYPE = "subject_type";

  public static final String INTEREST_TYPE = "interest_type";

  //    Ping++支付
  public static final String ALIPAY = "alipay";
  public static final String WEIXIN = "wx";



    public final static String COMMENT_TYPE_COMMENT = "1";
    public final static String COMMENT_TYPE_THUMBUPS = "2";
    public final static String LINK_TYPE_PARENTS = "2";
    public final static String LINK_TYPE_TEACHER = "3";

    /**
     * 无托班
     */
    public final static int SCHOOLNEWSLIST_ONE = 0;
  //    通知类型字段
  public static final String MSG_TYPE = "type";
  //短单
  public static final String TYPE_SMALL = "1";
  //周单
  public static final String TYPE_WEEK = "2";
  //月单s
  public static final String TYPE_MONTH = "3";

    //public final static String SAVE_IMAGE_PATH=
}
