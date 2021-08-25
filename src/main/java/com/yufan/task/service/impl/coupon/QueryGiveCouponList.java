package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CouponCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.utils.PageInfo;
import com.yufan.utils.ResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/15
 */
@Service("query_give_coupon_list")
public class QueryGiveCouponList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryGiveCouponList.class);

    @Autowired
    private ICouponDao iCouponDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {
            CouponCondition couponCondition = JSONObject.toJavaObject(receiveJsonBean.getData(), CouponCondition.class);
            couponCondition.setPrivateCouponFlag(true);
            PageInfo page = iCouponDao.loadCouponListPage(couponCondition);
            dataJson.put("hasNext", page.isHasNext());
            dataJson.put("currePage", page.getCurrePage());
            dataJson.put("pageSize", page.getPageSize());
            dataJson.put("list", page.getResultListMap());
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

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
