package com.yufan.kc.service.impl.param;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.param.ParamDao;
import com.yufan.kc.dao.supplier.SupplierDao;
import com.yufan.kc.service.impl.supplier.FindSupplierPage;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 22:00
 * 功能介绍:
 */
@Service("kc_page_param")
public class FindParamCodePage implements IResultOut {

    private Logger LOG = Logger.getLogger(FindParamCodePage.class);

    @Autowired
    private ParamDao iParamDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data,ConditionCommon.class);
            PageInfo page = iParamDao.loadParamPage(conditionCommon);
            dataJson.put("param_list",page.getResultListMap());
            dataJson.put("has_next",page.isHasNext());
            dataJson.put("total_num",page.getRecordSum());
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
