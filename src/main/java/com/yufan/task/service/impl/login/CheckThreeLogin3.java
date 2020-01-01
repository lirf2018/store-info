package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbUserSns;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.bean.UserSnsBean;
import com.yufan.task.service.impl.addr.AddUserAddr;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 15:39
 * 功能介绍: (共3步骤) 步骤3： 第三方登录 --> 判断注册手机号是否存在    -->1.手机号未注册,注册新手机号用户
 * -->1.手机号已注册,(绑定微信)
 * 注册和绑定
 */
@Service("check_three_login3")
public class CheckThreeLogin3 implements IResultOut {

    private Logger LOG = Logger.getLogger(CheckThreeLogin3.class);

    @Autowired
    private IAccountDao iAccountDao;


    @Override
    @Transactional
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            String phone = userSnsBean.getPhone();
            String phoneCode = userSnsBean.getPhoneCode();

            //检验手机验证码
            if (!CommonMethod.checkPhoneCode(Constants.VALID_TYPE_1,phone, phoneCode,iAccountDao)) {
                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
            }

            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByLogin(phone);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            if (userInfoList.size() == 0) {
                //手机未注册,注册新用户和绑定微信
                dataJson = registerOther(userSnsBean);
                if (null == dataJson) {
                    return packagMsg(ResultCode.FAIL.getResp_code(), new JSONObject());
                }
            } else {
                //手机已注册  绑定微信
                Map<String, Object> map = userInfoList.get(0);

                int userId = Integer.parseInt(map.get("user_id").toString());
                boolean flag = bangThreeAccount(userSnsBean,userId);
                if (!flag) {
                    return packagMsg(ResultCode.FAIL.getResp_code(), new JSONObject());
                }

                //判断用户状态
                int userStatus = Integer.parseInt(map.get("user_state").toString());
                if (Constants.USER_STATUS_0 == userStatus) {
                    LOG.info("--------用户待验证---------");
                    return packagMsg(ResultCode.FAIL_USER_NEED_VERIFY.getResp_code(), dataJson);
                }
                if (Constants.USER_STATUS_2 == userStatus) {
                    LOG.info("--------用户已锁定状态---------");
                    return packagMsg(ResultCode.FAIL_USER_LOCK.getResp_code(), dataJson);
                }

                dataJson.put("user_id", userId);
                dataJson.put("nick_name", "");
                dataJson.put("user_mobile", phone);
                dataJson.put("member_id", "");
                dataJson.put("money", 0);
                dataJson.put("start_time", "");
                dataJson.put("end_time", "");
                dataJson.put("user_img", "");
                dataJson.put("sns_img", userSnsBean.getSnsImg());
                dataJson.put("is_use_img", userSnsBean.getIsUseImg());
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 注册新用户
     *
     * @param userSnsBean
     * @return
     */
    @Transactional
    public JSONObject registerOther(UserSnsBean userSnsBean) {
        try {
            //注册用户
            TbUserInfo userInfo = new TbUserInfo();
            userInfo.setLoginName(userSnsBean.getPhone());
            userInfo.setUserMobile(userSnsBean.getPhone());
            userInfo.setMobileValite(1);
            userInfo.setUserState(1);
            userInfo.setLogCount(1);
            userInfo.setMemberId("");
            userInfo.setCreatetime(new Timestamp(new Date().getTime()));
            userInfo.setMoney(new BigDecimal("0"));
            iAccountDao.saveObj(userInfo);
            //绑定微信
            TbUserSns userSns = new TbUserSns();
            userSns.setUserId(userInfo.getUserId());
            userSns.setSnsType(userSnsBean.getSnsType());
            userSns.setUid(userSnsBean.getUid());
            userSns.setOpenkey(userSnsBean.getOpenkey());
            userSns.setSnsName(userSnsBean.getSnsName());
            userSns.setSnsAccount(userSnsBean.getSnsAccount());
            userSns.setSnsImg(userSnsBean.getSnsImg());
            userSns.setIsUseImg(userSnsBean.getIsUseImg());
            userSns.setCreatetime(new Timestamp(new Date().getTime()));
            userSns.setStatus(1);
            iAccountDao.saveObj(userSns);
            JSONObject dataJson = new JSONObject();
            dataJson.put("user_id", userInfo.getUserId());
            dataJson.put("nick_name", userInfo.getNickName());
            dataJson.put("user_mobile", userInfo.getUserMobile());
            dataJson.put("member_id", userInfo.getMemberId());
            dataJson.put("money", userInfo.getMoney());
            dataJson.put("start_time", userInfo.getStartTime());
            dataJson.put("end_time", userInfo.getEndTime());
            dataJson.put("user_img", userInfo.getUserImg());
            dataJson.put("sns_img", userSnsBean.getSnsImg());
            dataJson.put("is_use_img", userSnsBean.getIsUseImg());
            return dataJson;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 绑定第三方账号
     * @param userSnsBean
     * @return
     */
    @Transactional
    public boolean bangThreeAccount(UserSnsBean userSnsBean,int userId) {
        try {
            //绑定微信
            TbUserSns userSns = new TbUserSns();
            userSns.setUserId(userId);
            userSns.setSnsType(userSnsBean.getSnsType());
            userSns.setUid(userSnsBean.getUid());
            userSns.setOpenkey(userSnsBean.getOpenkey());
            userSns.setSnsName(userSnsBean.getSnsName());
            userSns.setSnsAccount(userSnsBean.getSnsAccount());
            userSns.setSnsImg(userSnsBean.getSnsImg());
            userSns.setIsUseImg(userSnsBean.getIsUseImg());
            userSns.setCreatetime(new Timestamp(new Date().getTime()));
            userSns.setStatus(1);
            iAccountDao.saveObj(userSns);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            String phone = userSnsBean.getPhone();
            String phoneCode = userSnsBean.getPhoneCode();
            String openId = userSnsBean.getUid();
            Integer snsType = userSnsBean.getSnsType();
            if (null == snsType || StringUtils.isEmpty(openId) || StringUtils.isEmpty(phoneCode) || StringUtils.isEmpty(phone)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}