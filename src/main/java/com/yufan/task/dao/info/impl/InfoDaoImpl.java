package com.yufan.task.dao.info.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbInfo;
import com.yufan.task.dao.info.IInfoDao;
import com.yufan.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/17 22:22
 * 功能介绍:
 */
@Transactional
@Repository
public class InfoDaoImpl implements IInfoDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryInfoList(Integer size) {
        if (null == size || size == 0) {
            size = 30;
        }
        String sql = " SELECT info_id,info_title,info_url,CONCAT('" + Constants.IMG_WEB_URL + "',info_img) as info_img,DATE_FORMAT(create_time,'%Y-%m-%d %T') as create_time,DATE_FORMAT(create_time,'%c月%e日') as showTime,read_count from tb_info where `status`=1 ORDER BY info_index desc,info_id desc LIMIT " + size + " ";
        return iGeneralDao.getBySQLListMap(sql);
    }

    @Override
    public TbInfo loadInfo(int id) {
        String hql = " from TbInfo where id=?1 and status=1 ";
        return iGeneralDao.queryUniqueByHql(hql, id);
    }

    @Override
    public void updateReadCount(int id) {
        String sql = " update tb_info set read_count=read_count+1 where info_id=?  ";
        iGeneralDao.queryUniqueByHql(sql, id);
    }
}
