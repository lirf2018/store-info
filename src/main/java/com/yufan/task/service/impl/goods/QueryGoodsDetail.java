package com.yufan.task.service.impl.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.dao.category.ICategoryDao;
import com.yufan.dao.goods.IGoodsDao;
import com.yufan.dao.goods.IGoodsJpaDao;
import com.yufan.dao.img.IImgDao;
import com.yufan.dao.param.IParamDao;
import com.yufan.dao.shop.IShopJapDao;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbShop;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 11:58
 * 功能介绍: 查询商品详情
 */
@Service("query_goods_detail")
public class QueryGoodsDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryGoodsDetail.class);

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


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer goodsId = data.getInteger("goods_id");
            if (!checkParam(receiveJsonBean)) {
                return packagMsg(ResultCode.NEED_PARAM_ERROR.getResp_code(), dataJson);
            }
            //查询商品详情
            TbGoods goods = iGoodsJpaDao.getOne(goodsId);
            if (goods == null || goods.getGoodsId() == 0) {
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
            String beginTime = DatetimeUtil.timeStamp2Date(goods.getStartTime().getTime() / 1000, format);
            String endTime = DatetimeUtil.timeStamp2Date(goods.getEndTime().getTime() / 1000, format);
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

            Integer timeGoodsId = 0;//抢购商品标识
            BigDecimal timePrice = new BigDecimal(0);//抢购价格
            Integer timeGoodsStore = 0;//抢购库存
            Integer isTimeGoodsShell = 0;//抢购商品是否在抢购时间内2在0不在

            String nowTime = DatetimeUtil.getNow(format);

            //是否单品
            List<Map<String, Object>> skuList = new ArrayList<>();
            if (goods.getIsSingle() == 0) {
                //查询商品sku
                List<Map<String, Object>> skuListData = iGoodsDao.queryGoodsSkuListMap(goodsId);
                for (int i = 0; i < skuListData.size(); i++) {
                    Map<String, Object> map = skuListData.get(i);
                    Integer skuId = Integer.parseInt(map.get("sku_id").toString());
                    Integer skuNum = Integer.parseInt(map.get("sku_num").toString());
                    String skuImg = map.get("sku_img") == null ? "" : Constants.IMG_URL + map.get("sku_img");
                    BigDecimal skuTrueMoney = new BigDecimal(map.get("true_money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal skuNowMoney = new BigDecimal(map.get("now_money").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);

                    map.put("sku_id", skuId);
                    map.put("sku_num", skuNum);
                    map.put("sku_img", skuImg);
                    map.put("sku_true_money", skuTrueMoney);
                    map.put("sku_now_money", skuNowMoney);
                    skuList.add(map);
                }
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
                            List<Map<String, Object>> timeGoodsListMap = iGoodsDao.queryTimeGoodsListMap(goodsId);
                            if (timeGoodsListMap.size() > 0) {
                                Map<String, Object> map = timeGoodsListMap.get(0);
                                String timeStime = map.get("begin_time").toString();
                                String timeEtime = map.get("end_time").toString();
                                if (DatetimeUtil.compareDate(nowTime, timeStime, format) > -1 && DatetimeUtil.compareDate(nowTime, timeEtime) < 1) {
                                    isTimeGoodsShell = 1;
                                }
                                timeGoodsId = Integer.parseInt(map.get("id").toString());
                                timePrice = new BigDecimal(map.get("time_price").toString());//抢购价格
                                timeGoodsStore = Integer.parseInt(map.get("goods_store").toString());//抢购库存
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
            List<Map<String, Object>> imgListMap = iImgDao.queryTableRelImg(null, goodsId, Constants.CLASSIFY_GOODS);
            for (int i = 0; i < imgListMap.size(); i++) {
                //1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
                int imgType = Integer.parseInt(imgListMap.get(i).get("img_type").toString());
                String imgUrl = imgListMap.get(i).get("img_url").toString();
                JSONObject img = new JSONObject();
                img.put("img_url", StringUtils.isEmpty(imgUrl) ? "" : Constants.IMG_URL + imgUrl);
                if (imgType == 1) {
                    bannerImgList.add(img);
                } else if (imgType == 2) {
                    goodsInfoImgList.add(imgType);
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
            TbShop shop = iShopJapDao.getOne(goods.getShopId());
            String shopEndTime = DatetimeUtil.timeStamp2Date(shop.getEnterEndTime().getTime() / 1000, format);
            if (null == shop || shop.getStatus() != 1 || DatetimeUtil.compareDate(nowTime, shopEndTime) > 1) {
                LOG.info("----店铺无效---");
                allStatus = 0;
            }

            //查询商品销售和非销售属性
            List<Map<String, Object>> propList = new ArrayList<>();
            Map<Integer, Integer> mark = new HashMap<>();
            List<Map<String, Object>> queryCategoryRelListMap = iCategoryDao.queryCategoryRelListMap(goods.getCategoryId());
            for (int i = 0; i < queryCategoryRelListMap.size(); i++) {
                Integer propId = Integer.parseInt(queryCategoryRelListMap.get(i).get("prop_id").toString());
                if (mark.get(propId) != null) {
                    continue;
                }
                mark.put(propId, propId);
                propList.add(queryCategoryRelListMap.get(i));
            }

            //
            List<Map<String, Object>> shellPropList = new ArrayList<>();//销售属性
            List<Map<String, Object>> ubshellPropList = new ArrayList<>();//非销售属性

            for (int i = 0; i < propList.size(); i++) {
                Integer propId = Integer.parseInt(queryCategoryRelListMap.get(i).get("prop_id").toString());
                Integer isShell = Integer.parseInt(queryCategoryRelListMap.get(i).get("it_is_shell").toString());
                String propName = queryCategoryRelListMap.get(i).get("it_prop_name").toString();
                Map<String,Object> mapProp = new HashMap<>();
                mapProp.put("prop_id",propId);
                mapProp.put("prop_name",propName);




            }


            //商品信息
            dataJson.put("all_status", allStatus);

            dataJson.put("goods_name", goodsName);
            dataJson.put("title", title);
            dataJson.put("goods_img", goodsImg == null ? "" : Constants.IMG_URL + goodsImg);
            dataJson.put("true_money", trueMoney);
            dataJson.put("now_money", nowMoney);
            dataJson.put("intro", intro);
            dataJson.put("is_yuding", isYuding);
            dataJson.put("get_way", getWay);
            dataJson.put("is_invoice", isInvoice);
            dataJson.put("is_putaway", isPutaway);
            dataJson.put("property", property);
            dataJson.put("begin_time", beginTime);
            dataJson.put("end_time", endTime);
            dataJson.put("goods_code", goodsCode);
            dataJson.put("goods_unit", goodsUnit);
            dataJson.put("is_single", isSingle);
            dataJson.put("goods_num", goodsNum);
            dataJson.put("is_return", isReturn);
            dataJson.put("coupon_id", couponId);
            dataJson.put("status", status);
            dataJson.put("remark", remark);
            dataJson.put("goods_type", goodsType);
            dataJson.put("is_pay_online", isPayOnline);
            dataJson.put("out_code", outCode);
            dataJson.put("deposit_money", depositMoney);
            dataJson.put("peisong_zc_desc", peisongZcDesc);
            dataJson.put("peisong_pei_desc", peisongPeiDesc);
            dataJson.put("advance_price", advancePrice);
            dataJson.put("sell_count", sellCount);

            //抢购商品
            dataJson.put("is_time_goods", isTimeGoods);
            dataJson.put("time_goods_id", timeGoodsId);
            dataJson.put("time_goods_store", timeGoodsStore);
            dataJson.put("time_price", timePrice);
            dataJson.put("is_time_goods_shell", isTimeGoodsShell);

            dataJson.put("banner_img_list", bannerImgList);
            dataJson.put("goods_img_list", goodsInfoImgList);
            dataJson.put("goods_sku_list", skuList);

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