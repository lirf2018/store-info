package com.yufan.dao.order.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.dao.order.IOrderDao;
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
        sql.append(" ,s.shop_name,s.shop_logo,if(g.lastaltertime>cart.createtime,0,1) as last_status ");
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
}
