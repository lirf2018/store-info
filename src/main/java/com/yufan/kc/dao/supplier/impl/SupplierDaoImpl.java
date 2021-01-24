package com.yufan.kc.dao.supplier.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbSupplier;
import com.yufan.kc.dao.supplier.SupplierDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 14:18
 * 功能介绍:
 */
@Transactional
@Repository
public class SupplierDaoImpl implements SupplierDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadSupplierPage(ConditionCommon conditionCommon) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(getSql(conditionCommon));
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    private String getSql(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select su.supplier_id,su.supplier_name,su.supplier_phone,su.supplier_addr,su.create_time,su.create_user_name,su.remark,su.supplier_code from tb_supplier su where status=1 ");
        if (null != conditionCommon) {
            if (StringUtils.isNotEmpty(conditionCommon.getSupplierName())) {
                sql.append(" and su.supplier_name like '%").append(conditionCommon.getSupplierName().trim()).append("%' ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getSupplierCode())) {
                sql.append(" and su.supplier_code = '").append(conditionCommon.getSupplierCode().trim()).append("' ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getSupplierAddr())) {
                sql.append(" and su.supplier_addr like '%").append(conditionCommon.getSupplierAddr().trim()).append("%' ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getSupplierPhone())) {
                sql.append(" and su.supplier_phone = '").append(conditionCommon.getSupplierPhone().trim()).append("' ");
            }
        }

        sql.append(" order by su.supplier_id desc ");
        return sql.toString();
    }

    @Override
    public void saveSupplier(TbSupplier supplier) {
        iGeneralDao.save(supplier);
    }

    @Override
    public void deleteSupplier(int id) {
        String sql = " update tb_supplier set status=0 where supplier_id = ? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public void updateSupplier(TbSupplier supplier) {
        iGeneralDao.saveOrUpdate(supplier);
    }

    @Override
    public List<Map<String, Object>> listSupplier(ConditionCommon conditionCommon) {
        return iGeneralDao.getBySQLListMap(getSql(conditionCommon));
    }
}
