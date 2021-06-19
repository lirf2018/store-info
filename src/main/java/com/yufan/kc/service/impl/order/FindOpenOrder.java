package com.yufan.kc.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/9 23:20
 * 功能介绍: 开单页面查询商品订单列表
 */
@Service("kc_open_order_list")
public class FindOpenOrder implements IResultOut {

    private Logger LOG = Logger.getLogger(FindOpenOrder.class);

    @Autowired
    private OpenOrderDao openOrderDao;

    @Autowired
    private OrderPriceUtil orderPriceUtil;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data, ConditionCommon.class);

            Map<String, Map<String, Object>> maps = new HashMap<>();
            List<Map<String, Object>> list = openOrderDao.findOrderDetailList(conditionCommon);
            for (int i = 0; i < list.size(); i++) {
                String orderNo = list.get(i).get("orderNum").toString();
                maps.put(orderNo, list.get(i));
            }
            if (null != list && list.size() > 0) {
                // 清除失效的订单详情
                int orderId = Integer.parseInt(list.get(0).get("orderId").toString());
                String userPhone = list.get(0).get("user_phone") == null ?"" : list.get(0).get("user_phone").toString();
                boolean flag = openOrderDao.updateOutTimeDetail(orderId, null);
                if (flag) {
                    // 重新计算订单商品价格
                    orderPriceUtil.resetOrderInfo(orderId, userPhone);
                    list = openOrderDao.findOrderDetailList(conditionCommon);
                    for (int i = 0; i < list.size(); i++) {
                        String orderNo = list.get(i).get("orderNum").toString();
                        maps.put(orderNo, list.get(i));
                    }
                }
            }
            List<Map<String, Object>> orderList = new ArrayList<>();
            for (Map.Entry<String, Map<String, Object>> map : maps.entrySet()) {
                String orderNum = map.getKey();
                Map<String, Object> orderInfo = map.getValue();

                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("discountsPrice", orderInfo.get("discountsPrice"));
                orderMap.put("discountsMemberPrice", orderInfo.get("discountsMemberPrice"));
                orderMap.put("discountsTicketPrice", orderInfo.get("discountsTicketPrice"));
                BigDecimal orderPrice = new BigDecimal(orderInfo.get("orderPrice").toString());
                BigDecimal realPrice = new BigDecimal(orderInfo.get("realPrice").toString());
                orderMap.put("orderPrice", orderPrice);
                orderMap.put("realPrice", realPrice);
                orderMap.put("discountsPriceCountAll", orderPrice.subtract(realPrice));
                orderMap.put("userPhone", orderInfo.get("userPhone") == null ? "" : orderInfo.get("userPhone"));
                orderMap.put("orderNum", orderInfo.get("orderNum"));
                orderMap.put("orderId", orderInfo.get("orderId"));
                orderMap.put("goodsCount", orderInfo.get("goodsCount"));
                orderMap.put("serverName", orderInfo.get("serverName"));
                orderMap.put("personCount", orderInfo.get("personCount"));
                orderMap.put("tableName", orderInfo.get("tableName"));
                orderMap.put("orderStatus", orderInfo.get("orderStatus"));
                orderMap.put("payMethod", orderInfo.get("payMethod"));
                orderMap.put("orderStatusName", Constants.MAP_NAME.get("orderStatus" + orderInfo.get("orderStatus")));
                orderMap.put("payMethodName", Constants.MAP_NAME.get("payType" + orderInfo.get("payMethod")));
                orderMap.put("lastUpdateTime", orderInfo.get("lastUpdateTime"));
                orderMap.put("style", "mouse-out-goods-ul");
                List<Map<String, Object>> orderDetailList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    String orderNum_ = list.get(i).get("orderNum").toString();
                    // 处理显示价格
                    BigDecimal salePriceTrue = new BigDecimal(list.get(i).get("salePriceTrue").toString());
                    BigDecimal goodsSalePrice = new BigDecimal(list.get(i).get("goodsSalePrice").toString());
                    BigDecimal memberPrice = new BigDecimal(list.get(i).get("memberPrice").toString());
                    BigDecimal goodsDiscountsPrice = new BigDecimal(list.get(i).get("goodsDiscountsPrice").toString());
                    String salePriceTrueDesc = "";
                    if (salePriceTrue.compareTo(goodsSalePrice) != 0) {
                        if (salePriceTrue.compareTo(memberPrice) == 0) {
                            salePriceTrueDesc = "会员";
                        } else if (salePriceTrue.compareTo(goodsDiscountsPrice) == 0) {
                            salePriceTrueDesc = "促销";
                        }
                    }
                    list.get(i).put("salePriceTrueDesc", salePriceTrueDesc);
                    if (orderNum.equals(orderNum_)) {
                        orderDetailList.add(list.get(i));
                    }
                }
                orderMap.put("detail_list", orderDetailList);
                orderList.add(orderMap);
            }
            // 防止生成多个订单
            String uniqueKey = UUID.randomUUID().toString() + CommonMethod.randomStr("");
            dataJson.put("unique_key", uniqueKey);
            dataJson.put("order_list", orderList);
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
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data, ConditionCommon.class);
            if (null == conditionCommon || StringUtils.isEmpty(conditionCommon.getOrderNo()) && StringUtils.isEmpty(conditionCommon.getUserPhone()) && StringUtils.isEmpty(conditionCommon.getTableName())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}