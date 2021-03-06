package com.yufan.kc.dao.store.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbStoreInout;
import com.yufan.kc.dao.store.StoreInOutDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/29 21:40
 * 功能介绍:
 */
@Transactional
@Repository
public class StoreInOutDaoImpl implements StoreInOutDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadStoreInOutPage(ConditionCommon conditionCommon) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(getSql(conditionCommon));
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    private String getSql(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        if (conditionCommon.getSearchType() != null && conditionCommon.getSearchType() == 1) {
            // 相同商品汇总显示
            sql.append(" select 0 as income_id,ins.supplier_code,ins.goods_code,ins.shop_code,ins.goods_name,ins.goods_unit,CONCAT('≈',round(AVG(ins.income_price),2)) as income_price,count(ins.unit_count) as unit_count,ins.income_type, ");
        } else {
            sql.append(" select ins.income_id,ins.supplier_code,ins.goods_code,ins.shop_code,ins.goods_name,ins.goods_unit,ins.income_price,ins.unit_count,ins.income_type, ");
        }
        sql.append(" DATE_FORMAT(ins.make_day,'%Y-%m-%d') as make_day_name,ins.make_day,DATE_FORMAT(ins.effect_to_time,'%Y-%m-%d') as effect_to_time_name,ins.effect_to_time,ins.effect_day,ins.status, ");
        sql.append(" DATE_FORMAT(ins.create_time,'%Y-%m-%d %T') as create_time_name,ins.create_time,ins.create_user_name,ins.is_matching,ins.remark,ins.classify_code, ");
        sql.append(" DATE_FORMAT(ins.in_time,'%Y-%m-%d %T') as in_time_name,ins.in_time,DATE_FORMAT(ins.out_time,'%Y-%m-%d %T') as out_time_name,ins.out_time,ins.shop_code_date ");
        sql.append(" ,cass.classify_name,su.supplier_name,p.param_value as goods_unit_name ");
        sql.append(" from tb_store_inout ins LEFT JOIN tb_classify cass on cass.classify_code=ins.classify_code and cass.status=1 ");
        sql.append(" LEFT JOIN tb_supplier su on su.supplier_code=ins.supplier_code and su.status=1 ");
        sql.append(" LEFT JOIN tb_param p on p.param_code='goods_unit' and p.param_key=ins.goods_unit and p.`status`=1 ");
        sql.append(" where ins.`status` !=2 ");

        if (StringUtils.isNotEmpty(conditionCommon.getSupplierCode())) {
            sql.append(" and su.supplier_code='").append(conditionCommon.getSupplierCode().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getClassifyCode())) {
            sql.append(" and cass.classify_code='").append(conditionCommon.getClassifyCode().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getGoodsCode())) {
            sql.append(" and (ins.shop_code like '%").append(conditionCommon.getGoodsCode().trim()).append("%' or ins.goods_code like '%").append(conditionCommon.getGoodsCode().trim()).append("%') ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getGoodsName())) {
            sql.append(" and ins.goods_name like '%").append(conditionCommon.getGoodsName().trim()).append("%' ");
        }
        if (null != conditionCommon.getIncomeType() && conditionCommon.getIncomeType() > -1) {
            sql.append(" and ins.income_type=").append(conditionCommon.getIncomeType()).append(" ");
        }
        if (null != conditionCommon.getStatus() && conditionCommon.getStatus() > -1) {
            sql.append(" and ins.status=").append(conditionCommon.getStatus()).append(" ");
        }
        if (null != conditionCommon.getIsMatching() && conditionCommon.getIsMatching() > -1) {
            sql.append(" and ins.is_matching=").append(conditionCommon.getIsMatching()).append(" ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getGoodsUnit())) {
            sql.append(" and ins.goods_unit='").append(conditionCommon.getGoodsUnit().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(conditionCommon.getShopCodeDate())) {
            sql.append(" and ins.shop_code_date ='").append(conditionCommon.getShopCodeDate()).append("' ");
        }
        if (null != conditionCommon.getUnitCount()) {
            sql.append(" and ins.unit_count =").append(conditionCommon.getUnitCount()).append(" ");
        }
        if (conditionCommon.getSearchType() != null && conditionCommon.getSearchType() == 1) {
            // 相同商品汇总显示
            sql.append(" GROUP BY ins.goods_code ");
        }
        sql.append(" order by ins.last_update_time desc,ins.goods_code,ins.in_time desc ");
        return sql.toString();
    }

    @Override
    public void saveStoreInOut(TbStoreInout storeInout) {
        iGeneralDao.save(storeInout);
    }

    @Override
    public void deleteStoreInOut(int id) {
        String sql = " update tb_store_inout set status=2 where income_id = ? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public void updateStoreInOut(TbStoreInout storeInout) {
        iGeneralDao.saveOrUpdate(storeInout);
    }

    @Override
    public Map<String, Object> findStoreInOutInfo(ConditionCommon conditionCommon) {
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(getSql(conditionCommon));
        if (!CollectionUtils.isEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> findStoreInOutList(ConditionCommon conditionCommon) {
        return iGeneralDao.getBySQLListMap(getSql(conditionCommon));
    }

    @Override
    public void batchSureStore(String incomeIds, Integer incomeType) {
        if(incomeIds.endsWith(",")){
            incomeIds = incomeIds.substring(0,incomeIds.length()-1);
        }
        StringBuffer sql = new StringBuffer();
        if (incomeType == 0) {
            // 出库
            sql.append(" update tb_store_inout set status=1, income_type=").append(incomeType).append(",out_time=now() where income_id  in (").append(incomeIds).append(") ");
        } else {
            // 入库
            sql.append(" update tb_store_inout set status=1, income_type=").append(incomeType).append(",in_time=now() where income_id  in (").append(incomeIds).append(") ");
        }
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public void updateStoreMatching(String goodsCode, int matching) {
        String sql = " update tb_store_inout set is_matching=? where goods_code=? ";
        iGeneralDao.executeUpdateForSQL(sql, matching, goodsCode);
    }

    @Override
    public void updateStoreMatching2(String goodsIds, int matching) {
        if(goodsIds.endsWith(",")){
            goodsIds = goodsIds.substring(0,goodsIds.length()-1);
        }
        String sql = " update tb_store_inout set is_matching=" + matching + " where goods_code in (select goods_code from tb_kc_goods where goods_id in (" + goodsIds + ")) ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public Map<String, Object> findOneStoreByGoodsCode(String goodsCode) {
        String sql = " select ins.goods_unit,ins.unit_count from tb_store_inout ins where ins.goods_code=? ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, goodsCode);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean checkShopNoOut(String shopNo) {
        String sql = " select de.detail_id,de.shop_code from tb_kc_order_detail de where de.shop_code=? and de.status=1 ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, shopNo);
        if (!CollectionUtils.isEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteGoods(int inoutId) {
        String sql = " delete from tb_kc_goods where goods_code = (select goods_code from tb_store_inout where income_id=" + inoutId + ") ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public boolean checkGoodsExist(String goodsCode) {

        String sql = " select g.goods_id from tb_kc_goods g where g.goods_code=? ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, goodsCode);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
            return true;
        }
        return false;
    }
}
