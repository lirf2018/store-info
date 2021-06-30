package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 领取生成用户卡券
 * @author: lirf
 * @time: 2021/6/18
 */
@Service("down_qr_coupon")
public class DownLoadQrCoupon implements IResultOut {

    private Logger LOG = Logger.getLogger(DownLoadQrCoupon.class);

    @Autowired
    private ICouponDao iCouponDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer couponId = data.getInteger("couponId");
            Integer userId = data.getInteger("userId");
            Integer recodeState = data.getInteger("recodeState");

            TbCoupon coupon = iCouponDao.loadCoupon(couponId);
            if (coupon == null) {
                return packagMsg(ResultCode.COUPON_NOT_EXIST.getResp_code(), dataJson);
            }
            // 校验是否能生成优惠券
            String check = checkDownQR(couponId, userId, coupon);
            if (StringUtils.isNotEmpty(check)) {
                return check;
            }
            String time = CommonMethod.qrChangeOutTime();
            // 生成优惠券
            String changeCode = System.currentTimeMillis() + CommonMethod.randomNum(2);
            int qrId = createQr(coupon, userId, changeCode, recodeState, time);
            if (qrId == 0) {
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            dataJson.put("qrId", qrId);
            dataJson.put("changeCode", changeCode);
            dataJson.put("changeCodeOutTime", time);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private synchronized String checkDownQR(Integer couponId, Integer userId, TbCoupon coupon) {
        JSONObject dataJson = new JSONObject();
        try {
            // 判断优惠券是否有效
            String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
            String st = DatetimeUtil.timeStamp2Date(coupon.getStartTime().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT);
            String et = DatetimeUtil.timeStamp2Date(coupon.getEndTime().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT);
            if (DatetimeUtil.compareDate(now, et) > 0) {
                // 已结束
                LOG.info("=======已结束========" + couponId + " ,  userId=" + userId);
                return packagMsg(ResultCode.COUPON_NOT_EXIST.getResp_code(), dataJson);
            }
            if (DatetimeUtil.compareDate(now, st) < 0) {
                // 未开始
                LOG.info("=======未开始========" + couponId + " ,  userId=" + userId);
                return packagMsg(ResultCode.COUPON_NOT_EXIST.getResp_code(), dataJson);
            }
            if (coupon.getStatus() != 1 || coupon.getIsPutaway() != 2) {
                LOG.info("=======状态无效或者已下架========" + couponId + " ,  userId=" + userId);
                return packagMsg(ResultCode.COUPON_NOT_EXIST.getResp_code(), dataJson);
            }
            if (coupon.getCouponNum() <= 0) {
                LOG.info("=======数量不足========" + couponId + " ,  userId=" + userId);
                return packagMsg(ResultCode.GOODS_STORE_NOENOUGH.getResp_code(), dataJson);
            }
            // 未开发功能(校验是否指定用户领取)
            if (coupon.getIsShow() == 0) {
                LOG.info("=======待开发功能========" + couponId + " ,  userId=" + userId);
                return packagMsg(ResultCode.LIMIT_COUPON_RULE.getResp_code(), dataJson);
            }

            // 限购方式: 1.按天 2按月 3.按年 4不限购 5按数量
            if (coupon.getLimitWay() == 4) {
                return "";
            }
            // 逻辑删除过期QR
            iCouponDao.deleteUserQrCoupon(userId, couponId);
            String limitTime = DatetimeUtil.timeStamp2Date(coupon.getLimitBeginTime().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT_STRING);
            List<Map<String, Object>> list = iCouponDao.loadUserQRCouponListLimit(couponId, userId, limitTime);
            if (list == null) {
                LOG.info("=======查询失败=======" + couponId + " ,  userId=" + userId);
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (list.size() == 0) {
                return "";
            }
            // 限购开始时间
            Map<String, Integer> dayMap = new HashMap<>();
            Map<String, Integer> yearAndMonthsMap = new HashMap<>();
            Map<String, Integer> yearMap = new HashMap<>();
            int getCount = 0;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                int status = Integer.parseInt(map.get("recode_state").toString());
                if (status == 1) {
                    LOG.info("已存在有效的相同优惠券qr");
                    return packagMsg(ResultCode.LIMIT_COUPON_RULE.getResp_code(), dataJson);
                }
                //
                String createTime = map.get("createtime").toString();
                String[] arrayDay_ = createTime.split("-");
                String year_ = arrayDay_[0];// 2021
                String yearAndMonths_ = arrayDay_[0] + "-" + arrayDay_[1];// 2021-01
                if (createTime.equals(now)) {
                    if (dayMap.get(now) == null) {
                        dayMap.put(now, 1);
                    } else {
                        dayMap.put(now, dayMap.get(now) + 1);
                    }
                }
                if (yearAndMonthsMap.get(yearAndMonths_) == null) {
                    yearAndMonthsMap.put(yearAndMonths_, 1);
                } else {
                    yearAndMonthsMap.put(yearAndMonths_, yearAndMonthsMap.get(yearAndMonths_) + 1);
                }
                if (yearMap.get(year_) == null) {
                    yearMap.put(year_, 1);
                } else {
                    yearMap.put(year_, yearMap.get(year_) + 1);
                }
                getCount++;
            }

            String[] arrayDay = now.split("-");
            String year = arrayDay[0];// 2021
            String yearAndMonths = arrayDay[0] + "-" + arrayDay[1];// 2021-01
            int limitNum = coupon.getLimitNum();
            // 限购方式: 1.按天 2按月 3.按年 4不限购 5按数量
            if (coupon.getLimitWay() == 1) {
                if (dayMap.get(now) > (limitNum - 1)) {
                    LOG.info("========按天===限领=======" + now);
                    return packagMsg(ResultCode.COUPONQR_QOWM_FAIL.getResp_code(), dataJson);
                }
            } else if (coupon.getLimitWay() == 2) {
                if (yearAndMonthsMap.get(yearAndMonths) > (limitNum - 1)) {
                    LOG.info("========按月===限领=======" + now);
                    return packagMsg(ResultCode.COUPONQR_QOWM_FAIL.getResp_code(), dataJson);
                }
            } else if (coupon.getLimitWay() == 3) {
                if (yearMap.get(year) > (limitNum - 1)) {
                    LOG.info("========按年===限领=======" + now);
                    return packagMsg(ResultCode.COUPONQR_QOWM_FAIL.getResp_code(), dataJson);
                }
            } else if (coupon.getLimitWay() == 5) {
                if (getCount > (limitNum - 1)) {
                    LOG.info("========按数量===限领=======" + now);
                    return packagMsg(ResultCode.COUPONQR_QOWM_FAIL.getResp_code(), dataJson);
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private Integer createQr(TbCoupon coupon, int userId, String changeCode, Integer recodeState, String time) throws Exception {
        // 生成卡券
        TbCouponDownQr qr = new TbCouponDownQr();
        //卡券信息
        qr.setCouponId(coupon.getCouponId());
        qr.setUserId(userId);
        qr.setCouponName(coupon.getCouponName());
        qr.setTitle(coupon.getTitle());
        qr.setCouponImg(coupon.getCouponImg());
        qr.setIntro(coupon.getIntro());
        qr.setClassifyId(coupon.getClassifyId());
        qr.setLeve1Id(coupon.getLeve1Id());
        qr.setCouponType(coupon.getCouponType());//卡券类型
        qr.setCouponValue("");//卡券类型对应规则值
        qr.setShopId(coupon.getShopId());
        qr.setGetType(coupon.getGetType());
        qr.setCouponPrice(coupon.getCouponPrice());
        qr.setNeedKnow(coupon.getNeedKnow());
        // qr信息
        qr.setCreatetime(new Timestamp(System.currentTimeMillis()));
        qr.setCreateman("SYS");
        qr.setStatus(1);
        qr.setRemark("");
        qr.setChannelId(0);
        qr.setChangeDate(null);
        qr.setQrCodeOuttime(new Timestamp(DatetimeUtil.convertStrToDate(time).getTime()));
        int defaultDay = 30;
        Integer appointType = coupon.getAppointType();
        if (appointType == 0) {
            if (coupon.getOutDate() != null) {
                defaultDay = coupon.getOutDate();
            }
            // 卡券有效截止日期(根据)过期方式appointType：0按兑换过期天计算过期时间1指定过期时间2指定使用时间
            qr.setChangeOutDate(new Timestamp(DatetimeUtil.addDays(new Date(), defaultDay).getTime()));
        } else if (appointType == 1 || appointType == 2) {
            // 指定过期时间 \ 指定使用时间
            qr.setChangeOutDate(coupon.getAppointDate());
        }

        if (qr.getChangeOutDate() == null) {
            qr.setChangeOutDate(new Timestamp(DatetimeUtil.addDays(new Date(), 30).getTime()));
            LOG.info("--------过期时间计算异常--------");
            return 0;
        }
        //
        String outDayStr = DatetimeUtil.timeStamp2Date(qr.getChangeOutDate().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT) + " 23:59:59";
        qr.setChangeOutDate(new Timestamp(DatetimeUtil.convertStrToDate(outDayStr, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));

        qr.setRecodeState(null == recodeState ? 0 : recodeState);//记录状态 0生成二维码1已下载2已兑换3生成检验
        qr.setChangeCode(changeCode);// 卡券兑换码
        qr.setContent(changeCode);
        qr.setCheckCode("");
        qr.setQrDesc("");
        qr.setQrImg("");
        appointType = coupon.getAppointType() == 2 ? coupon.getAppointType() : 1;
        qr.setAppointType(appointType);//过期方式：0按兑换过期天计算过期时间  1指定过期时间  2指定使用时间
        int qrId = iCouponDao.saveCouponDownQr(qr);
        iCouponDao.updateCouponGetCount(coupon.getCouponId());
        return qrId;
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer couponId = data.getInteger("couponId");
            Integer userId = data.getInteger("userId");
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