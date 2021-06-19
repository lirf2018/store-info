package com.yufan.kc.service.impl.store;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbStoreInout;
import com.yufan.kc.dao.store.StoreInOutDao;
import com.yufan.kc.service.impl.param.GenerateShopCode;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/31 21:45
 * 功能介绍:
 */
@Service("batch_sure_store")
public class BatchSureStore implements IResultOut {

    private Logger LOG = Logger.getLogger(BatchSureStore.class);

    @Autowired
    private StoreInOutDao storeInOutDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String incomeIds = data.getString("incomeIds");
            Integer incomeType = data.getInteger("incomeType");
            storeInOutDao.batchSureStore(incomeIds,incomeType);
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

            String incomeIds = data.getString("incomeIds");
            Integer incomeType = data.getInteger("incomeType");
            if (StringUtils.isEmpty(incomeIds) || incomeType == null || !(incomeType == 1 || incomeType == 0)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}