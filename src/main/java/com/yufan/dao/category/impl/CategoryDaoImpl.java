package com.yufan.dao.category.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.dao.category.ICategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 16:47
 * 功能介绍:
 */
@Transactional
@Repository
public class CategoryDaoImpl implements ICategoryDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryCategoryRelListMap(int categoryId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_code,ca.category_name ");
        sql.append(" ,it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.is_sales as it_is_shell ");
        sql.append(" ,pv.value_id as pv_value_id,pv.value_name as pv_value_name,pv.`value` as pv_value ");
        sql.append(" from tb_category ca ");
        sql.append(" JOIN tb_itemprops it on it.category_id=ca.category_id and it.status=1 ");
        sql.append(" JOIN tb_props_value pv on pv.prop_id=it.prop_id and pv.`status`=1 ");
        sql.append(" where ca.category_id=").append(categoryId).append(" and ca.`status`=1 ");
        sql.append(" ORDER BY it.prop_id DESC,pv.data_index DESC,pv.value_id DESC ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
