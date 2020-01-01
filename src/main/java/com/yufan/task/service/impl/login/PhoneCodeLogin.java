package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            //检验手机验证码
            if (!CommonMethod.checkPhoneCode(Constants.VALID_TYPE_6, phone, phoneCode, iAccountDao)) {
                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
            }
            //删除手机验证码
            iAccountDao.deltVerificationStatus(phone, phoneCode);

            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByLogin(phone);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            Map<String, Object> map = userInfoList.size() == 0 ? null : userInfoList.get(0);
            if (map == null) {
                map = new HashMap<>();
            } else {
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
            }
            Object userId = map.get("user_id");
            Object nickName = map.get("nick_name");
            Object memberId = map.get("member_id");
            Object money = map.get("money");
            Object startTime = map.get("start_time");
            Object endTime = map.get("end_time");
            Object userImg = map.get("user_img") == null || "".equals(map.get("user_img").toString()) ? "" : Constants.IMG_WEB_URL + map.get("user_img");
            Object snsImg = map.get("sns_img");
            Object isUseImg = map.get("is_use_img");

            if (userInfoList.size() == 0) {
                //注册新用户
                TbUserInfo userInfo = new TbUserInfo();
                userInfo.setLoginName(phone);
                userInfo.setUserMobile(phone);
                userInfo.setMobileValite(1);
                userInfo.setUserState(1);
                userInfo.setLogCount(1);
                userInfo.setMemberId("");
                userInfo.setCreatetime(new Timestamp(new Date().getTime()));
                userInfo.setMoney(new BigDecimal("0"));
                userId = iAccountDao.saveObj(userInfo);
            }
            dataJson.put("user_id", userId);
            dataJson.put("nick_name", nickName == null ? "" : nickName);
            dataJson.put("user_mobile", phone);
            dataJson.put("member_id", memberId == null ? "" : memberId);
            dataJson.put("money", money == null ? "0" : money);
            dataJson.put("start_time", startTime == null ? "" : startTime);
            dataJson.put("end_time", endTime == null ? "" : endTime);
            dataJson.put("user_img", userImg == null ? "" : userImg);
            dataJson.put("sns_img", snsImg == null ? "" : snsImg);
            dataJson.put("is_use_img", isUseImg == null ? "" : isUseImg);

            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
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

}
