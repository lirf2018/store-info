package com.yufan.kc.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/21 22:18
 * 功能介绍: 重新设置订单支付(暂时不做)
 */
@Service("kc_reset_order_status")
public class ResetOrderPay implements IResultOut {

    private Logger LOG = Logger.getLogger(ResetOrderPay.class);

    @Autowired
    private OpenOrderDao openOrderDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer orderId = data.getInteger("order_id");
            String orderPass = data.getString("order_pass");
            if(!Constants.RESET_ORER_PASSWD.equals(orderPass)){
                return packagMsg(ResultCode.PASS_FAIL_1.getResp_code(), dataJson);
            }
            TbKcOrder order = openOrderDao.loadOrder(orderId);
            if (null == order) {
                LOG.info("-------查询订单不存在--------");
                return packagMsg(ResultCode.ORDER_NOT_EXIST.getResp_code(), dataJson);
            }
            order.setOrderStatus(new Byte(String.valueOf(Constants.ORDER_STATUS_0)));
            order.setPayMethod(null);
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
            String orderPass = data.getString("order_pass");
            if (null == orderId || orderId == 0 || StringUtils.isEmpty(orderPass)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}