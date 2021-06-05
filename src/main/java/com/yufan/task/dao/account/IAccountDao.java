package com.yufan.task.dao.account;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbMemberId;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbUserSns;
import com.yufan.pojo.TbVerification;
import com.yufan.task.service.bean.UserSnsBean;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/2 21:29
 * 功能介绍:
 */
public interface IAccountDao {

    /**
     * 校验openid 是否已绑定
     *
     * @param openId
     * @return
     */
    public boolean checkWXBang(String openId);

    /**
     * 根据uid查询用户登录信息
     *
     * @param uid
     * @return
     */
    public List<Map<String, Object>> queryUserInfo(String uid, int snsType);

    public List<Map<String, Object>> queryUserInfoByPhone(String phone, int snsType);

    public List<Map<String, Object>> queryUserInfoByLogin(String phone);


    /**
     * @return
     */
    public int saveObj(Object object);

    /**
     * 逻辑删除手机验证码
     *
     * @param id
     */
    public void deltVerificationStatus(int id);

    public void deltVerificationStatus(String validParam, String validCode);

    public void deltVerificationStatus(String validParam, String validCode, Integer validType);


    /**
     * 查询有效手机验证码(发送成功的)（校验）
     *
     * @param validParam
     * @param validCode
     * @return
     */
    public TbVerification loadVerification(int validType, String validParam, String validCode);

    /**
     * 查询有效手机验证码(发送成功的)
     *
     * @param validParam
     * @return
     */
    public TbVerification loadVerification(int validType, String validParam);

    /**
     * 查询手机是否已绑定
     *
     * @param phone
     * @return
     */
    public TbUserInfo loadUserInfo(String phone);

    public TbUserInfo loadUserInfo(int userId);

    /**
     * 绑定手机号码
     *
     * @param userId
     * @param phone
     */
    public void bangPhone(int userId, String phone);


    /**
     * 更新验证码信息
     *
     * @param id
     * @param status
     * @param passTime
     * @param sendStatus
     * @param sendMsg
     */
    public void updateVerificationInfo(int id, int status, String passTime, int sendStatus, String sendMsg);

    /**
     * 重置密码
     *
     * @param phone
     * @param passwd
     */
    public void updateUserLoginPasswd(String phone, String passwd);


    /**
     * 注册账号
     *
     * @param phone
     */
    public void deleteAccount(String phone);

    /**
     * 删除账号绑定信息
     */
    public void deleAccountBangSns(int userId);


    /**
     * 解绑第三方账号
     *
     * @param userId
     * @param snsType
     */
    public void deleteSnsBang(int userId, int snsType);


    /**
     * 校验推荐人是否存在
     */
    public boolean checkInviterNum(String inviterNum);


    /**
     * 查询绑定列表
     *
     * @param userId
     * @return
     */
    public List<TbUserSns> findUserSnsList(int userId);

    /**
     * 更新用户会员号
     *
     * @param userId
     * @param memberNum
     */
    public void updateUserMemberNum(int userId, String memberNum);

    /**
     * 查询绑定用户列表
     *
     * @param memberNum
     * @return
     */
    public List<TbUserInfo> findUserInfoByMemberNumList(String memberNum);


    public TbMemberId loadMemberId(String memberId);

}
