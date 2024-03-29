package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.UserCartOrderDetail;
import com.yufan.bean.UserOrderCart;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:03
 * 功能介绍: 查询购物车
 */
@Service("query_order_cart")
public class QueryOrderCarts implements IResultOut {


    private Logger LOG = Logger.getLogger(QueryOrderCarts.class);

    @Autowired
    private IOrderDao iOrderDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            Integer carType = data.getInteger("car_type");

            // 先清理失效的购物车数据
            iOrderDao.clearOrderCart(userId);
            List<Map<String, Object>> mapList = iOrderDao.queryUserOrderCartListMap(userId, carType);
            List<UserCartOrderDetail> outTimeGoodsList = new ArrayList<>();//失效商品

            List<UserOrderCart> outList = new ArrayList<>();
            Map<Integer, Integer> markMap = new HashMap<>();
            //mapList 店铺
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map = mapList.get(i);
                int shopId = Integer.parseInt(map.get("shop_id").toString());
                String shopName = map.get("shop_name").toString();
                String shopLogo = null == map.get("shop_logo") ? "" : Constants.IMG_WEB_URL + map.get("shop_logo").toString();
                if (markMap.get(shopId) != null) {
                    continue;
                }
                UserOrderCart shop = new UserOrderCart();
                shop.setShopId(shopId);
                shop.setShopName(shopName);
                shop.setShopLogo(shopLogo);
                outList.add(shop);
                //去重
                markMap.put(shopId, shopId);
            }

            int orderCount = 0;
            boolean showCartEmptyFlag = true;// true 没有商品 false 有商品

            //购物车详情
            for (int i = 0; i < outList.size(); i++) {
                UserOrderCart shop = outList.get(i);
                List<UserCartOrderDetail> cartDetailList = new ArrayList<>();
                for (int j = 0; j < mapList.size(); j++) {
                    Map<String, Object> map = mapList.get(j);
                    int shopId = Integer.parseInt(map.get("shop_id").toString());
                    if (shopId != shop.getShopId()) {
                        continue;
                    }
                    int cartId = Integer.parseInt(map.get("cart_id").toString());
                    Integer goodsId = Integer.parseInt(map.get("goods_id").toString());
                    String goodsName = map.get("goods_name").toString();
                    String goodsImg = Constants.IMG_WEB_URL + map.get("goods_img").toString();
                    String goodsSpec = null == map.get("goods_spec") ? "" : map.get("goods_spec").toString();
                    String goodsSpecName = null == map.get("goods_spec_name") ? "" : map.get("goods_spec_name").toString();
                    Integer goodsCount = Integer.parseInt(map.get("goods_count").toString());
                    BigDecimal goodsPrice = new BigDecimal(map.get("goods_price").toString());
                    BigDecimal trueMoney = new BigDecimal(map.get("true_money").toString());
                    Integer status = Integer.parseInt(map.get("status").toString());
                    Integer isSingle = Integer.parseInt(map.get("is_single").toString());
                    Integer skuId = Integer.parseInt(map.get("sku_id").toString());
                    Integer timeGoodsId = Integer.parseInt(map.get("time_goods_id").toString());
                    //如果商品的修改时间>购物车商品的创建时间,则购物车视为无效
                    int lastStatus = Integer.parseInt(map.get("last_status").toString());
                    if (lastStatus == 2) {
                        status = lastStatus;
                    }

                    if (!(status == 1 || status == 2)) {
                        continue;
                    }
                    Integer goodsType = Integer.parseInt(map.get("goods_type").toString());
                    String rentPayTypeName = String.valueOf(map.get("rent_pay_type_name"));

                    String goodsSpecNameStr = null == map.get("goods_spec_name_str") ? "" : map.get("goods_spec_name_str").toString();
                    Integer cartType = Integer.parseInt(null == map.get("status") ? null : map.get("status").toString());
                    UserCartOrderDetail userCartOrderDetail = new UserCartOrderDetail();
                    userCartOrderDetail.setCartId(cartId);
                    userCartOrderDetail.setUserId(userId);
                    userCartOrderDetail.setGoodsId(goodsId);
                    userCartOrderDetail.setGoodsImg(goodsImg);
                    userCartOrderDetail.setGoodsName(goodsName);
                    userCartOrderDetail.setGoodsSpec(goodsSpec);
                    userCartOrderDetail.setGoodsSpecName(goodsSpecName);
                    userCartOrderDetail.setGoodsCount(goodsCount);
                    userCartOrderDetail.setGoodsPrice(goodsPrice);
                    userCartOrderDetail.setTrueMoney(trueMoney);
                    userCartOrderDetail.setStatus(status);
                    userCartOrderDetail.setGoodsSpecNameStr(goodsSpecNameStr);
                    userCartOrderDetail.setCartType(cartType);
                    userCartOrderDetail.setIsSingle(isSingle);
                    userCartOrderDetail.setSkuId(skuId);
                    userCartOrderDetail.setTimeGoodsId(timeGoodsId);
                    userCartOrderDetail.setGoodsType(goodsType);
                    userCartOrderDetail.setRentPayTypeName(rentPayTypeName);
                    if (status == 1) {
                        cartDetailList.add(userCartOrderDetail);//
                        orderCount = orderCount + 1;
                    } else {
                        outTimeGoodsList.add(userCartOrderDetail);
                    }
                }
                if (!CollectionUtils.isEmpty(cartDetailList)) {
                    shop.setShowCartEmptyFlag(false);
                    // 有商品
                    showCartEmptyFlag = false;
                }
                shop.setCartDetailList(cartDetailList);
            }

            dataJson.put("cartGoodsCount", orderCount);//购物车数量
            dataJson.put("cartList", outList);
            dataJson.put("showCartEmptyFlag", showCartEmptyFlag);
            dataJson.put("outTimeCartList", outTimeGoodsList);
            String cartNullImg = Constants.IMG_WEB_URL + "shopcart.jpg";
            dataJson.put("cartNullImg", cartNullImg);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-----error-----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            if (null == userId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}