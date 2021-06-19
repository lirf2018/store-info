package com.yufan.kc.service.impl.report;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.report.ReportDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/26 22:41
 * 功能介绍:
 */
@Service("goods_report_table")
public class GoodsReportTable implements IResultOut {

    private Logger LOG = Logger.getLogger(GoodsReportTable.class);

    @Autowired
    private ReportDao reportDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data, ConditionCommon.class);
            PageInfo page = reportDao.loadGoodsReportPage(conditionCommon);
            dataJson.put("goods_list", page.getResultListMap());
            dataJson.put("has_next", page.isHasNext());
            dataJson.put("total_num", page.getRecordSum());
            // 数量
            List<Map<String, Object>> countList = reportDao.loadGoodsReportCount(conditionCommon);
            Map<String, BigDecimal> countMap = new HashMap<>();
            countMap.put("sale_price_all", BigDecimal.ZERO);
            countMap.put("income_price_all",BigDecimal.ZERO);
            countMap.put("get_price_all", BigDecimal.ZERO);
            if (CollectionUtils.isNotEmpty(countList)) {
                BigDecimal salePriceAll = new BigDecimal(countList.get(0).get("sale_price_all").toString());
                BigDecimal incomePriceAll = new BigDecimal(countList.get(0).get("income_price_all").toString());
                BigDecimal getPriceAll = salePriceAll.subtract(incomePriceAll);
                countMap.put("sale_price_all", salePriceAll);
                countMap.put("income_price_all", incomePriceAll);
                countMap.put("get_price_all", getPriceAll);
            }
            dataJson.put("countMap", countMap);
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