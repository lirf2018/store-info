package com.yufan.task.service.impl.main;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.service.impl.Test;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2021/2/8 22:29
 * 功能介绍: 查询购物车数量
 */
@Service("find_order_cart_count")
public class FindUserOrderCartCount implements IResultOut {

    private Logger LOG = Logger.getLogger(FindUserOrderCartCount.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            int count = 0;
            Integer userId = data.getInteger("userId");
            Integer goodsId = data.getInteger("goodsId");
            List<Map<String, Object>> list = iOrderDao.findCartCount(userId, goodsId);
            for (int i = 0; i < list.size(); i++) {
                int c = Integer.parseInt(list.get(i).get("goodsCount").toString());
                count = count + c;
            }
            dataJson.put("count", count);
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
            Integer goodsId = data.getInteger("goodsId");


            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}