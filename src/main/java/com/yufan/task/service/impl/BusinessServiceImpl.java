package com.yufan.task.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.yufan.utils.ResultCode;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/2/28
 */
@Service("businessServiceImpl")
public class BusinessServiceImpl implements IBusinessService {

    private Logger LOG = Logger.getLogger(BusinessServiceImpl.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public void findCheckGoodsInfo(String goodsIds, String skuIds, String timeGoodsIds, Map<Integer, Map<String, Object>> goodsMap,
                                   Map<Integer, Map<String, Object>> goodsSkuMap, Map<Integer, Map<String, Object>> timeGoodsMap) {
        try {
            // 查询购买的所有商品
            if (StringUtils.isNotEmpty(goodsIds)) {
                if (goodsIds.endsWith(",")) {
                    goodsIds = goodsIds.substring(0, goodsIds.length() - 1);
                }
                List<Map<String, Object>> list = iGoodsDao.queryTbGoodsListMapByGoodsIds(goodsIds);//查询商品列表
                for (int i = 0; i < list.size(); i++) {
                    int goodsId = Integer.parseInt(list.get(i).get("goods_id").toString());
                    goodsMap.put(goodsId, list.get(i));
                }
            }
            //查询商品sku列表
            if (StringUtils.isNotEmpty(skuIds)) {
                if (skuIds.endsWith(",")) {
                    skuIds = skuIds.substring(0, skuIds.length() - 1);
                }
                List<Map<String, Object>> list = iGoodsDao.queryTbGoodsSkuListMapBySkuIds(skuIds);//查询商品sku列表
                for (int i = 0; i < list.size(); i++) {
                    int goodsId = Integer.parseInt(list.get(i).get("goods_id").toString());
                    int skuId = Integer.parseInt(list.get(i).get("sku_id").toString());
                    goodsSkuMap.put(skuId, list.get(i));
                }
            }

            // 查询抢购商品列表
            if (StringUtils.isNotEmpty(timeGoodsIds)) {
                if (timeGoodsIds.endsWith(",")) {
                    timeGoodsIds = timeGoodsIds.substring(0, timeGoodsIds.length() - 1);
                }
                List<Map<String, Object>> list = iGoodsDao.queryTimeGoodsListMapByTimeGoodsIds(timeGoodsIds);
                for (int i = 0; i < list.size(); i++) {
                    int timeGoodsId = Integer.parseInt(list.get(i).get("time_goods_id").toString());
                    timeGoodsMap.put(timeGoodsId, list.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int checkGoodsInfo(JSONArray goodsList, Map<Integer, Map<String, Object>> goodsMap, Map<Integer, Map<String, Object>> goodsSkuMap,
                              Map<Integer, Map<String, Object>> timeGoodsMap, int userId) {
        try {
            // 租赁类型商品与其它类型商品不能同时形成订单
            Map<Integer, Integer> goodsTypeMap = new HashMap<>();
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");//
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");//
                Integer timeGoodsId = goodsList.getJSONObject(i).getInteger("time_goods_id");
                Integer buyCount = goodsList.getJSONObject(0).getInteger("buy_count");
                //
                if (null != timeGoodsId && timeGoodsId > 0) {
                    // 获取抢购商品
                    Map<String, Object> mapTimeGoods = timeGoodsMap.get(timeGoodsId);
                    if (null == mapTimeGoods) {
                        //商品抢购活动已结束
                        return ResultCode.TIMEGOODS_OUTTIME_ERROR.getResp_code();
                    }
                    //判断库存
                    int storeDb = Integer.parseInt(mapTimeGoods.get("goods_store").toString());
                    if (buyCount > storeDb) {
                        //商品抢购库存不足
                        return ResultCode.TIMEGOODS_STORE_EMPTY.getResp_code();
                    }
                    // 抢购商品限购
                    // 限购方式: 1.按天 2按月 3.按年 4不限购 5按次
                    int limitWay = Integer.parseInt(mapTimeGoods.get("time_way").toString());
                    int limitNum = Integer.parseInt(mapTimeGoods.get("limit_num").toString());
                    String limitBeginTime = mapTimeGoods.get("limit_begin_time").toString();
                    int checkFlag = checkLimitWay(userId, limitWay, limitNum, limitBeginTime, goodsId, skuId, timeGoodsId, buyCount);
                    if (checkFlag > 0) {
                        return checkFlag;
                    }
                }
                // 校验商品信息部分
                Map<String, Object> goods = goodsMap.get(goodsId);//商品信息
                String intro = String.valueOf(goods.get("intro"));
                goodsList.getJSONObject(i).put("intro", intro);
                for (Map.Entry<String, Object> m : goods.entrySet()) {
                    goodsList.getJSONObject(i).put(m.getKey(), m.getValue());
                }
                if (goods == null) {
                    LOG.info("-----查询商品不存在----goodsId: " + goodsId);
                    return ResultCode.GOODS_NOT_SALE.getResp_code();
                }
                int isSingle = Integer.parseInt(goods.get("is_single").toString());
                if (isSingle == 0 && (skuId == null || skuId == 0)) {
                    LOG.info("------商品信息有误-----goodsId: " + goodsId);
                    return ResultCode.GOODSINFO_NOT_EXIST.getResp_code();
                }
                if (null != skuId && skuId > 0) {
                    // sku商品
                    Map<String, Object> goodsSku = goodsSkuMap.get(skuId);
                    if (goodsSku == null) {
                        LOG.info("------商品sku信息有误-----goodsId: " + goodsId + "   skuId:" + skuId);
                        return ResultCode.GOODS_NOT_SALE.getResp_code();
                    }
                    int skuNum = Integer.parseInt(goodsSku.get("sku_num").toString());
                    if (buyCount > skuNum) {
                        LOG.info("-------商品sku库存不足---------goodsId: " + goodsId + "   skuId:" + skuId + "  skuNum:" + skuNum);
                        return ResultCode.GOODS_STORE_NOENOUGH.getResp_code();
                    }
                }
                int goodsNum = Integer.parseInt(goods.get("goods_num").toString());
                if (buyCount > goodsNum) {
                    LOG.info("-------商品库存不足---------goodsId: " + goodsId + "  goodsNum:" + goodsNum);
                    return ResultCode.GOODS_STORE_NOENOUGH.getResp_code();
                }
                // 商品限购
                // 限购方式: 1.按天 2按月 3.按年 4不限购 5按次
                int limitWay = Integer.parseInt(goods.get("limit_way").toString());
                int limitNum = Integer.parseInt(goods.get("limit_num").toString());
                String limitBeginTime = goods.get("limit_begin_time").toString();
                int checkFlag = checkLimitWay(userId, limitWay, limitNum, limitBeginTime, goodsId, skuId, 0, buyCount);
                if (checkFlag > 0) {
                    return checkFlag;
                }
                // 租赁类型商品与其它类型商品不能同时形成订单
                int goodsType = Integer.parseInt(goods.get("goods_type").toString());
                if (goodsType == 3) {
                    goodsTypeMap.put(3, goodsType);
                } else {
                    goodsTypeMap.put(-1, goodsType);
                }
            }
            // 租赁类型商品与其它类型商品不能同时形成订单
            if (goodsTypeMap.size() > 1) {
                LOG.info("租赁类型商品与其它类型商品不能同时形成订单");
                return ResultCode.GOODS_TYPE_ERROR.getResp_code();
            }
            return ResultCode.OK.getResp_code();
        } catch (Exception e) {
            LOG.error("-----checkGoodsInfo------", e);
        }
        return ResultCode.NET_ERROR.getResp_code();
    }


    /**
     * // 限购方式: 1.按天 2按月 3.按年 4不限购 5按数量
     *
     * @return
     */
    private int checkLimitWay(int userId, int limitWay, int limitNum, String limitBeginTime, int goodsId, Integer skuId, int timeGoodsId, int buyCount) {
        try {
            if (limitWay == 4) {
                return ResultCode.OK.getResp_code();
            }
            // 查询商品订单
            List<Map<String, Object>> limitGoodsList = iOrderDao.findUserOrderGoodsByLimitTime(userId, limitBeginTime, skuId, goodsId, timeGoodsId);
            if (null == limitGoodsList) {
                LOG.info("======查询订单商品异常======");
                return ResultCode.FAIL.getResp_code();
            }
            if (limitGoodsList.size() == 0) {
                return ResultCode.OK.getResp_code();
            }
            String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
            Map<String, Integer> dayMap = new HashMap<>();
            Map<String, Integer> yearAndMonthsMap = new HashMap<>();
            Map<String, Integer> yearMap = new HashMap<>();
            int getCount = 0;
            for (int i = 0; i < limitGoodsList.size(); i++) {
                int goodsCount = Integer.parseInt(limitGoodsList.get(i).get("goods_count").toString());
                String orderTime = limitGoodsList.get(i).get("order_time").toString();
                String[] arrayDay_ = orderTime.split("-");
                String year_ = arrayDay_[0];// 2021
                String yearAndMonths_ = arrayDay_[0] + "-" + arrayDay_[1];// 2021-01
                if (orderTime.equals(now)) {
                    if (dayMap.get(now) == null) {
                        dayMap.put(now, goodsCount);
                    } else {
                        dayMap.put(now, dayMap.get(now) + goodsCount);
                    }
                }
                if (yearAndMonthsMap.get(yearAndMonths_) == null) {
                    yearAndMonthsMap.put(yearAndMonths_, goodsCount);
                } else {
                    yearAndMonthsMap.put(yearAndMonths_, yearAndMonthsMap.get(yearAndMonths_) + goodsCount);
                }
                if (yearMap.get(year_) == null) {
                    yearMap.put(year_, goodsCount);
                } else {
                    yearMap.put(year_, yearMap.get(year_) + goodsCount);
                }
                // 已购买总数
                getCount = getCount + goodsCount;
            }
            String[] arrayDay = now.split("-");
            String year = arrayDay[0];// 2021
            String yearAndMonths = arrayDay[0] + "-" + arrayDay[1];// 2021-01
            // 限购方式: 1.按天 2按月 3.按年 4不限购 5按数量
            if (limitWay == 1) {

                if (dayMap.get(now) > (limitNum - buyCount)) {
                    LOG.info("========按天===限领=======" + now);
                    return ResultCode.LIMIT_GOODS_RULE.getResp_code();
                }
            } else if (limitWay == 2) {
                if (yearAndMonthsMap.get(yearAndMonths) > (limitNum - buyCount)) {
                    LOG.info("========按月===限领=======" + now);
                    return ResultCode.LIMIT_GOODS_RULE.getResp_code();
                }
            } else if (limitWay == 3) {
                if (yearMap.get(year) > (limitNum - buyCount)) {
                    LOG.info("========按年===限领=======" + now);
                    return ResultCode.LIMIT_GOODS_RULE.getResp_code();
                }
            } else if (limitWay == 5) {
                if (getCount > (limitNum - buyCount)) {
                    LOG.info("========按数量===限领=======" + now);
                    return ResultCode.LIMIT_GOODS_RULE.getResp_code();
                }
            }
            return ResultCode.OK.getResp_code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCode.NET_ERROR.getResp_code();
    }


    /**
     * 运费处理
     *
     * @param addrIdsFreightMap
     * @param addrIdsArray
     * @return 运费
     */
    @Override
    public BigDecimal findPostPrice(Map<Integer, BigDecimal> addrIdsFreightMap, String[] addrIdsArray) {
        //用户默认收货地址运费
        BigDecimal userAddrDefaulFreight = new BigDecimal("20.00");
        if (addrIdsArray.length != 4) {
            LOG.info("------------findPostPrice-----默认运费----------------");
            return userAddrDefaulFreight;
        }
        BigDecimal freight = new BigDecimal("0.00");
        //镇
        Integer freight4 = Integer.parseInt(addrIdsArray[3]);
        //县
        Integer freight3 = Integer.parseInt(addrIdsArray[2]);
        //市
        Integer freight2 = Integer.parseInt(addrIdsArray[1]);
        //省
        Integer freight1 = Integer.parseInt(addrIdsArray[0]);

        // 地址如果失效，则默认运费
        BigDecimal f4 = addrIdsFreightMap.get(freight4);
        BigDecimal f3 = addrIdsFreightMap.get(freight3);
        BigDecimal f2 = addrIdsFreightMap.get(freight2);
        BigDecimal f1 = addrIdsFreightMap.get(freight1);
        if (null == f1 || null == f2 || null == f3 || null == f4) {
            //默认运费
            LOG.info("-------默认运费------");
            freight = userAddrDefaulFreight;
        } else {
            // 取最大的一个
            BigDecimal maxfreight = f1;
            if (maxfreight.compareTo(f2) < 0) {
                maxfreight = f2;
            }
            if (maxfreight.compareTo(f3) < 0) {
                maxfreight = f3;
            }
            if (maxfreight.compareTo(f4) < 0) {
                maxfreight = f4;
            }
            freight = maxfreight;
        }
        return freight;
    }


}
