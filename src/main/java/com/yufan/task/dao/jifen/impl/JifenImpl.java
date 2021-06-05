package com.yufan.task.dao.jifen.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.jifen.IJifen;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/2
 */
@Repository
@Transactional
public class JifenImpl implements IJifen {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> findJifenListMap(int userId, String taskIds, Integer inOut) {
        StringBuffer sql = jifenListSql(userId, taskIds, inOut);
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    private StringBuffer jifenListSql(int userId, String taskIds, Integer inOut) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select j.task_id,j.is_inout,j.jifen,j.items,DATE_FORMAT(j.createtime,'%Y-%m-%d') as createtime  ");
        sql.append(" ,p.param_value as task_name ");
        sql.append(" from tb_jifen j  LEFT JOIN tb_param p on p.param_code='jifen_task_id' and p.param_key=j.task_id ");
        sql.append(" where j.status=1 and j.user_id=").append(userId).append(" and j.pass_time>=NOW()  ");
        if (StringUtils.isNotEmpty(taskIds)) {
            sql.append(" and j.task_id in (").append(taskIds).append(") ");
        }
        if (null != inOut) {
            sql.append(" and j.is_inout = ").append(inOut).append(" ");
        }
        sql.append(" ORDER BY j.createtime desc ");
        return sql;
    }

    @Override
    public PageInfo loadJifenListPage(int userId, String taskIds, Integer inOut, Integer pageSize, int currePage) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize == null ? 20 : pageSize);
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(jifenListSql(userId, taskIds, inOut).toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }
}
