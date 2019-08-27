package com.yufan.task.dao.param.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.param.IParamDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 13:23
 * 功能介绍:
 */
@Transactional
@Repository
public class ParamDaoImpl implements IParamDao {

    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public List<Map<String, Object>> queryParamListMap(Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  param_id,param_name,param_code,param_key,param_value,param_value1,param_value2,remark from tb_param where 1=1 ");
        if (null != status) {
            sql.append(" and `status`= ").append(status).append(" ");
        }

        sql.append(" ORDER BY param_code,param_key ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryParamListMap(String paramCode, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  param_id,param_name,param_code,param_key,param_value,param_value1,param_value2,remark from tb_param where 1=1 ");
        if (null != status) {
            sql.append(" and `status`= ").append(status).append(" ");
        }
        if (StringUtils.isNotEmpty(paramCode)) {
            sql.append(" and param_code='").append(paramCode).append("' ");
        }

        sql.append(" ORDER BY param_code,param_key ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryParamListMap(String paramCode, String paramKey, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  param_id,param_name,param_code,param_key,param_value,param_value1,param_value2,remark from tb_param where 1=1 ");
        if (null != status) {
            sql.append(" and `status`= ").append(status).append(" ");
        }
        if (StringUtils.isNotEmpty(paramCode)) {
            sql.append(" and param_code='").append(paramCode).append("' ");
        }
        if (StringUtils.isNotEmpty(paramKey)) {
            sql.append(" and param_key='").append(paramKey).append("' ");
        }

        sql.append(" ORDER BY param_code,param_key ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
