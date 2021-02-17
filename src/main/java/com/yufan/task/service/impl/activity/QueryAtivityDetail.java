package com.yufan.task.service.impl.activity;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbActivity;
import com.yufan.task.dao.activity.IActivityDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2021/2/11 13:36
 * 功能介绍:
 */
@Service("query_activity_detail")
public class QueryAtivityDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryAtivityDetail.class);

    @Autowired
    private IActivityDao iActivityDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer activityId = data.getInteger("activityId");
            TbActivity activity = iActivityDao.loadActivity(activityId);
            dataJson.put("activity", activity);
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
            Integer activityId = data.getInteger("activityId");

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}