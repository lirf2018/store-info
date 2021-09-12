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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
@Service("find_itf_item_detail_page")
public class FindItfItemDetailPage implements IResultOut {

    private Logger LOG = Logger.getLogger(FindItfItemDetailPage.class);

    @Autowired
    private IItfItemDao iItfItemDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionBean condition = JSONObject.toJavaObject(data, ConditionBean.class);
            PageInfo page = iItfItemDao.loadItfItemDetailPage(condition);
            dataJson.put("hasNext", page.isHasNext());
            dataJson.put("currePage", page.getCurrePage());
            dataJson.put("pageSize", page.getPageSize());
            dataJson.put("list", initList(page.getResultListMap()));
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private List<Map<String, Object>> initList(List<Map<String, Object>> list) {
        List<Map<String, Object>> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
//            String itemYear = String.valueOf(map.get("item_year"));
//            String itemMonth = String.valueOf(map.get("item_month"));
            String itemDate = String.valueOf(map.get("item_date"));
            map.put("item_date_format", itemDate.substring(0, 4) + "-" + itemDate.substring(4, 6) + "-" + itemDate.substring(6, 8));

            newList.add(map);
        }
        return newList;
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