package com.yufan.task.dao.suggest.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.suggest.ISuggestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/6
 */
@Transactional
@Repository
public class SuggestDaoImpl implements ISuggestDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> listSuggest(int userId, Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,user_id,information,contents,status,is_read,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(createtime,'%Y-%m-%d') as createDate, ");
        sql.append(" DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime,complain_type,answer from tb_complain c where status=1 ");
        sql.append(" and user_id=").append(userId).append(" ");
        if (null != id) {
            sql.append(" and id=").append(id).append(" ");
        }
        sql.append(" ORDER BY createtime desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public int addSuggest(Object obj) {
        return iGeneralDao.save(obj);
    }
}
