package com.yufan.kc.dao.classify;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.kc.bean.TbClassify;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 12:13
 * 功能介绍:
 */
public interface ClassifyDao {

    public PageInfo loadClassifyPage(ConditionCommon conditionCommon);

    public void saveClassify(TbClassify classify);

    public void deleteClassify(int id);

    public void updateClassify(TbClassify classify);

    public List<Map<String,Object>> listClassify(ConditionCommon conditionCommon);

}
