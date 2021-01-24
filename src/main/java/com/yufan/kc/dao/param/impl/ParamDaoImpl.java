package com.yufan.kc.dao.param.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbParam;
import com.yufan.kc.bean.TbSequence;
import com.yufan.kc.dao.param.ParamDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 14:34
 * 功能介绍:
 */
@Transactional
@Repository("iParamDao")
public class ParamDaoImpl implements ParamDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadParamPage(ConditionCommon conditionCommon) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(getSql(conditionCommon));
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    private String getSql(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select p.param_id,p.param_name,p.param_code,p.param_key,p.param_value,p.param_value1,p.param_value2,p.createman, ");
        sql.append(" p.createtime,p.status,p.remark,p.is_make_sure,p.data_index from tb_param p where status=1 ");
        if (!StringUtil.isNullOrEmpty(conditionCommon.getParamName())) {
            sql.append(" and p.param_name like '%").append(conditionCommon.getParamName().trim()).append("%' ");
        }
        if (!StringUtil.isNullOrEmpty(conditionCommon.getParamCode())) {
            sql.append(" and p.param_code ='").append(conditionCommon.getParamCode().trim()).append("' ");
        }
        if (!StringUtil.isNullOrEmpty(conditionCommon.getParamKey())) {
            sql.append(" and p.param_key ='").append(conditionCommon.getParamKey().trim()).append("' ");
        }
        sql.append(" order by p.param_code, p.data_index desc ");
        return sql.toString();
    }

    @Override
    public void saveParam(TbParam param) {
        iGeneralDao.save(param);
    }

    @Override
    public void deleteParam(int id) {
        String sql = " update tb_param set status=0 where param_id = ? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public void updateParam(TbParam param) {
        iGeneralDao.saveOrUpdate(param);
    }

    @Override
    public List<Map<String, Object>> listParamCode(ConditionCommon conditionCommon) {
        return iGeneralDao.getBySQLListMap(getSql(conditionCommon));
    }

    @Override
    public void updateGenerateValue(int sequenceType, int count) {
        String sql = "update tb_sequence set sequence_value=sequence_value+" + count + " where sequence_type=?";
        iGeneralDao.executeUpdateForSQL(sql, sequenceType);
    }

    @Override
    public TbSequence generateValue(int sequenceType) {
        String hql = " from TbSequence where sequenceType=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, sequenceType);
    }

    @Override
    public void resetGenerateValue(int sequenceType) {
        String sql = "update tb_sequence set sequence_value=1000 where sequence_type=?";
        iGeneralDao.executeUpdateForSQL(sql, sequenceType);
    }
}
