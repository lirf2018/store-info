package com.yufan.task.dao.coupon.impl;

import com.yufan.bean.CouponCondition;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.collections.CollectionUtils;
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
        // 更新过期优惠券
        updatePassDate(userId, null);
        StringBuffer sql = new StringBuffer();
        sql.append(" select qr.id, CONCAT('" + Constants.IMG_WEB_URL + "',qr.coupon_img) as coupon_img,qr.coupon_name,qr.title, ");
        sql.append(" DATE_FORMAT(qr.change_out_date,'%Y-%m-%d') as change_out_date,qr.recode_state,qr.appoint_type ");
        sql.append(" from tb_coupon_down_qr  qr ");
        sql.append(" where 1=1 and qr.user_id = ").append(userId).append(" ");
        sql.append(" ORDER BY createtime desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public PageInfo loadCouponListPage(CouponCondition couponCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.coupon_id,c.coupon_name,c.title,c.coupon_num, DATE_FORMAT(c.start_time,'%Y.%m.%d') as start_time,DATE_FORMAT(c.end_time,'%Y.%m.%d') as end_time,c.appoint_type, ");
        sql.append(" CONCAT('" + Constants.IMG_WEB_URL + "',c.coupon_img) as coupon_img,c.count_get ");
        sql.append("  ,if(DATE_FORMAT(NOW(),'%Y-%m-%d')=DATE_FORMAT(c.appoint_date,'%Y-%m-%d'),1,0) as now_use_date ");
        sql.append(" from tb_coupon c ");
        sql.append(" where c.`status`=1 and c.is_putaway=2 and NOW()>=c.start_time and NOW()<=c.end_time and c.coupon_num>0 ");
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

    @Override
    public TbCoupon loadCoupon(int couponId) {
        String hql = " from TbCoupon where couponId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, couponId);
    }

    @Override
    public TbCouponDownQr loadCouponDownQrByQrId(int qrId) {
        String hql = " from TbCouponDownQr where id=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, qrId);
    }

    @Override
    public TbCouponDownQr loadCouponDownQrByQrCode(String code) {
        String hql = " from TbCouponDownQr where changeCode=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, code);
    }

    @Override
    public TbCouponDownQr loadCouponDownQrByCouponId(int couponId, int userId) {
        String hql = " from TbCouponDownQr where couponId=?1 and userId=?2 and recodeState=1 order by lastaltertime desc ";
        List<TbCouponDownQr> list = (List<TbCouponDownQr>) iGeneralDao.queryListByHql(hql, couponId, userId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateQrCodeChangeTime(int userId, int qrId, String time) {
        String sql = " update tb_coupon_down_qr set qr_code_outtime=? ,lastaltertime=now() where user_id=? and id=? ";
        iGeneralDao.executeUpdateForSQL(sql, time, userId, qrId);
        // 更新过期优惠券
        updatePassDate(userId, qrId);
    }

    /**
     * 逻辑删除过期QR
     *
     * @param userId
     * @param qrId
     */
    private void updatePassDate(int userId, Integer qrId) {
        // 更新过期优惠券
        if (null != qrId) {
            String sql = " update tb_coupon_down_qr set recode_state=4 where id=? and user_id=? and DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(change_out_date,'%Y-%m-%d') and recode_state in (1,0) ";
            iGeneralDao.executeUpdateForSQL(sql, qrId, userId);
        } else {
            String sql = " update tb_coupon_down_qr set recode_state=4 where user_id=? and DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(change_out_date,'%Y-%m-%d') and recode_state in (1,0)  ";
            iGeneralDao.executeUpdateForSQL(sql, userId);
        }
    }

    @Override
    public int saveCouponDownQr(TbCouponDownQr qr) {
        return iGeneralDao.save(qr);
    }

    @Override
    public void updateQrRecordStatusWith1(int userId, int qrId) {
        String time = CommonMethod.qrChangeOutTime();
        String sql = " update tb_coupon_down_qr set qr_code_outtime=? ,lastaltertime=now() where user_id=? and id=? ";
        iGeneralDao.executeUpdateForSQL(sql, time, userId, qrId);
    }

    @Override
    public int changeQrRecordStatus(int id) {
        String sql = " update tb_coupon_down_qr set recode_state=2,lastaltertime=now(),change_date=now() where id=? and DATE_FORMAT(NOW(),'%Y-%m-%d') <= DATE_FORMAT(change_out_date,'%Y-%m-%d') ";
        return iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public void updateCouponGetCount(int couponId) {
        String sql = " update tb_coupon set count_get=count_get+1 ,lastaltertime=now() where coupon_id=?  ";
        iGeneralDao.executeUpdateForSQL(sql, couponId);
    }

    @Override
    public List<Map<String, Object>> loadUserQRCouponListLimit(int couponId, int userId, String limitTime) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select DATE_FORMAT(q.createtime,'%Y-%m-%d') as createtime,q.coupon_id,q.recode_state ");
        sql.append(" from tb_coupon_down_qr q where q.user_id=? and q.coupon_id=? and q.createtime>=? and (q.recode_state = 1 or q.recode_state = 2) order by q.createtime desc ");
        return iGeneralDao.getBySQLListMap(sql.toString(), userId, couponId, limitTime);
    }

    @Override
    public void deleteUserQrCoupon(int userId, int coupon) {
        String sql = " update tb_coupon_down_qr set recode_state=4 where coupon_id=? and user_id=? and DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(change_out_date,'%Y-%m-%d') and recode_state in (1,0)  ";
        iGeneralDao.executeUpdateForSQL(sql, coupon, userId);
    }
}
