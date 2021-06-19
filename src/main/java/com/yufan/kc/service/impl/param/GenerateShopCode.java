package com.yufan.kc.service.impl.param;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbParam;
import com.yufan.kc.bean.TbSequence;
import com.yufan.kc.dao.param.ParamDao;
import com.yufan.utils.DatetimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/30 22:20
 * 功能介绍:
 */
@Service("generate_shop_code")
public class GenerateShopCode implements IResultOut {

    private Logger LOG = Logger.getLogger(GenerateShopCode.class);

    @Autowired
    private ParamDao paramDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {


            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    public synchronized String generate(int sequenceType, int count) {
        String str = "";
        TbSequence sequence = paramDao.generateValue(sequenceType);
        paramDao.updateGenerateValue(sequenceType, count);
        if (null != sequence) {
            if (sequence.getSequenceValue() > 9998) {
                // 重置
                paramDao.resetGenerateValue(sequenceType);
            }
            // 自动生成 18+3
            str = "AUTO" + DatetimeUtil.getNow("yyyyMMddHHmmss-") + sequence.getSequenceValue();
        }
        return str;
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