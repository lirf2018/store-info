package com.yufan.kc.dao.discounts.impl;

import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.kc.bean.TbDiscountsConfig;
import com.yufan.kc.dao.discounts.DiscountsDao;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 12:10
 * 功能介绍:
 */
public class DiscountsDaoImpl implements DiscountsDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadDiscountsConfigPage(ConditionCommon conditionCommon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select dis.discounts_id,dis.discounts_code,dis.discounts_desc,dis.goods_id,dis.discounts_type,dis.discounts_price, ");
        sql.append(" dis.discounts_percent,dis.enough_money,dis.subtract_money,dis.status,dis.pass_time,dis.create_time from tb_discounts_config dis where 1=1 ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionCommon.getPageSize() == null ? 20 : conditionCommon.getPageSize());
        pageInfo.setCurrePage(conditionCommon.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void saveDiscountsConfig(TbDiscountsConfig discountsConfig) {
        iGeneralDao.save(discountsConfig);
    }

    @Override
    public void deleteDiscountsConfig(int id) {
        String sql = " delete from tb_discounts_config where discounts_id = ? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public void updateDiscountsConfig(TbDiscountsConfig discountsConfig) {
        iGeneralDao.saveOrUpdate(discountsConfig);
    }
}
