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
     */
    public static Integer ORDER_STATUS_FINISH = 3;//已完成

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
    public static String IMG_URL = "http://192.168.3.39:8888/";


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
}
