package com.yufan.task.dao.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbMemberId;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbUserSns;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.bean.UserSnsBean;
import com.yufan.utils.Constants;
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
            String sql = " SELECT sns_type,uid from tb_user_sns where uid=? and sns_type=? and status=? ";
            List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, openId, Constants.USER_SNS_TYPE_4, Constants.USER_SNS_TYPE_1);
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
        sql.append(" SELECT u.user_id,u.nick_name,u.user_mobile,u.member_id,u.money,DATE_FORMAT(u.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(u.end_time,'%Y-%m-%d %T') as end_time,u.user_img,u.user_state  ");
        sql.append(" ,us.sns_img,us.is_use_img  ");
        sql.append(" from tb_user_info u JOIN tb_user_sns us on u.user_id-us.user_id ");
        sql.append(" where u.user_state != 3 ");
        sql.append(" and us.`status` != 3 and us.uid=? and us.sns_type=? ");
        return iGeneralDao.getBySQLListMap(sql.toString(), uid, snsType);
    }

    @Override
    public List<Map<String, Object>> queryUserInfoByPhone(String phone, int snsType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT u.user_id,u.nick_name,u.user_mobile,u.member_id,u.money,DATE_FORMAT(u.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(u.end_time,'%Y-%m-%d %T') as end_time,u.user_img,u.user_state  ");
        sql.append(" ,us.sns_img,us.is_use_img  ");
        sql.append(" from tb_user_info u left join tb_user_sns us on u.user_id-us.user_id and us.`status` != 3 and us.sns_type=?  ");
        sql.append(" where u.user_state != 3 and u.user_mobile=? ");
        return iGeneralDao.getBySQLListMap(sql.toString(), snsType, phone);
    }

    @Override
    public List<Map<String, Object>> queryUserInfoByLogin(String phone) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT u.user_id,u.nick_name,u.user_mobile,u.member_id,u.money,DATE_FORMAT(u.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(u.end_time,'%Y-%m-%d %T') as end_time,u.user_img,u.user_state,u.login_pass  ");
        sql.append(" from tb_user_info u ");
        sql.append(" where u.user_state != 3 and u.user_mobile=?  ");
        return iGeneralDao.getBySQLListMap(sql.toString(), phone);
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
    public void deltVerificationStatus(String validParam, String validCode) {
        String sql = " update tb_verification set status=2 where valid_param=? and valid_code=? ";
        iGeneralDao.executeUpdateForSQL(sql, validParam, validCode);
    }

    @Override
    public void deltVerificationStatus(String validParam, String validCode, Integer validType) {
        String sql = " update tb_verification set status=2 where valid_param=? and valid_code=? and valid_type=? ";
        iGeneralDao.executeUpdateForSQL(sql, validParam, validCode, validType);
    }

    @Override
    public TbVerification loadVerification(int validType, String validParam, String validCode) {
        String hql = " from TbVerification where validType=?1 and validParam=?2 and validCode=?3 and status=1 and sendStatus=2 ";
        return iGeneralDao.queryUniqueByHql(hql, validType, validParam, validCode);
    }

    @Override
    public TbVerification loadVerification(int validType, String validParam) {
        // 更新失效的
        String sql = " update tb_verification set status=0,lastaltertime=NOW()  where `status`=1 and pass_time<NOW() and valid_param='" + validParam + "' ";
        iGeneralDao.executeUpdateForSQL(sql);


        String hql = " from TbVerification where validType=?1 and validParam=?2 and status=1 and sendStatus=2 ";
        return iGeneralDao.queryUniqueByHql(hql, validType, validParam);
    }

    @Override
    public TbUserInfo loadUserInfo(String phone) {
        String hql = " from TbUserInfo where userMobile=?1 and userState !=3 ";
        return iGeneralDao.queryUniqueByHql(hql, phone);
    }

    @Override
    public TbUserInfo loadUserInfo(int userId) {
        String hql = " from TbUserInfo where userId=?1 and userState !=3 ";
        return iGeneralDao.queryUniqueByHql(hql, userId);
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

    @Override
    public void updateUserLoginPasswd(String phone, String passwd) {
        String sql = " update tb_user_info set login_pass=?,lastaltertime=now() where user_mobile=? and user_state !=3 ";
        iGeneralDao.executeUpdateForSQL(sql, passwd, phone);
    }

    @Override
    public void deleteAccount(String phone) {
        String sql = " update tb_user_info set user_state=?,lastaltertime=now() where user_mobile=? ";
        iGeneralDao.executeUpdateForSQL(sql, Constants.USER_STATUS_3, phone);
    }

    @Override
    public void deleAccountBangSns(int userId) {
        String sql = " update tb_user_sns set status=?,update_time=now() where user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, Constants.USER_SNS_STATUS_2, userId);
    }

    @Override
    public void deleteSnsBang(int userId, int snsType) {
        String sql = " update tb_user_sns set status=?,update_time=now() where user_id=? and  sns_type = ? ";
        iGeneralDao.executeUpdateForSQL(sql, Constants.USER_SNS_STATUS_2, userId, snsType);
    }

    @Override
    public boolean checkInviterNum(String inviterNum) {
        String sql = " select user_id,member_id from tb_user_info where member_id='" + inviterNum + "' ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql);
        if (list.size() >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<TbUserSns> findUserSnsList(int userId) {
        String hql = " from TbUserSns where userId=?1 and status =1 ";
        return (List<TbUserSns>) iGeneralDao.queryListByHql(hql, userId);
    }

    @Override
    public void updateUserMemberNum(int userId, String memberNum) {
        String sql = " update tb_user_info set member_id=?,lastaltertime=now() where user_id=?  ";
        iGeneralDao.executeUpdateForSQL(sql, memberNum, userId);
    }

    @Override
    public List<TbUserInfo> findUserInfoByMemberNumList(String memberNum) {
        String hql = " from TbUserInfo where inviterNum=?1 and userState =1 order by createtime desc ";
        return (List<TbUserInfo>) iGeneralDao.queryListByHql(hql, memberNum);
    }

    @Override
    public TbMemberId loadMemberId(String memberId) {
        String hql = " from TbMemberId where memberId=?1 ";

        return (TbMemberId) iGeneralDao.queryUniqueByHql(hql, memberId);
    }
}
