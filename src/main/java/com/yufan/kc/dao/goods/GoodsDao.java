package com.yufan.kc.dao.goods;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.kc.bean.TbKcGoods;
import com.yufan.utils.PageInfo;

import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/2 20:30
 * 功能介绍:
 */
public interface GoodsDao {

    PageInfo loadGoodsPage(ConditionCommon conditionCommon);

    void saveGoods(TbKcGoods goods);

    void deleteGoods(String ids);

    void updateGoods(TbKcGoods goods);

    void updateGoodsStatus(String goodsIds, int status);


    PageInfo loadStoreGoodsPage(ConditionCommon conditionCommon);

    boolean checkGoods(String goodsCode);

    Map<String,Object> findOpenOrderGoodsOne(ConditionCommon conditionCommon);

    TbKcGoods loadGoods(int goodsId);

    PageInfo loadGoodsListPage(ConditionCommon conditionCommon);

}
