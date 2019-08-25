package com.yufan.dao.goods;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 12:01
 * 功能介绍:
 */
public interface IGoodsDao {

    /**
     * 查询商品sku列表
     * @param goodsId
     * @return
     */
    public List<Map<String,Object>> queryGoodsSkuListMap(int goodsId);


    /**
     * 查询抢购商品
     * @param goodsId
     * @return
     */
    public List<Map<String,Object>> queryTimeGoodsListMap(Integer goodsId);
}
