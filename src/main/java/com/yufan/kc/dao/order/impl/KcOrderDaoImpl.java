package com.yufan.kc.dao.order.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.dao.order.KcOrderDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/9 21:47
 * 功能介绍:
 */
@Transactional
@Repository
public class KcOrderDaoImpl implements KcOrderDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadOrderListPage(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select kc.order_id,kc.order_num,kc.user_id,kc.goods_count,kc.order_price,kc.real_price,kc.discounts_ticket_price,kc.discounts_member_price,kc.discounts_price,kc.discounts_remark, ");
        sql.append(" kc.pay_method,kc.consume_count,kc.order_status,DATE_FORMAT(kc.create_time,'%Y-%m-%d %T') as create_time,kc.remark,kc.user_phone,kc.member_no,kc.order_source,kc.last_update_time, ");
        sql.append(" kc.server_name,kc.person_count,kc.table_name,p1.param_value as order_status_name,p2.param_value as  pay_method_name,false as _expanded ");
        sql.append(" from tb_kc_order kc  ");
        sql.append(" LEFT JOIN tb_param p1 on p1.param_code='order_status' and p1.param_key=kc.order_status ");
        sql.append(" LEFT JOIN tb_param p2 on p2.param_code='pay_method' and p2.param_key=kc.pay_method ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(conditionCommon.getOrderNo())) {
            sql.append(" and kc.order_num like '%").append(conditionCommon.getOrderNo().trim()).append("%' ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getUserPhone())) {
            sql.append(" and kc.user_phone like '%").append(conditionCommon.getUserPhone().trim()).append("%' ");
        }
        if (conditionCommon.getPayMethod() != null) {
            sql.append(" and kc.pay_method = ").append(conditionCommon.getPayMethod()).append(" ");
        }
        if (conditionCommon.getOrderStatus() != null) {
            sql.append(" and kc.order_status = ").append(conditionCommon.getOrderStatus()).append(" ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getTableName())) {
            sql.append(" and kc.table_name =").append(conditionCommon.getTableName()).append(" ");
        }
        sql.append(" ORDER BY kc.create_time desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> findOrderList(int orderId) {

        return iGeneralDao.getBySQLListMap(findOrderListSql(orderId));
    }

    public static String findOrderListSql(int orderId){
        StringBuffer sql = new StringBuffer();
        sql.append(" select de.detail_id,de.order_id,de.goods_id,de.buy_count,de.goods_code,de.shop_code,de.goods_name,de.sale_price_true, ");
        sql.append(" de.sale_price,de.member_price,de.discounts_price,DATE_FORMAT(de.discounts_end_time,'%Y-%m-%d') as discounts_end_time, ");
        sql.append(" DATE_FORMAT(de.discounts_start_time,'%Y-%m-%d') as discounts_start_time, ");
        sql.append(" de.goods_unit_name,if(de.is_discounts = 1,'是','否') as is_discounts_name, ");
        sql.append(" de.unit_count,de.is_discounts,de.create_time  ");
        sql.append(" ,o.user_phone,o.order_status,o.goods_count,(o.order_price-o.real_price) as discounts_price_all,o.real_price,o.order_price,o.discounts_price as discounts_price_order ");
        sql.append(" ,o.discounts_member_price,o.discounts_ticket_price ");
        sql.append(" from tb_kc_order_detail de join tb_kc_order o on o.order_id = de.order_id ");
        sql.append(" where de.order_id=").append(orderId).append(" and de.status=1 ");
        sql.append("  order by de.goods_code,de.last_update_time desc ");
        return sql.toString();
    }
}
