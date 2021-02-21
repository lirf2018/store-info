package com.yufan.task.dao.goods;

import com.yufan.bean.GoodsCondition;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.pojo.TbOrderCart;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.utils.PageInfo;

import java.math.BigDecimal;
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
     * 查询商品列表
     *
     * @param
     * @return
     */
    public PageInfo loadGoodsListPage(GoodsCondition condition);

    public List<Map<String, Object>> loadGoodsList(GoodsCondition condition);

    /**
     * 查询商品列表
     *
     * @param
     * @return
     */
    public PageInfo loadTimeGoodsList(GoodsCondition condition);


    /**
     * type 查询类别 最新new, 最热 hot, 推荐 weight
     *
     * @param size
     * @param type
     * @return
     */
    public List<Map<String, Object>> mainGoodsListMap(int size, String type);

    /**
     * 抢购商品
     *
     * @param size
     * @return
     */
    public List<Map<String, Object>> mainTimeGoodsListMap(int size);


    /**
     * 查询用户购物车
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryUserOrderCart(int userId, Integer goodsId);

    public List<Map<String, Object>> queryUserOrderCart(int userId, Integer goodsId, String propCode);


    /**
     * 保存购物车
     *
     * @param orderCart
     */
    public void saveOrderCart(TbOrderCart orderCart);

    /**
     * 更新购物车
     */
    public void updateOrderCart(int cartId, int goodsCount, String goodsSpec, String goodsSpecName, String goodsSpecNameStr, BigDecimal goodsPrice, BigDecimal trueMoney);

    /**
     * 查询商品sku
     *
     * @param goodsId
     * @param propCode
     * @return
     */
    public TbGoodsSku loadGoodsSku(int goodsId, String propCode);

    public TbGoodsSku loadGoodsSkuBySkuId(int skuId);

    /**
     * 查询抢购商品
     */
    public TbTimeGoods loadTimeGoods(int timeGoodsId);

    public List<Map<String, Object>> queryTbGoodsListMapByGoodsIds(String goodsIds);

    public List<Map<String, Object>> queryTbGoodsSkuListMapBySkuIds(String skuIds);

    /**
     * 减少抢购商品库存
     */
    public void subtractTimeGoodsStore(int timeGoodsId, int num);

    /**
     * 减少商品库存
     */
    public void subtractGoodsStore(int goodsId, int num);

    /**
     * 减少商品sku库存
     */
    public void subtractGoodsSkuStore(int goodsId, int skuId, int num);

    /**
     * 更新商品销售数
     * @param goodsId
     * @param count
     */
    public void updateGoodsSellCount(int goodsId, int count);

    /**
     * 更新商品SKU销售数
     * @param goodsId
     * @param propCode
     * @param count
     */
    public void updateGoodsSkuSellCount(int goodsId, String propCode, int count);

    /**
     * 查询所有有效商品字段信息和sku信息
     * @param goodsIds
     * @return
     */
    List<Map<String,Object>> findAllGoodsDetailInfo(String goodsIds);
}