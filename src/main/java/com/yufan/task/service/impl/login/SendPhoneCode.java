package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.sms.aliyun.AliyunSmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

            String checkResult = checkAccount(validParam, validType);//特殊检验

            if (checkResult == null) {
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (!"".equals(checkResult)) {
                return checkResult;
            }

            //查询是否存在有效验证码
            TbVerification isExist = iAccountDao.loadVerification(validType, validParam);
            if (null != isExist && isExist.getStatus() == 1) {
                //判断过期时间
                String format = "yyyy-MM-dd HH:mm:ss";
                String passTime = DatetimeUtil.timeStamp2Date(isExist.getPassTime().getTime(), format);
                String now = DatetimeUtil.getNow();
                if (DatetimeUtil.compareDate(passTime, now) > -1) {
                    return packagMsg(ResultCode.OK.getResp_code(), dataJson);
                }
            }

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
            JSONObject result = AliyunSmsUtil.getInstence().test();
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


    /**
     * 检验手机号码
     * 检验账号手机号状态来决定是否允许发送短信
     *
     * @param phone
     * @param validType
     * @return
     */
    private String checkAccount(String phone, Integer validType) {
        JSONObject dataJson = new JSONObject();
        try {
            if (Constants.VALID_TYPE_7 == validType) {//账号注销
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
            } else if (Constants.VALID_TYPE_3 == validType) {//重置密码
                //查询用户信息
                List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByLogin(phone);
                if (null == userInfoList || userInfoList.size() != 1) {
                    LOG.info("-------查询用户数据异常-------");
                    return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer validType = data.getInteger("valid_type");//验证码类型:1手机绑定2修改密码3重置密码4手机解绑5手机注册
            String validParam = data.getString("valid_param");//验证标识参数 如：手机号,邮箱
            if (null == validType || StringUtils.isEmpty(validParam)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
