package com.yufan.kc.dao.timetask.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.dao.timetask.TimeTaskDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/20 9:43
 * 功能介绍:
 */
@Transactional
@Repository
public class TimeTaskDaoImpl implements TimeTaskDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public void updateGoodsStore(String goodsCode) {
        String delTempSql = " delete from tb_temp where tmp_type=1 ";
        iGeneralDao.executeUpdateForSQL(delTempSql);
        //
        String uuid = UUID.randomUUID() + "" + System.currentTimeMillis();
        uuid = uuid.replace("-", "");
        // 插入临时表
        StringBuffer sqlOut = new StringBuffer();
        sqlOut.append(" INSERT into tb_temp(goods_code,sale_count,uuid_key,tmp_type) (select de.goods_code,count(de.goods_code) as c,'").append(uuid).append("',1 ");
        sqlOut.append("  from tb_kc_order o JOIN  ");
        sqlOut.append(" tb_kc_order_detail de on de.order_id=o.order_id ");
        sqlOut.append(" where o.order_status>0 and de.`status`=1 ");
        if (StringUtils.isNotEmpty(goodsCode)) {
            sqlOut.append(" and de.goods_code='").append(goodsCode).append("' ");
        }
        sqlOut.append(" GROUP BY de.goods_code)  ");
        iGeneralDao.executeUpdateForSQL(sqlOut.toString());

        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_kc_goods_store gs join ");
        sql.append(" (select count(i.goods_code) as c,i.goods_code from tb_store_inout i where i.income_type=1 and i.`status`=1  ");
        if (StringUtils.isNotEmpty(goodsCode)) {
            sql.append(" and i.goods_code='").append(goodsCode).append("' ");
        }
        sql.append(" and i.is_matching=1 GROUP BY ");
        sql.append(" i.goods_code) s on s.goods_code=gs.goods_code  JOIN tb_temp t on t.goods_code=gs.goods_code and t.uuid_key='").append(uuid).append("' ");
        sql.append(" set gs.store=s.c-t.sale_count,gs.update_type=0,last_update_time=now() ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    /**
     * 删除商品报表
     *
     * @param month
     */
    @Override
    public void deleteGoodsSaleReport(String month) {
        String sql = " delete from tb_goods_sale_month_report  ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public List<Map<String, Object>> findOrderPayDateMonth() {
        String sql = " select DATE_FORMAT(o.pay_date,'%Y-%m') as pay_month from tb_kc_order o where o.order_id>0 GROUP BY DATE_FORMAT(o.pay_date,'%Y-%m') ";
        return iGeneralDao.getBySQLListMap(sql);
    }

    @Override
    public void goodsSaleReport(String month) {
        // 2020-01
        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT into tb_goods_sale_month_report(goods_id,goods_code,goods_name,sale_count,sale_price_all,update_time,sale_month,income_price_all,income_count) ");
        sql.append(" select de.goods_id,de.goods_code,de.goods_name,count(de.goods_code) as c,SUM(de.sale_price_true) as price_all,now(),'").append(month).append("-01',0,0 from tb_kc_order o  ");
        sql.append(" join tb_kc_order_detail de on de.order_id=o.order_id  ");
        sql.append(" where o.order_status>0 and de.status=1  ");
        sql.append(" and  DATE_FORMAT(o.pay_date,'%Y-%m')='").append(month).append("' ");
        sql.append(" GROUP BY de.goods_code  ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public void goodsStoreInPriceAll(String month) {
        // 2020-01
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_goods_sale_month_report gs JOIN ");
        sql.append(" (select SUM(i.income_price) as income_price_all,i.goods_code,count(i.goods_code) as income_count_all from tb_store_inout i where i.income_type=1 and i.`status`=1  ");
        sql.append(" and DATE_FORMAT(i.in_time,'%Y-%m')='").append(month).append("' ");
        sql.append(" GROUP BY i.goods_code) s on s.goods_code= gs.goods_code ");
        sql.append(" set gs.income_price_all = s.income_price_all,gs.income_count=s.income_count_all ,gs.update_time=NOW() ");
        sql.append(" where DATE_FORMAT(gs.sale_month,'%Y-%m')='").append(month).append("' ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }


    @Override
    public void deleteAndInitOrderReport(String year) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_kc_order_month_report ");
        sql.append(" where DATE_FORMAT(sale_month,'%Y')='").append(year).append("' ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
        // 初始化本年度订单数据
        for (int i = 1; i <= 12; i++) {
            String month = year + "-" + i + "-01";
            String sqlAdd = " INSERT INTO tb_kc_order_month_report(sale_month,order_price_all,update_time,goods_inprice_all) VALUES('" + month + "',0,NOW(),0); ";
            iGeneralDao.executeUpdateForSQL(sqlAdd);
        }
    }

    @Override
    public void orderSaleReport(String year) {
        // 2020-01
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_kc_order_month_report os join ");
        sql.append(" (select SUM(o.real_price) as price_all ,DATE_FORMAT(o.pay_date,'%Y-%m') as  pay_date_month from tb_kc_order o where o.order_status>0 ");
        sql.append(" GROUP BY DATE_FORMAT(o.pay_date,'%Y-%m')) s on s.pay_date_month=DATE_FORMAT(os.sale_month,'%Y-%m') ");
        sql.append(" set os.order_price_all=s.price_all,os.update_time=now() ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public void goodsInPriceAllOrder(String year) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_kc_order_month_report os join ");
        sql.append(" (select SUM(i.income_price) as income_price_all,DATE_FORMAT(i.in_time,'%Y-%m') as in_time from tb_store_inout i  ");
        sql.append(" where i.income_type=1 and i.`status`=1 and i.is_matching=1  ");
        sql.append(" GROUP BY DATE_FORMAT(i.in_time,'%Y-%m')) s on s.in_time=DATE_FORMAT(os.sale_month,'%Y-%m') ");
        sql.append(" set os.goods_inprice_all=s.income_price_all,os.update_time=now() ");


        iGeneralDao.executeUpdateForSQL(sql.toString());
    }
}
