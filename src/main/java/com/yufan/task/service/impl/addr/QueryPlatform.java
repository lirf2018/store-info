package com.yufan.task.service.impl.addr;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.addr.IAddrDao;
import com.yufan.task.service.impl.Test;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 9:29
 * 功能介绍: 查询平台地址
 */
@Service("query_Platform")
public class QueryPlatform implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryPlatform.class);

    @Autowired
    private IAddrDao iAddrDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer addrType = data.getInteger("addr_type");
            List<Map<String, Object>> list = iAddrDao.queryPlatformListMap(addrType);
            //pa.id,pa.detail_addr,pa.freight,pa.sort_char,pa.addr_type,pa.addr_name,pa.addr_lng,pa.addr_lat
            List<String> charWord = new ArrayList<>();//地点字母
            Map<String, String> mark = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                String sortChar = list.get(i).get("sort_char").toString();
                if (null == mark.get(sortChar)) {
                    charWord.add(sortChar);
                    mark.put(sortChar, sortChar);
                }
            }

            List<Map<String, Object>> outList = new ArrayList<>();
            for (int i = 0; i < charWord.size(); i++) {
                Map<String, Object> outMap = new HashMap<>();
                outMap.put("sort_char", charWord.get(i));

                List<Map<String, Object>> outAddrList = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    if (!charWord.get(i).equals(list.get(j).get("sort_char").toString())) {
                        continue;
                    }
                    Map<String, Object> detailMap = new HashMap<>();
                    int id = Integer.parseInt(list.get(j).get("id").toString());
                    String detailAddr = list.get(j).get("detail_addr").toString();
                    String sortChar = list.get(j).get("sort_char").toString();
                    String addrType_ = list.get(j).get("addr_type").toString();
                    String addrName = list.get(j).get("addr_name").toString();
                    String addrLng = list.get(j).get("addr_lng").toString();
                    String addrLat = list.get(j).get("addr_lat").toString();
                    BigDecimal freight = new BigDecimal(list.get(j).get("freight").toString());
                    detailMap.put("id", id);
                    detailMap.put("detail_addr", detailAddr);
                    detailMap.put("sort_char", sortChar);
                    detailMap.put("addr_type", addrType_);
                    detailMap.put("addr_name", addrName);
                    detailMap.put("addr_lng", addrLng);
                    detailMap.put("addr_lat", addrLat);
                    detailMap.put("freight", freight);
                    outAddrList.add(detailMap);
                }
                outMap.put("addr_list", outAddrList);
                outList.add(outMap);
            }
            dataJson.put("platform_addr_list",outList);
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
