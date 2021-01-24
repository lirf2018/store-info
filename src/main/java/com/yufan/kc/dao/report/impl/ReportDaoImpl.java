package com.yufan.kc.dao.report.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.dao.report.ReportDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/26 22:43
 * 功能介绍:
 */
@Transactional
@Repository
public class ReportDaoImpl implements ReportDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadGoodsReportPage(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select g.goods_name,ifnull(tb.sale_price_all,0) as sale_price_all,ifnull(tb.income_price_all,0) as income_price_all,ifnull(tb.get_price,0) as get_price  from tb_kc_goods g left join  ");
        sql.append(" (select gr.goods_code,sum(gr.sale_price_all) as sale_price_all,sum(gr.income_price_all) as income_price_all,(sum(gr.sale_price_all)-sum(gr.income_price_all)) as get_price ");
        sql.append(" from tb_goods_sale_month_report gr  ");
        sql.append(" where 1=1 ");
        if ("year".equals(conditionCommon.getReportType())) {
            sql.append(" and DATE_FORMAT(gr.sale_month,'%Y') = '").append(conditionCommon.getReportCondition()).append("'  ");
        } else if ("season".equals(conditionCommon.getReportType())) {
            String year = conditionCommon.getReportYear();
            if (Constants.JIDU_2.equals(conditionCommon.getReportCondition())) {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-04','").append(year).append("-05','").append(year).append("-06')  ");
            } else if (Constants.JIDU_3.equals(conditionCommon.getReportCondition())) {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-07','").append(year).append("-08','").append(year).append("-09')  ");
            } else if (Constants.JIDU_4.equals(conditionCommon.getReportCondition())) {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-10','").append(year).append("-11','").append(year).append("-12')  ");
            } else {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-01','").append(year).append("-02','").append(year).append("-03')  ");
            }
        } else {
            sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') = '").append(conditionCommon.getReportCondition()).append("'  ");
        }
        sql.append(" GROUP BY gr.goods_code ) tb on g.goods_code = tb.goods_code ");
        sql.append(" order by tb.sale_price_all desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> loadGoodsReportCount(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ifnull(sum(gr.sale_price_all),0) as sale_price_all,ifnull(sum(gr.income_price_all),0)  as income_price_all ");
        sql.append(" from tb_goods_sale_month_report gr  ");
        sql.append(" where 1=1 ");
        if ("year".equals(conditionCommon.getReportType())) {
            sql.append(" and DATE_FORMAT(gr.sale_month,'%Y') = '").append(conditionCommon.getReportCondition()).append("'  ");
        } else if ("season".equals(conditionCommon.getReportType())) {
            String year = conditionCommon.getReportYear();
            if (Constants.JIDU_2.equals(conditionCommon.getReportCondition())) {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-04','").append(year).append("-05','").append(year).append("-06')  ");
            } else if (Constants.JIDU_3.equals(conditionCommon.getReportCondition())) {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-07','").append(year).append("-08','").append(year).append("-09')  ");
            } else if (Constants.JIDU_4.equals(conditionCommon.getReportCondition())) {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-10','").append(year).append("-11','").append(year).append("-12')  ");
            } else {
                sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') in ('").append(year).append("-01','").append(year).append("-02','").append(year).append("-03')  ");
            }
        } else {
            sql.append(" and DATE_FORMAT(gr.sale_month,'%Y-%m') = '").append(conditionCommon.getReportCondition()).append("'  ");
        }
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String,Object>> loadOrderReportPage(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select CONCAT(monthTab.m,'月') as sale_month,IFNULL(md.order_price_all,0) as order_price_all,IFNULL(md.goods_inprice_all,0) as goods_inprice_all,  ");
        sql.append(" IFNULL((md.order_price_all-md.goods_inprice_all),0) as get_price_all ");
        sql.append(" from ( ");
        sql.append(" select mon.m from (  ");
        sql.append(" SELECT '01' as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '02'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '03'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '04'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '05'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '06'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '07'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '08'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '09'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '10'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '11'  as m from dual  ");
        sql.append(" UNION All  ");
        sql.append(" SELECT '12'  as m from dual) as mon ) monthTab  ");
        sql.append(" LEFT JOIN  ");
        sql.append(" (  ");
        sql.append(" SELECT DATE_FORMAT(orp.sale_month,'%m') as m,orp.order_price_all,orp.goods_inprice_all from tb_kc_order_month_report orp  ");
        sql.append(" where 1=1  ");
        sql.append(" and DATE_FORMAT(orp.sale_month,'%Y') = '").append(conditionCommon.getReportYear()).append("'  ");
        sql.append(" ) md on monthTab.m = md.m  ");
        sql.append(" order by monthTab.m ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

}
