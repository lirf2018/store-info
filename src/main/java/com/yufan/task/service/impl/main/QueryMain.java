package com.yufan.task.service.impl.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResponeUtil;
import com.yufan.pojo.TbParam;
import com.yufan.utils.CacheData;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.activity.IActivityDao;
import com.yufan.task.dao.banner.IBannerDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.info.IInfoDao;
import com.yufan.task.dao.param.IParamDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.J;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 17:44
 * 功能介绍: main页面
 */
@Service("main")
public class QueryMain implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryMain.class);

    @Autowired
    private IBannerDao iBannerDao;

    @Autowired
    private IActivityDao iActivityDao;

    @Autowired
    private IInfoDao iInfoDao;

    @Autowired
    private IParamDao iParamDao;

    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {
            /**
             * 活动   6个
             */
            List<Map<String, Object>> activityList = iActivityDao.loadActivityListMap(Constants.SEARCH_COUNT_6);
            JSONArray activityArray = new JSONArray();
            for (int i = 0; i < activityList.size(); i++) {
                Map<String, Object> map = activityList.get(i);
                String activityLink = map.get("activity_link").toString();
                String activityImg = map.get("activity_img").toString();
                String activityTitle = map.get("activity_title").toString();
                String activityIntro = map.get("activity_intro").toString();
                //
                JSONObject activity = new JSONObject();
                activity.put("activity_link", activityLink);
                activity.put("activity_img", activityImg);
                activity.put("activity_title", activityTitle);
                activity.put("activity_intro", activityIntro);
                activityArray.add(activity);
            }

            /**
             * bannel 6个
             */
            List<Map<String, Object>> bannerList = iBannerDao.loadBannerListMap(Constants.SEARCH_COUNT_6);
            JSONArray bannerArray = new JSONArray();
            for (int i = 0; i < bannerList.size(); i++) {
                Map<String, Object> map = bannerList.get(i);
                String bannerLink = map.get("banner_link").toString();
                String bannerImg = map.get("banner_img").toString();
                //
                JSONObject banner = new JSONObject();
                banner.put("banner_link", bannerLink);
                banner.put("banner_img", bannerImg);
                bannerArray.add(banner);
            }

            /**
             * 推荐 8个
             */
            List<Map<String, Object>> listWeight = iGoodsDao.mainGoodsListMap(Constants.SEARCH_COUNT_8, "weight");
            JSONArray weightGoodsArray = new JSONArray();
            for (int i = 0; i < listWeight.size(); i++) {
                Map<String, Object> map = listWeight.get(i);
                int goodsId = Integer.parseInt(map.get("goods_id").toString());
                int isSingle = Integer.parseInt(map.get("is_single").toString());
                String title = map.get("title").toString();
                String goodsName = map.get("goods_name").toString();
                String skuNowMoney = map.get("sku_now_money").toString();

                String trueMoney = map.get("true_money").toString();
                String nowMoney = map.get("now_money").toString();
                String goodsImg = map.get("goods_img").toString();
                int sellCount = Integer.parseInt(map.get("sell_count").toString());
                int isZiYin = Integer.parseInt(map.get("is_zi_yin").toString());
                //
                int timeGoodsId = Integer.parseInt(map.get("time_goods_id").toString());
                String timePrice = map.get("time_price").toString();
                BigDecimal salePrice = new BigDecimal(nowMoney);
                if (timeGoodsId > 0) {
                    salePrice = new BigDecimal(timePrice);
                }
                //
                JSONObject goods = new JSONObject();
                goods.put("goods_id", goodsId);
                goods.put("title", title);
                goods.put("goods_name", goodsName);
                goods.put("true_money", new BigDecimal(trueMoney));
                goods.put("now_money", salePrice);
                goods.put("goods_img", goodsImg);
                goods.put("sell_count", sellCount);
                goods.put("is_single", isSingle);
                goods.put("sku_now_money", new BigDecimal(skuNowMoney));
                goods.put("is_zi_yin", isZiYin);
                goods.put("time_goods_id", timeGoodsId);

                weightGoodsArray.add(goods);
            }

            /**
             * 新品 12个
             */
            List<Map<String, Object>> listNew = iGoodsDao.mainGoodsListMap(Constants.SEARCH_COUNT_12, "new");
            JSONArray newGoodsArray = new JSONArray();
            for (int i = 0; i < listNew.size(); i++) {
                Map<String, Object> map = listNew.get(i);
                int goodsId = Integer.parseInt(map.get("goods_id").toString());
                int isSingle = Integer.parseInt(map.get("is_single").toString());
                String skuNowMoney = map.get("sku_now_money").toString();
                String title = map.get("title").toString();
                String goodsName = map.get("goods_name").toString();
                String trueMoney = map.get("true_money").toString();
                String nowMoney = map.get("now_money").toString();
                String goodsImg = map.get("goods_img").toString();
                int sellCount = Integer.parseInt(map.get("sell_count").toString());
                int isZiYin = Integer.parseInt(map.get("is_zi_yin").toString());
                //
                int timeGoodsId = Integer.parseInt(map.get("time_goods_id").toString());
                String timePrice = map.get("time_price").toString();
                BigDecimal salePrice = new BigDecimal(nowMoney);
                if (timeGoodsId > 0) {
                    salePrice = new BigDecimal(timePrice);
                }
                //
                JSONObject goods = new JSONObject();
                goods.put("goods_id", goodsId);
                goods.put("title", title);
                goods.put("goods_name", goodsName);
                goods.put("true_money", new BigDecimal(trueMoney));
                goods.put("now_money", salePrice);
                goods.put("goods_img", goodsImg);
                goods.put("sell_count", sellCount);
                goods.put("is_single", isSingle);
                goods.put("sku_now_money", new BigDecimal(skuNowMoney));
                goods.put("is_zi_yin", isZiYin);
                goods.put("time_goods_id", timeGoodsId);
                newGoodsArray.add(goods);
            }

            /**
             * 最热 10个
             */
            List<Map<String, Object>> listHot = iGoodsDao.mainGoodsListMap(Constants.SEARCH_COUNT_10, "hot");
            JSONArray hotGoodsArray = new JSONArray();
            for (int i = 0; i < listHot.size(); i++) {
                Map<String, Object> map = listHot.get(i);
                int goodsId = Integer.parseInt(map.get("goods_id").toString());
                int isSingle = Integer.parseInt(map.get("is_single").toString());
                String skuNowMoney = map.get("sku_now_money").toString();
                String title = map.get("title").toString();
                String goodsName = map.get("goods_name").toString();
                String trueMoney = map.get("true_money").toString();
                String nowMoney = map.get("now_money").toString();
                String goodsImg = map.get("goods_img").toString();
                int sellCount = Integer.parseInt(map.get("sell_count").toString());
                int isZiYin = Integer.parseInt(map.get("is_zi_yin").toString());
                //
                int timeGoodsId = Integer.parseInt(map.get("time_goods_id").toString());
                String timePrice = map.get("time_price").toString();
                BigDecimal salePrice = new BigDecimal(nowMoney);
                if (timeGoodsId > 0) {
                    salePrice = new BigDecimal(timePrice);
                }
                //
                JSONObject goods = new JSONObject();
                goods.put("goods_id", goodsId);
                goods.put("title", title);
                goods.put("goods_name", goodsName);
                goods.put("true_money", new BigDecimal(trueMoney));
                goods.put("now_money", salePrice);
                goods.put("goods_img", goodsImg);
                goods.put("sell_count", sellCount);
                goods.put("is_single", isSingle);
                goods.put("is_zi_yin", isZiYin);
                goods.put("time_goods_id", timeGoodsId);
                hotGoodsArray.add(goods);
            }
            /**
             * 抢购   8个
             */
            JSONArray timeGoodsArray = new JSONArray();
            //查询参数中是否设置了抢购开始结束时间time_goods_time
            List<Map<String, Object>> listParam = iParamDao.queryParamListMap("time_goods_time", "time_goods_time", 1);
            if (null != listParam && listParam.size() > 0) {
                String format = "yyyy-MM-dd HH:mm:ss";
                String nowTime = DatetimeUtil.getNow(format);
                try {
                    String setStime = listParam.get(0).get("param_value").toString();
                    String setEtime = listParam.get(0).get("param_value1").toString();
                    if (DatetimeUtil.compareDate(nowTime, setStime, format) > -1 && DatetimeUtil.compareDate(nowTime, setEtime, format) < 1) {
                        LOG.info("-------------参数抢购时间范围内------------------");
                        //抢购商品
                        List<Map<String, Object>> timeGoodsList = iGoodsDao.mainTimeGoodsListMap(Constants.SEARCH_COUNT_8);
                        for (int i = 0; i < timeGoodsList.size(); i++) {
                            Map<String, Object> map = timeGoodsList.get(i);
                            int goodsId = Integer.parseInt(map.get("goods_id").toString());
                            String title = map.get("title").toString();
                            String goodsName = map.get("goods_name").toString();
                            BigDecimal trueMoney = new BigDecimal(map.get("true_money").toString());//商品原价格
                            BigDecimal nowMoney = new BigDecimal(map.get("time_price").toString());//商品抢购价格
                            String goodsImg = map.get("goods_img").toString();
                            int sellCount = Integer.parseInt(map.get("sell_count").toString());
                            int isZiYin = Integer.parseInt(map.get("is_zi_yin").toString());
                            //
                            JSONObject goods = new JSONObject();
                            goods.put("goods_id", goodsId);
                            goods.put("title", title);
                            goods.put("goods_name", goodsName);
                            goods.put("true_money", trueMoney);
                            goods.put("now_money", nowMoney);
                            goods.put("goods_img", goodsImg);
                            goods.put("sell_count", sellCount);
                            goods.put("is_zi_yin", isZiYin);

                            timeGoodsArray.add(goods);
                        }
                    } else {
                        LOG.info("-------参数中是否设置了抢购开始结束时间---不在当前时间内-------");
                    }
                } catch (Exception e) {
                    LOG.error("----设置参数抢购开始结束时间异常---", e);
                }
            }

            //查询一条资讯
            List<Map<String, Object>> list = iInfoDao.queryInfoList(Constants.SEARCH_COUNT_1);
            String InfoTitle = "";
            String InfoImg = "";
            if (null != list && list.size() > 0) {
                InfoTitle = list.get(0).get("info_title").toString();
                InfoImg = list.get(0).get("info_img").toString();
            }

            // 获取通知
            List<TbParam> paramList = CacheData.PARAMLIST;
            String newsInfo = "";
            for (int i = 0; i < paramList.size(); i++) {
                TbParam param = paramList.get(i);
                if ("main_notice".equals(param.getParamCode())) {
                    newsInfo = param.getParamValue();
                }
            }

            dataJson.put("news_info", newsInfo);
            dataJson.put("info_title", InfoTitle);
            dataJson.put("info_img", InfoImg);
            dataJson.put("activity_list", activityArray);
            dataJson.put("banner_list", bannerArray);
            dataJson.put("weight_goods_list", weightGoodsArray);
            dataJson.put("new_goods_list", newGoodsArray);
            dataJson.put("hot_goods_list", hotGoodsArray);
            dataJson.put("time_goods_list", timeGoodsArray);
            return ResponeUtil.packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("---------error", e);
        }
        return ResponeUtil.packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {


            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}