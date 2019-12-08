package com.yufan.task.service.impl.account;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/7 22:30
 * 功能介绍: 绑定手机号码
 */
public class BangPhone implements IResultOut {

    private Logger LOG = Logger.getLogger(BangPhone.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            return bangPhone(data);
        } catch (Exception e) {
            LOG.error("-------error----" , e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Transactional
    public String bangPhone(JSONObject data) {
        JSONObject dataJson = new JSONObject();
        try {
            Integer userId = data.getInteger("user_id");
            String phone = data.getString("phone");
            String phoneCode = data.getString("phone_code");//手机验证码

            //校验手机验证码
            TbVerification verification = iAccountDao.loadVerification(1, phone, phoneCode);
            if (null == verification) {
                LOG.info("---手机验证码无效---");
                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
            }
            //判断手机验证码是否过期
            long nowTime = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(),"yyyy-MM-dd HH:mm:ss").getTime();
            long passTime = verification.getPassTime().getTime();
            if(nowTime>passTime){
                LOG.info("---手机验证码过期---");
                iAccountDao.deltVerificationStatus(verification.getId());
                return packagMsg(ResultCode.CODE_NONEFFECTIVE.getResp_code(), dataJson);
            }
            //校验手机号是否已绑定
            TbUserInfo userInfo = iAccountDao.loadUserInfo(phone);
            if (null != userInfo) {
                LOG.info("---手机号已绑定---");
                return packagMsg(ResultCode.PHONE_HAS_BANG.getResp_code(), dataJson);
            }
            //绑定手机号
            iAccountDao.bangPhone(userId,phone);
            //手机验证码修改为无效
            iAccountDao.deltVerificationStatus(verification.getId());
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            String phone = data.getString("phone");
            String phoneCode = data.getString("phone_code");//手机验证码
            if (null == userId || userId <= 0 || StringUtils.isEmpty(phone) || StringUtils.isEmpty(phoneCode)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---" , e);
        }
        return false;
    }

}
