package com.yufan.task.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.service.impl.Test;
import com.yufan.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 查询分类商品（单品） 左边是菜单 右边是对应全部商品
 * @author: lirf
 * @time: 2021/1/30
 */
@Service("query_goods_list1")
public class QuerySingleGoodsList implements IResultOut {

    private Logger LOG = Logger.getLogger(QuerySingleGoodsList.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private ICategoryDao iCategoryDao;

    @Autowired
    private IOrderDao iOrderDao;


    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    public static int threadCount = 3;// 拆分线程数

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        dataJson.put("menusList", new ArrayList<>());
        try {
            Integer userId = data.getInteger("user_id");
            // 查询分类菜单
            List<Map<String, Object>> menusList = iCategoryDao.findMainMenu(Constants.MENU_TYPE_1);
            if (!CollectionUtils.isNotEmpty(menusList)) {
                // 未配置菜单
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            String levelIds = "";

            String categoryIds = "";
            // 查询商品列表
            for (int i = 0; i < menusList.size(); i++) {
                Map<String, Object> map = menusList.get(i);
                String levelIds_ = map.get("leve1Ids") == null ? "" : map.get("leve1Ids").toString();
                String categoryIds_ = map.get("categoryIds") == null ? "" : map.get("categoryIds").toString();
                int relType = Integer.parseInt(map.get("relType").toString());
                // 用于分类页面的关联类型 1关联一级分类 2 关联2级分类
                if (Constants.MENU_REL__TYPE_1 == relType) {
                    if (StringUtils.isEmpty(levelIds_)) {
                        continue;
                    }
                    levelIds = levelIds + levelIds_ + ",";
                } else {
                    if (StringUtils.isEmpty(categoryIds_)) {
                        continue;
                    }
                    categoryIds = categoryIds + categoryIds_ + ",";
                }
            }
            if (!StringUtils.isNotEmpty(levelIds) && !StringUtils.isNotEmpty(categoryIds)) {
                // 未配置菜单
                LOG.info("------未配置菜单-----");
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            // 查询购物车数量
            Map<String, Integer> cartGoodsCountMap = new HashMap<>();
            List<Map<String, Object>> cartList = iOrderDao.findCartSumCount(userId, null);
            for (int i = 0; i < cartList.size(); i++) {
                String goodsId = cartList.get(i).get("goods_id").toString();
                int count = Integer.parseInt(cartList.get(i).get("goodsCount").toString());
                cartGoodsCountMap.put(goodsId, count);
            }
            //
            if (levelIds.endsWith(",")) {
                levelIds = levelIds.substring(0, levelIds.length() - 1);
            }
            if (categoryIds.endsWith(",")) {
                categoryIds = categoryIds.substring(0, categoryIds.length() - 1);
            }
            GoodsCondition goodsCondition = new GoodsCondition();
            goodsCondition.setLevelIds(levelIds);
            goodsCondition.setCategoryIds(categoryIds);
            goodsCondition.setIsSingle(1);
            goodsCondition.setUserId(userId);
            // 查询商品信息
            List<Map<String, Object>> goodsList = iGoodsDao.loadGoodsList(goodsCondition);
            if (CollectionUtils.isEmpty(goodsList)) {
                // 未配置菜单
                LOG.info("-----查询商品信息不存在--------");
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            // 商品根据一级分类和类目
            Map<Integer, List<Map<String, Object>>> levelGoodsMap = new HashMap<>();
            Map<Integer, List<Map<String, Object>>> categoryGoodsMap = new HashMap<>();
            //
            for (int i = 0; i < goodsList.size(); i++) {
                //
                Map<String, Object> mapOld = goodsList.get(i);
                //
                int timeGoodsId = Integer.parseInt(mapOld.get("time_goods_id").toString());
                String timePrice = mapOld.get("time_price").toString();
                BigDecimal salePrice = new BigDecimal(mapOld.get("now_money").toString());
                if (timeGoodsId > 0) {
                    salePrice = new BigDecimal(timePrice);
                }
                //
                Map<String, Object> mapNew = new HashMap<>();
                mapNew.put("goodsName", mapOld.get("goods_name"));
                mapNew.put("sellCount", Integer.parseInt(mapOld.get("sell_count").toString()));
                mapNew.put("goodsId", Integer.parseInt(mapOld.get("goods_id").toString()));
                mapNew.put("title", mapOld.get("title"));
                mapNew.put("nowMoney", salePrice);
                mapNew.put("timeGoodsId", timeGoodsId);
                mapNew.put("goodsNum", Integer.parseInt(mapOld.get("goods_num").toString()));
                mapNew.put("goodsType", Integer.parseInt(mapOld.get("goods_type").toString()));
                mapNew.put("goodsImg", mapOld.get("goods_img"));
                mapNew.put("rentPayType", mapOld.get("rent_pay_type"));
                mapNew.put("rentPayTypeName", mapOld.get("rent_pay_type_name"));
                mapNew.put("trueMoney", new BigDecimal(mapOld.get("true_money").toString()));
                Integer count = cartGoodsCountMap.get(mapOld.get("goods_id").toString());
                mapNew.put("userGoodsCount", count == null ? 0 : count);
                int levelId = Integer.parseInt(mapOld.get("levelId").toString());
                int categoryId = Integer.parseInt(mapOld.get("categoryId").toString());
                if (levelGoodsMap.get(levelId) == null) {
                    List<Map<String, Object>> addGoods = new ArrayList<>();
                    addGoods.add(mapNew);
                    levelGoodsMap.put(levelId, addGoods);
                } else {
                    List<Map<String, Object>> addGoods = levelGoodsMap.get(levelId);
                    addGoods.add(mapNew);
                    levelGoodsMap.put(levelId, addGoods);

                }
                if (categoryGoodsMap.get(categoryId) == null) {
                    List<Map<String, Object>> addGoods = new ArrayList<>();
                    addGoods.add(mapNew);
                    categoryGoodsMap.put(categoryId, addGoods);
                } else {
                    List<Map<String, Object>> addGoods = categoryGoodsMap.get(categoryId);
                    addGoods.add(mapNew);
                    categoryGoodsMap.put(categoryId, addGoods);
                }
            }

            return doMenus(menusList, levelGoodsMap, categoryGoodsMap);

            // 多线程处理
//             return moreThreadDo(menusList, levelGoodsMap, categoryGoodsMap);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 直接处理
     *
     * @return
     */
    private String doMenus(List<Map<String, Object>> menusList, Map<Integer, List<Map<String, Object>>> levelGoodsMap,
                           Map<Integer, List<Map<String, Object>>> categoryGoodsMap) {
        JSONObject dataJson = new JSONObject();
        FindGoodsList findGoodsList = new FindGoodsList();
        List<Map<String, Object>> outList = new ArrayList<>();
        for (int i = 0; i < menusList.size(); i++) {
            Map<String, Object> mapMenuItem = menusList.get(i);
            // 菜单关联的一级分类或者类目
            String levelIds_ = mapMenuItem.get("leve1Ids") == null ? "" : mapMenuItem.get("leve1Ids").toString();
            String categoryIds_ = mapMenuItem.get("categoryIds") == null ? "" : mapMenuItem.get("categoryIds").toString();
            int relType_ = Integer.parseInt(mapMenuItem.get("relType").toString());
            // 用于分类页面的关联类型 1关联一级分类 2 关联2级分类
            List<Map<String, Object>> goodsListAdd = new ArrayList<>();
            if (Constants.MENU_REL__TYPE_1 == relType_) {
                if (StringUtils.isNotEmpty(levelIds_)) {
                    String[] array = levelIds_.split(",");
                    for (int j = 0; j < array.length; j++) {
                        int levelId = Integer.parseInt(array[j]);
                        List<Map<String, Object>> goodsList_ = levelGoodsMap.get(levelId);
                        findGoodsList.foreachGoods(goodsListAdd, goodsList_);
                    }
                }
            } else {
                if (StringUtils.isNotEmpty(categoryIds_)) {
                    String[] array = categoryIds_.split(",");
                    for (int j = 0; j < array.length; j++) {
                        int categoryId = Integer.parseInt(array[j]);
                        List<Map<String, Object>> goodsList_ = categoryGoodsMap.get(categoryId);
                        findGoodsList.foreachGoods(goodsListAdd, goodsList_);
                    }
                }
            }
            mapMenuItem.put("goodsList", goodsListAdd);
            if (CollectionUtils.isEmpty(goodsListAdd)) {
                continue;
            }
            outList.add(mapMenuItem);
        }
        dataJson.put("menusList", outList);
        return packagMsg(ResultCode.OK.getResp_code(), dataJson);
    }

    /**
     * 多线程处理
     *
     * @param menusList
     * @param levelGoodsMap
     * @param categoryGoodsMap
     * @return
     * @throws Exception
     */
    private String moreThreadDo(List<Map<String, Object>> menusList, Map<Integer, List<Map<String, Object>>> levelGoodsMap,
                                Map<Integer, List<Map<String, Object>>> categoryGoodsMap) throws Exception {
        JSONObject dataJson = new JSONObject();
        // 启动 多线和处理
        List<Future<List<Map<String, Object>>>> fList = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            FindGoodsList findGoodsList = new FindGoodsList(menusList, levelGoodsMap, categoryGoodsMap, (i + 1));
            Future<List<Map<String, Object>>> future = executorService.submit(findGoodsList);
            fList.add(future);
        }

        // 处理结果
        Map<String, Object> duplicateMap = new HashMap<>();//去重
        List<Map<String, Object>> resultsMenusThread = new ArrayList<>();
        for (int i = 0; i < fList.size(); i++) {
            // 返回处理菜单（一组）
            List<Map<String, Object>> result = fList.get(i).get();
            for (int j = 0; j < result.size(); j++) {
                Map<String, Object> map = result.get(j);
                // 获取菜单下面的商品列表
                List<Map<String, Object>> goodsListOld = (List<Map<String, Object>>) map.get("goodsList");
                List<Map<String, Object>> goodsListNew = new ArrayList<>();
                for (int k = 0; k < goodsListOld.size(); k++) {
                    String goodsId = goodsListOld.get(k).get("goodsId").toString();
                    if (duplicateMap.get(goodsId) != null) {
                        continue;
                    }
                    duplicateMap.put(goodsId, goodsId);
                    goodsListNew.add(goodsListOld.get(k));
                }
                map.put("goodsList", goodsListNew);
                resultsMenusThread.add(map);
            }
        }
        dataJson.put("menusList", resultsMenusThread);
        return packagMsg(ResultCode.OK.getResp_code(), dataJson);
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


class FindGoodsList implements Callable<List<Map<String, Object>>> {

    private List<Map<String, Object>> menusList;
    private Map<Integer, List<Map<String, Object>>> levelGoodsMap;
    private Map<Integer, List<Map<String, Object>>> categoryGoodsMap;
    private int index;

    public FindGoodsList() {

    }

    public FindGoodsList(List<Map<String, Object>> menusList, Map<Integer, List<Map<String, Object>>> levelGoodsMap, Map<Integer, List<Map<String, Object>>> categoryGoodsMap, int index) {
        this.menusList = menusList;
        this.levelGoodsMap = levelGoodsMap;
        this.categoryGoodsMap = categoryGoodsMap;
        this.index = index;
    }

    @Override
    public List<Map<String, Object>> call() throws Exception {
        int size = menusList.size();// 5
        // 处理的菜单数组数据下标
        int doCount = size / QuerySingleGoodsList.threadCount;
        if (doCount == 0) {
            doCount = size;
        } else if (doCount == 1) {
            doCount = QuerySingleGoodsList.threadCount;
        }
        // 返回的菜单
        List<Map<String, Object>> menusListReturn = new ArrayList<>();

        int arrayIndexBegin = (index - 1) * doCount;
        int arrayIndexEnd = arrayIndexBegin + doCount;
        if (index == QuerySingleGoodsList.threadCount) {
            // 执行剩下全部
            arrayIndexEnd = size;
        }

        if (arrayIndexEnd > size) {
            //判断数据长度是否在下标区间
            if (size < arrayIndexBegin) {
                return new ArrayList<>();
            }
            arrayIndexEnd = size;
            // 查询商品
            for (int i = arrayIndexBegin; i < arrayIndexEnd; i++) {
                Map<String, Object> mapMenuItem = this.menusList.get(i);
                doGoodsList(mapMenuItem);
                if (mapMenuItem.containsKey("goodsList")) {
                    menusListReturn.add(mapMenuItem);
                }
            }
            return menusListReturn;
        }
        // 查询商品列表
        for (int i = arrayIndexBegin; i < arrayIndexEnd; i++) {
            Map<String, Object> mapMenuItem = this.menusList.get(i);
            doGoodsList(mapMenuItem);
            if (mapMenuItem.containsKey("goodsList")) {
                menusListReturn.add(mapMenuItem);
            }
        }
        return menusListReturn;
    }

    private Map<String, Object> doGoodsList(Map<String, Object> mapMenuItem) {
        // 菜单关联的一级分类或者类目
        String levelIds = mapMenuItem.get("leve1Ids") == null ? "" : mapMenuItem.get("leve1Ids").toString();
        String categoryIds = mapMenuItem.get("categoryIds") == null ? "" : mapMenuItem.get("categoryIds").toString();
        int relType = Integer.parseInt(mapMenuItem.get("relType").toString());
        // 用于分类页面的关联类型 1关联一级分类 2 关联2级分类
        List<Map<String, Object>> goodsListAdd = new ArrayList<>();
//        Map<Integer, Object> duplicateMap = new HashMap<>();//去重
        if (Constants.MENU_REL__TYPE_1 == relType) {
            if (StringUtils.isNotEmpty(levelIds)) {
                String[] array = levelIds.split(",");
                for (int i = 0; i < array.length; i++) {
                    int levelId = Integer.parseInt(array[i]);
                    List<Map<String, Object>> goodsList = levelGoodsMap.get(levelId);
                    foreachGoods(goodsListAdd, goodsList);
                }
            }
        } else {
            if (StringUtils.isNotEmpty(categoryIds)) {
                String[] array = categoryIds.split(",");
                for (int i = 0; i < array.length; i++) {
                    int categoryId = Integer.parseInt(array[i]);
                    List<Map<String, Object>> goodsList = categoryGoodsMap.get(categoryId);
                    foreachGoods(goodsListAdd, goodsList);
                }
            }
        }
        if (!CollectionUtils.isEmpty(goodsListAdd)) {
            mapMenuItem.put("goodsList", goodsListAdd);
            mapMenuItem.put("isdisabled", false);
        }
        return mapMenuItem;
    }

    public void foreachGoods(List<Map<String, Object>> goodsListAdd, List<Map<String, Object>> goodsList) {
        if (null == goodsList) {
            return;
        }
        for (int j = 0; j < goodsList.size(); j++) {
            Map<String, Object> mapNew = new HashMap<>();
            mapNew.put("goodsName", goodsList.get(j).get("goodsName"));
            mapNew.put("sellCount", Integer.parseInt(goodsList.get(j).get("sellCount").toString()));
            mapNew.put("goodsId", Integer.parseInt(goodsList.get(j).get("goodsId").toString()));
            mapNew.put("title", goodsList.get(j).get("title"));
            mapNew.put("nowMoney", new BigDecimal(goodsList.get(j).get("nowMoney").toString()));
            mapNew.put("goodsNum", Integer.parseInt(goodsList.get(j).get("goodsNum").toString()));
            mapNew.put("goodsImg", goodsList.get(j).get("goodsImg"));
            mapNew.put("rentPayType", goodsList.get(j).get("rentPayType"));
            mapNew.put("rentPayTypeName", goodsList.get(j).get("rentPayTypeName"));
            mapNew.put("trueMoney", new BigDecimal(goodsList.get(j).get("trueMoney").toString()));
            mapNew.put("timeGoodsId", goodsList.get(j).get("timeGoodsId").toString());
            mapNew.put("userGoodsCount", Integer.parseInt(goodsList.get(j).get("userGoodsCount").toString()));
            mapNew.put("goodsType", goodsList.get(j).get("goodsType").toString());
            goodsListAdd.add(mapNew);
        }
    }
}