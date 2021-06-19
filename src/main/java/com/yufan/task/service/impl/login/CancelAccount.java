package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
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
 * 创建时间:  2019/12/15 18:02
 * 功能介绍:  注销账号(因为账号为手机号码，所以账号手机号码不能修改，只能注销账号,注销不可逆不可恢复)
 */
@Service("cancel_account")
public class CancelAccount implements IResultOut {

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
            Integer userId = data.getInteger("user_id");

            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByLogin(phone);
            if (null == userInfoList || userInfoList.size() != 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }

            Map<String, Object> map = userInfoList.get(0);
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

            //检验手机验证码
            if (!CommonMethod.checkPhoneCode(Constants.VALID_TYPE_7, phone, phoneCode, iAccountDao)) {
                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
            }
            //删除手机验证码
            iAccountDao.deltVerificationStatus(phone, phoneCode);
            String result = deleteAccount(phone, userId);
            if (StringUtils.isEmpty(result)) {
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 删除账号
     *
     * @param phone
     * @return
     */
    @Transactional
    public String deleteAccount(String phone, Integer userId) {
        try {
            //删除账号
            LOG.info("---------删除账号-------" + phone);
            iAccountDao.deleteAccount(phone);
            //删除账号绑定
            if (null != userId && userId != 0) {
                LOG.info("---------删除账号绑定-------userId:" + userId);
                iAccountDao.deleAccountBangSns(userId);
            }
            return packagMsg(ResultCode.OK.getResp_code(), new JSONObject());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            String phone = data.getString("phone");
            String phoneCode = data.getString("phone_code");//手机验证码
            Integer userId = data.getInteger("user_id");
            if (null == userId || userId == 0 || StringUtils.isEmpty(phone) || StringUtils.isEmpty(phoneCode)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}

