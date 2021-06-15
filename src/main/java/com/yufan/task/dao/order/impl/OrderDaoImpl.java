package com.yufan.task.dao.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbOrder;
import com.yufan.pojo.TbOrderDetail;
import com.yufan.pojo.TbOrderDetailProperty;
import com.yufan.pojo.TbOrderRefund;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
        sql.append(" SELECT cart.cart_id,cart.user_id,cart.goods_id,cart.goods_name,cart.goods_img,cart.goods_spec,cart.goods_spec_name,cart.goods_count,cart.goods_price,cart.true_money,cart.shop_id,cart.status, ");
        sql.append(" cart.remark,cart.goods_spec_name_str,cart.cart_type,ifnull(cart.time_goods_id,0) as time_goods_id  ");
        sql.append(" ,s.shop_name,s.shop_logo,if(g.lastaltertime>cart.createtime,2,1) as last_status,g.is_single,IFNULL(sku.sku_id,0) as sku_id ");
        sql.append(" from tb_order_cart cart JOIN tb_shop s on s.shop_id=cart.shop_id ");
        sql.append(" JOIN tb_goods g on g.goods_id=cart.goods_id ");
        sql.append(" left join tb_goods_sku sku on sku.goods_id=cart.goods_id and sku.prop_code=cart.goods_spec and sku.`status`=1 ");
        sql.append(" where cart.user_id=").append(userId).append(" and cart.`status`in (1,2)  ");
        if (null != carType) {
            sql.append(" and cart.cart_type=").append(carType).append(" ");
        }
        sql.append(" ORDER BY cart.createtime desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void deleteShopcart(int userId, String cartIds) {
        if (cartIds.endsWith(",")) {
            cartIds = cartIds.substring(0, cartIds.length() - 1);
        }
        String sql = " update tb_order_cart set `status`=?,lastaltertime=now() where user_id=? and cart_id in (" + cartIds + ") ";
        iGeneralDao.executeUpdateForSQL(sql, 4, userId);
    }

    @Override
    public void updateShopCart(int userId, int cartId, int count) {
        String sql = " update tb_order_cart set goods_count=?,lastaltertime=now() where cart_id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, count, cartId, userId);
    }

    @Override
    public List<Map<String, Object>> queryOrderPayAllListMap(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT order_price,order_status,user_read_mark from tb_order where user_id=").append(userId).append(" ");
        sql.append(" and order_status in (0,2,5) ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public int userCartCount(int userId, Integer goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SUM(cart.goods_count) as goods_count from tb_order_cart cart  JOIN tb_goods g on g.goods_id=cart.goods_id ");
        sql.append(" where cart.user_id=? and cart.`status`=1 and cart.createtime>g.lastaltertime ");
        if (null != goodsId) {
            sql.append(" and cart.goods_id != ").append(goodsId).append(" ");
        }

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
        sql.append(" SELECT  o.order_id,o.order_no,o.order_price,o.real_price,o.order_count,o.order_status ");
        sql.append(" ,DATE_FORMAT(o.order_time,'%Y-%m-%d %T') as order_time,user_remark ");
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
        if (orderIds.endsWith(",")) {
            orderIds = orderIds.substring(0, orderIds.length() - 1);
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT d.order_id,CONCAT('").append(Constants.IMG_WEB_URL).append("',d.goods_img) as goods_img,d.goods_id,d.goods_name,d.sale_money,d.goods_count,d.goods_spec_name_str ");
        sql.append(" ,d.goods_spec,d.goods_spec_name,d.goods_true_money ");
        sql.append(" from tb_order_detail d where d.order_id in (").append(orderIds).append(") ORDER BY d.detail_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryUserOrderDetail(int userId, Integer orderId, String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  o.order_id,o.order_price,o.order_count,o.order_status,o.order_no,DATE_FORMAT(o.order_time,'%Y-%m-%d %T') as order_time,DATE_FORMAT(o.pay_time,'%Y-%m-%d %T') as pay_time ");
        sql.append(" ,o.pay_way,o.user_name,o.user_phone,o.user_addr,o.real_price,o.post_price,o.discounts_price,o.post_way,o.advance_pay_way,o.advance_price,o.user_remark ");
        sql.append(" ,d.goods_spec_name ");
        sql.append(" ,CONCAT('").append(Constants.IMG_WEB_URL).append("',d.goods_img) as goods_img,d.goods_id,d.goods_name,d.sale_money,d.goods_count,d.goods_spec_name_str ");
        sql.append(" ,d.deposit_price,d.goods_true_money ");
        sql.append(" ,s.shop_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',s.shop_logo) as shop_logo,DATE_FORMAT(o.finish_time,'%Y-%m-%d %T') as finish_time  ");
        sql.append(" from tb_order o JOIN tb_shop s on o.shop_id=s.shop_id ");
        sql.append(" JOIN tb_order_detail d on d.order_id=o.order_id ");
        sql.append(" where o.user_id=").append(userId).append(" ");
        if (StringUtils.isNotEmpty(orderNo)) {
            sql.append(" o.order_no='").append(orderNo).append("'  ");
        } else {
            sql.append(" o.order_id=").append(orderId).append("  ");
        }
        sql.append(" ORDER BY o.order_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
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
    public void updateOrderStatus(int orderId, int userId, int orderStatus, String remark) {
        String sql = " update tb_order set order_status=?,user_read_mark=1,lastaltertime=now(),remark=? where order_id=? and user_id=?  ";
        iGeneralDao.executeUpdateForSQL(sql, orderStatus, remark, orderId, userId);
    }

    @Override
    public void updateUserOrderReadMark(int userId, String orderIds) {
        if (orderIds.endsWith(",")) {
            orderIds = orderIds.substring(0, orderIds.length() - 1);
        }
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
//                String detailSql = " insert into tb_order_detail(order_id,goods_id,goods_name,goods_spec,goods_spec_name,goods_count,sale_money,goods_true_money,goods_purchase_price,time_price,deposit_price," +
//                        "shop_id,shop_name,out_code,get_addr_id,get_addr_name,get_time,back_addr_id,back_addr_name,back_time,detail_status," +
//                        "is_coupon,createtime,lastaltertime,lastalterman,remark,goods_img,cart_id,time_goods_id,goods_spec_name_str) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                int detailId = iGeneralDao.executeUpdateForSQL(detailSql, orderId, detail.getGoodsId(), detail.getGoodsName(), detail.getGoodsSpec(), detail.getGoodsSpecName(), detail.getGoodsCount(), detail.getSaleMoney()
//                        , detail.getGoodsTrueMoney(), detail.getGoodsPurchasePrice(), detail.getTimePrice(), detail.getDepositPrice(), detail.getShopId(), detail.getShopName(), detail.getOutCode(), detail.getGetAddrId(), detail.getGetAddrName()
//                        , detail.getGetTime(), detail.getBackAddrId(), detail.getBackAddrName(), detail.getBackTime(), detail.getDetailStatus(), detail.getIsCoupon(), detail.getCreatetime(), detail.getLastaltertime(), detail.getLastalterman()
//                        , detail.getRemark(), detail.getGoodsImg(), detail.getCartId(), detail.getTimeGoodsId(), detail.getGoodsSpecNameStr());
                detail.setOrderId(orderId);
                int detailId = iGeneralDao.save(detail);
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
        if (cartIds.endsWith(",")) {
            cartIds = cartIds.substring(0, cartIds.length() - 1);
        }
        String sql = " update tb_order_cart set `status`=" + status + " where cart_id in (" + cartIds + ") and user_id = " + userId + " ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public TbOrder loadOrder(int orderId) {
        String hql = " from TbOrder where orderId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, orderId);
    }

    @Override
    public TbOrder loadOrder(String orderNo) {
        String hql = " from TbOrder where orderNo=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, orderNo);
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

    @Override
    public int payOrderSuccess(String orderNo, Integer payWay, String payTime, String payCode) {
        String sql = " update tb_order set pay_way=?,pay_time=?,pay_code=?,lastaltertime=now(),order_status=? where order_no=? ";
        return iGeneralDao.executeUpdateForSQL(sql, payWay, payTime, payCode, Constants.ORDER_STATUS_1, orderNo);
    }

    @Override
    public List<Map<String, Object>> findCartCount(Integer userId, Integer goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select cart.goods_id,count(cart.goods_id) as goodsCount from tb_order_cart cart JOIN tb_goods g on g.goods_id=cart.goods_id where 1=1 and cart.createtime>=g.lastaltertime and cart.status=1 and cart.user_id=").append(userId).append(" ");
        if (null != goodsId) {
            sql.append(" and cart.goods_id = ").append(goodsId).append(" ");
        }
        sql.append(" GROUP BY cart.goods_id ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> findCartSumCount(Integer userId, Integer goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select cart.goods_id,sum(cart.goods_count) as goodsCount from tb_order_cart cart JOIN tb_goods g on g.goods_id=cart.goods_id where 1=1 and cart.createtime>=g.lastaltertime and cart.status=1 and cart.user_id=").append(userId).append(" ");
        if (null != goodsId) {
            sql.append(" and cart.goods_id = ").append(goodsId).append(" ");
        }
        sql.append(" GROUP BY cart.goods_id ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> findCartsByCartIds(Integer userId, String cartIds) {
        if (cartIds.endsWith(",")) {
            cartIds = cartIds.substring(0, cartIds.length() - 1);
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT cart.cart_id,cart.goods_id,cart.sku_id,cart.shop_id,cart.goods_count,ifnull(time_goods_id,0) as time_goods_id from tb_order_cart cart JOIN tb_goods g on g.goods_id=cart.goods_id where 1=1 and cart.createtime>=g.lastaltertime and cart.`status` = 1 ");
        sql.append(" and cart.cart_id in (").append(cartIds).append(") ");
        sql.append(" and cart.user_id = ").append(userId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> findUserOrderByCount(Integer userId, String limitBeginTime, Integer goodsId, Integer timeGoodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select sum(d.goods_count) as sum_count,DATE_FORMAT(o.pay_time,'%Y-%m-%d') as pay_time from tb_order o  ");
        sql.append(" JOIN tb_order_detail d on o.order_id = d.order_id where 1=1 ");
        sql.append(" and o.order_status in (1,2,4,5,6,9,11,12,13) ");
        sql.append(" and o.user_id=").append(userId).append(" ");
        if (null != goodsId && goodsId > 0) {
            sql.append(" and d.goods_id=").append(goodsId).append(" ");
        }
        if (timeGoodsId != null && timeGoodsId > 0) {
            sql.append(" and d.time_goods_id=").append(timeGoodsId).append(" ");
        }
        if (StringUtils.isNotEmpty(limitBeginTime)) {
            sql.append(" and o.pay_time>='").append(limitBeginTime).append("' ");
        }
        sql.append(" GROUP BY DATE_FORMAT(o.pay_time,'%Y-%m-%d') ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void updateOrderCartStatus(Integer userId, String goodsIds, int status, Integer withTimeGoods) {
        if (goodsIds.endsWith(",")) {
            goodsIds = goodsIds.substring(0, goodsIds.length() - 1);
        }
        String sql = " update tb_order_cart set status =  " + status + " where status = 1 and user_id=" + userId + " and goods_id in (" + goodsIds + ") ";
        if (null != withTimeGoods) {
            sql = sql + " and time_goods_id>0";
        }
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public void clearOrderCart(int userId) {
        // 查询有效的 购物车商品
        String sql1 = " select c.cart_id from tb_order_cart c JOIN tb_goods g on g.goods_id=c.goods_id JOIN tb_shop s on s.shop_id=g.shop_id where g.`status`=1 and s.`status`=1 and c.lastaltertime>g.lastaltertime and user_id=" + userId;
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql1);
        if (CollectionUtils.isEmpty(list)) {
            //
            String sqlUpdate1 = "update tb_order_cart set status=2 where status=1 and user_id=" + userId;
            iGeneralDao.executeUpdateForSQL(sqlUpdate1);
        } else {
            String cartIds = "";
            for (int i = 0; i < list.size(); i++) {
                String cartId = list.get(i).get("cart_id").toString();
                cartIds = cartIds + cartId + ",";
            }
            if (StringUtils.isNotEmpty(cartIds)) {
                cartIds = cartIds.substring(0, cartIds.length() - 1);
            }
            String sqlUpdate1 = "update tb_order_cart set status=2 where status=1 and user_id=" + userId + " and cart_id not in (" + cartIds + ") ";
            iGeneralDao.executeUpdateForSQL(sqlUpdate1);
        }
        // 清理抢购类购物车商品
        String sql2 = " select c.cart_id from tb_order_cart c JOIN tb_time_goods tg on c.time_goods_id=tg.id where now()>tg.begin_time and NOW()<tg.end_time and tg.`status`=1 and tg.goods_store>0 and user_id = " + userId;
        List<Map<String, Object>> list2 = iGeneralDao.getBySQLListMap(sql2);
        if (CollectionUtils.isEmpty(list2)) {
            //
            String sqlUpdate1 = "update tb_order_cart set status=2 where status=1 and  time_goods_id>0 and user_id=" + userId;
            iGeneralDao.executeUpdateForSQL(sqlUpdate1);
        } else {
            String cartIds = "";
            for (int i = 0; i < list2.size(); i++) {
                String cartId = list2.get(i).get("cart_id").toString();
                cartIds = cartIds + cartId + ",";
            }
            if (StringUtils.isNotEmpty(cartIds)) {
                cartIds = cartIds.substring(0, cartIds.length() - 1);
            }
            String sqlUpdate1 = "update tb_order_cart set status=2 where status=1 and  time_goods_id>0 and user_id=" + userId + " and cart_id not in (" + cartIds + ") ";
            iGeneralDao.executeUpdateForSQL(sqlUpdate1);
        }
        // 删除失效
        String now = DatetimeUtil.convertDateToStr(DatetimeUtil.addDays(new Date(), -14));
        String sql = " update tb_order_cart set status=4 where user_id=" + userId + " and status=2 and lastaltertime<'" + now + "' ";
        iGeneralDao.executeUpdateForSQL(sql);
    }
}
