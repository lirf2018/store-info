package com.yufan.task.dao.user.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.utils.PageInfo;
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
            count = Integer.parseInt(null == list.get(0).get("jifen") ? "0" : list.get(0).get("jifen").toString());
        }
        return count;
    }

    @Override
    public PageInfo loadUserPrivateList(int userId, int findType, Integer pageSize, Integer currePage) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,DATE_FORMAT(pay_time,'%Y-%m-%d %T') as pay_time,private_code,DATE_FORMAT(reservation_time,'%Y-%m-%d %T') as reservation_time, ");
        sql.append(" status,contents,DATE_FORMAT(get_time,'%Y-%m-%d') as get_time,post_way,is_yuyue,DATE_FORMAT(update_time,'%Y-%m-%d %T') as update_time ");
        sql.append(" from tb_private_custom ");
        sql.append(" where  user_id=").append(userId).append(" ");
        if (findType == 0) {
            sql.append(" and `status` = 0 or status =1 ORDER BY is_yuyue desc,get_time,create_time ");
        } else {
            sql.append(" and `status`=2 ORDER BY update_time desc ");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize == null ? 20 : pageSize);
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void cancelUserPrivate(int userId, int id) {
        String sql = " update tb_private_custom set reservation_time=null,status=0,get_time=null,is_yuyue=0 where id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, id, userId);
    }

    @Override
    public void updateUserPrivate(int userId, int id, String getDate) {
        String sql = " update tb_private_custom set reservation_time=now(),status=1,get_time=?,is_yuyue=1 where id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, getDate, id, userId);
    }
}
