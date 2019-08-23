package com.yufan.task.service;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResponeUtil;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.dao.account.IAccountJpaDao;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbInfAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 16:52
 * 功能介绍: 测试
 */
@Service("test")
public class Test implements IResultOut {

    @Autowired
    private IAccountJpaDao iAccountJpaDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        TbInfAccount account = iAccountJpaDao.getOne(1);
        if (null != account) {
            System.out.println("=========="+account.getSid());
        }
        return ResponeUtil.packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }
}
