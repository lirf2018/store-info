package com.yufan.task.service.main;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResponeUtil;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 17:44
 * 功能介绍: main页面
 */
@Service("main")
public class QueryMain implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryMain.class);

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {

            //活动

            //抢购

            //推荐 6个

            //新品 6个

            //活动

            return ResponeUtil.packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("---------error", e);
        }
        return ResponeUtil.packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }
}