package com.yufan.task.dao.account;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbUserInfo;
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


    /**
     * 查询有效手机验证码
     *
     * @param validParam
     * @param validCode
     * @return
     */
    public TbVerification loadVerification(int validType, String validParam, String validCode);

    /**
     * 查询手机是否已绑定
     *
     * @param phone
     * @return
     */
    public TbUserInfo loadUserInfo(String phone);

    /**
     * 绑定手机号码
     *
     * @param userId
     * @param phone
     */
    public void bangPhone(int userId, String phone);


    /**
     * 更新验证码信息
     * @param id
     * @param status
     * @param passTime
     * @param sendStatus
     * @param sendMsg
     */
    public void updateVerificationInfo(int id, int status, String passTime, int sendStatus, String sendMsg);
}
