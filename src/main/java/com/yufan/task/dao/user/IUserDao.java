package com.yufan.task.dao.user;

import com.yufan.pojo.TbPrivateCustom;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询私人定制
     *
     * @param userId
     * @param findType
     * @return
     */
    public PageInfo loadUserPrivateList(int userId, int findType, Integer pageSize, Integer currePage);

    /**
     * 取消私人定制
     *
     * @param userId
     * @param id
     */
    public void cancelUserPrivate(int userId, int id);

    public void updateUserPrivate(int userId, int id, String getDate,String getDateStr, int index);

    List<TbPrivateCustom> loadPrivateCustom(String privateCode);


}
