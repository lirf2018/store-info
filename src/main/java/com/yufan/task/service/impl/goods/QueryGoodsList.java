package com.yufan.task.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbSearchHistory;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.history.IHistoryDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    private IHistoryDao iHistoryDao;

    @Autowired
    private ICategoryDao iCategoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {
            GoodsCondition goodsCondition = JSONObject.toJavaObject(receiveJsonBean.getData(), GoodsCondition.class);

            PageInfo page = iGoodsDao.loadGoodsListPage(goodsCondition);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> listData = page.getResultListMap();

            for (int i = 0; i < listData.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                int isSingle = Integer.parseInt(listData.get(i).get("is_single").toString());
                //
                int timeGoodsId = Integer.parseInt(map.get("time_goods_id").toString());
                String timePrice = map.get("time_price").toString();
                BigDecimal salePrice = new BigDecimal(listData.get(i).get("now_money").toString());
                if(timeGoodsId>0){
                    salePrice = new BigDecimal(timePrice);
                }
                map.put("goodsId", Integer.parseInt(listData.get(i).get("goods_id").toString()));
                map.put("title", listData.get(i).get("title"));
                map.put("goodsName", listData.get(i).get("goods_name"));
                map.put("trueMoney", new BigDecimal(listData.get(i).get("true_money").toString()));
                map.put("nowMoney", salePrice);
                map.put("timeGoodsId", timeGoodsId);
                map.put("goodsImg", listData.get(i).get("goods_img"));
                map.put("isZiYin", listData.get(i).get("is_zi_yin"));
                map.put("sellCount", Integer.parseInt(listData.get(i).get("sell_count").toString()));
                map.put("goodsNum", Integer.parseInt(listData.get(i).get("goods_num").toString()));
                map.put("isSingle", isSingle);//如果为非单品 页面价格应该显示sku区间价格
                map.put("skuNowMoney", 0);
                if (isSingle == 0) {
                    String sku_now_money = listData.get(i).get("sku_now_money").toString();
                    BigDecimal lowPrice = new BigDecimal("0.00");
                    BigDecimal highPrice = new BigDecimal("0.00");
                    String[] strPrice = sku_now_money.split(",");
                    lowPrice = new BigDecimal(strPrice[0]);
                    if (strPrice.length > 1) {
                        highPrice = new BigDecimal(strPrice[strPrice.length - 1]);
                    }
                    map.put("lowPrice", lowPrice);
                    map.put("highPrice", highPrice);
                }
                outList.add(map);
            }
            //增加查询历史记录
            if (StringUtils.isNotEmpty(goodsCondition.getGoodsName())) {
                TbSearchHistory searchHistory = new TbSearchHistory();
                searchHistory.setType(1);
                searchHistory.setUserId(goodsCondition.getUserId());
                searchHistory.setTypeWord(goodsCondition.getGoodsName());
                searchHistory.setStatus(1);
                searchHistory.setCreatetime(new Timestamp(new Date().getTime()));
                searchHistory.setShopId(0);

                iHistoryDao.saveSearchData(searchHistory);
            }
            String categoryName = "";//catogeryId
            // 查询分类
            Integer catogeryId = goodsCondition.getCatogeryId();
            if (null != catogeryId) {
                TbCategory category = iCategoryDao.loadTbCategoryById(catogeryId);
                categoryName = category.getCategoryName();
            }
            dataJson.put("categoryName", categoryName);

            dataJson.put("hasNext", page.isHasNext());
            dataJson.put("currePage", page.getCurrePage());
            dataJson.put("pageSize", page.getPageSize());
            dataJson.put("goodsList", outList);
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