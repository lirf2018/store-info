package com.yufan.itf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.itf.dao.IItfAdminDao;
import com.yufan.pojo.ItfTbItemAdmin;
import com.yufan.utils.MD5;
import com.yufan.utils.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
@Service("update_itf_admin_passwd")
public class UpdateItfPasswd implements IResultOut {

    private Logger LOG = Logger.getLogger(UpdateItfPasswd.class);

    @Autowired
    private IItfAdminDao iItfAdminDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            String oldPasswd = data.getString("oldPasswd").trim();
            String newPasswd = data.getString("newPasswd").trim();

            // 查询用户
            ItfTbItemAdmin admin = iItfAdminDao.loadItfTbItemAdminById(userId);
            if (null == admin) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }
            String phone = admin.getAdminName();
            String md5Old = MD5.enCodeStandard(oldPasswd + phone);
            if (!md5Old.equals(admin.getPasswd())) {
                LOG.info("--------新旧密码不一致-----");
                return packagMsg(ResultCode.OLD_PASSWD_ERROR.getResp_code(), dataJson);
            }

            String md5 = MD5.enCodeStandard(newPasswd + phone);
            admin.setPasswd(md5);
            iItfAdminDao.updateItfTbItemAdminByName(admin);
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

            Integer userId = data.getInteger("userId");
            String oldPasswd = data.getString("oldPasswd").trim();
            String newPasswd = data.getString("newPasswd").trim();
            if (null == userId || StringUtils.isEmpty(oldPasswd) || StringUtils.isEmpty(newPasswd)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(MD5.enCodeStandard("123456admin"));
    }
}