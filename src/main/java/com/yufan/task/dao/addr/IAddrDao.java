package com.yufan.task.dao.addr;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 11:47
 * 功能介绍:
 */
public interface IAddrDao {


    /**
     * 查询用户取货地址
     * @param userId
     * @param addrType
     * @return
     */
    public List<Map<String, Object>> queryUserAddrListMap(int userId,Integer addrType);


    /**
     * 查询平台地址
     * @param addrType
     * @return
     */
    public List<Map<String,Object>> queryPlatformListMap(Integer addrType);

}
