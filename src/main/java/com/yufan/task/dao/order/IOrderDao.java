package com.yufan.task.dao.order;

import com.alibaba.fastjson.JSONArray;
import com.yufan.pojo.TbOrder;
import com.yufan.pojo.TbOrderDetail;
import com.yufan.pojo.TbOrderDetailProperty;
import com.yufan.pojo.TbOrderRefund;
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
    public int userCartCount(int userId,Integer goodsId);


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

    public List<Map<String, Object>> queryUserOrderDetail(int userId, String orderNo);

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
     *
     * @param userId
     * @param orderIds
     */
    public void updateUserOrderReadMark(int userId, String orderIds);

    /**
     * 创建订单
     *
     * @param order
     * @param detailList
     * @param detailPropMap
     * @return
     */
    public int createOrder(TbOrder order, List<TbOrderDetail> detailList, Map<String, JSONArray> detailPropMap);


    /**
     * 删除购物
     *
     * @param cartIds
     */
    public void deleteShopCartByCartIds(int userId, String cartIds, int status);

    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    public TbOrder loadOrder(int orderId);

    public TbOrder loadOrder(String orderNo);

    public TbOrder loadOrder(int orderId, int userId);


    public int saveObj(Object obj);


    /**
     * 查询退款信息
     *
     * @param orderNo
     * @return
     */
    public TbOrderRefund loadOrderRefund(String orderNo);


    /**
     * 订单支付成功
     *
     * @param orderNo
     * @return
     */
    public int payOrderSuccess(String orderNo,Integer payWay,String payTime,String payCode);

    /**
     * 查询购物车数量
     *
     * @param userId
     * @param goodsId
     * @return
     */
    List<Map<String, Object>> findCartCount(Integer userId, Integer goodsId);
    List<Map<String, Object>> findCartSumCount(Integer userId, Integer goodsId);

    /**
     * 查询购物车
     *
     * @param userId
     * @param cartIds
     * @return
     */
    List<Map<String, Object>> findCartsByCartIds(Integer userId, String cartIds);
}
