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

    TbCouponDownQr loadCouponDownQr(int qrId);

}
