package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.service.impl.IBusinessService;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
    private IBusinessService iBusinessService;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            //{"user_id":1,"goods_list":{"goods_id":100, "time_goods_id":0,"buy_count":10,"sku_id":0}}
            Integer userId = data.getInteger("user_id");
            JSONArray goodsList = data.getJSONArray("goods_list");

            String goodsIds = "";
            String skuIds = "";
            String timeGoodsIds = "";

            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                Integer timeGoodsId = goodsList.getJSONObject(i).getInteger("time_goods_id");
                if (null != skuId && skuId > 0) {
                    skuIds = skuIds + skuId + ",";
                }
                goodsIds = goodsIds + goodsId + ",";
                if (null != timeGoodsId && timeGoodsId != 0) {
                    timeGoodsIds = timeGoodsIds + timeGoodsId + ",";
                }
            }
            Map<Integer, Map<String, Object>> goodsMap = new HashMap<>();//商品
            Map<Integer, Map<String, Object>> goodsSkuMap = new HashMap<>();//商品sku
            Map<Integer, Map<String, Object>> timeGoodsMap = new HashMap<>();//抢购商品
            // 查询商品信息
            iBusinessService.findCheckGoodsInfo(goodsIds,skuIds,timeGoodsIds,goodsMap,goodsSkuMap,timeGoodsMap);
            LOG.info("---开始校验---");
            int resultCode = iBusinessService.checkGoodsInfo(goodsList, goodsMap, goodsSkuMap, timeGoodsMap, userId);
            return packagMsg(resultCode, dataJson);
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
