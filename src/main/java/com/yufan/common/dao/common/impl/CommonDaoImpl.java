package com.yufan.common.dao.common.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.common.dao.common.ICommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/26
 */
@Transactional
@Repository
public class CommonDaoImpl implements ICommonDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public int saveObj(Object object) {
        return iGeneralDao.save(object);
    }
}
