package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.task.dao.goods.IGoodsDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/15 20:57
 * 功能介绍: 校验商品
 */
@Service("check_goods")
public class CheckGoods implements IResultOut {

    private Logger LOG = Logger.getLogger(CheckGoods.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            //{"user_id":1, "time_goods_id":0,"goods_list":{"goods_id":100,"goods_count":10,"sku_id":0}}
            Integer userId = data.getInteger("user_id");
            Integer timeGoodsId = data.getInteger("time_goods_id");
            JSONArray goodsList = data.getJSONArray("goods_list");

            String goodsIds = "";
            String skuIds = "";
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                if (null != skuId && skuId > 0) {
                    skuIds = skuIds + skuId + ",";
                }
                goodsIds = goodsIds + goodsId + ",";
            }
            Map<Integer, Map<String, Object>> goodsMap = new HashMap<>();//商品
            Map<String, Map<String, Object>> goodsSkuMap = new HashMap<>();//商品sku
            //查询商品列表
            if (StringUtils.isNotEmpty(goodsIds)) {
                goodsIds = goodsIds.substring(0, goodsIds.length() - 1);
                List<Map<String, Object>> list = iGoodsDao.queryTbGoodsListMapByGoodsIds(goodsIds);//查询商品列表
                for (int i = 0; i < list.size(); i++) {
                    int goodsId = Integer.parseInt(list.get(i).get("goods_id").toString());
                    goodsMap.put(goodsId, list.get(i));
                }

            }
            //查询商品sku列表
            if (StringUtils.isNotEmpty(skuIds)) {
                skuIds = skuIds.substring(0, skuIds.length() - 1);
                List<Map<String, Object>> list = iGoodsDao.queryTbGoodsSkuListMapBySkuIds(skuIds);//查询商品sku列表
                for (int i = 0; i < list.size(); i++) {
                    int goodsId = Integer.parseInt(list.get(i).get("goods_id").toString());
                    int skuId = Integer.parseInt(list.get(i).get("sku_id").toString());
                    goodsSkuMap.put(goodsId + "-" + skuId, list.get(i));
                }
            }

            LOG.info("---开始校验---");
            //判断库存
            if (timeGoodsId != null && timeGoodsId > 0) {
                LOG.info("------抢购商品-------");
                Integer goodsCount = goodsList.getJSONObject(0).getInteger("goods_count");
                TbTimeGoods timeGoods = iGoodsDao.loadTimeGoods(timeGoodsId);
                if (goodsCount > timeGoods.getGoodsStore()) {
                    LOG.info("------抢购商品-----库存不足-------");
                    return packagMsg(ResultCode.STORE_EMPTY.getResp_code(), dataJson);
                }
                //开始时间
                Timestamp beginTime = timeGoods.getBeginTime();//抢购开始时间
                Timestamp endTime = timeGoods.getEndTime();//结束时间
                long nowTime = System.currentTimeMillis();
                if (!(nowTime >= beginTime.getTime() && nowTime <= endTime.getTime())) {
                    LOG.info("-------未到抢购开始时间---------timeGoodsId: " + timeGoodsId);
                    return packagMsg(ResultCode.OUT_TIME_FIND.getResp_code(), dataJson);
                }

                //限购方式  限购方式: 1.每天一次2每月一次3.每年一次4不限购5只允许购买一次
                Timestamp limitBeginTime = timeGoods.getLimitBeginTime();
                int limitWay = timeGoods.getTimeWay();
                int limitNum = timeGoods.getLimitNum();

            }
            LOG.info("---开始校验商品---");
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer goodsCount = goodsList.getJSONObject(i).getInteger("goods_count");
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");

                Map<String, Object> goods = goodsMap.get(goodsId);//商品信息
                if (goods == null) {
                    LOG.info("-----查询商品不存在----goodsId: " + goodsId);
                    return packagMsg(ResultCode.GOODSINFO_NOT_EXIST.getResp_code(), dataJson);
                }
                int isSingle = Integer.parseInt(goods.get("is_single").toString());
                if (isSingle == 0 && (skuId == null || skuId == 0)) {
                    LOG.info("------商品信息有误-----goodsId: " + goodsId);
                    return packagMsg(ResultCode.GOODSINFO_NOT_EXIST.getResp_code(), dataJson);
                }
                if (skuId != null && skuId > 0) {
                    Map<String, Object> goodsSku = goodsSkuMap.get(goodsId + "-" + skuId);
                    if (null == goodsSku) {
                        LOG.info("------商品sku信息有误-----goodsId: " + goodsId + "   skuId:" + skuId);
                        return packagMsg(ResultCode.GOODSINFO_NOT_EXIST.getResp_code(), dataJson);
                    }
                    int skuNum = Integer.parseInt(goodsSku.get("sku_num").toString());
                    if (goodsCount > skuNum) {
                        LOG.info("-------商品sku库存不足---------goodsId: " + goodsId + "   skuId:" + skuId + "  skuNum:" + skuNum);
                        return packagMsg(ResultCode.GOODS_STORE_NOENOUGH.getResp_code(), dataJson);
                    }
                }

                int goodsNum = Integer.parseInt(goods.get("goods_num").toString());
                if (goodsCount > goodsNum) {
                    LOG.info("-------商品库存不足---------goodsId: " + goodsId + "  goodsNum:" + goodsNum);
                    return packagMsg(ResultCode.GOODS_STORE_NOENOUGH.getResp_code(), dataJson);
                }

                //限购方式  限购方式: 1.每天一次2每月一次3.每年一次4不限购5只允许购买一次
                String limitBeginTime = String.valueOf(goods.get("limit_begin_time"));
                int limitWay = Integer.parseInt(goods.get("limit_way").toString());
                int limitNum = Integer.parseInt(goods.get("limit_num").toString());


            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-----error-----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            JSONArray goodsList = data.getJSONArray("goods_list");
            if (null == goodsList || goodsList.size() == 0 || userId == null || userId == 0) {
                return false;
            }
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer goodsCount = goodsList.getJSONObject(i).getInteger("goods_count");
                Integer timeGoodsId = data.getInteger("time_goods_id");
                if (goodsId == null || goodsId == 0 || goodsCount == null || goodsCount == 0) {
                    return false;
                }
                if (timeGoodsId != null && timeGoodsId > 0 && goodsList.size() > 1) {
                    LOG.info("---------抢购商品goodsList有误----------");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
