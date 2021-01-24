package com.yufan.kc.dao.supplier;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.kc.bean.TbSupplier;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 14:18
 * 功能介绍:
 */
public interface SupplierDao {

    public PageInfo loadSupplierPage(ConditionCommon conditionCommon);

    public void saveSupplier(TbSupplier supplier);

    public void deleteSupplier(int id);

    public void updateSupplier(TbSupplier supplier);

    public List<Map<String,Object>> listSupplier( ConditionCommon conditionCommon);
    
}
