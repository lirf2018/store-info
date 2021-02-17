package com.yufan.task.dao.activity.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbActivity;
import com.yufan.task.dao.activity.IActivityDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 14:53
 * 功能介绍:
 */
@Repository
@Transactional
public class ActivityDaoImpl implements IActivityDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> loadActivityListMap(int size) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT activity_id,activity_title,activity_name,activity_link,CONCAT('" + Constants.IMG_WEB_URL + "',activity_img) as activity_img,activity_intro ");
        sql.append(" from tb_activity ");
        sql.append(" where `status`=1 and NOW()>=start_time and NOW()<=end_time ");
        sql.append(" ORDER BY data_index desc,activity_id desc LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    public PageInfo loadActivityPage(Integer currePage, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ac.activity_id as activityId ,ac.activity_title as activityTitle ,ac.activity_name as activityName ");
        sql.append(" ,ac.activity_link as activityLink,CONCAT('" + Constants.IMG_WEB_URL + "',ac.activity_img) as activityImg, ");
        sql.append(" ac.activity_intro as activityIntro,ac.remark,DATE_FORMAT(ac.end_time,'%Y.%m.%d') as endTime ");
        sql.append(" from tb_activity ac ");
        sql.append(" where ac.`status`=1 and DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(ac.start_time,'%Y-%m-%d') ");
        sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')<=DATE_FORMAT(ac.end_time,'%Y-%m-%d') ");
        sql.append(" ORDER BY ac.data_index desc ");
        //
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize == null ? 20 : pageSize);
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public TbActivity loadActivity(int id) {
        String sql = " from TbActivity where activityId=?1 ";
        return iGeneralDao.queryUniqueByHql(sql, id);
    }
}
