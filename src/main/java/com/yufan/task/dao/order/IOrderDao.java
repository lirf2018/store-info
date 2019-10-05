package com.yufan.task.dao.order;

import com.yufan.utils.PageInfo;

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
     * 更新购物车数据
     *
     * @param cartId
     * @param count
     */
    public void updateShopCart(int userId, int cartId, int count);

    /**
     * 删除购物车
     *
     * @param userId
     * @param cartIds
     */
    public void deleteShopcart(int userId, String cartIds);


    /**
     * 查询用户订单支付信息
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryOrderPayAllListMap(int userId);


    /**
     * 查询用户购物车数量
     *
     * @return
     */
    public int userCartCount(int userId);


    /**
     * 查询用户订单列表
     *
     * @param userId
     * @param status
     * @return
     */
    public PageInfo queryUserOrderList(int currePage, Integer pageSize, int userId, Integer status);

    public List<Map<String, Object>> queryOrderDetailListmap(String orderIds);

    /**
     * 查询用户订单详情
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryUserOrderDetail(int userId, int orderId);

    /**
     * 查询订单详情属性
     *
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> queryUserOrderDetailProp(int orderId);


    /**
     * 更新订单状态
     *
     * @param orderId
     * @param userId
     * @param orderStatus
     */
    public void updateOrderStatus(int orderId, int userId, int orderStatus);

    /**
     * 更新阅读标记
     * @param userId
     * @param orderIds
     */
    public void updateUserOrderReadMark(int userId,String orderIds);

}
