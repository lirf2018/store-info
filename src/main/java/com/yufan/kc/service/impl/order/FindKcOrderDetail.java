package com.yufan.kc.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/11 23:09
 * 功能介绍:
 */
@Service("find_kc_order_detail")
public class FindKcOrderDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(FindKcOrderDetail.class);

    @Autowired
    private OrderPriceUtil orderPriceUtil;

    @Autowired
    private OpenOrderDao openOrderDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer orderId = data.getInteger("order_id");
            List<Map<String, Object>> detailList = openOrderDao.findOrderList(orderId);
            String userPhone = null;
            Integer orderStatus = null;
            if (!CollectionUtils.isEmpty(detailList)) {
                userPhone = null == detailList.get(0).get("user_phone") ? "" : detailList.get(0).get("user_phone").toString();
                orderStatus = Integer.parseInt(detailList.get(0).get("order_status").toString());
            }
            boolean resetOrder = false;
            if (orderStatus == Constants.ORDER_STATUS_0) {
                // 重新计算更新商品信息（待付款订单）
                boolean flag = openOrderDao.updateOutTimeDetail(orderId, null);
                if (flag) {
                    // 重新计算订单商品价格
                    orderPriceUtil.resetOrderInfo(orderId, userPhone);
                    resetOrder = true;
                    detailList = openOrderDao.findOrderList(orderId);
                }
            }
            dataJson.put("reset_order", resetOrder);
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
            Integer orderId = data.getInteger("order_id");
            if (null == orderId || orderId == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}