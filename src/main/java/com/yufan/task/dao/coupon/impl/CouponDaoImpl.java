package com.yufan.task.dao.coupon.impl;

import com.yufan.bean.CouponCondition;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/12
 */
@Repository
@Transactional
public class CouponDaoImpl implements ICouponDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> findUserCouponList(int userId, CouponCondition couponCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  CONCAT('" + Constants.IMG_WEB_URL + "',qr.coupon_img) as coupon_img,qr.coupon_name,qr.title, ");
        sql.append(" DATE_FORMAT(qr.qr_code_outtime,'%Y-%m-%d') as qr_code_outtime,qr.recode_state,if(NOW()>qr.qr_code_outtime,0,1) as over_flag ");
        sql.append(" from tb_coupon_down_qr  qr ");
        sql.append(" ORDER BY createtime desc ");
        return null;
    }

    @Override
    public PageInfo loadCouponListPage(CouponCondition couponCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.coupon_id,c.coupon_name,c.title,c.coupon_num, DATE_FORMAT(c.start_time,'%Y-%m-%d') as start_time,DATE_FORMAT(c.end_time,'%Y-%m-%d') as end_time,c.appoint_type, ");
        sql.append(" CONCAT('" + Constants.IMG_WEB_URL + "',c.coupon_img) as coupon_img,c.count_get ");
        sql.append("  ,if(DATE_FORMAT(NOW(),'%Y-%m-%d')=DATE_FORMAT(c.appoint_date,'%Y-%m-%d'),1,0) as now_use_date ");
        sql.append(" from tb_coupon c ");
        sql.append(" where c.`status`=1 and c.is_putaway=2 and NOW()>=c.start_time and NOW()<=c.end_time ");
        if (couponCondition.getShow() != null) {
            sql.append(" and c.is_show=").append(couponCondition.getShow()).append(" ");
        }
        if (couponCondition.getCouponId() != null) {
            sql.append(" and c.coupon_id=").append(couponCondition.getCouponId()).append(" ");
        }
        sql.append(" ORDER BY c.createtime desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(couponCondition.getPageSize() == null ? 20 : couponCondition.getPageSize());
        pageInfo.setCurrePage(couponCondition.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }
}
