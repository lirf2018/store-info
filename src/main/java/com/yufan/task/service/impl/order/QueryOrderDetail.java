package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbOrderRefund;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.param.IParamDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
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
 * 创建人: lirf
 * 创建时间:  2019/8/27 15:41
 * 功能介绍: 查询订单详情
 */
@Service("query_order_detail")
public class QueryOrderDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryOrderList.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IParamDao iParamDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            //查询订单状态
            List<Map<String, Object>> paramList = iParamDao.queryParamListMap(Constants.DATA_STATUS_YX);
            Map<String, String> paramName = new HashMap<>();
            for (int i = 0; i < paramList.size(); i++) {
                String paramCode = paramList.get(i).get("param_code").toString();
                if ("pay_way".equals(paramCode) || "order_status".equals(paramCode) || "post_way".equals(paramCode)) {
                    String key = paramList.get(i).get("param_key").toString();
                    String value = paramList.get(i).get("param_value").toString();
                    paramName.put(paramCode + "-" + key, value);
                }
            }
            Integer userId = data.getInteger("user_id");
            Integer orderId = data.getInteger("order_id");

            //商品总价
            BigDecimal goodsPriceAll = new BigDecimal(0.00);

            List<Map<String, Object>> listOrder = iOrderDao.queryUserOrderDetail(userId, orderId);
            List<Map<String, Object>> detailList = new ArrayList<>();
            for (int i = 0; i < listOrder.size(); i++) {
                Object orderNo = listOrder.get(i).get("order_no");
                Object orderPrice = listOrder.get(i).get("order_price");
                Object realPrice = listOrder.get(i).get("real_price");
                Object orderCount = listOrder.get(i).get("order_count");
                Object orderStatus = listOrder.get(i).get("order_status");
                Object orderTime = listOrder.get(i).get("order_time");
                Object payTime = listOrder.get(i).get("pay_time") == null ? "" : listOrder.get(i).get("pay_time");
                Object finishTime = listOrder.get(i).get("finish_time") == null ? "" : listOrder.get(i).get("finish_time");
                Object advancePrice = listOrder.get(i).get("advance_price") == null ? "0" : listOrder.get(i).get("advance_price");
                Object advancePayWay = listOrder.get(i).get("advance_pay_way") == null ? "" : listOrder.get(i).get("advance_pay_way");
                Object postWay = listOrder.get(i).get("post_way") == null ? "" : listOrder.get(i).get("post_way");
                Object discountsPrice = listOrder.get(i).get("discounts_price") == null ? "0" : listOrder.get(i).get("discounts_price");
                Object postPrice = listOrder.get(i).get("post_price");
                Object userAddr = listOrder.get(i).get("user_addr");
                Object userPhone = listOrder.get(i).get("user_phone");
                Object userName = listOrder.get(i).get("user_name");
                Object payWay = listOrder.get(i).get("pay_way") == null ? "" : listOrder.get(i).get("pay_way");
                Object shopLogo = listOrder.get(i).get("shop_logo");
                Object shopName = listOrder.get(i).get("shop_name");
                dataJson.put("order_id", orderId);
                dataJson.put("order_no", orderNo);
                dataJson.put("order_price", new BigDecimal(orderPrice.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataJson.put("real_price", new BigDecimal(realPrice.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataJson.put("order_count", Integer.parseInt(orderCount.toString()));
                dataJson.put("order_status", Integer.parseInt(orderStatus.toString()));
                String orderStatusName = paramName.get("order_status-" + orderStatus);
                dataJson.put("order_status_name", orderStatusName == null ? "" : orderStatusName);
                dataJson.put("order_time", orderTime);
                dataJson.put("pay_time", payTime);
                dataJson.put("finish_time", finishTime);
                dataJson.put("advance_price", new BigDecimal(advancePrice.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataJson.put("advance_pay_way", advancePayWay);
                String advancePayWayName = paramName.get("pay_way-" + advancePayWay);
                dataJson.put("advance_pay_way_name", advancePayWayName == null ? "" : advancePayWayName);
                dataJson.put("post_way", postWay);
                String postWayName = paramName.get("post_way-" + postWay);
                dataJson.put("post_way_name", postWayName == null ? "" : postWayName);
                dataJson.put("discounts_price", new BigDecimal(discountsPrice.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataJson.put("post_price", new BigDecimal(postPrice.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                dataJson.put("user_addr", userAddr);
                dataJson.put("user_phone", userPhone);
                dataJson.put("user_name", userName);
                dataJson.put("pay_way", payWay);
                String payWayName = paramName.get("pay_way-" + payWay);
                dataJson.put("pay_way_name", payWayName == null ? "" : payWayName);
                dataJson.put("shop_logo", shopLogo);
                dataJson.put("shop_name", shopName);


                Map<String, Object> detailMap = new HashMap<>();
                Object goodsImg = listOrder.get(i).get("goods_img");
                Object goodsId = listOrder.get(i).get("goods_id");
                Object goodsName = listOrder.get(i).get("goods_name");
                Object saleMoney = listOrder.get(i).get("sale_money");
                Object goodsCount = listOrder.get(i).get("goods_count");
                Object goodsSpecNameStr = listOrder.get(i).get("goods_spec_name_str");
                detailMap.put("goods_img", goodsImg);
                detailMap.put("goods_id", Integer.parseInt(goodsId.toString()));
                detailMap.put("goods_name", goodsName);


                detailMap.put("sale_money", saleMoney);
                detailMap.put("goods_count", goodsCount);
                detailMap.put("goods_spec_name_str", goodsSpecNameStr);
                detailList.add(detailMap);
                //计算商品总价
                goodsPriceAll = goodsPriceAll.add(new BigDecimal(saleMoney.toString()).multiply(new BigDecimal(goodsCount.toString())));
            }

            //查询退款信息
            String refundApplyTime = "";//退款申请时间
            String refundFinishTime = "";//退款完成时间
            if (dataJson.getInteger("order_status") == Constants.ORDER_STATUS_9) {
                String format = "yyyy-MM-dd HH:mm:ss";
                TbOrderRefund refund = iOrderDao.loadOrderRefund(dataJson.getString("order_no"));
                if (null != refund) {
                    refundApplyTime = DatetimeUtil.timeStamp2Date(refund.getApplyTime().getTime(), format);
                    if (null != refund.getFinishTime()) {
                        refundFinishTime = DatetimeUtil.timeStamp2Date(refund.getFinishTime().getTime(), format);
                    }
                }
            }

            dataJson.put("refund_apply_time", refundApplyTime);
            dataJson.put("refund_finish_time", refundFinishTime);
            dataJson.put("goods_price_all", goodsPriceAll.toString());
            dataJson.put("detail_list", detailList);
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
            Integer userId = data.getInteger("user_id");
            Integer orderId = data.getInteger("order_id");
            if (null == userId || orderId == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
