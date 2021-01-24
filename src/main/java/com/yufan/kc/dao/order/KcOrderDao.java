package com.yufan.kc.dao.order;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/9 21:47
 * 功能介绍:
 */
public interface KcOrderDao {

    public PageInfo loadOrderListPage(ConditionCommon conditionCommon);

    List<Map<String, Object>> findOrderList(int orderId);

}
