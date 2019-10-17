package com.yufan.utils;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.task.dao.goods.IGoodsDao;
import org.apache.log4j.Logger;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/15 21:10
 * 功能介绍:  校验类
 */
public class CheckUtil {

    private static Logger LOG = Logger.getLogger(CheckUtil.class);

    /**
     * 校验限时抢购商品
     * @return
     */
    public static JSONObject checkTimeGoods(IGoodsDao iGoodsDao,int timeGoodsId) {
        LOG.info("---------校验限时抢购商品------------");
        try {



        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 校验商品
     * @return
     */
    public static JSONObject checkGoods(IGoodsDao iGoodsDao,int goodsId) {
        LOG.info("---------校验商品------------");
        try {

        } catch (Exception e) {

        }
        return null;
    }



    /**
     * 校验商品SKU
     * @return
     */
    public static JSONObject checkGoodsSku(IGoodsDao iGoodsDao,int skuId) {
        LOG.info("---------校验商品SKU------------");
        try {

        } catch (Exception e) {

        }
        return null;
    }

}
