package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.testRedis.LoginCache;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin.util.UIUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 17:30
 * 功能介绍: 步骤1发送验证码，   步骤2手机验证码登录
 */
@Service("phone_code_login")
public class PhoneCodeLogin implements IResultOut {

    private Logger LOG = Logger.getLogger(PhoneCodeLogin.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String phone = data.getString("phone");
            String phoneCode = data.getString("phone_code");//手机验证码
            String memberCode = data.getString("member_code");//推荐人

            //检验手机验证码
//            if (!CommonMethod.checkPhoneCode(Constants.VALID_TYPE_6, phone, phoneCode, iAccountDao)) {
//                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
//            }

            //删除手机验证码
            iAccountDao.deltVerificationStatus(phone, phoneCode, Constants.VALID_TYPE_6);

            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByLogin(phone);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            Map<String, Object> map = userInfoList.size() == 0 ? null : userInfoList.get(0);
            if (map == null) {
                map = new HashMap<>();
                map.put("user_mobile", phone);
            } else {
                //判断用户状态(用户已存在)
                int userStatus = Integer.parseInt(map.get("user_state").toString());
                if (Constants.USER_STATUS_0 == userStatus) {
                    LOG.info("--------用户待验证---------");
                    return packagMsg(ResultCode.FAIL_USER_NEED_VERIFY.getResp_code(), dataJson);
                }
                if (Constants.USER_STATUS_2 == userStatus) {
                    LOG.info("--------用户已锁定状态---------");
                    return packagMsg(ResultCode.FAIL_USER_LOCK.getResp_code(), dataJson);
                }
            }
            Integer userId = map.get("user_id") == null ? 0 : Integer.parseInt(map.get("user_id").toString());
            // 推荐人userId
            if (userInfoList.size() == 0) {
                //注册新用户
                TbUserInfo userInfo = new TbUserInfo();
                userInfo.setNickName("淘果匠" + getNickName());
                userInfo.setLoginName(phone);
                userInfo.setUserMobile(phone);
                userInfo.setMobileValite(1);
                userInfo.setUserState(1);
                userInfo.setLogCount(0);
                userInfo.setMemberId("");
                userInfo.setCreatetime(new Timestamp(new Date().getTime()));
                userInfo.setMoney(new BigDecimal("0"));
                userInfo.setInviterJf(0);
                userInfo.setInviterMoney(BigDecimal.ZERO);
                userInfo.setInviterNum("");
                userInfo.setJifen(0);
                userInfo.setUserImg("touxiang.jpg");
                userInfo.setLastlogintime(new Timestamp(System.currentTimeMillis()));
                // 判断推荐人是否已存在
                Integer tuiJianUserId = iAccountDao.checkInviterNum(memberCode);
                if (StringUtils.isNotEmpty(memberCode) && tuiJianUserId != null) {
                    userInfo.setInviterNum(memberCode);
                    // 更新推荐人（更新推荐人数量）
                    Map<String, String> mapValue = new HashMap<>();
                    mapValue.put("tuiJianCount", "1");
                    iAccountDao.updateUserInfoTime(tuiJianUserId, mapValue);
                }
                userId = iAccountDao.saveObj(userInfo);
                map.put("user_id", userId);
            }
            if (userId == 0) {
                LOG.info("-------保存用户异常--------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            // 更新登录时间
            Map<String, String> mapValue = new HashMap<>();
            mapValue.put("updateLoginTime", "1");
            iAccountDao.updateUserInfoTime(userId, mapValue);

            // 注册或者登录成功（保存缓存并生成token）
            String token = getToken();
            long tokenPassTime = DatetimeUtil.addMinutes(new Date(), Constants.LOGIN_TOKEN_PASS_TIME).getTime();// 过期时间戳
            saveRedisSession(token, map, tokenPassTime);
            dataJson.put("token", token);
            dataJson.put("passTime", tokenPassTime);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private String getToken() {
        synchronized (PhoneCodeLogin.class) {
            String token = UUID.randomUUID().toString().replace("-", "");
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return token;
        }
    }

    /**
     * 保存登录信息（登录成功）
     */
    private void saveRedisSession(String token, Map<String, Object> map, long tokenPassTime) {
        Object userId = map.get("user_id");
        Object userMobile = map.get("user_mobile");
        JSONObject tokenObj = new JSONObject();
        tokenObj.put("userId", userId);
        tokenObj.put("userMobile", userMobile);
        tokenObj.put("tokenPassTime", tokenPassTime);
        LoginCache.loginMapsToken.put(token, tokenObj);
        //
        JSONObject userIdObj = new JSONObject();
        userIdObj.put("token", token);
        userIdObj.put("tokenPassTime", tokenPassTime);
        LoginCache.loginMapsUserId.put(String.valueOf(userId), userIdObj);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            String phone = data.getString("phone");
            String phoneCode = data.getString("phone_code");//手机验证码
            if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(phoneCode)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

    public String getNickName() {
        String[] number = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};// 9
        String[] a_z = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};// 26
        String[] a_z_ = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};// 26

        StringBuffer str = new StringBuffer();
        Random random = new Random();
        int n1 = random.nextInt(25);
        str.append(a_z[n1]);
        n1 = random.nextInt(25);
        str.append(a_z[n1]);
        n1 = random.nextInt(25);
        str.append(a_z_[n1]);
        n1 = random.nextInt(25);
        str.append(a_z_[n1]);
        return str.toString();
    }
}
