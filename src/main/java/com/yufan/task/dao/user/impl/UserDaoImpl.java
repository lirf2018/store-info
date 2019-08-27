package com.yufan.task.dao.user.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.user.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 9:10
 * 功能介绍:
 */
@Repository
@Transactional
public class UserDaoImpl implements IUserDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public Integer userJifen(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SUM(jifen) as jifen   from tb_jifen where user_id=").append(userId).append(" and `status`=1 and pass_time>=NOW() ");
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        int count = 0;
        if (null != list && list.size() > 0) {
            count = Integer.parseInt(list.get(0).get("jifen").toString());
        }
        return count;
    }
}
