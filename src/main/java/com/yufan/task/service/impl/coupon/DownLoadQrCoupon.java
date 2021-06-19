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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

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
            String time = CommonMethod.qrChangeOutTime();
            // 生成优惠券
            String changeCode = System.currentTimeMillis() + CommonMethod.randomNum(2);
            int qrId = createQr(coupon, userId, changeCode, recodeState, time);
            dataJson.put("qrId", qrId);
            dataJson.put("changeCode", changeCode);
            dataJson.put("changeCodeOutTime", time);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
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
        Integer appointType = coupon.getAppointType() == 2 ? coupon.getAppointType() : 1;
        qr.setAppointType(appointType);//过期方式：0按兑换过期天计算过期时间1指定过期时间2指定使用时间
        // qr信息
        qr.setCreatetime(new Timestamp(System.currentTimeMillis()));
        qr.setCreateman("SYS");
        qr.setStatus(1);
        qr.setRemark("");
        qr.setChannelId(0);
        qr.setChangeDate(null);
        qr.setQrCodeOuttime(new Timestamp(DatetimeUtil.convertStrToDate(time).getTime()));
        int defaultDay = 30;
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
        }
        qr.setRecodeState(null == recodeState ? 0 : recodeState);//记录状态 0生成二维码1已下载2已兑换3生成检验
        qr.setChangeCode(changeCode);// 卡券兑换码
        qr.setContent(changeCode);
        qr.setCheckCode("");
        qr.setQrDesc("");
        qr.setQrImg("");
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