package com.yufan.kc.service.impl.supplier;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.supplier.SupplierDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 22:43
 * 功能介绍:
 */
@Service("kc_delete_supplier")
public class DeleteSupplier implements IResultOut {

    private Logger LOG = Logger.getLogger(DeleteSupplier.class);

    @Autowired
    private SupplierDao supplierDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            int supplierId = data.getInteger("supplierId");
            supplierDao.deleteSupplier(supplierId);
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
            Integer supplierId = data.getInteger("supplierId");
            if (null == supplierId || supplierId < 1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}