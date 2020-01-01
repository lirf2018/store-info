package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.bean.UserSnsBean;
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
 * 创建时间:  2019/12/15 15:39
 * 功能介绍: (共3步骤) 步骤1： 第三方登录 --> 根据第三方openid先获取账号信息 --> 1.获取用户信息成功（登录成功）
 *                                                     --> 2.获取用户信息异常（重新获取）
 *                                                     --> 3.获取用户信息不存在（跳到注册并绑定手机号码页面）
 *
 *     根据第三方账号获取用户信息
 *
 */
@Service("check_three_login")
public class CheckThreeLogin implements IResultOut {

    private Logger LOG = Logger.getLogger(CheckThreeLogin.class);

    @Autowired
    private IAccountDao iAccountDao;


    @Override
    @Transactional
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            String openId = userSnsBean.getUid();
            int snsType = userSnsBean.getSnsType();
            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfo(openId, snsType);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (0 == userInfoList.size()) {
                LOG.info("-------查询用户数据不存在------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            } else {
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

                //已存在用户(返回用户信息)
                dataJson.put("user_id" , map.get("user_id"));
                dataJson.put("nick_name" , map.get("nick_name"));
                dataJson.put("user_mobile" , map.get("user_mobile"));
                dataJson.put("member_id" , map.get("member_id"));
                dataJson.put("money" , map.get("money"));
                dataJson.put("start_time" , map.get("start_time"));
                dataJson.put("end_time" , map.get("end_time"));
                dataJson.put("user_img" , Constants.IMG_WEB_URL + map.get("user_img"));
                dataJson.put("sns_img" , map.get("sns_img"));
                dataJson.put("is_use_img" , map.get("is_use_img"));
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----" , e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }


    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            String openId = userSnsBean.getUid();
            Integer snsType = userSnsBean.getSnsType();
            if (null == snsType || StringUtils.isEmpty(openId)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---" , e);
        }
        return false;
    }
}