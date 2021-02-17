package com.yufan.task.dao.addr.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbPlatformAddr;
import com.yufan.pojo.TbUserAddr;
import com.yufan.task.dao.addr.IAddrDao;
import com.yufan.utils.Constants;
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
        sql.append(" SELECT pa.id,pa.detail_addr,pa.freight,pa.sort_char,pa.addr_type,pa.addr_name,pa.addr_lng,pa.addr_lat,ifnull(md.store_name,'') as store_name from tb_platform_addr pa  ");
        sql.append(" LEFT JOIN tb_mendian md on md.store_id=pa.store_id ");
        sql.append(" where pa.status=1  ");
        if (null != addrType) {
            sql.append(" and pa.addr_type=").append(addrType).append(" ");
        }
        sql.append(" ORDER BY pa.sort_char,pa.addr_sort DESC  ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void saveUserAddr(TbUserAddr userAddr) {
        iGeneralDao.save(userAddr);
    }

    @Override
    public void updateUserAddr(TbUserAddr userAddr) {
        iGeneralDao.saveOrUpdate(userAddr);
    }

    @Override
    public List<Map<String, Object>> queryCountryAddrList(String parentCode) {
        String sql = " select region_id,region_name,freight,region_code from tb_region where parent_id=? and status=1 ORDER BY region_order desc,region_id desc ";
        return iGeneralDao.getBySQLListMap(sql, parentCode);
    }

    @Override
    public List<Map<String, Object>> queryCountryAddrListByLevel(int regionLevel) {
        String sql = " select region_id,region_name from tb_region where region_level=? and status=1 ORDER BY region_order desc,region_id desc ";
        return iGeneralDao.getBySQLListMap(sql, regionLevel);
    }

    @Override
    public void updateAddrDefaul(Integer id, int userId) {
        //取消默认
        String sql = " update tb_user_addr set is_default=0 where user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, userId);
        if (id == null) {
            return;
        }
        //更新默认
        String sqlDefaul = " update tb_user_addr set is_default=1 where id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sqlDefaul, id, userId);
    }

    @Override
    public void deleteUserAddr(Integer id, int userId) {
        if (null == id) {
            String sql = " update tb_user_addr set status=0 where user_id=?  ";
            iGeneralDao.executeUpdateForSQL(sql, userId);
            return;
        }
        String sql = " update tb_user_addr set status=0 where user_id=? and id=?   ";
        iGeneralDao.executeUpdateForSQL(sql, userId, id);
    }

    @Override
    public List<Map<String, Object>> queryAddrByRegionCode(String regionCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_code,freight from tb_region where region_code in ('").append(regionCode).append("') ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public TbUserAddr loadUserAddrById(int userAddrId) {
        String hql = " from TbUserAddr where id=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, userAddrId);
    }

    @Override
    public TbPlatformAddr loadPlatformAddr(int id) {
        String hql = " from TbPlatformAddr where id=?1 and status=?2 ";
        return iGeneralDao.queryUniqueByHql(hql, id, Constants.DATA_STATUS_YX);
    }

    @Override
    public List<Map<String, Object>> queryGlobelAddr(String parentId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select re.region_id as regionId,re.region_code as regionCode,re.region_name as regionName,re.parent_id as parentId,re.freight,re.region_level as regionLevel  ");
        sql.append(" from tb_region re where re.`status`=1 ");
        sql.append(" and re.parent_id='").append(parentId).append("' ");
        sql.append(" order by re.region_order desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryGlobelAddrByIds(String ids) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select re.region_id,re.region_name from tb_region re where re.region_id in (" + ids + ") ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
