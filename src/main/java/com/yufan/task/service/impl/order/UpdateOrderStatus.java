package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbOrder;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 10:35
 * 功能介绍: 更新订单状态
 */
@Service("update_order_status")
public class UpdateOrderStatus implements IResultOut {

    private Logger LOG = Logger.getLogger(UpdateOrderStatus.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer orderId = data.getInteger("order_id");
            Integer userId = data.getInteger("user_id");
            Integer orderStatus = data.getInteger("order_status");
            //
            TbOrder order = iOrderDao.loadOrder(orderId, userId);
            if (null == order) {
                LOG.info("-------订单不存在当前状态数据-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            boolean flag = false;

            //不同状态判断
            if (orderStatus == Constants.ORDER_STATUS_1) {

            } else if (orderStatus == Constants.ORDER_STATUS_2) {

            } else if (orderStatus == Constants.ORDER_STATUS_3) {

            } else if (orderStatus == Constants.ORDER_STATUS_4) {

            } else if (orderStatus == Constants.ORDER_STATUS_5) {

            } else if (orderStatus == Constants.ORDER_STATUS_6) {
                //完成订单(确认收货) 订单状态必须为5 代收货状态
                if (order.getOrderStatus() == Constants.ORDER_STATUS_5) {
                    flag = true;
                }
            } else if (orderStatus == Constants.ORDER_STATUS_7) {
                //取消订单  订单状态=待付款  或者订单状态为1已付款且订单支付价格为0
                if (order.getOrderStatus() == Constants.ORDER_STATUS_0 || (order.getOrderStatus() == Constants.ORDER_STATUS_1 && order.getOrderPrice().compareTo(new BigDecimal(0)) == 0)) {
                    flag = true;
                }
            } else if (orderStatus == Constants.ORDER_STATUS_8) {
                //删除订单 订单状态必须为7已取消
                if (order.getOrderStatus() == Constants.ORDER_STATUS_7) {
                    flag = true;
                }
            } else if (orderStatus == Constants.ORDER_STATUS_9) {

            } else if (orderStatus == Constants.ORDER_STATUS_10) {

            } else if (orderStatus == Constants.ORDER_STATUS_11) {

            } else if (orderStatus == Constants.ORDER_STATUS_12) {

            } else if (orderStatus == Constants.ORDER_STATUS_13) {

            }

            if (flag) {
                iOrderDao.updateOrderStatus(orderId, userId, orderStatus);
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
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
            Integer userId = data.getInteger("user_id");
            Integer orderStatus = data.getInteger("order_status");
            if (null == orderId || null == userId || null == orderStatus) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}