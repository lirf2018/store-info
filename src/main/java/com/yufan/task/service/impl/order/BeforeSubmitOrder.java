package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.Constants;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 下单页面
 * @author: lirf
 * @time: 2021/2/19
 */
@Service("before_submit_order")
public class BeforeSubmitOrder implements IResultOut {

    private Logger LOG = Logger.getLogger(BeforeSubmitOrder.class);


    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            //商品
            Integer goodsId = data.getInteger("goods_id");// 商品id
            Integer skuId = data.getInteger("sku_id");// 商品sku
            Integer buyCount = data.getInteger("buy_count");// 购买数量
            Integer timeGoodsId = data.getInteger("time_goods_id");//

            // 购物车
            String cartIds = data.getString("cart_ids");// 购物车ids
            Integer userId = data.getInteger("user_id");//
            String goodsIds = goodsId == null ? "" : String.valueOf(goodsId);
            String goodsIdsForTimeGoods = goodsIds;// 查询抢购商品
            // 用于存在商品与购物车对应
            Map<String, Map<String, Object>> cartGoodsMap = new HashMap<>();
            // keyMap  key=goodsId-skuId
            Map<String, Integer> keyMap = new HashMap<>();
            String key = goodsIds + "-" + (skuId == null ? "" : skuId);
            keyMap.put(key, buyCount);
            int cartCount = 0;
            if (StringUtils.isNotEmpty(cartIds)) {
                // 购物车
                keyMap = new HashMap<>();
                goodsIds = "";
                if (cartIds.endsWith(",")) {
                    cartIds = cartIds.substring(0, cartIds.length() - 1);
                }
                // 查询购物车
                String[] cartIdsArray = cartIds.split(",");
                cartCount = cartIdsArray.length;
                List<Map<String, Object>> cartGoodsList = iOrderDao.findCartsByCartIds(userId, cartIds);
                if (CollectionUtils.isEmpty(cartGoodsList) || cartCount != cartGoodsList.size()) {
                    LOG.info("---------查询购物车商品不一致------------");
                    return packagMsg(ResultCode.PART_GOODS_OUTTIME_ERROR.getResp_code(), dataJson);
                }
                // 租赁类型商品与其它类型商品不能同时形成订单
                Map<Integer, Integer> goodsTypeMap = new HashMap<>();
                int shopId = 0;
                for (int i = 0; i < cartGoodsList.size(); i++) {
                    int goodsId_ = Integer.parseInt(cartGoodsList.get(i).get("goods_id").toString());
                    int shopId_ = Integer.parseInt(cartGoodsList.get(i).get("shop_id").toString());
                    int goodsCount_ = Integer.parseInt(cartGoodsList.get(i).get("goods_count").toString());
                    int timeGoodsId_ = Integer.parseInt(cartGoodsList.get(i).get("time_goods_id").toString());
                    // 租赁类型商品与其它类型商品不能同时形成订单
                    int goodsType = Integer.parseInt(cartGoodsList.get(i).get("goods_type").toString());
                    if (goodsType == Constants.GOODS_TYPE_3) {
                        goodsTypeMap.put(Constants.GOODS_TYPE_3, Constants.GOODS_TYPE_3);
                    } else {
                        goodsTypeMap.put(-1, goodsType);
                    }

                    if (timeGoodsId_ > 0) {
                        goodsIdsForTimeGoods = goodsIdsForTimeGoods + timeGoodsId_ + ",";
                    }
                    if (shopId != 0) {
                        if (shopId != shopId_) {
                            LOG.info("------shopId != shopId_-------");
                            return packagMsg(ResultCode.PART_GOODS_OUTTIME_ERROR.getResp_code(), dataJson);
                        }
                    }
                    shopId = shopId_;
                    String sku_id_ = cartGoodsList.get(i).get("sku_id") == null ? "" : cartGoodsList.get(i).get("sku_id").toString();
                    goodsIds = goodsIds + goodsId_ + ",";
                    //
                    key = goodsId_ + "-" + sku_id_;
                    keyMap.put(key, goodsCount_);
                    //
                    String key2 = goodsId_ + "_" + sku_id_;
                    cartGoodsMap.put(key2, cartGoodsList.get(i));
                }
                if (goodsIds.endsWith(",")) {
                    goodsIds = goodsIds.substring(0, goodsIds.length() - 1);
                }
                // 租赁类型商品与其它类型商品不能同时形成订单
                if (goodsTypeMap.size() > 1) {
                    LOG.info("租赁类型商品与其它类型商品不能同时形成订单");
                    return packagMsg(ResultCode.GOODS_TYPE_ERROR.getResp_code(), dataJson);
                }
            }
            // 查询抢购商品
            List<Map<String, Object>> timeGoodsListMap = new ArrayList<>();
            if (StringUtils.isNotEmpty(goodsIdsForTimeGoods)) {
                if (goodsIdsForTimeGoods.equals(",")) {
                    goodsIdsForTimeGoods = goodsIdsForTimeGoods.substring(0, goodsIdsForTimeGoods.length() - 1);
                }
                timeGoodsListMap = iGoodsDao.queryTimeGoodsListMap(goodsIdsForTimeGoods);
            }
            Map<String, Map<String, Object>> timeGoodsMap = new HashMap<>();
            for (int i = 0; i < timeGoodsListMap.size(); i++) {
                String goodsId_ = timeGoodsListMap.get(i).get("goods_id").toString();
                timeGoodsMap.put(goodsId_, timeGoodsListMap.get(i));
            }
            // 输出商品列表
            List<Map<String, Object>> outGoodsList = new ArrayList<>();
            // 查询Db商品信息
            List<Map<String, Object>> goodsData = iGoodsDao.findAllGoodsDetailInfo(goodsIds);
            if (CollectionUtils.isEmpty(goodsData)) {
                LOG.info("----------查询商品----------");
                return packagMsg(ResultCode.PART_GOODS_OUTTIME_ERROR.getResp_code(), dataJson);
            }
            //
            int commonGoodsType = -1;//普通商品类型
            //
            BigDecimal goodsPriceAll = BigDecimal.ZERO;//
            BigDecimal goodsTruePriceAll = BigDecimal.ZERO;// 商品现价总和
            BigDecimal advancePriceAll = BigDecimal.ZERO;
            BigDecimal depositMoneyAll = BigDecimal.ZERO;
            for (Map.Entry<String, Integer> map : keyMap.entrySet()) {
                String goodsKey = map.getKey();//goodsId-skuId
                Integer buyCount_ = map.getValue();
                for (int i = 0; i < goodsData.size(); i++) {
                    String goodsId_ = goodsData.get(i).get("goods_id").toString();
                    String skuId_ = goodsData.get(i).get("sku_id").toString();
                    String goodsImg = goodsData.get(i).get("goods_img").toString();
                    String goodsName = goodsData.get(i).get("goods_name").toString();
                    int isSingle = Integer.parseInt(goodsData.get(i).get("is_single").toString());
                    commonGoodsType = Integer.parseInt(goodsData.get(i).get("goods_type").toString());
                    BigDecimal trueMoney = new BigDecimal(goodsData.get(i).get("true_money").toString());
                    BigDecimal nowMoney = new BigDecimal(goodsData.get(i).get("now_money").toString());
                    // sku
                    String skuName = goodsData.get(i).get("sku_name").toString();
                    String skuImg = goodsData.get(i).get("sku_img").toString();
                    BigDecimal skuTrueMoney = new BigDecimal(goodsData.get(i).get("sku_true_money").toString());
                    BigDecimal skuNowMoney = new BigDecimal(goodsData.get(i).get("sku_now_money").toString());
                    String propCode = goodsData.get(i).get("prop_code").toString();
                    String propCodeName = goodsData.get(i).get("prop_code_name").toString();
                    //
                    String rentPayTypeName = String.valueOf(goodsData.get(i).get("rent_pay_type_name"));
                    String goodsType = String.valueOf(goodsData.get(i).get("goods_type"));
                    //
                    String goodsKey_ = goodsId_ + "-" + skuId_;//goodsId-skuId
                    if (!goodsKey.equals(goodsKey_)) {
                        continue;
                    }
                    //
                    int timeGoodsIdDb = 0;
                    if (null != timeGoodsMap.get(goodsId_)) {
                        timeGoodsIdDb = Integer.parseInt(timeGoodsMap.get(goodsId_).get("time_goods_id").toString());
                        if (StringUtils.isEmpty(cartIds) && null != timeGoodsId && timeGoodsId == timeGoodsIdDb) {
                            nowMoney = new BigDecimal(timeGoodsMap.get(goodsId_).get("time_price").toString());
                        }
                    }
                    //
                    Map<String, Object> outGoodsMap = new HashMap<>();
                    outGoodsMap.put("goodsId", goodsId_);
                    outGoodsMap.put("goodsName", goodsName);
                    outGoodsMap.put("trueMoney", trueMoney);
                    outGoodsMap.put("nowMoney", nowMoney);
                    outGoodsMap.put("goodsImg", goodsImg);
                    outGoodsMap.put("buyCount", buyCount_);
                    if (isSingle == 0) {
                        outGoodsMap.put("trueMoney", skuTrueMoney);
                        outGoodsMap.put("nowMoney", skuNowMoney);
                        outGoodsMap.put("skuImg", skuImg);
                    }
                    outGoodsMap.put("skuId", skuId_);
                    outGoodsMap.put("propCode", propCode);
                    outGoodsMap.put("propCodeName", propCodeName);
                    outGoodsMap.put("timeGoodsId", timeGoodsId);
                    //
                    outGoodsMap.put("goodsType", goodsType);
                    outGoodsMap.put("rentPayTypeName", rentPayTypeName);
                    Integer cartId = 0;
                    String key2 = goodsId_ + "_" + skuId_;
                    if (cartGoodsMap.get(key2) != null) {
                        cartId = Integer.parseInt(cartGoodsMap.get(key2).get("cart_id").toString());
                    }
                    outGoodsMap.put("cartId", cartId);
                    // 计算商品总价
                    BigDecimal salePrice = new BigDecimal(outGoodsMap.get("nowMoney").toString());
                    BigDecimal goodsPrice = salePrice.multiply(new BigDecimal(buyCount_.toString()));
                    goodsPriceAll = goodsPriceAll.add(goodsPrice);
                    // 计算商品原价总和
                    BigDecimal saleTruePrice = new BigDecimal(outGoodsMap.get("trueMoney").toString());
                    BigDecimal goodsTruePrice = saleTruePrice.multiply(new BigDecimal(buyCount_.toString()));
                    goodsTruePriceAll = goodsTruePriceAll.add(goodsTruePrice);
                    // 预付款总价
                    BigDecimal advancePrice = new BigDecimal(goodsData.get(i).get("advance_price").toString());
                    advancePriceAll = advancePriceAll.add(advancePrice.multiply(new BigDecimal(buyCount_.toString())));
                    // 押金总价
                    BigDecimal depositMoney = new BigDecimal(goodsData.get(i).get("deposit_money").toString());
                    depositMoneyAll = depositMoneyAll.add(depositMoney.multiply(new BigDecimal(buyCount_.toString())));
                    //
                    outGoodsList.add(outGoodsMap);
                    break;
                }
            }
            //
            boolean flag = false;
            if (StringUtils.isNotEmpty(cartIds)) {
                if (cartCount != outGoodsList.size()) {
                    flag = true;
                }
            } else {
                if (outGoodsList.size() != 1) {
                    flag = true;
                }
            }
            if (flag) {
                LOG.info("----------处理结果不一致----------");
                return packagMsg(ResultCode.PART_GOODS_OUTTIME_ERROR.getResp_code(), dataJson);
            }

            String shopName = goodsData.get(0).get("shop_name").toString();
            String shopId = goodsData.get(0).get("shop_id").toString();

            // 输出
            dataJson.put("common_goods_type", commonGoodsType);
            dataJson.put("goods_price_all", goodsPriceAll);
            dataJson.put("goods_true_price_all", goodsTruePriceAll);
            dataJson.put("deposit_money_all", depositMoneyAll);
            dataJson.put("advance_price_all", advancePriceAll);
            dataJson.put("goods_list", outGoodsList);// 输出商品列表
            dataJson.put("shop_name", shopName);// 店铺名称
            dataJson.put("shop_id", shopId);//
            // orderPrice
            BigDecimal orderPrice = BigDecimal.ZERO;
            // 原价商品价格总额 + 押金总额 + 邮费总额(根据页面选择的相加)
            orderPrice = orderPrice.add(goodsTruePriceAll).add(depositMoneyAll);
            dataJson.put("order_price", orderPrice);
            // 商品优惠价格
            BigDecimal goodsDiscountsPrice = BigDecimal.ZERO;
            goodsDiscountsPrice = goodsTruePriceAll.subtract(goodsPriceAll);
            dataJson.put("goods_discounts_price", goodsDiscountsPrice);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer goodsId = data.getInteger("goods_id");// 商品id
            String cartIds = data.getString("cart_ids");// 购物车ids
            Integer userId = data.getInteger("user_id");//
            if (null == userId || (null == goodsId && StringUtils.isEmpty(cartIds))) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}