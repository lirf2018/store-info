package com.yufan.dao.goods.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.dao.goods.IGoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 12:01
 * 功能介绍:
 */
@Transactional
@Repository
public class GoodsDaoImpl implements IGoodsDao {


    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public List<Map<String, Object>> queryGoodsSkuListMap(int goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT sku_id,goods_id,sku_name,true_money,now_money,sku_code,prop_code,sku_num,sku_img,purchase_price from tb_goods_sku where `status`=1 ");
        sql.append(" and goods_id=").append(goodsId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryTimeGoodsListMap(Integer goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  tg.id,tg.goods_id,DATE_FORMAT(tg.begin_time,'%Y-%m-%d %T') as begin_time,DATE_FORMAT(tg.end_time,'%Y-%m-%d %T') as end_time,tg.time_price,tg.goods_store, ");
        sql.append(" g.goods_name,g.sell_count,g.now_money ");
        sql.append(" from tb_time_goods tg JOIN tb_goods g on g.goods_id = tg.goods_id ");
        sql.append(" where tg.status=1 ");
        if (goodsId != null) {
            sql.append(" and tg.goods_id=").append(goodsId).append(" ");
        }
        sql.append(" ORDER BY tg.weight desc,tg.id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
