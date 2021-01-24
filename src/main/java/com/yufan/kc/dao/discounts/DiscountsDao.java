package com.yufan.kc.dao.discounts;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.kc.bean.TbDiscountsConfig;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 12:15
 * 功能介绍:
 */
public interface DiscountsDao {

    public PageInfo loadDiscountsConfigPage(ConditionCommon conditionCommon);

    public void saveDiscountsConfig(TbDiscountsConfig discountsConfig);

    public void deleteDiscountsConfig(int id);

    public void updateDiscountsConfig(TbDiscountsConfig discountsConfig);

}
