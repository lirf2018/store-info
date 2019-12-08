package com.yufan.task.dao.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.bean.UserSnsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/2 21:30
 * 功能介绍:
 */
@Transactional
@Repository
public class AccountDaoImpl implements IAccountDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public boolean checkWXBang(String openId) {
        try {
            String sql = " SELECT sns_type,uid from tb_user_sns where uid=? and sns_type=4 and status=1 ";
            List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, openId);
            if (null != list && list.size() == 0) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> queryUserInfo(String uid, int snsType) {
        StringBuffer sql = new StringBuffer();
        sql.append("  ");
        sql.append(" SELECT u.user_id,u.nick_name,u.user_mobile,u.member_id,u.money,DATE_FORMAT(u.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(u.end_time,'%Y-%m-%d %T') as end_time,u.user_img  ");
        sql.append(" ,us.sns_img,us.is_use_img  ");
        sql.append(" from tb_user_info u JOIN tb_user_sns us on u.user_id-us.user_id  ");
        sql.append(" where u.user_state != 3 and us.`status` != 3  ");
        sql.append(" and us.uid=? and us.sns_type=? ");
        return iGeneralDao.getBySQLListMap(sql.toString(), uid, snsType);
    }

    @Override
    public int saveObj(Object object) {
        return iGeneralDao.save(object);
    }

    @Override
    public void deltVerificationStatus(int id) {
        String sql = " update tb_verification set status=2 where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public TbVerification loadVerification(int validType, String validParam, String validCode) {
        String hql = " from TbVerification where validType=?1 and validParam=?2 and validCode=?3 and status=1 and sendStatus=2 ";
        return iGeneralDao.queryUniqueByHql(hql, validType, validParam, validCode);
    }

    @Override
    public TbUserInfo loadUserInfo(String phone) {
        String hql = " from TbUserInfo where userMobile=?1 and userState !=3 ";
        return iGeneralDao.queryUniqueByHql(hql, phone);
    }

    @Override
    public void bangPhone(int userId, String phone) {
        String sql = " update tb_user_info set user_mobile=? ,mobile_valite=1,lastaltertime=now() where user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, phone, userId);
    }

    @Override
    public void updateVerificationInfo(int id, int status, String passTime, int sendStatus, String sendMsg) {
        String sql = " update tb_verification set status=?,pass_time=?,lastaltertime=NOW(),send_status=?,send_msg=? where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, passTime, sendStatus, sendMsg, id);
    }
}
