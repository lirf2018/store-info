package com.yufan.dao.order;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:06
 * 功能介绍:
 */
public interface IOrderDao {


    /**
     * 查询用户购物车列表
     *
     * @param userId
     * @param carType
     * @return
     */
    public List<Map<String, Object>> queryUserOrderCartListMap(int userId, Integer carType);


    /**
     * 查询用户订单支付信息
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryOrderPayAllListMap(int userId);


    /**
     * 查询用户购物车数量
     * @return
     */
    public int userCartCount(int userId);

}
