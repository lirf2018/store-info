package com.yufan.itf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.itf.dao.IItfItemDao;
import com.yufan.itf.service.bean.ConditionBean;
import com.yufan.utils.PageInfo;
import com.yufan.utils.ResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/4
 */
@Service("find_itf_item_detail_report")
public class FindItfItemDetailReport implements IResultOut {

    private Logger LOG = Logger.getLogger(FindItfItemDetailPage.class);

    @Autowired
    private IItfItemDao iItfItemDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionBean condition = JSONObject.toJavaObject(data, ConditionBean.class);
            List<Map<String, Object>> list = iItfItemDao.loadItfItemDetailReport(condition);
            BigDecimal incomeAll = BigDecimal.ZERO; // 总收入
            BigDecimal outcomeAll = BigDecimal.ZERO;// 总支出
            BigDecimal lirunAll = BigDecimal.ZERO;// 总利润
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                int itemType = Integer.parseInt(map.get("item_type").toString());// '0支出 1收入'
                String itemPriceStr = map.get("item_price").toString();
                if (itemType == 0) {
                    outcomeAll = outcomeAll.add(new BigDecimal(itemPriceStr));
                    continue;
                }
                incomeAll = incomeAll.add(new BigDecimal(itemPriceStr));
            }
            //
            lirunAll = incomeAll.subtract(outcomeAll);
            //
            dataJson.put("incomeAll", incomeAll);
            dataJson.put("outcomeAll", outcomeAll);
            dataJson.put("lirunAll", lirunAll);

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