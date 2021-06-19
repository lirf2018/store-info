package com.yufan.kc.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbKcGoods;
import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.bean.TbKcOrderDetail;
import com.yufan.kc.dao.goods.GoodsDao;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.kc.dao.store.StoreInOutDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/15 16:00
 * 功能介绍:
 */
@Service("kc_open_order_add")
public class AddOpenOrder implements IResultOut {

    private Logger LOG = Logger.getLogger(FindOpenOrder.class);

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private OpenOrderDao openOrderDao;

    @Autowired
    private StoreInOutDao storeInOutDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer goodsId = data.getInteger("goods_id");
            String userPhone = data.getString("user_phone");//手机号(先不做会员)
            // 检验是否是会员,如果不是会员,则手机号不能有值
            if (StringUtils.isNotEmpty(userPhone)) {
                boolean isMember = false;
                if (!isMember) {
                    return packagMsg(ResultCode.MEMBER_NOT_EXIST.getResp_code(), dataJson);
                }
            }
            //
            String shopNo = data.getString("shop_no");
            if (StringUtils.isNotEmpty(shopNo) && !getLockGoods(shopNo)) {
                LOG.info("-----商品添加过快-----shopNo=" + shopNo);
                return packagMsg(ResultCode.ADD_TO_FAST.getResp_code(), dataJson);
            }
            //
            TbKcGoods goods = goodsDao.loadGoods(goodsId);
            if (null == goods) {
                LOG.info("-----------商品不存在或者已下架----goodsId=" + goodsId);
                return packagMsg(ResultCode.GOODS_NOT_SALE.getResp_code(), dataJson);
            }
            // 生成订单
            return createOrder(data, goods);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 控制添加商品速度(判断是否过期)
     */
    public synchronized boolean getLockGoods(String shopNo) {
        long now = System.currentTimeMillis() / 1000;// 秒
        LOG.info("-----------getLockGoods------------" + now);
        Long time = Constants.MAP_GOODS_CODE_LOCK.get(shopNo);
        if (null == time) {
            Constants.MAP_GOODS_CODE_LOCK.put(shopNo, now);
            return true;
        }
        int outTime = 2;// 过期时间2s
        // 判断是否过期
        if (now - time.longValue() > outTime) {
            // 已过期
            Constants.MAP_GOODS_CODE_LOCK.put(shopNo, now);
            return true;
        }
        return false;
    }

    /**
     * 促销折扣总价 = 订单总价格-促销总价
     * 会员折扣总价 = 订单总价格-会员总价
     * 促销折扣总价 = 订单总价格-促销总价
     */
    public synchronized String createOrder(JSONObject data, TbKcGoods goods) {
        LOG.info("-------createOrder------");
        JSONObject dataJson = new JSONObject();
        String shopNo = data.getString("shop_no");
        if (shopNo == null || !shopNo.startsWith("AUTO")) {
            //店铺码
            shopNo = "";
        }
        // 判断促销是否有过期
        int isDiscounts = goods.getIsDiscounts();
        if (isDiscounts == 1) {
            try {
                String format = "yyyy-MM-dd";
                String stdate = DatetimeUtil.timeStamp2Date(goods.getDiscountsStartTime().getTime(), format);
                String etdate = DatetimeUtil.timeStamp2Date(goods.getDiscountsEndTime().getTime(), format);
                String now = DatetimeUtil.getNow(format);
                LOG.info("-----------stdate=" + stdate + "        etime=" + etdate + "          now=" + now);
                if (!(DatetimeUtil.compareDate(now, stdate) >= 0 && DatetimeUtil.compareDate(now, etdate) <= 0)) {
                    LOG.error("----------促销已过期--------");
                    goods.setIsDiscounts((byte) 1);
                    goods.setDiscountsPrice(goods.getSalePrice());
                }
            } catch (Exception e) {
                LOG.error("----------格式化异常--------");
                goods.setIsDiscounts((byte) 1);
                goods.setDiscountsPrice(goods.getSalePrice());
            }
        }
        String uniqueKey = data.getString("unique_key");
        String orderNo = data.getString("order_no");
        String userPhone = data.getString("user_phone");//手机号(先不做会员)
        String goodsUnitName = data.getString("goods_unit_name");
        String discountsRemark = data.getString("discounts_remark");
        Integer consumeCount = data.containsKey("consume_count") ? data.getInteger("consume_count") : 0;
        //
        String serverName = data.getString("server_name");
        Integer personCount = data.getInteger("person_count");
        String tableName = data.getString("table_name");
        //
        Timestamp dateTime = new Timestamp(new Date().getTime());
        BigDecimal discountsTicketPrice = BigDecimal.ZERO;// 优惠券金额暂时不做
        BigDecimal salePriceTrue = getSalePriceTrue(goods.getIsDiscounts(), goods.getSalePrice(), goods.getMemberPrice(), goods.getDiscountsPrice(), userPhone);
        // 订单详情
        // 详情
        TbKcOrderDetail detail = new TbKcOrderDetail();
        detail.setGoodsId(goods.getGoodsId());
        detail.setBuyCount(1);
        detail.setGoodsCode(goods.getGoodsCode());
        detail.setShopCode(shopNo);
        detail.setGoodsName(goods.getGoodsName());
        detail.setSalePrice(goods.getSalePrice());
        detail.setSalePriceTrue(salePriceTrue);
        detail.setMemberPrice(goods.getMemberPrice());
        detail.setDiscountsPrice(goods.getDiscountsPrice());
        detail.setDiscountsStartTime(goods.getDiscountsStartTime());
        detail.setDiscountsEndTime(goods.getDiscountsEndTime());
        detail.setIsDiscounts(goods.getIsDiscounts());
        detail.setUnitCount(goods.getUnitCount());
        detail.setCreateTime(dateTime);
        detail.setLastUpdateTime(dateTime);
        detail.setGoodsUnitName(goodsUnitName);
        detail.setStatus(1);
        TbKcOrder order = new TbKcOrder();
        LOG.info("-------log-----------缓存订单号数---------" + Constants.ORDER_NO_KEY_MAP.size());
        if (StringUtils.isEmpty(orderNo)) {
            // 生成新订单
            //生成新订单号
            orderNo = Constants.ORDER_NO_KEY_MAP.get(uniqueKey);
            if (StringUtils.isEmpty(orderNo)) {
                orderNo = CommonMethod.randomStr("");
                LOG.info("------log-----生成新订单号------orderNo=" + orderNo);
                Constants.ORDER_NO_KEY_MAP.put(uniqueKey, orderNo);
            } else {
                //订单已存在
                order = openOrderDao.loadOrder(orderNo);
                if (null == order) {
                    LOG.info("-------查询订单不存在--------");
                    return packagMsg(ResultCode.ORDER_NOT_EXIST.getResp_code(), dataJson);
                }
                // 判断订单是否已支付
                if (order.getOrderStatus().intValue() != Constants.ORDER_STATUS_0) {
                    LOG.info("-------查询订单已付款--------");
                    return packagMsg(ResultCode.ORDER_IS_PAY.getResp_code(), dataJson);
                }

                // 检验所有订单商品的店铺编码是否已存在(保证一个出库)
                if (StringUtils.isNotEmpty(shopNo) && shopNo.length() != 13) {
                    boolean flag = storeInOutDao.checkShopNoOut(shopNo);
                    if (flag) {
                        // 订单详情中商品店铺编码已存在
                        LOG.info("-------订单详情中商品店铺编码已存在----shopNo=" + shopNo);
                        return packagMsg(ResultCode.ORDER_SHOPCODE_EXIST.getResp_code(), dataJson);
                    }
                }
                updateOrder(uniqueKey, orderNo, userPhone, discountsRemark, dateTime, discountsTicketPrice, detail, order, 0);

                dataJson.put("order_no", orderNo);
                dataJson.put("order_id", order.getOrderId());
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            BigDecimal orderPrice = BigDecimal.ZERO;
            BigDecimal realPrice = BigDecimal.ZERO;
            BigDecimal discountsMemberPrice = BigDecimal.ZERO;
            BigDecimal discountsPrice = BigDecimal.ZERO;
            orderPrice = goods.getSalePrice();
            realPrice = salePriceTrue.subtract(discountsTicketPrice);

            if (StringUtils.isNotEmpty(userPhone)) {
                // 是会员,计算会员折扣
                discountsMemberPrice = orderPrice.subtract(goods.getMemberPrice());
            }
            discountsPrice = orderPrice.subtract(goods.getDiscountsPrice());
            order.setOrderNum(orderNo);
            order.setUserId(0);
            order.setGoodsCount(1);
            order.setOrderPrice(orderPrice);
            order.setRealPrice(realPrice);
            order.setDiscountsTicketPrice(discountsTicketPrice);
            order.setDiscountsMemberPrice(discountsMemberPrice);
            order.setDiscountsPrice(discountsPrice);
            order.setDiscountsRemark(discountsRemark);
            order.setPayMethod(null);
            order.setConsumeCount(consumeCount);
            order.setOrderStatus(new Byte(String.valueOf(Constants.ORDER_STATUS_0)));
            order.setCreateTime(dateTime);
            order.setLastUpdateTime(dateTime);
            order.setOrderSource(new Integer(1).byteValue());
            order.setMemberNo("");
            order.setUserPhone(userPhone);
            order.setRemark("");
            order.setServerName(serverName);
            order.setPersonCount(personCount);
            order.setTableName(tableName);
            openOrderDao.saveObj(order);
            detail.setOrderId(order.getOrderId());
            openOrderDao.saveObj(detail);
        } else {
            //订单已存在
            order = openOrderDao.loadOrder(orderNo);
            if (null == order) {
                LOG.info("-------查询订单不存在--------");
                return packagMsg(ResultCode.ORDER_NOT_EXIST.getResp_code(), dataJson);
            }
            // 判断订单是否已支付
            if (order.getOrderStatus().intValue() != Constants.ORDER_STATUS_0) {
                LOG.info("-------查询订单已付款--------");
                return packagMsg(ResultCode.ORDER_IS_PAY.getResp_code(), dataJson);
            }

            // 检验所有订单商品的店铺编码是否已存在(保证一个出库)
            if (StringUtils.isNotEmpty(shopNo) && shopNo.length() != 13) {
                boolean flag = storeInOutDao.checkShopNoOut(shopNo);
                if (flag) {
                    // 订单详情中商品店铺编码已存在
                    LOG.info("-------订单详情中商品店铺编码已存在----shopNo=" + shopNo);
                    return packagMsg(ResultCode.ORDER_SHOPCODE_EXIST.getResp_code(), dataJson);
                }
            }
            updateOrder(uniqueKey, orderNo, userPhone, discountsRemark, dateTime, discountsTicketPrice, detail, order, 1);
        }
        dataJson.put("order_no", orderNo);
        dataJson.put("order_id", order.getOrderId());
        return packagMsg(ResultCode.OK.getResp_code(), dataJson);
    }

    private void updateOrder(String uniqueKey, String orderNo, String userPhone, String discountsRemark, Timestamp dateTime, BigDecimal discountsTicketPrice, TbKcOrderDetail detail, TbKcOrder order, int type) {
        // 计算订单价格
        Integer goodsCount = order.getGoodsCount() + 1;
        BigDecimal orderPriceAll = BigDecimal.ZERO;//订单总金额
        BigDecimal realPriceAll = BigDecimal.ZERO;//订单实付总金额
        //
        BigDecimal memberPriceAll = BigDecimal.ZERO;//会员价格总金额
        BigDecimal discountsPriceAll = BigDecimal.ZERO;//促销价格总金额


        List<TbKcOrderDetail> updateDetailList = new ArrayList<>();//如果用户是会员,则整个订单重新计算实付金额单价
        // 订单已添加的商品数(重新计算实付单价)
        List<TbKcOrderDetail> detailList = openOrderDao.loadOrderDetailList(order.getOrderId());
        detailList.add(detail);
        for (int i = 0; i < detailList.size(); i++) {
            TbKcOrderDetail de = detailList.get(i);
            // 实付单价
            BigDecimal trueGoodsSalePrice = getSalePriceTrue(de.getIsDiscounts(), de.getSalePrice(), de.getMemberPrice(), de.getDiscountsPrice(), userPhone);
            de.setSalePriceTrue(trueGoodsSalePrice);
            orderPriceAll = orderPriceAll.add(de.getSalePrice());
            realPriceAll = realPriceAll.add(trueGoodsSalePrice);
            memberPriceAll = memberPriceAll.add(de.getMemberPrice());
            discountsPriceAll = discountsPriceAll.add(de.getDiscountsPrice());
            updateDetailList.add(de);
        }
        order.setGoodsCount(goodsCount);
        order.setOrderPrice(orderPriceAll);
        order.setRealPrice(realPriceAll);
        order.setDiscountsRemark(discountsRemark);

        //订单优惠券优惠价格
        order.setDiscountsTicketPrice(discountsTicketPrice);
        //订单促销优惠价格
        order.setDiscountsPrice(orderPriceAll.subtract(discountsPriceAll));
        //会员优惠总金额
        BigDecimal discountsMemberPriceAll = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(userPhone)) {
            // 是会员
            discountsMemberPriceAll = orderPriceAll.subtract(memberPriceAll);
        }
        order.setDiscountsMemberPrice(discountsMemberPriceAll);//订单会员优惠价格

        order.setLastUpdateTime(dateTime);
        openOrderDao.updateObj(order);
        // 重新计算实付单价
        for (int i = 0; i < updateDetailList.size(); i++) {
            TbKcOrderDetail de = updateDetailList.get(i);
            de.setOrderId(order.getOrderId());
            if (de.getDetailId() <= 0) {
                openOrderDao.saveObj(de);
            } else {
                openOrderDao.updateObj(de);
            }
        }
        // 清除缓存订单号
        if (type == 1) {
            Constants.ORDER_NO_KEY_MAP.remove(uniqueKey, orderNo);
        }

    }

    public static BigDecimal getSalePriceTrue(int isDiscounts, BigDecimal salePrice, BigDecimal memberPrice, BigDecimal discountsPrice, String memberNum) {
        BigDecimal salePriceTrue = salePrice;// 计算价格
        if (isDiscounts == 1) {
            // 促销
            salePriceTrue = discountsPrice;
        }
        if (StringUtils.isNotEmpty(memberNum)) {
            // 是会员
            if (memberPrice.compareTo(discountsPrice) < 0) {
                salePriceTrue = memberPrice;
            }
        }
        return salePriceTrue;
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer goodsId = data.getInteger("goods_id");
            String uniqueKey = data.getString("unique_key");
            if (null == goodsId || goodsId == 0 || StringUtils.isEmpty(uniqueKey)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}