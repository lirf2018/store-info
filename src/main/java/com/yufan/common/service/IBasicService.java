package com.yufan.common.service;

import com.yufan.pojo.TbInfAccount;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 14:59
 * 功能介绍:
 */
public interface IBasicService {

    /**
     * 查询接口账号信息
     *
     * @param account
     * @return
     */
    public TbInfAccount loadTbInfAccount(String account);

}
