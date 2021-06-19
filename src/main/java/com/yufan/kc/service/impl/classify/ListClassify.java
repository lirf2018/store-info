package com.yufan.kc.service.impl.classify;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.classify.ClassifyDao;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/30 21:19
 * 功能介绍:
 */
@Service("kc_find_classify_list")
public class ListClassify implements IResultOut {

    private Logger LOG = Logger.getLogger(ListClassify.class);

    @Autowired
    private ClassifyDao classifyDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data,ConditionCommon.class);
            dataJson.put("classify_list",classifyDao.listClassify(conditionCommon));
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