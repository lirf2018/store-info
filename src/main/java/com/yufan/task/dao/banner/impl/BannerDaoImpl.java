package com.yufan.task.dao.banner.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.banner.IBannerDao;
import com.yufan.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 14:51
 * 功能介绍:
 */
@Repository
@Transactional
public class BannerDaoImpl implements IBannerDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> loadBannerListMap(int size) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT banner_id,banner_title,banner_name,banner_link,CONCAT('" + Constants.IMG_URL + "',banner_img) as banner_img,remark ");
        sql.append(" from tb_banner ");
        sql.append(" where `status`=1 and NOW()>=start_time and NOW()<=end_time ");
        sql.append(" ORDER BY data_index desc,banner_id desc LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
