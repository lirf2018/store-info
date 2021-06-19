package com.yufan.kc.service.impl.store;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.classify.ClassifyDao;
import com.yufan.kc.dao.param.ParamDao;
import com.yufan.kc.dao.store.StoreInOutDao;
import com.yufan.kc.dao.supplier.SupplierDao;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/29 21:25
 * 功能介绍:
 */
@Service("kc_list_store")
public class FindStoreInOutList implements IResultOut {

    private Logger LOG = Logger.getLogger(FindStoreInOutList.class);

    @Autowired
    private StoreInOutDao storeInOutDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data, ConditionCommon.class);
            if (StringUtils.isNotEmpty(conditionCommon.getShopCodeDate())) {
                Date shopCodeDate = DatetimeUtil.convertStrToDate(conditionCommon.getShopCodeDate());
                conditionCommon.setShopCodeDate(DatetimeUtil.convertDateToStr(shopCodeDate, "yyyyMMdd"));
            }
            List<Map<String, Object>> list = storeInOutDao.findStoreInOutList(conditionCommon);
            dataJson.put("store_list", list);
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
