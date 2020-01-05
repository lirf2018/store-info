package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.bean.UserSnsBean;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.sms.aliyun.AliyunSmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 15:39
 * 功能介绍: (共3步骤) 步骤2： 第三方登录 --> 判断注册手机号是否已绑定相同类型的第三方账号    -->1.手机号未注册,发验证码 1.1 注册绑定微信
 * -->2.手机号已注册，
 * ---------->（2.1未绑定）,发验证码   1.1绑定微信
 * ---------->（2.2已绑定）,不发送验证码，并提示用户通过手机验证码登录并更新绑定微信
 * <p>
 * 发送绑定验证码
 */
@Service("check_three_login2")
public class CheckThreeLogin2 implements IResultOut {

    private Logger LOG = Logger.getLogger(CheckThreeLogin2.class);

    @Autowired
    private IAccountDao iAccountDao;


    @Override
    @Transactional
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            int snsType = data.getInteger("sns_type");
            String phone = data.getString("phone");

            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByPhone(phone, snsType);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询数据异常-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            if (userInfoList.size() == 0) {
                LOG.info("-----手机号未注册--发送验证码------");
                return sendPhoneCode(Constants.VALID_TYPE_1, phone, "第三方账号登录,绑定手机号");
            }

            //手机已注册
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

            //判断是否已绑定
            String snsImg = map.get("sns_img") == null ? "" : map.get("sns_img").toString();
            if (StringUtils.isNotEmpty(snsImg)) {
                LOG.info("-----手机号已注册--已绑定第三方信息------");
                return packagMsg(ResultCode.PHONE_HAS_USED.getResp_code(), dataJson);
            }

            //-----手机号已注册--未绑定第三方信息------
            return sendPhoneCode(Constants.VALID_TYPE_1, phone, "第三方账号登录,绑定手机号");
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 发送验证码
     */
    private String sendPhoneCode(int validType, String phone, String validDesc) {
        LOG.info("--------发送验证码---------");
        JSONObject dataJson = new JSONObject();
        try {
            Random random = new Random();
            int r = random.nextInt(8999) + 1000;//随机4位数
            String validCode = String.valueOf(r);//生成验证码
            LOG.info("------------生成验证码:" + validCode);
            //生成记录
            TbVerification verification = new TbVerification();
            verification.setValidType(validType);
            verification.setValidParam(phone);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }


    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer snsType = data.getInteger("sns_type");
            String phone = data.getString("phone");
            if (null == snsType || StringUtils.isEmpty(phone)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}