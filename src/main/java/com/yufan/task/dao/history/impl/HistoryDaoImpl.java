package com.yufan.task.dao.history.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.history.IHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 15:59
 * 功能介绍:
 */
@Transactional
@Repository
public class HistoryDaoImpl implements IHistoryDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryHistorySearchList(int size) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT type_word, COUNT(type_word) as type_word_count from tb_search_history ");
        sql.append(" GROUP BY type_word order by type_word_count desc LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryUserHistorySearchList(int userId, int size) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT type_word, COUNT(type_word) as type_word_count from tb_search_history  where status=1 and user_id=").append(userId).append(" ");
        sql.append(" GROUP BY type_word order by id desc LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void deleteUserHistorySearch(int userId) {
        String sql = " update tb_search_history set STATUS=0 where user_id=? and status=1 ";
        iGeneralDao.executeUpdateForSQL(sql, userId);
    }
}
