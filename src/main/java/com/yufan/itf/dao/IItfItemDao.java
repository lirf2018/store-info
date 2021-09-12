package com.yufan.itf.dao;

import com.yufan.itf.service.bean.ConditionBean;
import com.yufan.pojo.ItfTbItem;
import com.yufan.pojo.ItfTbItemDetail;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
public interface IItfItemDao {

    ItfTbItem findItfItemInfo(ItfTbItem itfTbItem);

    void saveItfItem(ItfTbItem itfTbItem);

    void updateItfItem(ItfTbItem itfTbItem);

    void delItfItem(ItfTbItem itfTbItem);

    PageInfo loadItfItemPage(ConditionBean conditionBean);

    boolean checkItfDetailExists(int itemId);

    void saveItfTbItemDetail(ItfTbItemDetail itfTbItemDetail);

    void delItfTbItemDetailById(int itemDetailId);

    PageInfo loadItfItemDetailPage(ConditionBean condition);

    List<Map<String, Object>> loadItfItemDetailReport(ConditionBean condition);

    boolean checkItemName(Integer itemId, String itemName);
}
