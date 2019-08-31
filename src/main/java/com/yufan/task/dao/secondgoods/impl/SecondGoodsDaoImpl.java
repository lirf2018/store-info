package com.yufan.task.dao.secondgoods.impl;

import com.yufan.bean.GoodsCondition;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.secondgoods.ISecondGoodsDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/31 20:32
 * 功能介绍:
 */
@Transactional
@Repository
public class SecondGoodsDaoImpl implements ISecondGoodsDao {

    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public void UpdateSecondGoodsReadCount(int goodsId) {
        String sql = " UPDATE tb_second_goods set read_num=read_num+1 where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, goodsId);
    }

    @Override
    public PageInfo loadGoodsList(GoodsCondition condition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,goods_name,goods_img,now_price,read_num from tb_second_goods where status=1 ");
        if (StringUtils.isNotEmpty(condition.getGoodsName())) {
            sql.append(" and goods_name like '%").append(condition.getGoodsName().trim()).append("%' ");
        }
        sql.append(" ORDER BY data_index desc,read_num desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(condition.getPageSize() == null ? 20 : condition.getPageSize());
        pageInfo.setCurrePage(condition.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }
}
