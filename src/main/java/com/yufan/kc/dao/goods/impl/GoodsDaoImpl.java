package com.yufan.kc.dao.goods.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbKcGoods;
import com.yufan.kc.dao.goods.GoodsDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/2 20:30
 * 功能介绍:
 */
@Transactional
@Repository("iGoodsDao")
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadGoodsPage(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select g.goods_id,g.goods_py,g.goods_code,g.goods_name,gs.store,g.store_warning,g.sale_price,g.member_price,g.discounts_price, ");
        sql.append(" DATE_FORMAT(g.discounts_start_time,'%Y-%m-%d') as discounts_start_time_name,g.discounts_start_time,DATE_FORMAT(g.discounts_end_time,'%Y-%m-%d') as discounts_end_time_name, ");
//        sql.append(" g.discounts_end_time,if(g.`status`=1,true,false) as _disabled , ");
        sql.append(" g.discounts_end_time, ");
        sql.append(" g.status,g.goods_unit,g.unit_count,g.is_discounts,p.param_value as goods_unit_name from tb_kc_goods g  ");
        sql.append("  LEFT JOIN tb_kc_goods_store gs on gs.goods_code=g.goods_code ");
        sql.append(" LEFT JOIN tb_param p on p.param_code='goods_unit' and p.param_key=g.goods_unit and p.`status`=1  ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(conditionCommon.getGoodsName())) {
            sql.append(" and g.goods_name like '%").append(conditionCommon.getGoodsName().trim()).append("%' ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getGoodsCode())) {
            sql.append(" and g.goods_code like '%").append(conditionCommon.getGoodsCode().trim()).append("%' ");
        }
        if (null != conditionCommon.getIsDiscounts()) {
            sql.append(" and g.is_discounts=").append(conditionCommon.getIsDiscounts()).append(" ");
        }
        if (null != conditionCommon.getStatus()) {
            sql.append(" and g.status=").append(conditionCommon.getStatus()).append(" ");
        }
        sql.append(" order by g.last_update_time desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void saveGoods(TbKcGoods goods) {
        iGeneralDao.save(goods);
    }

    @Override
    public void deleteGoods(String ids) {
        if (ids.endsWith(",")) {
            ids = ids.substring(0, ids.length() - 1);
        }
        String sql = " delete from tb_kc_goods where goods_code in (select goods_code where goods_id in (" + ids + ")) ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public void updateGoods(TbKcGoods goods) {
        iGeneralDao.saveOrUpdate(goods);
    }

    @Override
    public void updateGoodsStatus(String goodsIds, int status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_kc_goods set status=").append(status).append(" ");
        sql.append(" where goods_id in(").append(goodsIds).append(") ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public PageInfo loadStoreGoodsPage(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ins.income_id,ins.goods_name,ins.goods_code,ins.goods_unit,ins.unit_count,CONCAT('≈',round(AVG(ins.income_price),2)) as income_price,p.param_value as goods_unit_name ");
        sql.append(" from tb_store_inout ins LEFT JOIN tb_param p on p.param_code='goods_unit' and p.param_key=ins.goods_unit and p.`status`=1 ");
        sql.append(" where ins.is_matching = 0 and ins.status !=2 and ins.income_type=1   ");
        if (StringUtils.isNotEmpty(conditionCommon.getGoodsCode())) {
            sql.append(" and ins.goods_code ='").append(conditionCommon.getGoodsCode()).append("' ");
        }
        sql.append(" GROUP BY ins.goods_code ORDER BY ins.in_time desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public boolean checkGoods(String goodsCode) {
        String sql = " select g.goods_code,g.goods_id from tb_kc_goods g where g.goods_code=? ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, goodsCode);
        if (null != list && list.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public PageInfo loadGoodsListPage(ConditionCommon conditionCommon) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(findGoodsListSQl(conditionCommon));
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }


    private String findGoodsListSQl(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select g.goods_id as id,g.goods_py as goodsPingYin,g.goods_code as goodsCode,g.goods_name as goodsName,g.store,g.store_warning,g.sale_price as goodsSalePrice,g.member_price as memberPrice, ");
        sql.append(" if(g.is_discounts=1,g.discounts_price,g.sale_price) as goodsDiscountsPrice,g.status,g.goods_unit,g.unit_count as unitCount,g.is_discounts,p.param_value as goodsUnitName,IFNULL(tab.buyCount,0)  as buyCount,'mouse-out-goods-ul' as style ");
        sql.append("  from tb_kc_goods g  ");
        sql.append(" left join tb_param p on p.`status`=1 and p.param_code='goods_unit' and p.param_key = g.goods_unit ");
        sql.append(" left join(select sum(de.buy_count) as buyCount,de.goods_code  from tb_kc_order_detail de join tb_kc_order o on o.order_id=de.order_id where de.status=1 and o.order_num='").append(conditionCommon.getOrderNo().trim()).append("' GROUP BY de.goods_code ) tab on tab.goods_code=g.goods_code ");
        sql.append(" where g.`status` = 1 ");
        // 添加商品--查询商品
        if (conditionCommon.getSearchAbsolute() == 1) {
            sql.append("  and (g.goods_code =  '").append(conditionCommon.getGoodsCode()).append("'  ");
            sql.append(" or g.goods_code = (select ins.goods_code from tb_store_inout ins where ins.shop_code = '").append(conditionCommon.getGoodsCode().trim()).append("'))  ");
        } else {
            if (StringUtils.isNotEmpty(conditionCommon.getGoodsCode())) {
                sql.append("  and g.goods_code like '%").append(conditionCommon.getGoodsCode()).append("%'  ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getShopCode()) && StringUtils.isNotEmpty(conditionCommon.getClassifyCode())) {
                sql.append(" and g.goods_code in (select ins.goods_code from tb_store_inout ins where ins.classify_code='").append(conditionCommon.getClassifyCode()).append("' and ins.shop_code like '%").append(conditionCommon.getShopCode().trim()).append("%')  ");
            } else if (StringUtils.isNotEmpty(conditionCommon.getShopCode())) {
                sql.append(" and g.goods_code in (select ins.goods_code from tb_store_inout ins where ins.shop_code like '%").append(conditionCommon.getShopCode().trim()).append("%')  ");
            } else if (StringUtils.isNotEmpty(conditionCommon.getClassifyCode())) {
                sql.append(" and g.goods_code in (select ins.goods_code from tb_store_inout ins where ins.classify_code='").append(conditionCommon.getClassifyCode()).append("')  ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getGoodsPy())) {
                sql.append(" and g.goods_py like '%").append(conditionCommon.getGoodsPy().trim()).append("%' ");
            }
            if (StringUtils.isNotEmpty(conditionCommon.getGoodsName())) {
                sql.append(" and g.goods_name like '%").append(conditionCommon.getGoodsName().trim()).append("%' ");
            }
            if (null != conditionCommon.getIsDiscounts() && conditionCommon.getIsDiscounts() > 0) {
                sql.append(" and g.is_discounts = ").append(conditionCommon.getIsDiscounts()).append(" ");
            }
        }

        return sql.toString();
    }

    @Override
    public Map<String, Object> findOpenOrderGoodsOne(ConditionCommon conditionCommon) {
        return null;
    }

    @Override
    public TbKcGoods loadGoods(int goodsId) {
        String hql = " from TbKcGoods where goodsId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, goodsId);
    }


}
