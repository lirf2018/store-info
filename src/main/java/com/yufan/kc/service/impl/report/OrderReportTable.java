package com.yufan.kc.service.impl.report;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.report.ReportDao;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/26 23:39
 * 功能介绍:
 */
@Service("order_report_table")
public class OrderReportTable implements IResultOut {

    private Logger LOG = Logger.getLogger(OrderReportTable.class);

    @Autowired
    private ReportDao reportDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data, ConditionCommon.class);
            // 默认查询 当前年
            if (StringUtils.isEmpty(conditionCommon.getReportYear())) {
                String year = DatetimeUtil.getNow("yyyy");
                conditionCommon.setReportYear(year);
            }

            List<Map<String, Object>> list = reportDao.loadOrderReportPage(conditionCommon);
            // 数量
            Map<String, BigDecimal> countMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(list)) {
                BigDecimal salePriceAll = BigDecimal.ZERO;
                BigDecimal incomePriceAll = BigDecimal.ZERO;
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String saleMonth = map.get("sale_month").toString();
                    map.put("year_sale_month", conditionCommon.getReportYear() + "-" + saleMonth);
                    if (saleMonth.startsWith("0")) {
                        map.put("sale_month", saleMonth.replace("0", ""));
                    }
                    BigDecimal salePrice = new BigDecimal(map.get("order_price_all").toString());
                    BigDecimal incomePrice = new BigDecimal(map.get("goods_inprice_all").toString());
                    salePriceAll = salePriceAll.add(salePrice);
                    incomePriceAll = incomePriceAll.add(incomePrice);
                }
                BigDecimal getPriceAll = salePriceAll.subtract(incomePriceAll);
                countMap.put("order_price_all", salePriceAll);
                countMap.put("goods_inprice_all", incomePriceAll);
                countMap.put("get_price_all", getPriceAll);
            }
            dataJson.put("list", list);
            dataJson.put("count", countMap);
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
