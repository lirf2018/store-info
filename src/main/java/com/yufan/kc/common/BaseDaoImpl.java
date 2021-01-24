package com.yufan.kc.common;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 13:56
 * 功能介绍:
 */
public class BaseDaoImpl {

    PageInfo pageInfo = new PageInfo();

    public BaseDaoImpl(ConditionCommon conditionCommon,StringBuffer sql){
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
    }

}
