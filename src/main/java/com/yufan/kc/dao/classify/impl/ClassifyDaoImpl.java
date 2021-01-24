package com.yufan.kc.dao.classify.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbClassify;
import com.yufan.kc.dao.classify.ClassifyDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 12:07
 * 功能介绍:
 */
@Transactional
@Repository
public class ClassifyDaoImpl implements ClassifyDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadClassifyPage(ConditionCommon conditionCommon) {

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(getSql(conditionCommon));
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    private String getSql(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ca.classify_id,ca.classify_name,ca.classify_code,ca.classify_sort,ca.create_time,ca.create_user_name,ca.remark from tb_classify ca where status=1 ");
        if (null != conditionCommon) {
            if (StringUtils.isNotEmpty(conditionCommon.getClassifyName())) {
                sql.append(" and ca.classify_name like '%").append(conditionCommon.getClassifyName().trim()).append("%' ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getClassifyCode())) {
                sql.append(" and ca.classify_code = '").append(conditionCommon.getClassifyCode().trim()).append("'");
            }
        }
        sql.append(" order by ca.classify_sort desc");
        return sql.toString();

    }

    @Override
    public void saveClassify(TbClassify classify) {
        iGeneralDao.save(classify);
    }

    @Override
    public void deleteClassify(int id) {
        String sql = " update tb_classify set status=0 where classify_id = ? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public void updateClassify(TbClassify classify) {
        iGeneralDao.saveOrUpdate(classify);
    }

    @Override
    public List<Map<String, Object>> listClassify(ConditionCommon conditionCommon) {
        return iGeneralDao.getBySQLListMap(getSql(conditionCommon));
    }
}
