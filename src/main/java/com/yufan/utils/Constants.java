package com.yufan.utils;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/2 11:46
 * 功能介绍:
 */
public class Constants {

    /**
     * 固定系统参数MD5
     */
    public static String ADMINPASSWORD = "40999628e3b875dae36bb4867c5c1c5f";//系统管理员固定参数
    public static String SHOPVALIDITY = "2019-01-01";//店铺有有效截止日期
    public static String ACTIVITYDATE = "2019-03-01";//jar包有效日期（4个月必须更新一次jar,防止合作单方面终止）

    /**
     * 订单重置秘钥
     */
    public static String ORDER_RESET_KEY = "c4ca4238a0b923820dcc509a6f75849b";

    /**
     * 订单状态
     * //0	待付款
     * //1	已付款
     * //3	已失败
     * //4	待发货
     * //5	待收货
     * //6	已完成
     * //7	已取消
     * //8	已删除
     * //9	退款中
     * //10	已退款
     * //11	处理中
     * //12	还货中
     * //13	已还货
     */
    public static Integer ORDER_STATUS_0 = 0;
    public static Integer ORDER_STATUS_1 = 1;
    public static Integer ORDER_STATUS_2 = 2;
    public static Integer ORDER_STATUS_3 = 3;
    public static Integer ORDER_STATUS_4 = 4;
    public static Integer ORDER_STATUS_5 = 5;
    public static Integer ORDER_STATUS_6 = 6;
    public static Integer ORDER_STATUS_7 = 7;
    public static Integer ORDER_STATUS_8 = 8;
    public static Integer ORDER_STATUS_9 = 9;
    public static Integer ORDER_STATUS_10 = 10;
    public static Integer ORDER_STATUS_11 = 11;
    public static Integer ORDER_STATUS_12 = 12;
    public static Integer ORDER_STATUS_13 = 13;

    /**
     * 数据状态
     */
    public static Integer DATA_STATUS_WX = 0;//无效
    public static Integer DATA_STATUS_YX = 1;//有效
    public static Integer DATA_STATUS_DJ = 2;//已冻结
    public static Integer DATA_STATUS_DELETE = 3;//已删除
    public static Integer DATA_STATUS_SHZ = 10;//10审核中

    /**
     * 图片访问路径
     */
    public static String IMG_WEB_URL = "";//在数据库参数配置


    /**
     * 关联图片
     */
    //0.商品图片1.卡券图片2.店铺图片
    public static Integer CLASSIFY_GOODS = 0;//商品图片
    public static Integer CLASSIFY_COUPON = 1;//卡券图片
    public static Integer CLASSIFY_SHOP = 2;//店铺图片

    //1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
    public static Integer IMGTYPE_GOODS_BANNER = 1;//
    public static Integer IMGTYPE_GOODS_INFO = 2;//
    public static Integer IMGTYPE_SHOP_BANNER = 3;//
    public static Integer IMGTYPE_SHOP_INFO = 4;//
    public static Integer IMGTYPE_COUPON_BANNER = 5;//
    public static Integer IMGTYPE_COUPON_INFO = 6;//


    /**
     * 参数paramCode
     */
    public static String PARAM_CODE_LIMIT_WAY = "limit_way";//限购方式

    /**
     * 网易短信参数
     */
    public static String APP_SECRET_WY = "";
    public static String APP_KEY_WY = "";
    public static String SENDTEMPLATE_URL_WY = "";
    public static String SENDCODE_URL_WY = "";

    /**
     * 阿里云
     */
    public static String ACCESSKEYID_ALIPAY = "";
    public static String ACCESSKEYSECRET_ALIPAY = "";


    /**
     * 验证码类型:1手机绑定2修改密码3重置密码4手机解绑5手机注册6验证码登录7注销账号
     */
    public static Integer VALID_TYPE_1 = 1;
    public static Integer VALID_TYPE_2 = 2;
    public static Integer VALID_TYPE_3 = 3;
    public static Integer VALID_TYPE_4 = 4;
    public static Integer VALID_TYPE_5 = 5;
    public static Integer VALID_TYPE_6 = 6;
    public static Integer VALID_TYPE_7 = 7;


    /**
     * 用户状态   0待验证 1正常 2锁定 3已注销
     */
    public static Integer USER_STATUS_0 = 0;
    public static Integer USER_STATUS_1 = 1;
    public static Integer USER_STATUS_2 = 2;
    public static Integer USER_STATUS_3 = 3;


    /**
     * 用户sns  状态0无效1有效2已解绑
     */
    public static Integer USER_SNS_STATUS_0 = 0;
    public static Integer USER_SNS_STATUS_1 = 1;
    public static Integer USER_SNS_STATUS_2 = 2;

    /**
     * 用户snsType   1、腾讯微博；2、新浪微博；3、人人网；4、微信；5、服务窗；6、一起沃；7、Q
     */
    public static Integer USER_SNS_TYPE_0 = 0;
    public static Integer USER_SNS_TYPE_1 = 1;
    public static Integer USER_SNS_TYPE_2 = 2;
    public static Integer USER_SNS_TYPE_3 = 3;
    public static Integer USER_SNS_TYPE_4 = 4;
    public static Integer USER_SNS_TYPE_5 = 5;
    public static Integer USER_SNS_TYPE_6 = 6;
    public static Integer USER_SNS_TYPE_7 = 7;


    //交易方式 0现金付款1微信2支付宝3账户余额
    public static Integer PAY_WAY_0 = 0;
    public static Integer PAY_WAY_1 = 1;
    public static Integer PAY_WAY_2 = 2;
    public static Integer PAY_WAY_3 = 3;


    /**
     * 取货方式 收货方式 1.邮寄4.自取5配送 postWay
     */
    public static Integer POST_WAY_1 = 1;
    public static Integer POST_WAY_2 = 2;
    public static Integer POST_WAY_3 = 3;


    /**
     * 支付结果
     */
    public static Integer PAY_STATUS = 1;//成功

}
