package com.yufan.task.dao.secondgoods;

import com.yufan.bean.GoodsCondition;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/31 20:31
 * 功能介绍:
 */
public interface ISecondGoodsDao {

    /**
     * 更新浏览数
     *
     * @param goodsId
     */
    public void UpdateSecondGoodsReadCount(int goodsId);


    /**
     * 查询商品列表
     *
     * @param
     * @return
     */
    public PageInfo loadGoodsList(GoodsCondition condition);
}
