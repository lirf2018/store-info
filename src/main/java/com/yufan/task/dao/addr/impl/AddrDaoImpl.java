package com.yufan.task.dao.addr.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.addr.IAddrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 11:48
 * 功能介绍:
 */
@Transactional
@Repository
public class AddrDaoImpl implements IAddrDao {

    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public List<Map<String, Object>> queryUserAddrListMap(int userId, Integer addrType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,area_ids,area_name,user_phone,user_name,addr_detail,is_default,addr_name,addr_type from tb_user_addr where `status`=1 and user_id=").append(userId).append(" ");
        if (null != addrType) {
            sql.append(" and addr_type=").append(addrType).append("  ");
        }
        sql.append(" ORDER BY is_default desc,createtime desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryPlatformListMap(Integer addrType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT pa.id,pa.detail_addr,pa.freight,pa.sort_char,pa.addr_type,pa.addr_name,pa.addr_lng,pa.addr_lat from tb_platform_addr pa  ");
        sql.append(" where pa.status=1  ");
        if (null != addrType) {
            sql.append(" and pa.addr_type=").append(addrType).append(" ");
        }
        sql.append(" ORDER BY pa.sort_char,pa.addr_sort DESC  ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
