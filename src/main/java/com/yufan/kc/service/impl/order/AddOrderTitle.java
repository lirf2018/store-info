package com.yufan.kc.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/21 21:23
 * 功能介绍:
 */
@Service("kc_add_order_title")
public class AddOrderTitle implements IResultOut {

    private Logger LOG = Logger.getLogger(AddOrderTitle.class);

    @Autowired
    private OpenOrderDao openOrderDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer orderId = data.getInteger("order_id");
            Integer personCount = data.getInteger("person_count");
            String tableName = data.getString("table_name");
            String serverName = data.getString("server_name");
            TbKcOrder order = openOrderDao.loadOrder(orderId);
            if (null == order) {
                LOG.info("-------查询订单不存在--------");
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            // 判断订单是否已支付
            if (order.getOrderStatus().intValue() != Constants.ORDER_STATUS_0) {
                LOG.info("-------查询订单已付款--------");
                return packagMsg(ResultCode.ORDER_IS_PAY.getResp_code(), dataJson);
            }
            order.setTableName(tableName);
            order.setServerName(serverName);
            order.setPersonCount(personCount);
            openOrderDao.updateObj(order);
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