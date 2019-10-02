package com.yufan.task.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 10:12
 * 功能介绍: 查询商品列表
 */
@Service("query_goods_list")
public class QueryGoodsList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryGoodsList.class);

    @Autowired
    private IGoodsDao iGoodsDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {
            GoodsCondition goodsCondition = JSONObject.toJavaObject(receiveJsonBean.getData(), GoodsCondition.class);

            PageInfo page = iGoodsDao.loadGoodsList(goodsCondition);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> listData = page.getResultListMap();
            for (int i = 0; i < listData.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                int isSingle = Integer.parseInt(listData.get(i).get("is_single").toString());
                map.put("goods_id", Integer.parseInt(listData.get(i).get("goods_id").toString()));
                map.put("title", listData.get(i).get("title"));
                map.put("goods_name", listData.get(i).get("goods_name"));
                map.put("true_money", new BigDecimal(listData.get(i).get("true_money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                map.put("now_money", new BigDecimal(listData.get(i).get("now_money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                map.put("goods_img", listData.get(i).get("goods_img"));
                map.put("sell_count", Integer.parseInt(listData.get(i).get("sell_count").toString()));
                map.put("is_single", isSingle);//如果为非单品 页面价格应该显示sku区间价格
                map.put("sku_now_money", 0);
                if (isSingle == 0) {
                    String sku_now_money = listData.get(i).get("sku_now_money").toString();
                    BigDecimal lowPrice = new BigDecimal("0.00");
                    BigDecimal highPrice = new BigDecimal("0.00");
                    String[] strPrice = sku_now_money.split(",");
                    lowPrice = new BigDecimal(strPrice[0]);
                    if (strPrice.length > 1) {
                        highPrice = new BigDecimal(strPrice[strPrice.length - 1]);
                        map.put("sku_now_money", lowPrice + "-" + highPrice);
                    } else {
                        map.put("sku_now_money", lowPrice + "起");
                    }

                }
                outList.add(map);
            }

            dataJson.put("has_next", page.isHasNext());
            dataJson.put("curre_page", page.getCurrePage());
            dataJson.put("page_size", page.getPageSize());
            dataJson.put("goods_list", outList);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            GoodsCondition goodsCondition = JSONObject.toJavaObject(data, GoodsCondition.class);
            if (null == goodsCondition.getCurrePage()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}