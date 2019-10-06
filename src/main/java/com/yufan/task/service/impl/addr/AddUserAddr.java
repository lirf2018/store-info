package com.yufan.task.service.impl.addr;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserAddr;
import com.yufan.task.dao.addr.IAddrDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/6 16:32
 * 功能介绍: 增加用户收货地址
 */
@Service("add_user_addr")
public class AddUserAddr implements IResultOut {

    private Logger LOG = Logger.getLogger(AddUserAddr.class);

    @Autowired
    private IAddrDao iAddrDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer id = data.getInteger("id");
            Integer userId = data.getInteger("user_id");
            String areaName = data.getString("area_Ids");
            Integer addrType = data.getInteger("addr_type");
            String addrName = data.getString("addr_name");
            String areaIds = data.getString("area_Ids");
            String userPhone = data.getString("user_phone");
            String userName = data.getString("user_name");
            String addrDetail = data.getString("addr_detail");

            TbUserAddr userAddr = new TbUserAddr();
            userAddr.setAreaIds(areaIds);
            userAddr.setAreaName(areaName);
            userAddr.setUserId(userId);
            userAddr.setUserName(userName);
            userAddr.setUserPhone(userPhone);
            userAddr.setAddrDetail(addrDetail);
            userAddr.setIsDefault(0);
            userAddr.setStatus(1);
            userAddr.setCreatetime(new Timestamp(new Date().getTime()));
            userAddr.setAddrName(addrName);
            userAddr.setAddrType(addrType);
            if (null != id && id > 0) {
                userAddr.setId(id);
                iAddrDao.updateUserAddr(userAddr);
            } else {
                iAddrDao.saveUserAddr(userAddr);
            }
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
            Integer userId = data.getInteger("user_id");
            String areaIds = data.getString("area_Ids");
            String userPhone = data.getString("user_phone");
            String userName = data.getString("user_name");
            String addrDetail = data.getString("addr_detail");

            if (null == userId || StringUtils.isEmpty(areaIds) || StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(addrDetail)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}