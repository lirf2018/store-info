package com.yufan.dao.category.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.dao.category.ICategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 16:47
 * 功能介绍:
 */
@Transactional
@Repository
public class CategoryDaoImpl implements ICategoryDao {

    @Autowired
    private IGeneralDao iGeneralDao;
}
