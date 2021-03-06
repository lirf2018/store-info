package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.pojo.TbOrderCart;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.goods.IGoodsJpaDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.service.impl.Test;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 10:44
 * 功能介绍: 增加购物车
 */
@Service("add_order_cart")
public class AddOrderCart implements IResultOut {

    private Logger LOG = Logger.getLogger(Test.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private IGoodsJpaDao iGoodsJpaDao;

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private ICategoryDao iCategoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer goodsId = data.getInteger("goods_id");
            Integer userId = data.getInteger("user_id");
            Integer timeGoodsId = data.getInteger("time_goods_id");
            String nowMoney = data.getString("now_price");
            Integer addModel = data.containsKey("add_model") ? data.getInteger("add_model") : 0;// 区分是购物车添加 还是直接商品详情添加到购物车
            Integer goodsCount = data.getInteger("goods_count") == 0 ? 1 : data.getInteger("goods_count");
            String goodsSpec = data.getString("goods_spec") == null ? "" : data.getString("goods_spec");
            // 删除购物车
            Integer delMark = data.getInteger("del_mark");
            if (null != delMark && delMark == 1) {
                iOrderDao.deleteShopcart(userId, goodsId);
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }

            //
            Integer cartId_ = data.getInteger("cart_id");//购物车标识
            if (null != cartId_ && cartId_ > 0) {
                //根据购物车标识更新
                iOrderDao.updateShopCart(userId, cartId_, goodsCount);
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }

            TbGoods goods = iGoodsJpaDao.findOne(goodsId);
            if (goods == null) {
                return packagMsg(ResultCode.GOODS_NOT_SALE.getResp_code(), dataJson);
            }
            if (goods.getIsSingle() == 0 && StringUtils.isEmpty(goodsSpec)) {
                return packagMsg(ResultCode.GOODS_SKU_NEED.getResp_code(), dataJson);
            }
            BigDecimal goodsPrice = BigDecimal.ZERO;//销售价格
            BigDecimal timePrice = null;//抢购价格
            BigDecimal trueMoney = new BigDecimal(0);//商品原价
            Integer skuId = null;
            String goodsSpecName = "";
            String goodsSpecNameStr = "";
            if (timeGoodsId != null && timeGoodsId > 0 && goods.getIsSingle() == 1 && goods.getIsTimeGoods() > 0) {
                // 查询抢购商品信息
                List<Map<String, Object>> list = iGoodsDao.queryTimeGoodsListMap(goodsId.toString());
                if (CollectionUtils.isNotEmpty(list)) {
                    Map<String, Object> timeGoods = list.get(0);
                    int goodsStore = Integer.parseInt(timeGoods.get("goods_store").toString());
                    if (goodsStore <= 0) {
                        return packagMsg(ResultCode.TIMEGOODS_STORE_EMPTY.getResp_code(), dataJson);
                    }
                    timePrice = new BigDecimal(timeGoods.get("time_price").toString());
                } else {
                    // 失效购物车 对应抢购商品
                    iOrderDao.updateOrderCartStatus(userId, goodsId.toString(), 2, 1);
                }
            }

            //查询选择的规格
            if (StringUtils.isNotEmpty(goodsSpec)) {
                String valueIds = goodsSpec.replace(";", ",");
                valueIds = valueIds.substring(0, valueIds.length() - 1);
                List<Map<String, Object>> list = iCategoryDao.loadPropValueItem(valueIds);
                for (int i = 0; i < list.size(); i++) {
                    //Integer valueId = Integer.parseInt(list.get(i).get("value_id").toString());
                    String propName = list.get(i).get("prop_name").toString();
                    String valueName = list.get(i).get("value_name").toString();

                    goodsSpecName = goodsSpecName + valueName + ";";
                    goodsSpecNameStr = goodsSpecNameStr + propName + ":" + valueName + ";";
                }
                if (StringUtils.isEmpty(goodsSpecName) || StringUtils.isEmpty(goodsSpecNameStr)) {
                    LOG.info("----->查询规格失败1");
                    return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
                }
                //查询商品sku
                TbGoodsSku goodsSku = iGoodsDao.loadGoodsSku(goodsId, goodsSpec);
                if (null == goodsSku || goodsSku.getSkuId() == 0) {
                    LOG.info("----->查询规格失败2");
                    return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
                }
                skuId = goodsSku.getSkuId();
                goodsPrice = goodsSku.getNowMoney();
                trueMoney = goodsSku.getTrueMoney();
                goods.setGoodsImg(goodsSku.getSkuImg());
            } else {
                goodsPrice = goods.getNowMoney();
                trueMoney = goods.getTrueMoney();
                if (null != timePrice) {
                    goodsPrice = timePrice;
                }
            }

            // 购物车 商品总数
            int cartGoodsCount = iOrderDao.userCartCount(userId, goodsId);
            //购物车数量限制
            if ((cartGoodsCount + goodsCount) >= 50) {
                return packagMsg(ResultCode.FULL_ORDER_CARD.getResp_code(), dataJson);
            }

            boolean flag = false;
            //查询用户购物车
            List<Map<String, Object>> cartList = new ArrayList<>();
            if (StringUtils.isEmpty(goodsSpec)) {
                cartList = iGoodsDao.queryUserOrderCart(userId, goodsId);
            } else {
                cartList = iGoodsDao.queryUserOrderCart(userId, goodsId, goodsSpec);
            }
            for (int i = 0; i < cartList.size(); i++) {
                //c.cart_id,c.goods_id,c.goods_spec,c.goods_count,c.goods_price,c.true_money
                int cartId = Integer.parseInt(cartList.get(i).get("cart_id").toString());
                int goodsCount_ = Integer.parseInt(cartList.get(i).get("goods_count").toString());
                int count = goodsCount_ + goodsCount;
                if (addModel == 1) {
                    count = goodsCount;
                }
                flag = true;
                //判断是规格商品还是单品
                iGoodsDao.updateOrderCart(cartId, count, goodsSpec, goodsSpecName, goodsSpecNameStr, goodsPrice, trueMoney, timeGoodsId);
                break;
            }

            if (!flag) {
                //新增加购物车
                TbOrderCart orderCart = new TbOrderCart();
                orderCart.setUserId(userId);
                orderCart.setGoodsId(goodsId);
                orderCart.setSkuId(skuId);
                orderCart.setGoodsName(goods.getGoodsName());
                orderCart.setGoodsImg(goods.getGoodsImg());
                orderCart.setGoodsSpec(goodsSpec);
                orderCart.setGoodsSpecName(goodsSpecName);
                orderCart.setGoodsCount(goodsCount);
                orderCart.setGoodsPrice(goodsPrice);
                orderCart.setTrueMoney(trueMoney);
                orderCart.setTimeGoodsId(timeGoodsId);
                orderCart.setShopId(goods.getShopId());
                orderCart.setCreatetime(new Timestamp(new Date().getTime()));
                orderCart.setStatus(1);
                orderCart.setLastaltertime(new Timestamp(new Date().getTime()));
                orderCart.setGoodsSpecNameStr(goodsSpecNameStr);
                iGoodsDao.saveOrderCart(orderCart);
            }

            dataJson.put("cart_goods_count", (cartGoodsCount + goodsCount));//购物车数量

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
            Integer cartId = data.getInteger("cart_id");//购物车标识
            Integer goodsId = data.getInteger("goods_id");
            Integer userId = data.getInteger("user_id");
            if (null == userId) {
                return false;
            }

            if (null != cartId && cartId > 0) {
                return true;
            }

            if (null == goodsId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
