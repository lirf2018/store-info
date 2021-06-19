package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.impl.addr.AddUserAddr;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 14:39
 * 功能介绍: 账号密码登录
 */
@Service("passwd_login")
public class UserLogin implements IResultOut {

    private Logger LOG = Logger.getLogger(AddUserAddr.class);

    @Autowired
    private IAccountDao iAccountDao;


    @Override
    @Transactional
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {


            String loginName = data.getString("login_name");//手机号码
            String passwd = data.getString("passwd");//（loginName + passwd）MD5后的密码

            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfoByLogin(loginName);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            if (userInfoList.size() == 0) {
                LOG.info("-------查询用户数据不存在-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            Map<String, Object> map = userInfoList.get(0);
            //判断密码
            String passwdDb = String.valueOf(map.get("login_pass"));
            if (!passwdDb.equals(passwd)) {
                LOG.info("-------查询用户密码有误-------");
                return packagMsg(ResultCode.PASSWD_ERROR.getResp_code(), dataJson);
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
            if (Constants.USER_STATUS_3 == userStatus) {
                LOG.info("--------用户已注销状态---------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
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
            Object phone = map.get("user_mobile");

            //已存在用户(返回用户信息)
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

            String loginName = data.getString("login_name");
            String passwd = data.getString("passwd");//（loginName + passwd）MD5后的密码
            if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passwd)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
