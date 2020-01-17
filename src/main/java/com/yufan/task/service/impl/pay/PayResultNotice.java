package com.yufan.task.service.impl.pay;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbOrder;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/15 16:39
 * 功能介绍: 支付结果通知
 */
@Service("pay_notice")
public class PayResultNotice implements IResultOut {
    private Logger LOG = Logger.getLogger(PayResultNotice.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        return doOrder(receiveJsonBean);
    }

    private synchronized String doOrder(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {
            JSONObject data = receiveJsonBean.getData();
            String orderNo = data.getString("order_no");
            Integer payWay = data.getInteger("pay_way");
            String payTime = data.getString("pay_time");
            String payCode = data.getString("pay_code");
            //查询订单详情
            TbOrder order = iOrderDao.loadOrder(orderNo);
            if (order.getOrderStatus() == Constants.ORDER_STATUS_0) {
                int index = iOrderDao.payOrderSuccess(orderNo, payWay, payTime, payCode);//订单支付成功通知
                LOG.info("index=" + index + " 订单支付成功通知orderNo:" + orderNo);
                //更新销售数
                List<Map<String, Object>> orderDetailListMap = iOrderDao.queryOrderDetailListmap(String.valueOf(order.getOrderId()));
                for (int i = 0; i < orderDetailListMap.size(); i++) {
                    int goodsId = Integer.parseInt(orderDetailListMap.get(i).get("goods_id").toString());
                    int goodsCount = Integer.parseInt(orderDetailListMap.get(i).get("goods_count").toString());
                    String goodsSpec = orderDetailListMap.get(i).get("goods_spec") == null ? "" : orderDetailListMap.get(i).get("goods_spec").toString();
                    iGoodsDao.updateGoodsSellCount(goodsId, goodsCount);
                    if (StringUtils.isNotEmpty(goodsSpec)) {
                        iGoodsDao.updateGoodsSkuSellCount(goodsId, goodsSpec, goodsCount);
                    }
                }
            } else {
                LOG.info("该订单已处理orderNo:" + orderNo);
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            String payTime = data.getString("pay_time");
            if (StringUtils.isEmpty(payTime)) {
                return false;
            }
            String payCode = data.getString("pay_code");
            if (StringUtils.isEmpty(payCode)) {
                return false;
            }
            String orderNo = data.getString("order_no");
            if (StringUtils.isEmpty(orderNo)) {
                return false;
            }
            Integer payWay = data.getInteger("pay_way");
            if (null == payWay) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
