package com.yufan.task.dao.goods;

import com.yufan.bean.GoodsCondition;
import com.yufan.utils.PageInfo;

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
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> queryGoodsSkuListMap(int goodsId);


    /**
     * 查询抢购商品
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> queryTimeGoodsListMap(Integer goodsId);

    /**
     *  查询商品列表
     * @param
     * @return
     */
    public PageInfo loadGoodsList(GoodsCondition condition);

    /**
     *  查询商品列表
     * @param
     * @return
     */
    public PageInfo loadTimeGoodsList(GoodsCondition condition);


    /**
     * type 查询类别 最新new, 最热 hot, 推荐 weight
     * @param size
     * @param type
     * @return
     */
    public  List<Map<String, Object>> mainGoodsListMap(int size,String type);

    /**
     * 抢购商品
     * @param size
     * @return
     */
    public  List<Map<String, Object>> mainTimeGoodsListMap(int size);

}
