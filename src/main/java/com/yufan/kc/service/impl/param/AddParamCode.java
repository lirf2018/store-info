package com.yufan.kc.service.impl.param;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbParam;
import com.yufan.kc.dao.param.ParamDao;
import com.yufan.kc.service.impl.supplier.AddSupplier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 22:00
 * 功能介绍:
 */
@Service("kc_add_param")
public class AddParamCode implements IResultOut {

    private Logger LOG = Logger.getLogger(AddParamCode.class);

    @Autowired
    private ParamDao paramDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            TbParam param = JSONObject.toJavaObject(data, TbParam.class);
            param.setStatus(1);
            if (param.getParamId() > 0) {
                paramDao.updateParam(param);
            } else {
                param.setCreatetime(new Timestamp(new Date().getTime()));
                paramDao.saveParam(param);
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