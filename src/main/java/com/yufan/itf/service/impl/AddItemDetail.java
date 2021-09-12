package com.yufan.itf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.itf.dao.IItfItemDao;
import com.yufan.pojo.ItfTbItemDetail;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.ResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/4
 */
@Service("add_item_detail")
public class AddItemDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(AddItemDetail.class);

    @Autowired
    private IItfItemDao iItfItemDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ItfTbItemDetail itfTbItemDetail = JSONObject.toJavaObject(data, ItfTbItemDetail.class);
            itfTbItemDetail.setCreatetime(new Timestamp(System.currentTimeMillis()));
            itfTbItemDetail.setLastaltertime(new Timestamp(System.currentTimeMillis()));
            String itemDates = data.getString("itemDates");//yyyy-MM-dd
            String[] arrayDate = itemDates.split("-");
            int itemYear = Integer.parseInt(arrayDate[0]);
            int itemMonth = Integer.parseInt(arrayDate[1]);
            int itemDate = Integer.parseInt(arrayDate[0] + arrayDate[1] + arrayDate[2]);
            int itemSeason = HelpCommon.getSaleSeason(arrayDate[1]);
            //
            itfTbItemDetail.setItemYear(itemYear);
            itfTbItemDetail.setItemMonth(itemMonth);
            itfTbItemDetail.setItemDate(itemDate);
            itfTbItemDetail.setItemSeason(itemSeason);

            iItfItemDao.saveItfTbItemDetail(itfTbItemDetail);
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