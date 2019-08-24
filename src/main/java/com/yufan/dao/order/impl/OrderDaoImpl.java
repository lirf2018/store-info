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
        sql.append("  ");
        sql.append(" SELECT cart.cart_id,cart.user_id,cart.goods_id,cart.goods_name,cart.goods_img,cart.goods_spec,cart.goods_spec_name,cart.goods_count,cart.goods_price,cart.true_money,cart.shop_id,s.shop_name, cart.status,cart.remark,cart.goods_spec_name_str,cart.cart_type ");
        sql.append(" ,s.shop_name,s.shop_logo ");
        sql.append(" from tb_order_cart cart JOIN tb_shop s on s.shop_id=cart.shop_id ");
        sql.append(" where cart.user_id=").append(userId).append(" ");
        if (null != carType) {
            sql.append(" and cart.cart_type=").append(carType).append(" ");
        }
        sql.append(" ORDER BY cart.createtime desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
