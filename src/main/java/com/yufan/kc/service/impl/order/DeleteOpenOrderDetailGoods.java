package com.yufan.kc.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.bean.TbKcOrderDetail;
import com.yufan.kc.dao.order.OpenOrderDao;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/15 22:35
 * 功能介绍: 开单删除订单详情商品维度：订单详情）
 */
@Service("kc_open_delete_detail_goods")
public class DeleteOpenOrderDetailGoods implements IResultOut {

    private Logger LOG = Logger.getLogger(DeleteOpenOrderDetailGoods.class);

    @Autowired
    private OpenOrderDao openOrderDao;


    @Autowired
    private OrderPriceUtil orderPriceUtil;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer detailId = data.getInteger("detail_id");
            Integer orderId = data.getInteger("order_id");
            TbKcOrder order = openOrderDao.loadOrder(orderId);
            // 判断订单是否已支付
            if (order.getOrderStatus().intValue() != Constants.ORDER_STATUS_0) {
                LOG.info("-------查询订单已付款--------");
                return packagMsg(ResultCode.ORDER_IS_PAY.getResp_code(), dataJson);
            }
            openOrderDao.deleteDetailById(detailId);
            orderPriceUtil.resetOrderInfo2(orderId, detailId, order);
            //
            List<TbKcOrderDetail> detailList = openOrderDao.loadOrderDetailList(orderId);
            dataJson.put("detail_count", detailList.size());
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
            Integer detailId = data.getInteger("detail_id");
            Integer orderId = data.getInteger("order_id");
            if (null == detailId || detailId == 0 || orderId == null || orderId == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}