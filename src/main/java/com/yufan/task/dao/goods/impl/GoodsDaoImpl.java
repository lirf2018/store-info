package com.yufan.task.dao.goods.impl;

import com.yufan.bean.GoodsCondition;
import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.pojo.TbOrderCart;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        sql.append(" SELECT sku_id,goods_id,sku_name,true_money as sku_true_money,now_money as sku_now_money,sku_code,prop_code,sku_num,sku_img,purchase_price as sku_purchase_price from tb_goods_sku where `status`=1 ");
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

    @Override
    public PageInfo loadGoodsList(GoodsCondition goodsCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT g.goods_id,g.title,g.goods_name,g.true_money,g.now_money,CONCAT('" + Constants.IMG_WEB_URL + "',g.goods_img) as goods_img,IFNULL(g.sell_count,0) as sell_count,g.is_single ");
        sql.append(" ,IFNULL(sku.sku_now_money,0)  as sku_now_money ");
        sql.append(" from tb_goods g ");
        sql.append(" LEFT JOIN (SELECT goods_id,group_concat(now_money ORDER BY now_money ) as sku_now_money from tb_goods_sku where `status`=1  GROUP BY goods_id) sku on sku.goods_id=g.goods_id ");
        sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id ");
        if (null != goodsCondition.getPropId()) {
            sql.append(" JOIN tb_goods_attribute ga on ga.goods_id=g.goods_id and ga.prop_id=").append(goodsCondition.getPropId()).append(" ");
        }
        sql.append(" where NOW()>=g.start_time and NOW()<=g.end_time and g.`status`=1 and g.is_putaway=2 and s.`status`=1 ");
        if (null != goodsCondition.getGoodsName() && StringUtils.isNotEmpty(goodsCondition.getGoodsName().trim())) {
            sql.append(" and g.goods_name like '").append(goodsCondition.getGoodsName().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(goodsCondition.getCategoryIds())) {
            sql.append(" and g.category_id in (").append(goodsCondition.getCategoryIds()).append(") ");
        }
        if (StringUtils.isNotEmpty(goodsCondition.getLevelIds())) {
            sql.append(" and (g.level_id in (").append(goodsCondition.getLevelIds()).append(") or g.goods_id in (select goods_id from tb_goods where category_id in (SELECT ca.category_id from tb_category ca JOIN tb_level_category_rel rel on rel.category_id=ca.category_id where level_id in (").append(goodsCondition.getLevelIds()).append(")))) ");
        }
        if ("new".equals(goodsCondition.getType())) {
            sql.append(" ORDER BY g.createtime desc,g.goods_id desc ");
        } else if ("hot".equals(goodsCondition.getType())) {
            sql.append(" ORDER BY g.sell_count desc,g.goods_id desc ");
        } else {
            sql.append(" ORDER BY g.data_index desc,g.goods_id desc ");
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(goodsCondition.getPageSize() == null ? 20 : goodsCondition.getPageSize());
        pageInfo.setCurrePage(goodsCondition.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public PageInfo loadTimeGoodsList(GoodsCondition goodsCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tg.id,tg.goods_id,tg.time_price,tg.goods_store ");
        sql.append(" ,g.title,g.goods_name,g.true_money,CONCAT('" + Constants.IMG_WEB_URL + "',g.goods_img) as goods_img,IFNULL(g.sell_count,0) as sell_count ");
        sql.append(" from tb_time_goods tg ");
        sql.append(" JOIN tb_goods g on g.goods_id=tg.goods_id ");
        sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id ");
        sql.append(" where tg.`status`=1 and tg.begin_time<=NOW() and tg.end_time>=NOW() ");
        sql.append(" and NOW()>=g.start_time and NOW()<=g.end_time and g.`status`=1 and g.is_putaway=2 and s.`status`=1 ");
        if (null != goodsCondition.getGoodsName() && StringUtils.isNotEmpty(goodsCondition.getGoodsName().trim())) {
            sql.append(" and g.goods_name like '").append(goodsCondition.getGoodsName().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(goodsCondition.getCategoryIds())) {
            sql.append(" and g.category_id in (").append(goodsCondition.getCategoryIds()).append(") ");
        }
        if (StringUtils.isNotEmpty(goodsCondition.getLevelIds())) {
            sql.append(" and (g.level_id in (").append(goodsCondition.getLevelIds()).append(") or g.goods_id in (select goods_id from tb_goods where category_id in (SELECT ca.category_id from tb_category ca JOIN tb_level_category_rel rel on rel.category_id=ca.category_id where level_id in (").append(goodsCondition.getLevelIds()).append(")))) ");
        }
        sql.append(" ORDER BY tg.weight,tg.id desc DESC ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(goodsCondition.getPageSize() == null ? 20 : goodsCondition.getPageSize());
        pageInfo.setCurrePage(goodsCondition.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> mainGoodsListMap(int size, String type) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT g.goods_id,g.title,g.goods_name,g.true_money,g.now_money,CONCAT('" + Constants.IMG_WEB_URL + "',g.goods_img) as goods_img,IFNULL(g.sell_count,0) as sell_count,g.is_single ");
        sql.append(" from tb_goods g ");
        sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id ");
        sql.append(" where NOW()>=g.start_time and NOW()<=g.end_time and g.`status`=1 and g.is_putaway=2 and s.`status`=1 ");
        if ("new".equals(type)) {
            sql.append(" ORDER BY g.createtime desc,g.goods_id desc ");
        } else if ("hot".equals(type)) {
            sql.append(" ORDER BY g.sell_count desc,g.goods_id desc ");
        } else {
            sql.append(" ORDER BY g.data_index desc,g.goods_id desc ");
        }
        sql.append(" LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> mainTimeGoodsListMap(int size) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tg.id,tg.goods_id,tg.time_price,tg.goods_store ");
        sql.append(" ,g.title,g.goods_name,g.true_money,CONCAT('" + Constants.IMG_WEB_URL + "',g.goods_img) as goods_img,IFNULL(g.sell_count,0) as sell_count ");
        sql.append(" from tb_time_goods tg ");
        sql.append(" JOIN tb_goods g on g.goods_id=tg.goods_id ");
        sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id ");
        sql.append(" where tg.`status`=1 and tg.begin_time<=NOW() and tg.end_time>=NOW() ");
        sql.append(" and NOW()>=g.start_time and NOW()<=g.end_time and g.`status`=1 and g.is_putaway=2 and s.`status`=1 ");
        sql.append(" ORDER BY tg.weight,tg.id desc DESC ");
        sql.append(" LIMIT 0,").append(size).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryUserOrderCart(int userId, Integer goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT c.cart_id,c.goods_id,c.goods_spec,c.goods_count,c.goods_price,c.goods_spec_name_str from tb_order_cart c JOIN tb_goods g on g.goods_id=c.goods_id and c.createtime>=g.lastaltertime ");
        sql.append(" where c.`status`=1 AND c.user_id=").append(userId).append(" ");
        sql.append(" and c.goods_id=").append(goodsId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryUserOrderCart(int userId, Integer goodsId, String propCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT c.cart_id,c.goods_id,c.goods_spec,c.goods_count,c.goods_price,c.goods_spec_name_str from tb_order_cart c JOIN tb_goods g on g.goods_id=c.goods_id and c.createtime>=g.lastaltertime ");
        sql.append(" where c.`status`=1 AND c.user_id=").append(userId).append(" and goods_spec='").append(propCode).append("' ");
        sql.append(" and c.goods_id=").append(goodsId).append(" ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void saveOrderCart(TbOrderCart orderCart) {
        iGeneralDao.save(orderCart);
    }

    @Override
    public void updateOrderCart(int cartId, int goodsCount, String goodsSpec, String goodsSpecName, String goodsSpecNameStr, BigDecimal goodsPrice, BigDecimal trueMoney) {
        String sql = " update tb_order_cart set goods_spec=?,goods_spec_name=?,goods_count=?,goods_price=?,goods_spec_name_str=?,true_money=? where cart_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, goodsSpec, goodsSpecName, goodsCount, goodsPrice, goodsSpecNameStr, trueMoney, cartId);
    }

    @Override
    public TbGoodsSku loadGoodsSku(int goodsId, String propCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" from  TbGoodsSku where goodsId=?1 and propCode=?2 and status=?3 ");
        return iGeneralDao.queryUniqueByHql(sql.toString(), goodsId, propCode, Constants.DATA_STATUS_YX);
    }

    @Override
    public TbGoodsSku loadGoodsSkuBySkuId(int skuId) {
        String sql = " from TbGoodsSku where skuId=? ";
        return iGeneralDao.queryUniqueByHql(sql, skuId);
    }

    @Override
    public TbTimeGoods loadTimeGoods(int timeGoodsId) {
        String sql = " from TbTimeGoods where id=? ";
        return iGeneralDao.queryUniqueByHql(sql, timeGoodsId);
    }

    @Override
    public List<Map<String, Object>> queryTbGoodsListMapByGoodsIds(String goodsIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select goods_id,goods_name,title,goods_img,true_money,now_money,shop_id,is_yuding,get_way,is_invoice,is_putaway,data_index,category_id,property,is_single,goods_num,is_return,coupon_id,goods_type,is_pay_online,out_code, ");
        sql.append(" deposit_money,peisong_zc_desc,peisong_pei_desc,purchase_price,is_time_goods,limit_num,limit_way, ");
        sql.append(" DATE_FORMAT(limit_begin_time,'%Y-%m-%d %T') as limit_begin_time,level_id,advance_price,bar_code,bar_code_shop,sell_count ");
        sql.append(" from tb_goods where `status`=1 and NOW()>start_time and NOW()<end_time and goods_id in (").append(goodsIds).append(") ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryTbGoodsSkuListMapBySkuIds(String skuIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT sku_id,goods_id,sku_name,true_money,now_money,sku_code,prop_code,sku_num,sku_img,purchase_price,shop_id,sell_count,status from tb_goods_sku where `status`=1 and sku_id in (").append(skuIds).append(") ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
