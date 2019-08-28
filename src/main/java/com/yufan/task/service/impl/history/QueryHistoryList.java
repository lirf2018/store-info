package com.yufan.task.service.impl.history;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.history.IHistoryDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 16:09
 * 功能介绍:
 */
@Service("query_history_list")
public class QueryHistoryList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryHistoryList.class);

    @Autowired
    private IHistoryDao iHistoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");

            List<Map<String, Object>> historyList = iHistoryDao.queryHistorySearchList(9);
            List<Map<String, Object>> userHistoryList = new ArrayList<>();
            if (userId != null && userId > 0) {
                userHistoryList = iHistoryDao.queryHistorySearchList(9);
            }
            dataJson.put("history_list", historyList);
            dataJson.put("user_history_list", userHistoryList);
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


            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
