package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbOrder;
import com.yufan.pojo.TbOrderRefund;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.service.impl.login.PhoneCodeLogin;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/5 13:50
 * 功能介绍: 订单申请退款
 */
@Service("apply_order_refund")
public class OrderRefund implements IResultOut {

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
            //
            TbOrder order = iOrderDao.loadOrder(orderId, userId);
            if (null == order || order.getOrderPrice().compareTo(new BigDecimal(0)) == 0) {
                LOG.info("-------订单不存在或者订单价格为0-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            boolean flag = false;
            //订单状态必须处于 以下状态 1已付款   4	待发货 5	待收货
            if (order.getOrderStatus() == Constants.ORDER_STATUS_1 || order.getOrderStatus() == Constants.ORDER_STATUS_4 || order.getOrderStatus() == Constants.ORDER_STATUS_5) {
                flag = true;
            }
            if (flag && applyRefund(order, userId)) {
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }


    @Transactional
    public boolean applyRefund(TbOrder order, int userId) {
        try {
            int orderId = order.getOrderId();
            String orderNo = order.getOrderNo();
            //生成退款数据
            TbOrderRefund orderRefund = new TbOrderRefund();
            orderRefund.setOrderNo(orderNo);
            orderRefund.setDetailId(0);//默认订单全部
            orderRefund.setApplyReason( 0);//申请理由 0 无理由申请
            orderRefund.setPartnerTradeNo("");//由支付系统生成
            orderRefund.setStatus( 0);//状态 0 处理中 1退款成功 2 退款失败  3退款异常
            orderRefund.setRefundDesc("");
            orderRefund.setUserId(userId);
            orderRefund.setPrice(order.getOrderPrice());
            orderRefund.setApplyTime(new Timestamp(new Date().getTime()));
            int index = iOrderDao.saveObj(orderRefund);
            if (index > 0) {
                iOrderDao.updateOrderStatus(orderId, userId, Constants.ORDER_STATUS_9);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer orderId = data.getInteger("order_id");
            Integer userId = data.getInteger("user_id");
//            Integer payWay = data.getInteger("pay_way");//交易方式
//            String refundAccount = data.getString("refund_account");//退款账号
//            if (payWay == null || null == orderId || null == userId || StringUtils.isEmpty(refundAccount)) {
//                return false;
//            }
            if (null == orderId || null == userId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}