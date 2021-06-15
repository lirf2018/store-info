package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CouponCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/15
 */
@Service("query_user_qr_list")
public class QueryUserQRList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryCouponList.class);

    @Autowired
    private ICouponDao iCouponDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            CouponCondition couponCondition = JSONObject.toJavaObject(receiveJsonBean.getData(), CouponCondition.class);
            Integer userId = data.getInteger("userId");
            List<Map<String, Object>> list = iCouponDao.findUserCouponList(userId, couponCondition);
            dataJson.put("list", list);
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
            if (userId == null || userId == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
