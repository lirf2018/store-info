package com.yufan.task.dao.order.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:07
 * 功能介绍:
 */
@Repository
@Transactional
public class OrderDaoImpl implements IOrderDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryUserOrderCartListMap(int userId, Integer carType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT cart.cart_id,cart.user_id,cart.goods_id,cart.goods_name,cart.goods_img,cart.goods_spec,cart.goods_spec_name,cart.goods_count,cart.goods_price,cart.true_money,cart.shop_id,cart.status,cart.remark,cart.goods_spec_name_str,cart.cart_type ");
        sql.append(" ,s.shop_name,s.shop_logo,if(g.lastaltertime>cart.createtime,0,1) as last_status,g.is_single ");
        sql.append(" from tb_order_cart cart JOIN tb_shop s on s.shop_id=cart.shop_id ");
        sql.append(" JOIN tb_goods g on g.goods_id=cart.goods_id ");
        sql.append(" where cart.user_id=").append(userId).append(" ");
        if (null != carType) {
            sql.append(" and cart.cart_type=").append(carType).append(" ");
        }
        sql.append(" ORDER BY cart.createtime desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void updateShopCart(int userId, int cartId, int count) {
        String sql = " update tb_order_cart set goods_count=? where cart_id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, count, cartId, userId);
    }

    @Override
    public List<Map<String, Object>> queryOrderPayAllListMap(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT order_price,order_status from tb_order where user_id=").append(userId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public int userCartCount(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SUM(cart.goods_count) as goods_count from tb_order_cart cart  JOIN tb_goods g on g.goods_id=cart.goods_id");
        sql.append(" where cart.user_id=? and cart.`status`=1 and cart.createtime>g.lastaltertime ");

        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString(), userId);
        int count = 0;
        if (null != list && list.size() > 0) {
            count = Integer.parseInt(list.get(0).get("goods_count").toString());
        }
        return count;
    }

    @Override
    public PageInfo queryUserOrderList(int currePage, Integer pageSize, int userId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  o.order_id,o.order_price,o.order_count,o.order_status ");
        sql.append(" ,s.shop_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',s.shop_logo) as shop_logo ");
        sql.append(" from tb_order o JOIN tb_shop s on o.shop_id=s.shop_id where 1=1 ");
        if (null != status) {
            sql.append(" and o.order_status=").append(status).append(" ");
        }
        sql.append(" and o.user_id=").append(userId).append(" ");
        sql.append(" ORDER BY o.order_id desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo.setPageSize(pageSize == null ? 20 : pageSize);
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    public List<Map<String, Object>> queryOrderDetailListmap(String orderIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT d.order_id,CONCAT('").append(Constants.IMG_WEB_URL).append("',d.goods_img) as goods_img,d.goods_id,d.goods_name,d.sale_money,d.goods_count,d.goods_spec_name_str ");
        sql.append(" from tb_order_detail d where d.order_id in (").append(orderIds).append(") ORDER BY d.detail_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryUserOrderDetail(int userId, int orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  o.order_id,o.order_price,o.order_count,o.order_status,o.order_no,DATE_FORMAT(o.order_time,'%Y-%m-%d %T') as order_time,DATE_FORMAT(o.pay_time,'%Y-%m-%d %T') as pay_time ");
        sql.append(" ,o.pay_way,o.user_name,o.user_phone,o.user_addr,o.real_price,o.post_price,o.discounts_price,o.post_way,o.advance_pay_way,o.advance_price ");
        sql.append(" ,CONCAT('").append(Constants.IMG_WEB_URL).append("',d.goods_img) as goods_img,d.goods_id,d.goods_name,d.sale_money,d.goods_count,d.goods_spec_name_str ");
        sql.append(" ,s.shop_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',s.shop_logo) as shop_logo ");
        sql.append(" from tb_order o JOIN tb_shop s on o.shop_id=s.shop_id ");
        sql.append(" JOIN tb_order_detail d on d.order_id=o.order_id ");
        sql.append(" where o.order_id=? and o.user_id=? ");
        sql.append(" ORDER BY o.order_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString(), orderId, userId);
    }

    @Override
    public List<Map<String, Object>> queryUserOrderDetailProp(int orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT order_id,detail_id,property_key,property_value,remark from tb_order_detail_property where order_id=").append(orderId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void updateOrderStatus(int orderId, int userId, int orderStatus) {
        String sql = " update tb_order set order_status=? where order_id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, orderStatus, orderId, userId);
    }
}
