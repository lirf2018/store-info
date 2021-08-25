package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.pojo.TbShop;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:卡券详情
 * @author: lirf
 * @time: 2021/6/15
 */
@Service("query_coupon_detail")
public class QueryCouponDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryCouponDetail.class);

    @Autowired
    private IShopJapDao shopJapDao;

    @Autowired
    private ICouponDao iCouponDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            Integer couponId = data.getInteger("couponId");
            TbCoupon coupon = iCouponDao.loadCoupon(couponId);
            if (coupon == null) {
                return packagMsg(ResultCode.COUPON_NOT_EXIST.getResp_code(), dataJson);
            }
            TbShop shop = shopJapDao.findOne(coupon.getShopId());

            int qrId = 0;
            // 查询当前卡券用户是否已领取,并且有效
            TbCouponDownQr couponDownQr = iCouponDao.loadCouponDownQrByCouponId(couponId, userId);
            if (null != couponDownQr) {
                if (couponDownQr.getRecodeState() == 1) {
                    long qrOutTime = couponDownQr.getChangeOutDate().getTime();
                    String outDate = DatetimeUtil.timeStamp2Date(qrOutTime, DatetimeUtil.DEFAULT_DATE_FORMAT);
                    String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
                    if (DatetimeUtil.compareDate(now, outDate, DatetimeUtil.DEFAULT_DATE_FORMAT) < 1) {
                        qrId = couponDownQr.getId();
                    }
                }
            }
            // 对固定使用时间的优惠券处理 过期方式：0按兑换过期天计算过期时间1指定过期时间2指定使用时间
            int nowUseDate = 0;
            if (coupon.getAppointType() == 2) {
                // 指定使用时间
                String appointDate = DatetimeUtil.timeStamp2Date(coupon.getAppointDate().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT);
                String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
                if (now.equals(appointDate)) {
                    nowUseDate = 1;
                }
            }
            dataJson.put("coupon", coupon);
            dataJson.put("nowUseDate", nowUseDate);
            dataJson.put("qrId", qrId);
            dataJson.put("beginDate", DatetimeUtil.timeStamp2Date(coupon.getStartTime().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT));
            dataJson.put("endDate", DatetimeUtil.timeStamp2Date(coupon.getEndTime().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT));
            if (null != coupon.getAppointDate()) {
                dataJson.put("appointDate", DatetimeUtil.timeStamp2Date(coupon.getAppointDate().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT));
            }
            dataJson.put("coupon", coupon);

            dataJson.put("shop", shop);
            dataJson.put("imgPath", Constants.IMG_WEB_URL);
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
            Integer couponId = data.getInteger("couponId");
            if (null == userId || couponId == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}