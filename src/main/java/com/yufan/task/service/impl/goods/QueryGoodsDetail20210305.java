package com.yufan.task.service.impl.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbShop;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.goods.IGoodsJpaDao;
import com.yufan.task.dao.img.IImgDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.param.IParamDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
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
 * 创建时间:  2019/8/25 11:58
 * 功能介绍: 查询商品详情(加个点，查询sku商品规格时，只查询该商品数据关联的sku规格)
 */
@Deprecated
public class QueryGoodsDetail20210305 implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryGoodsDetail20210305.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private IImgDao iImgDao;

    @Autowired
    private IGoodsJpaDao iGoodsJpaDao;

    @Autowired
    private IParamDao iParamDao;

    @Autowired
    private IShopJapDao iShopJapDao;

    @Autowired
    private ICategoryDao iCategoryDao;

    @Autowired
    private IOrderDao iOrderDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer goodsId = data.getInteger("goods_id");
            Integer userId = data.getInteger("user_id");
            if (!checkParam(receiveJsonBean)) {
                return packagMsg(ResultCode.NEED_PARAM_ERROR.getResp_code(), dataJson);
            }
            //查询商品详情
            TbGoods goods = iGoodsJpaDao.findOne(goodsId);
            if (goods == null) {
                return packagMsg(ResultCode.FAIL_GOODS_INVALIDATE.getResp_code(), dataJson);
            }
            //
            String format = "yyyy-MM-dd HH:mm:ss";
            String goodsName = goods.getGoodsName();
            String title = goods.getTitle();
            String goodsImg = goods.getGoodsImg();
            BigDecimal trueMoney = goods.getTrueMoney();
            BigDecimal nowMoney = goods.getNowMoney();
            String intro = goods.getIntro();
            Integer isYuding = goods.getIsYuding();
            Integer getWay = goods.getGetWay();
            Integer isInvoice = goods.getIsInvoice();
            Integer isPutaway = goods.getIsPutaway();
            Integer property = goods.getProperty();
            String beginTime = DatetimeUtil.timeStamp2Date(goods.getStartTime().getTime(), format);
            String endTime = DatetimeUtil.timeStamp2Date(goods.getEndTime().getTime(), format);
            String goodsCode = goods.getGoodsCode();
            String goodsUnit = goods.getGoodsUnit();
            Integer isSingle = goods.getIsSingle();
            Integer goodsNum = goods.getGoodsNum();
            Integer isReturn = goods.getIsReturn();
            Integer couponId = goods.getCouponId();
            Integer status = goods.getStatus();
            String remark = goods.getRemark();
            Integer goodsType = goods.getGoodsType();
            Integer isPayOnline = goods.getIsPayOnline();
            String outCode = goods.getOutCode();
            BigDecimal depositMoney = goods.getDepositMoney();
            String peisongZcDesc = goods.getPeisongZcDesc();
            String peisongPeiDesc = goods.getPeisongPeiDesc();
            BigDecimal advancePrice = goods.getAdvancePrice();
            Integer sellCount = goods.getSellCount();
            Integer isTimeGoods = goods.getIsTimeGoods();
            Integer isZiYin = goods.getIsZiYin();

            Integer timeGoodsId = 0;//抢购商品标识
            BigDecimal timePrice = new BigDecimal(0);//抢购价格
            Integer timeGoodsStore = 0;//抢购库存
            Integer isTimeGoodsShell = 0;//抢购商品是否在抢购时间内2在0不在

            String nowTime = DatetimeUtil.getNow(format);

            //商品关联的属性值标识
            Map<Integer, Integer> valueIdMap = new HashMap<>();

            //是否单品
            List<Map<String, Object>> skuList = new ArrayList<>();
            if (goods.getIsSingle() == 0) {
                goodsNum = 0;
                //查询商品sku
                List<Map<String, Object>> skuListData = iGoodsDao.queryGoodsSkuListMap(goodsId);
                BigDecimal skuLowMoney = new BigDecimal(0);
                BigDecimal skuLowMoneyTrue = new BigDecimal(0);
                for (int i = 0; i < skuListData.size(); i++) {
                    Map<String, Object> map = skuListData.get(i);
                    Integer skuId = Integer.parseInt(map.get("sku_id").toString());
                    Integer skuNum = Integer.parseInt(map.get("sku_num").toString());
                    goodsNum = goodsNum + skuNum;
                    String skuImg = map.get("sku_img") == null ? "" : Constants.IMG_WEB_URL + map.get("sku_img");
                    BigDecimal skuNowMoney = new BigDecimal(map.get("sku_now_money").toString());
                    BigDecimal skuTrueMoney = new BigDecimal(map.get("sku_now_money").toString());
                    String valueIds = map.get("prop_code").toString();
                    String[] valueIdsArray = valueIds.split(";");
                    for (int j = 0; j < valueIdsArray.length; j++) {
                        int valueId = Integer.parseInt(valueIdsArray[j]);
                        valueIdMap.put(valueId, valueId);
                    }

                    if (i == 0) {
                        skuLowMoney = skuNowMoney;
                        skuLowMoneyTrue = skuTrueMoney;
                    } else {
                        if (skuLowMoney.compareTo(skuNowMoney) > 0) {
                            skuLowMoney = skuNowMoney;
                            skuLowMoneyTrue = skuTrueMoney;
                        }
                    }
                    map.put("skuCode", map.get("sku_code"));
                    map.put("skuTrueMoney", skuTrueMoney );
                    map.put("skuNowMoney", skuNowMoney);
                    map.put("skuId", skuId);
                    map.put("skuNum", skuNum);
                    map.put("skuImg", skuImg);
                    map.put("skuName", map.get("sku_name"));
                    map.put("propCode", map.get("prop_code"));
                    map.put("skuPurchasePrice", map.get("sku_purchase_price"));
                    skuList.add(map);
                }
                dataJson.put("skuLowMoney", skuLowMoney.setScale(2,BigDecimal.ROUND_HALF_UP));
                dataJson.put("skuLowMoneyTrue", skuLowMoneyTrue.setScale(2,BigDecimal.ROUND_HALF_UP));
            }
            if (couponId > 0) {
                //卡券
            }
            if (goods.getIsSingle() == 1 && isTimeGoods == 1) {
                //查询参数中是否设置了抢购开始结束时间time_goods_time
                List<Map<String, Object>> listParam = iParamDao.queryParamListMap("time_goods_time", "time_goods_time", 1);
                if (null != listParam && listParam.size() > 0) {
                    try {
                        String setStime = listParam.get(0).get("param_value").toString();
                        String setEtime = listParam.get(0).get("param_value1").toString();
                        if (DatetimeUtil.compareDate(nowTime, setStime, format) > -1 && DatetimeUtil.compareDate(nowTime, setEtime, format) < 1) {
                            LOG.info("-------------参数抢购时间范围内------------------");
                            //抢购商品
                            List<Map<String, Object>> timeGoodsListMap = iGoodsDao.queryTimeGoodsListMap(goodsId.toString());
                            if (timeGoodsListMap.size() > 0) {
                                Map<String, Object> map = timeGoodsListMap.get(0);
                                String timeStime = map.get("begin_time").toString();
                                String timeEtime = map.get("end_time").toString();
                                if (DatetimeUtil.compareDate(nowTime, timeStime, format) > -1 && DatetimeUtil.compareDate(nowTime, timeEtime) < 1) {
                                    isTimeGoodsShell = 2;
                                    timeGoodsId = Integer.parseInt(map.get("id").toString());
                                    timePrice = new BigDecimal(map.get("time_price").toString());//抢购价格
                                    timeGoodsStore = Integer.parseInt(map.get("goods_store").toString());//抢购库存
                                }
                            }
                        } else {
                            LOG.info("-------参数中是否设置了抢购开始结束时间---不在当前时间内-------");
                        }
                    } catch (Exception e) {
                        LOG.error("----设置参数抢购开始结束时间异常---", e);
                    }

                }
            }

            JSONArray bannerImgList = new JSONArray();
            JSONArray goodsInfoImgList = new JSONArray();
            //查询商品图片
            List<Map<String, Object>> imgListMap = iImgDao.queryTableRelImg(null, goodsId, Constants.IMG_CLASSIFY_GOODS);
            for (int i = 0; i < imgListMap.size(); i++) {
                //1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
                int imgType = Integer.parseInt(imgListMap.get(i).get("img_type").toString());
                String imgUrl = imgListMap.get(i).get("img_url").toString();
                JSONObject img = new JSONObject();
                img.put("imgUrl", StringUtils.isEmpty(imgUrl) ? "" : Constants.IMG_WEB_URL + imgUrl);
                if (imgType == 1) {
                    bannerImgList.add(img);
                } else if (imgType == 2) {
                    goodsInfoImgList.add(img);
                }
            }
            //商品综合状态,
            int allStatus = 1;//无效（店铺无效,未开始，已过期）
            if (!(DatetimeUtil.compareDate(nowTime, beginTime, format) > -1 && DatetimeUtil.compareDate(nowTime, endTime) < 1)) {
                LOG.info("----商品时间无效---");
                allStatus = 0;
            }
            if (status != 1) {
                LOG.info("----商品状态无效---");
                allStatus = 0;
            }
            if (isPutaway != 2) {
                LOG.info("----商品已下架---");
                //上架状态 0下架 1等待确认 2销售中
                allStatus = 0;
            }
            TbShop shop = iShopJapDao.findOne(goods.getShopId());
            String shopEndTime = DatetimeUtil.timeStamp2Date(shop.getEnterEndTime().getTime(), format);
            if (null == shop || shop.getStatus() != 1 || DatetimeUtil.compareDate(nowTime, shopEndTime) > 1) {
                LOG.info("----店铺无效---");
                allStatus = 0;
            }

            //查询商品销售和非销售属性
            List<Map<String, Object>> propList = new ArrayList<>();
            Map<Integer, Integer> mark = new HashMap<>();
            List<Map<String, Object>> queryCategoryRelListMap = iCategoryDao.queryCategoryRelListMap(goods.getCategoryId());
            for (int i = 0; i < queryCategoryRelListMap.size(); i++) {
                Integer propId = Integer.parseInt(queryCategoryRelListMap.get(i).get("it_prop_id").toString());
                if (mark.get(propId) != null) {
                    continue;
                }
                mark.put(propId, propId);
                propList.add(queryCategoryRelListMap.get(i));
            }

            //
            List<Map<String, Object>> salePropList = new ArrayList<>();//1销售属性
            List<Map<String, Object>> unsalePropList = new ArrayList<>();//0非销售属性

            for (int i = 0; i < propList.size(); i++) {
                Integer propId = Integer.parseInt(propList.get(i).get("it_prop_id").toString());
                Integer isShell = Integer.parseInt(propList.get(i).get("it_is_shell").toString());
                String propName = propList.get(i).get("it_prop_name").toString();
                Map<String, Object> mapProp = new HashMap<>();
                mapProp.put("propId", propId);
                mapProp.put("propName", propName);

                List<Map<String, Object>> valueList = new ArrayList<>();
                for (int j = 0; j < queryCategoryRelListMap.size(); j++) {
                    int valueId = Integer.parseInt(queryCategoryRelListMap.get(j).get("pv_value_id").toString());
                    if (propId != Integer.parseInt(queryCategoryRelListMap.get(j).get("it_prop_id").toString())) {
                        continue;
                    }
                    if (valueIdMap.get(valueId) == null) {
                        continue;
                    }
                    Map<String, Object> mapValue = new HashMap<>();
                    mapValue.put("valueId", queryCategoryRelListMap.get(j).get("pv_value_id").toString());
                    mapValue.put("valueName", queryCategoryRelListMap.get(j).get("pv_value_name").toString());
                    valueList.add(mapValue);
                }
                mapProp.put("valueList", valueList);
                if (isShell == 1) {
                    salePropList.add(mapProp);
                } else {
                    unsalePropList.add(mapProp);
                }
            }

            //返回购物车数量 购物车
            int cartGoodsCount = 0;
            if (null != userId && userId > 0) {
                cartGoodsCount = iOrderDao.userCartCount(userId,null);
            }
            dataJson.put("cartGoodsCount", cartGoodsCount);//购物车数量

            dataJson.put("salePropList", salePropList);
            dataJson.put("unsalePropList", unsalePropList);

            //商品信息
            dataJson.put("allStatus", allStatus);
            dataJson.put("shopId", shop.getShopId());
            dataJson.put("shopName", shop.getShopName());
            dataJson.put("goodsName", goodsName);
            dataJson.put("title", title);
            dataJson.put("goodsImg", goodsImg == null ? "" : Constants.IMG_WEB_URL + goodsImg);
            dataJson.put("trueMoney", trueMoney);
            dataJson.put("nowMoney", nowMoney);
            dataJson.put("intro", intro);
            dataJson.put("isYuding", isYuding);
            dataJson.put("getWay", getWay);
            dataJson.put("isInvoice", isInvoice);
            dataJson.put("isPutaway", isPutaway);
            dataJson.put("property", property);
            dataJson.put("beginTime", beginTime);
            dataJson.put("endTime", endTime);
            dataJson.put("goodsCode", goodsCode);
            dataJson.put("goodsUnit", goodsUnit);
            dataJson.put("isSingle", isSingle);
            dataJson.put("goodsNum", goodsNum);
            dataJson.put("isReturn", isReturn);
            dataJson.put("couponId", couponId);
            dataJson.put("status", status);
            dataJson.put("remark", remark);
            dataJson.put("goodsType", goodsType);
            dataJson.put("isPayOnline", isPayOnline);
            dataJson.put("outCode", outCode);
            dataJson.put("depositMoney", depositMoney);
            dataJson.put("peisongZcDesc", peisongZcDesc);
            dataJson.put("peisongPeiDesc", peisongPeiDesc);
            dataJson.put("advancePrice", advancePrice);
            dataJson.put("sellCount", sellCount);
            dataJson.put("goodsId", goodsId);
            dataJson.put("isZiYin", isZiYin);
            //抢购商品
            dataJson.put("isTimeGoods", isTimeGoods);
            dataJson.put("timeGoodsId", timeGoodsId);
            dataJson.put("timeGoodsStore", timeGoodsStore);
            dataJson.put("timePrice", timePrice);
            dataJson.put("isTimeGoodsShell", isTimeGoodsShell);

            dataJson.put("bannerImgList", bannerImgList);
            dataJson.put("goodsImgList", goodsInfoImgList);
            dataJson.put("goodsSkuList", skuList);

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
            Integer goodsId = data.getInteger("goods_id");
            if (null == goodsId) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}