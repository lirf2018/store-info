package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserSns;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.bean.UserSnsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 19:22
 * 功能介绍:  绑第三方账号
 */
@Service("sns_bang")
public class SnsBang implements IResultOut {

    private Logger LOG = Logger.getLogger(SnsBang.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            //绑定
            TbUserSns userSns = new TbUserSns();
            userSns.setUserId(userSnsBean.getUserId());
            userSns.setSnsType(userSnsBean.getSnsType());
            userSns.setUid(userSnsBean.getUid());
            userSns.setOpenkey(userSnsBean.getOpenkey());
            userSns.setSnsName(userSnsBean.getSnsName());
            userSns.setSnsAccount(userSnsBean.getSnsAccount());
            userSns.setSnsImg(userSnsBean.getSnsImg());
            userSns.setIsUseImg(userSnsBean.getIsUseImg());
            userSns.setCreatetime(new Timestamp(new Date().getTime()));
            userSns.setStatus(1);
            iAccountDao.saveObj(userSns);
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

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            Integer userId = userSnsBean.getUserId();
            String openId = userSnsBean.getUid();
            Integer snsType = userSnsBean.getSnsType();
            if (null == userId || userId == 0 || null == snsType || StringUtils.isEmpty(openId)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}