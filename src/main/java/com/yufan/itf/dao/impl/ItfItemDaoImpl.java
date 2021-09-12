package com.yufan.itf.dao.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.itf.dao.IItfItemDao;
import com.yufan.itf.service.bean.ConditionBean;
import com.yufan.pojo.ItfTbItem;
import com.yufan.pojo.ItfTbItemDetail;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/1
 */
@Transactional
@Repository
public class ItfItemDaoImpl implements IItfItemDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public ItfTbItem findItfItemInfo(ItfTbItem itfTbItem) {
        String hql = " from ItfTbItem where itemId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, itfTbItem.getItemId());
    }

    @Override
    public void saveItfItem(ItfTbItem itfTbItem) {
        iGeneralDao.save(itfTbItem);
    }

    @Override
    public void updateItfItem(ItfTbItem itfTbItem) {
        String sql = " update itf_tb_item set item_name=?,item_type=?,lastaltertime=now() where item_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, itfTbItem.getItemName(), itfTbItem.getItemType(), itfTbItem.getItemId());
    }

    @Override
    public void delItfItem(ItfTbItem itfTbItem) {
        String sql = " update itf_tb_item set status=0,lastaltertime=now() where item_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, itfTbItem.getItemId());
    }

    @Override
    public PageInfo loadItfItemPage(ConditionBean conditionBean) {
        StringBuilder sql = new StringBuilder(" select item_id,item_name,status,item_type from itf_tb_item where 1=1 and status=1 ");
//        if (null != conditionBean.getStatus()) {
//            sql.append(" and status=").append(conditionBean.getStatus()).append(" ");
//        }
        if (conditionBean.getItemType() != null && conditionBean.getItemType() >= 0) {
            sql.append(" and item_type=").append(conditionBean.getItemType()).append(" ");
        }
        sql.append(" ORDER BY lastaltertime desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(conditionBean.getPageSize() == null ? 1000 : conditionBean.getPageSize());
        pageInfo.setCurrePage(conditionBean.getCurrePage() == null ? 1 : conditionBean.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public boolean checkItfDetailExists(int itemId) {
        String sql = " select item_detail_id,item_id from itf_tb_item_detail where item_id=? and status = 1 ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, itemId);
        if (!CollectionUtils.isEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public void saveItfTbItemDetail(ItfTbItemDetail itfTbItemDetail) {
        iGeneralDao.save(itfTbItemDetail);
    }

    @Override
    public void delItfTbItemDetailById(int itemDetailId) {
        String sql = " delete from itf_tb_item_detail  where item_detail_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, itemDetailId);
    }

    @Override
    public PageInfo loadItfItemDetailPage(ConditionBean condition) {

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(condition.getPageSize() == null ? 20 : condition.getPageSize());
        pageInfo.setCurrePage(condition.getCurrePage() == null ? 1 : condition.getCurrePage());
        pageInfo.setSqlQuery(loadItfItemDetailSql(condition));
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> loadItfItemDetailReport(ConditionBean condition) {
        return iGeneralDao.getBySQLListMap(loadItfItemDetailSql(condition));
    }

    private String loadItfItemDetailSql(ConditionBean condition) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.item_detail_id,d.item_id,d.item_type,d.item_price,i.item_name,item_date,d.item_year,d.item_month  ");
        sql.append(" from itf_tb_item_detail d JOIN itf_tb_item i on d.item_id = i.item_id ");
        sql.append(" where d.STATUS=1 and i.STATUS = 1 ");
        int year = Integer.parseInt(StringUtils.isEmpty(condition.getYear()) ? "0" : condition.getYear());
        int month = Integer.parseInt(StringUtils.isEmpty(condition.getMonth()) ? "0" : condition.getMonth());
        if (year > 0) {
            sql.append(" and d.item_year = ").append(year).append("  ");
        }
        if (month > 0) {
            sql.append(" and d.item_month = ").append(month).append("  ");
        }
        sql.append(" ORDER BY d.item_date desc, d.createtime desc ");
        return sql.toString();
    }

    @Override
    public boolean checkItemName(Integer itemId, String itemName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select item_id,item_name from itf_tb_item where status = 1 and item_name='").append(itemName.trim()).append("' ");
        if (itemId != null && itemId > 0) {
            sql.append(" and item_id != ").append(itemId).append("  ");
        }
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        if (!CollectionUtils.isEmpty(list)) {
            return true;
        }
        return false;
    }
}
