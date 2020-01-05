package com.yufan.task.dao.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbOrder;
import com.yufan.pojo.TbOrderDetail;
import com.yufan.pojo.TbOrderDetailProperty;
import com.yufan.pojo.TbOrderRefund;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
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

    private Logger LOG = Logger.getLogger(OrderDaoImpl.class);

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryUserOrderCartListMap(int userId, Integer carType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT cart.cart_id,cart.user_id,cart.goods_id,cart.goods_name,cart.goods_img,cart.goods_spec,cart.goods_spec_name,cart.goods_count,cart.goods_price,cart.true_money,cart.shop_id,cart.status,cart.remark,cart.goods_spec_name_str,cart.cart_type ");
        sql.append(" ,s.shop_name,s.shop_logo,if(g.lastaltertime>cart.createtime,0,1) as last_status,g.is_single,IFNULL(sku.sku_id,0) as sku_id ");
        sql.append(" from tb_order_cart cart JOIN tb_shop s on s.shop_id=cart.shop_id ");
        sql.append(" JOIN tb_goods g on g.goods_id=cart.goods_id ");
        sql.append(" left join tb_goods_sku sku on sku.goods_id=cart.goods_id and sku.prop_code=cart.goods_spec and sku.`status`=1 ");
        sql.append(" where cart.user_id=").append(userId).append(" and cart.`status`=1 ");
        if (null != carType) {
            sql.append(" and cart.cart_type=").append(carType).append(" ");
        }
        sql.append(" ORDER BY cart.createtime desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void deleteShopcart(int userId, String cartIds) {
        String sql = " update tb_order_cart set `status`=? where user_id=? and cart_id in (" + cartIds + ") ";
        iGeneralDao.executeUpdateForSQL(sql, 4, userId);
    }

    @Override
    public void updateShopCart(int userId, int cartId, int count) {
        String sql = " update tb_order_cart set goods_count=? where cart_id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, count, cartId, userId);
    }

    @Override
    public List<Map<String, Object>> queryOrderPayAllListMap(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT order_price,order_status,user_read_mark from tb_order where user_id=").append(userId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public int userCartCount(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SUM(cart.goods_count) as goods_count from tb_order_cart cart  JOIN tb_goods g on g.goods_id=cart.goods_id ");
        sql.append(" where cart.user_id=? and cart.`status`=1 and cart.createtime>g.lastaltertime ");

        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString(), userId);
        int count = 0;
        if (null != list && list.size() > 0) {
            count = Integer.parseInt(null == list.get(0).get("goods_count") ? "0" : list.get(0).get("goods_count").toString());
        }
        return count;
    }

    @Override
    public PageInfo queryUserOrderList(int currePage, Integer pageSize, int userId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  o.order_id,o.order_price,o.order_count,o.order_status ");
        sql.append(" ,s.shop_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',s.shop_logo) as shop_logo ");
        sql.append(" from tb_order o JOIN tb_shop s on o.shop_id=s.shop_id where 1=1 ");
        if (null != status && status != -1) {
            sql.append(" and o.order_status=").append(status).append(" ");
        }
        sql.append(" and o.user_id=").append(userId).append(" ");
        sql.append(" ORDER BY o.user_read_mark desc,o.order_id desc ");

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
        sql.append(" ,s.shop_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',s.shop_logo) as shop_logo,DATE_FORMAT(o.finish_time,'%Y-%m-%d %T') as finish_time  ");
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
        String sql = " update tb_order set order_status=?,user_read_mark=1,lastaltertime=now() where order_id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, orderStatus, orderId, userId);
    }

    @Override
    public void updateUserOrderReadMark(int userId, String orderIds) {
        String sql = " update tb_order set user_read_mark=0,lastaltertime=now() where user_id=? and order_id in (" + orderIds + ") ";
        iGeneralDao.executeUpdateForSQL(sql, userId);
    }

    @Override
    @Transactional
    public int createOrder(TbOrder order, List<TbOrderDetail> detailList, Map<String, JSONArray> detailPropMap) {
        try {
            //保存订单
            iGeneralDao.save(order);
            int orderId = order.getOrderId();
            LOG.info("-----orderId--------" + orderId);
            //保存详情
            for (int i = 0; i < detailList.size(); i++) {
                TbOrderDetail detail = detailList.get(i);
                String detailSql = " insert into tb_order_detail(order_id,goods_id,goods_name,goods_spec,goods_spec_name,goods_count,sale_money,goods_true_money,goods_purchase_price,time_price,deposit_price," +
                        "shop_id,shop_name,out_code,get_addr_id,get_addr_name,get_time,back_addr_id,back_addr_name,back_time,detail_status," +
                        "is_coupon,createtime,lastaltertime,lastalterman,remark,goods_img,cart_id,time_goods_id,goods_spec_name_str) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                int detailId = iGeneralDao.executeUpdateForSQL(detailSql, orderId, detail.getGoodsId(), detail.getGoodsName(), detail.getGoodsSpec(), detail.getGoodsSpecName(), detail.getGoodsCount(), detail.getSaleMoney()
                        , detail.getGoodsTrueMoney(), detail.getGoodsPurchasePrice(), detail.getTimePrice(), detail.getDepositPrice(), detail.getShopId(), detail.getShopName(), detail.getOutCode(), detail.getGetAddrId(), detail.getGetAddrName()
                        , detail.getGetTime(), detail.getBackAddrId(), detail.getBackAddrName(), detail.getBackTime(), detail.getDetailStatus(), detail.getIsCoupon(), detail.getCreatetime(), detail.getLastaltertime(), detail.getLastalterman()
                        , detail.getRemark(), detail.getGoodsImg(), detail.getCartId(), detail.getTimeGoodsId(), detail.getGoodsSpecNameStr());

                LOG.info("-----detailId--------" + detailId);
                String key = detail.getGoodsId() + "-" + detail.getGoodsSpec();
                JSONArray detailPropList = detailPropMap.get(key);
                if (null != detailPropList) {
                    for (int j = 0; j < detailPropList.size(); j++) {
                        String propertyKey = detailPropList.getJSONObject(j).getString("property_key");
                        String propertyValue = detailPropList.getJSONObject(j).getString("property_value");
                        String remark = detailPropList.getJSONObject(j).getString("remark");
                        TbOrderDetailProperty property = new TbOrderDetailProperty();
                        property.setOrderId(orderId);
                        property.setDetailId(detailId);
                        property.setPropertyKey(propertyKey);
                        property.setPropertyValue(propertyValue);
                        property.setRemark(remark);
                        iGeneralDao.save(property);
                    }
                }
            }
            return orderId;
        } catch (Exception e) {
            LOG.error("--------订单保存失败------", e);
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteShopCartByCartIds(int userId, String cartIds, int status) {
        String sql = " update tb_order_cart set `status`=" + status + " where cart_id in (" + cartIds + ") and user_id = " + userId + " ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public TbOrder loadOrder(int orderId) {
        String hql = " from TbOrder where orderId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, orderId);
    }

    @Override
    public TbOrder loadOrder(int orderId, int userId) {
        String hql = " from TbOrder where orderId=?1 and userId=?2 ";
        return iGeneralDao.queryUniqueByHql(hql, orderId, userId);
    }

    @Override
    public int saveObj(Object obj) {
        return iGeneralDao.save(obj);
    }

    @Override
    public TbOrderRefund loadOrderRefund(String orderNo) {
        String hql = " from TbOrderRefund where orderNo = ?1 ";
        return iGeneralDao.queryUniqueByHql(hql, orderNo);
    }
}
