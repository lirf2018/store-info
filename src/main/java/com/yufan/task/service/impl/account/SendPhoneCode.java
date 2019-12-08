package com.yufan.task.service.impl.account;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/7 22:40
 * 功能介绍: 发送手机验证码
 */
@Service("send_phone_code")
public class SendPhoneCode implements IResultOut {

    private Logger LOG = Logger.getLogger(SendPhoneCode.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer validType = data.getInteger("valid_type");//验证码类型:1手机绑定2修改密码3重置密码4手机解绑5手机注册
            String validParam = data.getString("valid_param");//验证标识参数 如：手机号,邮箱
            String validDesc = data.getString("valid_desc");//验证类型说明

            Random random = new Random();
            int r = random.nextInt(8999) + 1000;//随机4位数
            String validCode = String.valueOf(r);//生成验证码
            LOG.info("------------生成验证码:" + validCode);
            //生成记录
            TbVerification verification = new TbVerification();
            verification.setValidType(validType);
            verification.setValidParam(validParam);
            verification.setValidCode(validCode);
            verification.setValidDesc(validDesc);
            verification.setStatus(0);
            //private Timestamp passTime;
            verification.setCreatetime(new Timestamp(new Date().getTime()));
            verification.setSendStatus(0);
            iAccountDao.saveObj(verification);
            //---------------------------------------
            //调用第三方接口发送短信
            JSONObject result = new JSONObject();
            result.put("code", 1);
            result.put("desc", "");
            //---------------------------------------
            int code = result.getInteger("code");
            String desc = result.getString("desc");
            String passTime = DatetimeUtil.addMinuteTime(DatetimeUtil.getNow(), 5, "yyyy-MM-dd HH:mm:ss");//5分钟有效
            if (code == 1) {
                //发送成功
                iAccountDao.updateVerificationInfo(verification.getId(), 1, passTime, 2, "");
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            //发送失败
            iAccountDao.updateVerificationInfo(verification.getId(), 1, passTime, 1, desc);
            return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer validType = data.getInteger("valid_type");//验证码类型:1手机绑定2修改密码3重置密码4手机解绑5手机注册
            String validParam = data.getString("valid_param");//验证标识参数 如：手机号,邮箱
            String validCode = data.getString("valid_code");//验证码
            if (null == validType || StringUtils.isEmpty(validParam) || StringUtils.isEmpty(validCode)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
