package com.yufan.kc.dao.store;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.kc.bean.TbStoreInout;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/29 21:39
 * 功能介绍:
 */
public interface StoreInOutDao {

    PageInfo loadStoreInOutPage(ConditionCommon conditionCommon);

    void saveStoreInOut(TbStoreInout storeInout);

    void deleteStoreInOut(int id);

    void updateStoreInOut(TbStoreInout storeInout);

    Map<String, Object> findStoreInOutInfo(ConditionCommon conditionCommon);

    List<Map<String, Object>> findStoreInOutList(ConditionCommon conditionCommon);

    void batchSureStore(String incomeIds, Integer incomeType);

    void updateStoreMatching(String goodsCode, int matching);
    void updateStoreMatching2(String goodsIds, int matching);

    Map<String, Object> findOneStoreByGoodsCode(String goodsCode);

    boolean checkShopNoOut(String shopNo);

    void deleteGoods(int inoutId);

    boolean checkGoodsExist(String goodsCode);

}
