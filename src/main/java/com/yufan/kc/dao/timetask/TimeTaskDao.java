package com.yufan.kc.dao.timetask;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/20 9:42
 * 功能介绍:
 */
public interface TimeTaskDao {


    /**
     * 更新商品库存
     */
    void updateGoodsStore(String goodsCode);

    /**
     * 删除商品报表
     *
     * @param month
     */
    void deleteGoodsSaleReport(String month);

    /**
     * 商品统计查询订单日期（生成订单月份）
     *
     * @return
     */
    List<Map<String, Object>> findOrderPayDateMonth();

    /**
     * 商品统计(已生成订单的商品月销售总额)生成查询月的商品销售金额
     */
    void goodsSaleReport(String month);

    /**
     * 商品统计(商品月进货总额)按月查询商品进货总额
     *
     * @param month
     */
    void goodsStoreInPriceAll(String month);

    /**
     * 订单统计删除和初始化订单报表（本年度）
     *
     * @param year
     */
    void deleteAndInitOrderReport(String year);

    /**
     * 订单统计(统计收入总额)
     *
     * @param year
     */
    void orderSaleReport(String year);

    /**
     * 订单统计(统计进货总额)
     * @param year
     */
    void goodsInPriceAllOrder(String year);



}
