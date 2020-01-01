package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * 功能介绍: 步骤1发送验证码，   步骤2重置密码
 */
@Service("reset_passwd")
public class ResetLoginPasswd implements IResultOut {

    private Logger LOG = Logger.getLogger(ResetLoginPasswd.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String phone = data.getString("phone");
            String phoneCode = data.getString("phone_code");//手机验证码
            String passwd = data.getString("passwd");//(账号+密码)MD5

            //检验手机验证码
            if (!CommonMethod.checkPhoneCode(Constants.VALID_TYPE_3, phone, phoneCode, iAccountDao)) {
                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
            }
            //删除手机验证码
            iAccountDao.deltVerificationStatus(phone, phoneCode);
            iAccountDao.updateUserLoginPasswd(phone,passwd);
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
            String passwd = data.getString("passwd");//(账号+密码)MD5
            if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(phoneCode) || StringUtils.isEmpty(passwd)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
