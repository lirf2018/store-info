package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.account.IAccountDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/15 19:22
 * 功能介绍:  解绑第三方
 */
@Service("cancel_sns_bang")
public class CancelSnsBang implements IResultOut {

    private Logger LOG = Logger.getLogger(CancelSnsBang.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            int snsType = data.getInteger("sns_type");
            int userId = data.getInteger("user_id");
            iAccountDao.deleteSnsBang(userId, snsType);
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
            Integer snsType = data.getInteger("sns_type");
            Integer userId = data.getInteger("user_id");

            if (null == snsType || snsType == 0 || userId == null || userId == 0) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}