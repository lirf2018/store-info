package com.yufan.task.dao.activity.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.activity.IActivityDao;
import com.yufan.utils.Constants;
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
        sql.append(" SELECT activity_id,activity_title,activity_name,activity_link,CONCAT('"+ Constants.IMG_WEB_URL +"',activity_img) as activity_img,activity_intro ");
        sql.append(" from tb_activity ");
        sql.append(" where `status`=1 and NOW()>=start_time and NOW()<=end_time ");
        sql.append(" ORDER BY data_index desc,activity_id desc LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
