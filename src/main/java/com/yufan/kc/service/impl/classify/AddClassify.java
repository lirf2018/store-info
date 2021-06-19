package com.yufan.kc.service.impl.classify;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbClassify;
import com.yufan.kc.dao.classify.ClassifyDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 21:15
 * 功能介绍:
 */
@Service("kc_add_classify")
public class AddClassify implements IResultOut {

    private Logger LOG = Logger.getLogger(AddClassify.class);

    @Autowired
    private ClassifyDao classifyDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            TbClassify calssify = JSONObject.toJavaObject(data, TbClassify.class);
            calssify.setStatus(new Integer(1).byteValue());
            if (calssify.getClassifyId() > 0) {
                classifyDao.updateClassify(calssify);
            } else {
                classifyDao.saveClassify(calssify);
                calssify.setCreateTime(new Timestamp(new Date().getTime()));
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


            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}