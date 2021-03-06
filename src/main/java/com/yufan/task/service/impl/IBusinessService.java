package com.yufan.task.service.impl;

import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/2/28
 */
public interface IBusinessService {


    /**
     * 查询要校验的商品信息
     *
     * @param goodsIds     商品ids
     * @param skuIds       商品skuIds
     * @param timeGoodsIds 抢购商品ids
     * @param goodsMap     商品信息map
     * @param goodsSkuMap  商品sku map
     * @param timeGoodsMap 抢购商品信息map
     */
    public void findCheckGoodsInfo(String goodsIds, String skuIds, String timeGoodsIds, Map<Integer, Map<String, Object>> goodsMap,
                                   Map<Integer, Map<String, Object>> goodsSkuMap, Map<Integer, Map<String, Object>> timeGoodsMap);

    /**
     * 校验商品信息
     *
     * @param goodsList    {"goods_id":100, "time_goods_id":0,"buy_count":10,"sku_id":0}
     * @param goodsMap     商品信息
     * @param goodsSkuMap  商品sku信息
     * @param timeGoodsMap 抢购商品信息
     * @param userId       用户标识
     * @return 校验结果 返回接口响应编码
     */
    public int checkGoodsInfo(JSONArray goodsList, Map<Integer, Map<String, Object>> goodsMap,
                              Map<Integer, Map<String, Object>> goodsSkuMap,
                              Map<Integer, Map<String, Object>> timeGoodsMap, int userId);


    /**
     * 查询邮寄地址运费
     *
     * @param addrIdsFreightMap Map.put(regionId, freight); 邮寄地址,对应的运费
     * @param addrIdsArray      String userAddrRegionIds= regionId-regionId-regionId-regionId    addrIdsArray=userAddrRegionIds.split("-"),长度为4
     * @return 运费
     */
    public BigDecimal findPostPrice(Map<Integer, BigDecimal> addrIdsFreightMap, String[] addrIdsArray);

}
