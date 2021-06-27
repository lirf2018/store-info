package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbOrder;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.service.impl.user.CreatePrivateGoods;
import com.yufan.utils.Constants;
import com.yufan.utils.ResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 订单支付成功
 * @author: lirf
 * @time: 2021/6/26
 */
@Service("order_pay_success")
public class OrderPaySuccess implements IResultOut {
    private Logger LOG = Logger.getLogger(OrderPaySuccess.class);

    private ExecutorService executors = Executors.newSingleThreadExecutor();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IOrderDao iOrderDao;

    @Override
    public synchronized String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            String orderNo = data.getString("orderNo");
            TbOrder order = iOrderDao.loadOrder(orderNo);
            if (order == null || order.getOrderStatus() != Constants.ORDER_STATUS_0) {
                LOG.info("============更新失败,订单不存在或者订单状态已更新==============");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            iOrderDao.updateOrderStatusPaySuccess(order.getOrderId(), order.getUserId());
            // 生成预约商品私人数据
            AddPrivateGoods addPrivateGoods = new AddPrivateGoods(userId, order.getOrderId());
            executors.execute(addPrivateGoods);
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
            Integer userId = data.getInteger("userId");
            String orderNo = data.getString("orderNo");
            if (null == userId || StringUtils.isEmpty(orderNo)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

    class AddPrivateGoods implements Runnable {
        private Integer userId;
        private Integer orderId;

        public AddPrivateGoods(Integer userId, Integer orderId) {
            this.userId = userId;
            this.orderId = orderId;
        }

        @Override
        public void run() {
            CreatePrivateGoods createPrivateGoods = (CreatePrivateGoods) applicationContext.getBean("create_private_goods");
            ReceiveJsonBean receiveJsonBean = new ReceiveJsonBean();
            receiveJsonBean.setUserId(userId);
            JSONObject data = new JSONObject();
            data.put("orderId", orderId);
            receiveJsonBean.setData(data);
            createPrivateGoods.getResult(receiveJsonBean);
        }
    }
}
