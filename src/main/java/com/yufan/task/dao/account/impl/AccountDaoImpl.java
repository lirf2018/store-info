package com.yufan.task.dao.account.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.account.IAccountDao;
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
    public List<Map<String, Object>> queryUserInfo(String uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("  ");
        sql.append(" SELECT u.user_id,u.nick_name,u.user_mobile,u.member_id,u.money,DATE_FORMAT(u.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(u.end_time,'%Y-%m-%d %T') as end_time,u.user_img  ");
        sql.append(" ,us.sns_img,us.is_use_img  ");
        sql.append(" from tb_user_info u JOIN tb_user_sns us on u.user_id-us.user_id  ");
        sql.append(" where u.user_state=1 and us.`status`=1  ");
        sql.append(" and us.uid=?  ");
        return iGeneralDao.getBySQLListMap(sql.toString(), uid);
    }
}
