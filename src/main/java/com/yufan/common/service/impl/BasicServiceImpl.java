package com.yufan.common.service.impl;

import com.yufan.common.dao.account.IAccountJpaDao;
import com.yufan.common.service.IBasicService;
import com.yufan.pojo.TbInfAccount;
import com.yufan.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 16:35
 * 功能介绍:
 */
@Service
public class BasicServiceImpl implements IBasicService {

    @Autowired
    private IAccountJpaDao iAccountJpaDao;

    @Override
    public TbInfAccount loadTbInfAccount(String account) {
        return iAccountJpaDao.findInfAccountBySid(account, Constants.DATA_STATUS_YX);
    }
}
