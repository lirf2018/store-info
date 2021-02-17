package com.yufan.task.service.impl.addr;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserAddr;
import com.yufan.task.dao.addr.IAddrDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2021/2/16 2:02
 * 功能介绍:
 */
@Service("query_globle_addr_detail")
public class QueryGlobleAddrDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryGlobleAddrDetail.class);

    @Autowired
    private IAddrDao iAddrDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            Integer id = data.getInteger("id");
            TbUserAddr userAddr = iAddrDao.loadUserAddrById(id);
            if (null == userAddr) {
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            String addrAreaIds = userAddr.getAreaIds();
            String[] addrAreaIdsArray = addrAreaIds.split("-");
            // 查询地址
            String searchcomdition = addrAreaIds.replace("-", ",");
            Map<String, Object> mapAddr = new HashMap<>();
            List<Map<String, Object>> addrList = iAddrDao.queryGlobelAddrByIds(searchcomdition);
            for (int i = 0; i < addrList.size(); i++) {
                Map<String, Object> map = addrList.get(i);
                String regionId = map.get("region_id").toString();
                String regionName = map.get("region_name").toString();
                mapAddr.put(regionId, regionName);
            }

            dataJson.put("province", mapAddr.get(addrAreaIdsArray[0]));
            dataJson.put("city", mapAddr.get(addrAreaIdsArray[1]));
            dataJson.put("county", mapAddr.get(addrAreaIdsArray[2]));
            dataJson.put("town", mapAddr.get(addrAreaIdsArray[3]));
            dataJson.put("user_name", userAddr.getUserName());
            dataJson.put("phone", userAddr.getUserPhone());
            dataJson.put("is_default", userAddr.getIsDefault() == 1 ? true : false);
            dataJson.put("addr_detail", userAddr.getAddrDetail());
            dataJson.put("area_ids", userAddr.getAreaIds());
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
            Integer userId = data.getInteger("user_id");
            Integer id = data.getInteger("id");
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}