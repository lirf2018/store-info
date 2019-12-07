package com.yufan.task.service.impl.account;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.dao.addr.IAddrDao;
import com.yufan.task.service.impl.addr.AddUserAddr;
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
 * 创建时间:  2019/12/2 21:26
 * 功能介绍:  微信登录（默认注册）
 */
@Service("wx_login")
public class WeiXinLogin implements IResultOut {

    private Logger LOG = Logger.getLogger(AddUserAddr.class);

    @Autowired
    private IAccountDao iAccountDao;


    @Override
    @Transactional
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String openId = data.getString("open_id");
            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfo(openId);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (0 == userInfoList.size()) {
                //默认创建账户并绑定微信
            } else {
                //已存在用户


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



            String openId = data.getString("open_id");

            if (StringUtils.isEmpty(openId)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---" , e);
        }
        return false;
    }
}