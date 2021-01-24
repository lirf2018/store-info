package com.yufan.kc.dao.order;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.bean.TbKcOrderDetail;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/9 23:20
 * 功能介绍:
 */
public interface OpenOrderDao {


    List<Map<String, Object>> findOrderDetailList(ConditionCommon conditionCommon);

    TbKcOrder loadOrder(String orderNo);

    TbKcOrder loadOrder(int orderId);

    List<TbKcOrderDetail> loadOrderDetailList(int orderId);

    int saveObj(Object obj);

    void updateObj(Object obj);

    void deleteDetailById(int detailId);

    boolean updateOutTimeDetail(Integer orderId, String goodsCode);

    List<Map<String, Object>> findOrderList(int orderId);

    void updateKcOrder(TbKcOrder order);

}
