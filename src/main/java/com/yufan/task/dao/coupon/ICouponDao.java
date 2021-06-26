package com.yufan.task.dao.coupon;

import com.yufan.bean.CouponCondition;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/12
 */
public interface ICouponDao {

    /**
     * 查询用户卡券列表
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> findUserCouponList(int userId, CouponCondition couponCondition);


    PageInfo loadCouponListPage(CouponCondition couponCondition);

    TbCoupon loadCoupon(int couponId);

    TbCouponDownQr loadCouponDownQrByQrId(int qrId);

    TbCouponDownQr loadCouponDownQrByQrCode(String code);

    TbCouponDownQr loadCouponDownQrByCouponId(int couponId, int userId);

    void updateQrCodeChangeTime(int userId, int qrId, String time);

    int saveCouponDownQr(TbCouponDownQr qr);

    void updateQrRecordStatusWith1(int userId, int qrId);

    int changeQrRecordStatus(int qrId);

    void updateCouponGetCount(int couponId);

    List<Map<String, Object>> loadUserQRCouponListLimit(int couponId, int userId, String limitTime);

    /**
     * 清除用户过期卡券
     *
     * @param userId
     * @param coupon
     */
    void deleteUserQrCoupon(int userId, int coupon);

}
