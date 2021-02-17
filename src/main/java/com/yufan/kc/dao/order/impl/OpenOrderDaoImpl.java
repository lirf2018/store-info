package com.yufan.kc.dao.order.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbKcGoods;
import com.yufan.kc.bean.TbKcOrder;
import com.yufan.kc.bean.TbKcOrderDetail;
import com.yufan.kc.dao.order.OpenOrderDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/9 23:21
 * 功能介绍:
 */
@Transactional
@Repository
public class OpenOrderDaoImpl implements OpenOrderDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> findOrderDetailList(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select de.detail_id as id,de.goods_id as goodsId,if(de.shop_code is null or de.shop_code='',de.goods_code,de.shop_code) as goodsCode,de.buy_count as buyCount,de.goods_name as goodsName,de.goods_unit_name as goodsUnitName, ");
        sql.append(" de.unit_count as unitCount,de.sale_price as goodsSalePrice,de.member_price as memberPrice,de.discounts_price as goodsDiscountsPrice,'mouse-out-goods-ul' as style ,de.is_discounts as isDiscounts, ");
        sql.append(" de.sale_price_true as salePriceTrue, ");
        sql.append(" o.discounts_price as discountsPrice,o.discounts_member_price as discountsMemberPrice,o.discounts_ticket_price as discountsTicketPrice,o.order_price as orderPrice,o.real_price as realPrice,o.user_phone as userPhone,o.order_num as orderNum ");
        sql.append(" ,o.order_id as orderId,o.goods_count as goodsCount  ,o.server_name as serverName,o.person_count as personCount,o.table_name as tableName,o.order_status as orderStatus,o.pay_method as payMethod ");
        sql.append(" ,DATE_FORMAT(o.last_update_time,'%Y-%m-%d %T') as lastUpdateTime ");
        sql.append("  from tb_kc_order_detail de JOIN tb_kc_order o on o.order_id=de.order_id  where de.status=1 ");
        if (StringUtils.isNotEmpty(conditionCommon.getOrderNo())) {
            sql.append(" and o.order_num='").append(conditionCommon.getOrderNo().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getUserPhone())) {
            sql.append(" and (o.user_phone='").append(conditionCommon.getUserPhone().trim()).append("' or o.order_num='").append(conditionCommon.getUserPhone().trim()).append("') ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getTableName())) {
            sql.append(" and o.table_name='").append(conditionCommon.getTableName().trim()).append("' ");
        }
        sql.append(" ORDER BY de.last_update_time desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }


    @Override
    public TbKcOrder loadOrder(String orderNo) {
        String hql = " from TbKcOrder where orderNum=?1  ";
        return iGeneralDao.queryUniqueByHql(hql, orderNo);
    }

    @Override
    public TbKcOrder loadOrder(int orderId) {
        String hql = " from TbKcOrder where orderId=?1  ";
        return iGeneralDao.queryUniqueByHql(hql, orderId);
    }

    @Override
    public List<TbKcOrderDetail> loadOrderDetailList(int orderId) {
        String hql = " from TbKcOrderDetail where orderId=?1 and status=1 ";
        return (List<TbKcOrderDetail>) iGeneralDao.queryListByHql(hql, orderId);
    }

    @Override
    public int saveObj(Object obj) {
        return iGeneralDao.save(obj);
    }

    @Override
    public void updateObj(Object obj) {
        iGeneralDao.saveOrUpdate(obj);
    }

    @Override
    public void deleteDetailById(int detailId) {
        String sql = " update  tb_kc_order_detail set status=0 where detail_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, detailId);
    }

    @Override
    public boolean updateOutTimeDetail(Integer orderId, String goodsCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select de.detail_id,de.goods_id from tb_kc_order_detail de  join tb_kc_order o on o.order_id = de.order_id ");
        sql.append(" where ");
        if (null != orderId) {
            sql.append(" de.order_id = ").append(orderId).append(" ");
        }
        if (StringUtils.isNotEmpty(goodsCode)) {
            sql.append(" de.goods_code = '").append(goodsCode).append("' ");
        }
        sql.append(" and o.order_status = 0 ");
        sql.append(" and de.is_discounts = 1 ");
        sql.append(" and de.`status` = 1 ");
        sql.append(" and de.discounts_end_time < NOW() ");

        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        if (!CollectionUtils.isEmpty(list)) {
            // 商品已经结束，按正常商品添加，并修改为正常商品
            for (int i = 0; i < list.size(); i++) {
                int goodsId = Integer.parseInt(list.get(i).get("goods_id").toString());
                // 查询商品 TbKcGoods goods
                String hql = " from TbKcGoods where goodsId=?1 ";
                TbKcGoods goods = iGeneralDao.queryUniqueByHql(hql, goodsId);
                if (goods != null) {
                    int detailId = Integer.parseInt(list.get(i).get("detail_id").toString());
                    BigDecimal salePrice = goods.getSalePrice();
                    // 恢复原价
                    String updateSql = " update tb_kc_order_detail set sale_price_true=?,discounts_price=?,is_discounts=0,discounts_start_time=null,discounts_end_time=null where `status`=1 and detail_id=? ";
                    iGeneralDao.executeUpdateForSQL(updateSql, salePrice,salePrice, detailId);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> findOrderList(int orderId) {
        return iGeneralDao.getBySQLListMap(KcOrderDaoImpl.findOrderListSql(orderId));
    }

    @Override
    public void updateKcOrder(TbKcOrder order) {
        String sql = " update tb_kc_order set order_price=?,real_price=?,discounts_price=?,discounts_member_price=?,goods_count=? where order_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, order.getOrderPrice(), order.getRealPrice(), order.getDiscountsPrice(), order.getDiscountsMemberPrice(), order.getGoodsCount(), order.getOrderId());
    }
}
