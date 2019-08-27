package com.yufan.task.dao.user;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 9:11
 * 功能介绍:
 */
public interface IUserDao {



    /**
     * 查询用户积分
     *
     * @param userId
     * @return
     */
    public Integer userJifen(int userId);


}
