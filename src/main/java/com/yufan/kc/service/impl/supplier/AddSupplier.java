package com.yufan.kc.service.impl.supplier;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbSupplier;
import com.yufan.kc.dao.supplier.SupplierDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 22:43
 * 功能介绍:
 */
@Service("kc_add_supplier")
public class AddSupplier implements IResultOut {

    private Logger LOG = Logger.getLogger(AddSupplier.class);

    @Autowired
    private SupplierDao supplierDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            TbSupplier supplier = JSONObject.toJavaObject(data, TbSupplier.class);
            supplier.setStatus(new Integer(1).byteValue());
            if (supplier.getSupplierId() > 0) {
                supplierDao.updateSupplier(supplier);
            } else {
                supplier.setCreateTime(new Timestamp(new Date().getTime()));
                supplierDao.saveSupplier(supplier);
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