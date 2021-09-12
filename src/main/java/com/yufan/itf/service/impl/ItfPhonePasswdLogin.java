package com.yufan.itf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.itf.dao.IItfAdminDao;
import com.yufan.pojo.ItfTbItemAdmin;
import com.yufan.testRedis.LoginCache;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.MD5;
import com.yufan.utils.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 17:30
 * 功能介绍: 步骤1发送验证码，   步骤2手机验证码登录
 */
@Service("phone_itf_login")
public class ItfPhonePasswdLogin implements IResultOut {

    private Logger LOG = Logger.getLogger(ItfPhonePasswdLogin.class);

    @Autowired
    private IItfAdminDao iItfAdminDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String phone = data.getString("phone").trim();
            String passwd = data.getString("password").trim();

            // 查询用户
            ItfTbItemAdmin admin = iItfAdminDao.loadItfTbItemAdminByName(phone);
            if (null == admin) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            //判断用户状态(用户已存在)
            String userStatus = admin.getStatus();
            if (!"1".equals(userStatus)) {
                LOG.info("--------用户无效状态---------");
                return packagMsg(ResultCode.FAIL_USER_LOCK.getResp_code(), dataJson);
            }
            String md5 = MD5.enCodeStandard(passwd + phone);
            if (!md5.equals(admin.getPasswd())) {
                LOG.info("--------用户密码有误---------");
                return packagMsg(ResultCode.ACCOUNT_OR_PASSWD_FAIL.getResp_code(), dataJson);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", admin.getAdminId());
            map.put("user_mobile", admin.getAdminName());
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
        synchronized (ItfPhonePasswdLogin.class) {
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
            String passwd = data.getString("password");

            if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(passwd)) {
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

    public static void main(String[] args) {
        System.out.println(MD5.enCodeStandard("admin123" + "admin"));
    }
}
