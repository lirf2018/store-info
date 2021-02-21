package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
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

            // 购物车
            String cartIds = data.getString("cart_ids");// 购物车ids
            Integer userId = data.getInteger("user_id");//
            String goodsIds = goodsId == null ? "" : String.valueOf(goodsId);
            // keyMap  key=goodsId-skuId
            Map<String, Integer> keyMap = new HashMap<>();
            String key = goodsIds + "-" + (skuId == null ? "" : skuId);
            keyMap.put(key, buyCount);
            int cartCount = 0;
            if (StringUtils.isNotEmpty(cartIds)) {
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

                int shopId = 0;
                for (int i = 0; i < cartGoodsList.size(); i++) {
                    int goodsId_ = Integer.parseInt(cartGoodsList.get(i).get("goods_id").toString());
                    int shopId_ = Integer.parseInt(cartGoodsList.get(i).get("shop_id").toString());
                    int goodsCount_ = Integer.parseInt(cartGoodsList.get(i).get("goods_count").toString());
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
                }
                if (goodsIds.endsWith(",")) {
                    goodsIds = goodsIds.substring(0, goodsIds.length() - 1);
                }
            }
            // 输出商品列表
            List<Map<String, Object>> outGoodsList = new ArrayList<>();
            // 查询商品
            List<Map<String, Object>> goodsData = iGoodsDao.findAllGoodsDetailInfo(goodsIds);
            if (CollectionUtils.isEmpty(goodsData)) {
                LOG.info("----------查询商品----------");
                return packagMsg(ResultCode.PART_GOODS_OUTTIME_ERROR.getResp_code(), dataJson);
            }
            BigDecimal goodsPriceAll = BigDecimal.ZERO;
            for (Map.Entry<String, Integer> map : keyMap.entrySet()) {
                String goodsKey = map.getKey();//goodsId-skuId
                Integer buyCount_ = map.getValue();
                for (int i = 0; i < goodsData.size(); i++) {
                    String goodsId_ = goodsData.get(i).get("goods_id").toString();
                    String skuId_ = goodsData.get(i).get("sku_id").toString();
                    String goodsImg = goodsData.get(i).get("goods_img").toString();
                    String goodsName = goodsData.get(i).get("goods_name").toString();
                    int isSingle = Integer.parseInt(goodsData.get(i).get("is_single").toString());
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
                    String goodsKey_ = goodsId_ + "-" + skuId_;//goodsId-skuId
                    if (!goodsKey.equals(goodsKey_)) {
                        continue;
                    }
                    //
                    Map<String, Object> outGoodsMap = new HashMap<>();
                    outGoodsMap.put("goodsId", goodsId);
                    outGoodsMap.put("goodsName", goodsName);
                    outGoodsMap.put("trueMoney", trueMoney);
                    outGoodsMap.put("nowMoney", nowMoney);
                    outGoodsMap.put("goodsImg", goodsImg);
                    outGoodsMap.put("buyCount", buyCount_);
                    if (isSingle == 0) {
                        outGoodsMap.put("goodsName", skuName);
                        outGoodsMap.put("trueMoney", skuTrueMoney);
                        outGoodsMap.put("nowMoney", skuNowMoney);
                        outGoodsMap.put("skuImg", skuImg);
                    }
                    outGoodsMap.put("skuId", skuId_);
                    outGoodsMap.put("propCode", propCode);
                    outGoodsMap.put("propCodeName", propCodeName);
                    // 计算商品总价
                    BigDecimal goodsPrice = new BigDecimal(outGoodsMap.get("nowMoney").toString()).multiply(new BigDecimal(buyCount_.toString()));
                    goodsPriceAll = goodsPriceAll.add(goodsPrice);
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

            // 输出
            dataJson.put("goods_price_all", goodsPriceAll);
            dataJson.put("goods_list", outGoodsList);// 输出商品列表
            dataJson.put("shop_name", shopName);// 店铺名称
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