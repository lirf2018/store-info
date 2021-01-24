package com.yufan.kc.dao.report;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/26 22:42
 * 功能介绍:
 */
public interface ReportDao {


    /**
     * 查询商品报表
     * @param conditionCommon
     * @return
     */
    public PageInfo loadGoodsReportPage(ConditionCommon conditionCommon);


    /**
     * 查询商品报表统计
     * @param conditionCommon
     * @return
     */
    List<Map<String,Object>> loadGoodsReportCount(ConditionCommon conditionCommon);



    /**
     * 查询订单表
     * @param conditionCommon
     * @return
     */
    List<Map<String,Object>> loadOrderReportPage(ConditionCommon conditionCommon);

}
