package com.yufan.kc.service.impl.order;

import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.bean.TbKcOrderDetail;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.kc.service.impl.order.AddOpenOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/19 22:46
 * 功能介绍:
 */
@Service
public class OrderPriceUtil {

    @Autowired
    private OpenOrderDao openOrderDao;


    /**
     * 重新计算订单信息
     *
     * @param orderId
     */
    public void resetOrderInfo(int orderId, String userPhone) {
        // 订单已添加的商品数(重新计算实付单价)
        List<TbKcOrderDetail> detailList = openOrderDao.loadOrderDetailList(orderId);
        // 计算订单价格
        Integer goodsCount = detailList.size();
        BigDecimal orderPriceAll = BigDecimal.ZERO;//订单总金额
        BigDecimal realPriceAll = BigDecimal.ZERO;//订单实付总金额
        BigDecimal memberPriceAll = BigDecimal.ZERO;//会员价格总金额
        BigDecimal discountsPriceAll = BigDecimal.ZERO;//促销价格总金额
        for (int i = 0; i < detailList.size(); i++) {
            TbKcOrderDetail de = detailList.get(i);
            int isDiscounts = de.getIsDiscounts();
            // 实付单价
            BigDecimal trueGoodsSalePrice = AddOpenOrder.getSalePriceTrue(isDiscounts, de.getSalePrice(),de.getMemberPrice(), de.getDiscountsPrice(), userPhone);
            de.setSalePriceTrue(trueGoodsSalePrice);
            orderPriceAll = orderPriceAll.add(de.getSalePrice());
            realPriceAll = realPriceAll.add(trueGoodsSalePrice);
            memberPriceAll = memberPriceAll.add(de.getMemberPrice());
            discountsPriceAll = discountsPriceAll.add(de.getDiscountsPrice());
        }
        TbKcOrder order = new TbKcOrder();
        order.setOrderId(orderId);
        order.setOrderPrice(orderPriceAll);
        order.setRealPrice(realPriceAll);
        //订单促销优惠价格
        order.setDiscountsPrice(orderPriceAll.subtract(discountsPriceAll));
        //会员优惠总金额
        BigDecimal discountsMemberPriceAll = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(order.getUserPhone())) {
            // 是会员
            discountsMemberPriceAll = orderPriceAll.subtract(memberPriceAll);
        }
        order.setDiscountsMemberPrice(discountsMemberPriceAll);//订单会员优惠价格
        order.setGoodsCount(goodsCount);
        openOrderDao.updateKcOrder(order);
    }

    /**
     * 重新计算订单信息
     *
     * @param orderId
     */
    public  void resetOrderInfo2(int orderId, Integer detailId, TbKcOrder order) {
        // 订单已添加的商品数(重新计算实付单价)
        List<TbKcOrderDetail> detailList = openOrderDao.loadOrderDetailList(orderId);
        // 计算订单价格
        Integer goodsCount = detailList.size();
        BigDecimal orderPriceAll = BigDecimal.ZERO;//订单总金额
        BigDecimal realPriceAll = BigDecimal.ZERO;//订单实付总金额
        BigDecimal memberPriceAll = BigDecimal.ZERO;//会员价格总金额
        BigDecimal discountsPriceAll = BigDecimal.ZERO;//促销价格总金额
        for (int i = 0; i < detailList.size(); i++) {
            TbKcOrderDetail de = detailList.get(i);
            if (null != detailId && detailId == de.getDetailId()) {
                continue;
            }
            // 实付单价
            BigDecimal trueGoodsSalePrice = AddOpenOrder.getSalePriceTrue(de.getIsDiscounts(), de.getSalePrice(), de.getMemberPrice(), de.getDiscountsPrice(), order.getUserPhone());
            de.setSalePriceTrue(trueGoodsSalePrice);
            orderPriceAll = orderPriceAll.add(de.getSalePrice());
            realPriceAll = realPriceAll.add(trueGoodsSalePrice);
            memberPriceAll = memberPriceAll.add(de.getMemberPrice());
            discountsPriceAll = discountsPriceAll.add(de.getDiscountsPrice());
        }

        order.setOrderPrice(orderPriceAll);
        order.setRealPrice(realPriceAll);

        //订单促销优惠价格
        order.setDiscountsPrice(orderPriceAll.subtract(discountsPriceAll));
        //会员优惠总金额
        BigDecimal discountsMemberPriceAll = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(order.getUserPhone())) {
            // 是会员
            discountsMemberPriceAll = orderPriceAll.subtract(memberPriceAll);
        }
        order.setDiscountsMemberPrice(discountsMemberPriceAll);//订单会员优惠价格
        order.setGoodsCount(goodsCount);
        openOrderDao.updateObj(order);
    }

}
