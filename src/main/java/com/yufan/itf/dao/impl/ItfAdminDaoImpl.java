package com.yufan.itf.dao.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.itf.dao.IItfAdminDao;
import com.yufan.pojo.ItfTbItemAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
@Transactional
@Repository
public class ItfAdminDaoImpl implements IItfAdminDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public ItfTbItemAdmin loadItfTbItemAdminByName(String name) {
        String hql = " from ItfTbItemAdmin where adminName=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, name);
    }
    @Override
    public ItfTbItemAdmin loadItfTbItemAdminById(int id) {
        String hql = " from ItfTbItemAdmin where adminId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, id);
    }

    @Override
    public void updateItfTbItemAdminByName(ItfTbItemAdmin itfTbItemAdmin) {
        iGeneralDao.saveOrUpdate(itfTbItemAdmin);
    }
}
