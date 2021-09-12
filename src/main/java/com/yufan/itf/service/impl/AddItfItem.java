package com.yufan.itf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.itf.dao.IItfItemDao;
import com.yufan.pojo.ItfTbItem;
import com.yufan.task.service.impl.Test;
import com.yufan.utils.ResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
@Service("add_itf_item")
public class AddItfItem implements IResultOut {

    private Logger LOG = Logger.getLogger(AddItfItem.class);

    @Autowired
    private IItfItemDao iItfItemDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ItfTbItem itfTbItem = JSONObject.toJavaObject(data, ItfTbItem.class);
            itfTbItem.setCreatetime(new Timestamp(System.currentTimeMillis()));
            itfTbItem.setLastaltertime(new Timestamp(System.currentTimeMillis()));
            boolean flag = iItfItemDao.checkItemName(itfTbItem.getItemId(),itfTbItem.getItemName());
            if(flag){
                return packagMsg(ResultCode.NAME_EXIST.getResp_code(), dataJson);
            }
            if (itfTbItem.getItemId() == 0) {
                iItfItemDao.saveItfItem(itfTbItem);
            } else {
                iItfItemDao.updateItfItem(itfTbItem);
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

            Integer userId = data.getInteger("userId");
            if (null == userId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}