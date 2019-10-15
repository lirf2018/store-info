package com.yufan.task.service.impl.addr;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.addr.IAddrDao;
import org.apache.commons.lang3.StringUtils;
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
 * 创建时间:  2019/8/25 11:22
 * 功能介绍: 查询用户地址
 */
@Service("query_user_addr")
public class QueryUserAddr implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryUserAddr.class);

    @Autowired
    private IAddrDao iAddrDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            Integer addrType = data.containsKey("addr_type") ? data.getInteger("addr_type") : 1;
            String parentCode = data.containsKey("parent_code") ? data.getString("parent_code") : "";
            List<Map<String, Object>> list = iAddrDao.queryUserAddrListMap(userId, addrType);
            //处理运费
            Map<String, String> addrIdsMap = new HashMap<>();
            String regionCodes = "";
            for (int i = 0; i < list.size(); i++) {
                String addrIds = list.get(i).get("area_ids").toString();
                String addrIdsArray[] = addrIds.split("-");
                for (int j = 0; j < addrIdsArray.length; j++) {
                    if (addrIdsMap.get(addrIdsArray[j]) == null) {
                        regionCodes = regionCodes + addrIdsArray[j] + ",";
                    }
                    addrIdsMap.put(addrIdsArray[j], addrIdsArray[j]);
                }
            }

            List<Map<String, Object>> outList = new ArrayList<>();
            //运费
            Map<String, String> addrIdsFreightMap = new HashMap<>();
            //查询运费
            if (StringUtils.isNotEmpty(regionCodes)) {
                regionCodes = regionCodes.substring(0, regionCodes.length() - 1);
                regionCodes = regionCodes.replace(",", "','");
                List<Map<String, Object>> listAddrs = iAddrDao.queryAddrByRegionCode(regionCodes);
                for (int i = 0; i < listAddrs.size(); i++) {
                    String regionCode = listAddrs.get(i).get("region_code").toString();
                    BigDecimal freight = new BigDecimal(listAddrs.get(i).get("freight").toString());
                    if (freight.compareTo(new BigDecimal("0")) > 0) {
                        addrIdsFreightMap.put(regionCode, freight.toString());
                    }
                }
            }

            //用户默认收货地址运费
            BigDecimal userAddrDefaulFreight = new BigDecimal("0.00");

            //设置运费（以最小单位为优先）
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String addrIds = list.get(i).get("area_ids").toString();
                String addrIdsArray[] = addrIds.split("-");
                BigDecimal freight = new BigDecimal("0.00");
                //县
                String freight3 = addrIdsArray[2];
                //市
                String freight2 = addrIdsArray[1];
                //省
                String freight1 = addrIdsArray[0];
                if (addrIdsFreightMap.get(freight3) != null) {
                    freight = new BigDecimal(addrIdsFreightMap.get(freight3));
                } else if (addrIdsFreightMap.get(freight2) != null) {
                    freight = new BigDecimal(addrIdsFreightMap.get(freight2));
                } else if (addrIdsFreightMap.get(freight1) != null) {
                    freight = new BigDecimal(addrIdsFreightMap.get(freight1));
                } else {
                    //默认
                    LOG.info("-------默认------");
                }

                Integer isDefault = Integer.parseInt(list.get(i).get("is_default").toString());
                if (isDefault == 1) {
                    userAddrDefaulFreight = freight;
                }

                map.put("freight", freight.setScale(2, BigDecimal.ROUND_HALF_UP));
                outList.add(map);
            }

            List<Map<String, Object>> listAddr = new ArrayList<>();
            if (StringUtils.isNotEmpty(parentCode)) {
                listAddr = iAddrDao.queryCountryAddrList(parentCode);
            }


            //dataJson.put("user_addr_defaul_freight", userAddrDefaulFreight);//用户默认收货地址运费
            dataJson.put("country_addr_list", listAddr);//地区列表
            dataJson.put("user_addr_list", outList);//用户收货地址列表

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