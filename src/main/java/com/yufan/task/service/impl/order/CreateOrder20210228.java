package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.*;
import com.yufan.task.dao.addr.IAddrDao;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/16 19:51
 * 功能介绍: 创建订单
 */
@Deprecated
public class CreateOrder20210228 implements IResultOut {

    private Logger LOG = Logger.getLogger(CreateOrder20210228.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private IAddrDao iAddrDao;

    @Autowired
    private IShopJapDao iShopJapDao;

    @Autowired
    private ICategoryDao iCategoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            return createOrder(data);
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
            Integer userAddrId = data.getInteger("user_addr_id");//用户收货地址
            Integer postWay = data.getInteger("post_way") ;
            Integer shopId = data.getInteger("shop_id");
            BigDecimal orderPrice = data.getBigDecimal("order_price");//订单支付价格
            BigDecimal realPrice = data.getBigDecimal("real_price");//订单实际价格
            BigDecimal goodsPriceAll = data.getBigDecimal("goods_price_all");//商品总价
            JSONArray goodsList = data.getJSONArray("goods_list");
            if (null == postWay || null == goodsList || goodsList.size() == 0 || userId == null || userId == 0) {
                return false;
            }
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer goodsCount = goodsList.getJSONObject(i).getInteger("goods_count");
                Integer timeGoodsId = data.getInteger("time_goods_id");
                if (goodsId == null || goodsId == 0 || goodsCount == null || goodsCount == 0 || userAddrId == null || userAddrId == 0
                        || shopId == null || shopId == 0 || null == orderPrice || realPrice == null || null == goodsPriceAll) {
                    return false;
                }
                if (timeGoodsId != null && timeGoodsId > 0 && goodsList.size() > 1) {
                    LOG.info("---------抢购商品goodsList有误----------");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOG.error("----checkParam-error---", e);
        }
        return false;
    }


    /**
     * 创建订单
     * {
     * "user_id": 1,
     * "time_goods_id": 0,
     * "user_addr_id": 10,
     * "shop_id": 1,
     * "order_price": 23.02,
     * "real_price": 63.00,
     * "goods_price_all": 65,
     * "discounts_id": 0,
     * "goods_list": {
     * "goods_id": 100,
     * "goods_count": 10,
     * "sku_id": 0,
     * "cart_id": 10,
     * "detail_prop": [{
     * "property_key": "iccid",
     * "property_value": "84852",
     * "remark": "001"
     * }]* 	}
     * }
     *
     * @return
     */
    @Transactional
    public synchronized String createOrder(JSONObject data) {
        JSONObject dataJson = new JSONObject();
        try {
            Integer userId = data.getInteger("user_id");
            Integer timeGoodsId = data.getInteger("time_goods_id");
            JSONArray goodsList = data.getJSONArray("goods_list");
            Integer userAddrId = data.getInteger("user_addr_id");//用户收货地址ID
            Integer shopId = data.getInteger("shop_id");
            BigDecimal orderPrice = data.getBigDecimal("order_price");//订单支付价格
            BigDecimal realPrice = data.getBigDecimal("real_price");//订单实际价格
            BigDecimal goodsPriceAll = data.getBigDecimal("goods_price_all");//商品总价
            Integer discountsId = data.getInteger("discounts_id");//优惠券标识
            BigDecimal discountsPrice = new BigDecimal("0.00");//优惠金额
            String userName = data.getString("user_name");
            String userPhone = data.getString("user_phone");

            /**
             * 收货方式 1.邮寄4.自取5配送 postWay
             */
            Integer postWay = data.getInteger("post_way");//收货方式 1.邮寄4.自取5配送地址查询用户信息失败-

            //地址处理（全国地址）
            BigDecimal freight = new BigDecimal("0.00");// 邮费
            String userAddrDetail = "";//收货完整地址

            if (Constants.POST_WAY_1 != postWay) {
                //自取或者配送地址
                TbPlatformAddr platformAddr = iAddrDao.loadPlatformAddr(userAddrId);
                if (null == platformAddr) {
                    LOG.info("----地址查询不存在----platformAddr:" + userAddrId);
                    return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
                }
                //当取货方式为配送时使用
                String peisongAddrDetail = data.getString("peisong_addr_detail") == null ? "" : data.getString("peisong_addr_detail");
                userAddrDetail = platformAddr.getDetailAddr() + peisongAddrDetail;//收货完整地址
                freight = platformAddr.getFreight();
            } else {
                //邮寄地址
                TbUserAddr userAddr = iAddrDao.loadUserAddrById(userAddrId);
                if (null == userAddr) {
                    LOG.info("----地址查询不存在----userAddrId:" + userAddrId);
                    return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
                }
                userName = userAddr.getUserName();
                userPhone = userAddr.getUserPhone();
                userAddrDetail = userAddr.getAddrName();

                //查询运费
                String regionCodes = userAddr.getAreaIds();
                String addrIdsArray[] = regionCodes.split("-");
                regionCodes = regionCodes.replace("-", "','");
                List<Map<String, Object>> listAddrs = iAddrDao.queryAddrByRegionIds(regionCodes,Constants.USER_SNS_STATUS_1);
                //运费
                Map<String, String> addrIdsFreightMap = new HashMap<>();
                for (int i = 0; i < listAddrs.size(); i++) {
                    String regionCode = listAddrs.get(i).get("region_code").toString();
                    BigDecimal freight_ = new BigDecimal(listAddrs.get(i).get("freight").toString());
                    if (freight_.compareTo(new BigDecimal("0")) > 0) {
                        addrIdsFreightMap.put(regionCode, freight_.toString());
                    }
                }
                //县
                String freight3 = addrIdsArray[2];
                //市
                String freight2 = addrIdsArray[1];
                //省
                String freight1 = addrIdsArray[0];
                if (addrIdsFreightMap.get(freight3) != null) {
                    freight = new BigDecimal(addrIdsFreightMap.get(freight3));
                } else if (addrIdsFreightMap.get(freight2) != null) {
                    freight = new BigDecimal(addrIdsFreightMap.get(freight2));
                } else if (addrIdsFreightMap.get(freight1) != null) {
                    freight = new BigDecimal(addrIdsFreightMap.get(freight1));
                }

            }
            LOG.info("-------查询运费为------" + freight);
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(userAddrDetail)) {
                LOG.info("----地址查询用户信息失败----");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }


            int orderCount = 0;
            //校验值
            BigDecimal orderPriceCheck = new BigDecimal("0.00");//订单支付价格
            BigDecimal realPriceCheck = new BigDecimal("0.00");//订单实际价格
            //查询店铺
            TbShop shop = iShopJapDao.findOne(shopId);
            if (null == shop || shop.getStatus() != 1) {
                LOG.info("-----------店铺查询无效-----------shopId:" + shopId);
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            String goodsIds = "";
            String skuIds = "";
            String cartIds = "";

            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                Integer cartId = goodsList.getJSONObject(i).getInteger("cart_id");
                if (null != skuId && skuId > 0) {
                    skuIds = skuIds + skuId + ",";
                }
                if (null != cartId && cartId > 0) {
                    cartIds = cartIds + cartId + ",";
                }
                goodsIds = goodsIds + goodsId + ",";
            }
            if (StringUtils.isNotEmpty(cartIds)) {
                cartIds = cartIds.substring(0, cartIds.length() - 1);
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
            Map<String, String> propValueMap = new HashMap<>();//属性属性值
            if (StringUtils.isNotEmpty(skuIds)) {
                String propCodes = "";
                skuIds = skuIds.substring(0, skuIds.length() - 1);
                List<Map<String, Object>> list = iGoodsDao.queryTbGoodsSkuListMapBySkuIds(skuIds);//查询商品sku列表
                for (int i = 0; i < list.size(); i++) {
                    int goodsId = Integer.parseInt(list.get(i).get("goods_id").toString());
                    int skuId = Integer.parseInt(list.get(i).get("sku_id").toString());
                    String propCode = list.get(i).get("prop_code").toString();
                    goodsSkuMap.put(goodsId + "-" + skuId, list.get(i));
                    propCodes = propCodes + propCode;
                }
                propCodes = propCodes.substring(0, propCodes.length() - 1);
                propCodes = propCodes.replace(";", ",");
                //查询属性属性值
                List<Map<String, Object>> itemPropList = iCategoryDao.loadPropValueItem(propCodes);
                for (int i = 0; i < itemPropList.size(); i++) {
                    String valueId = itemPropList.get(i).get("value_id").toString();
                    String propName = itemPropList.get(i).get("prop_name").toString();
                    String valueName = itemPropList.get(i).get("value_name").toString();
                    propValueMap.put(valueId, propName + "`" + valueName);
                }
            }
            LOG.info("------优惠价格--------" + discountsPrice);
            LOG.info("---开始校验(库存和限购)---");
            //判断库存
            BigDecimal timePrice = new BigDecimal("0.00");
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


                //校验价格
                timePrice = timeGoods.getTimePrice();
                BigDecimal goodsPriceAllCheck = timePrice.multiply(new BigDecimal(goodsCount));//商品总价
                if (goodsPriceAllCheck.compareTo(goodsPriceAll) != 0) {
                    LOG.info("--------抢购商品总价格校验失败-------goodsPriceAllCheck:" + goodsPriceAllCheck);
                    return packagMsg(ResultCode.CHECK_FIND.getResp_code(), dataJson);
                }
                //订单实际价格
                realPriceCheck = goodsPriceAllCheck.add(freight);
                if (realPriceCheck.compareTo(realPrice) != 0) {
                    LOG.info("--------抢购订单实际价格校验失败-------realPriceCheck:" + realPriceCheck);
                    return packagMsg(ResultCode.CHECK_FIND.getResp_code(), dataJson);
                }
                //订单支付价格
                orderPriceCheck = goodsPriceAllCheck.add(freight).subtract(discountsPrice);
                if (orderPriceCheck.compareTo(new BigDecimal(0.00)) < 0) {
                    LOG.info("--------支付价格小于0,自动转换为0------orderPriceCheck:" + orderPriceCheck);
                    orderPriceCheck = new BigDecimal("0.00");
                }
                if (orderPriceCheck.compareTo(orderPrice) != 0) {
                    LOG.info("--------抢购订单支付价格校验失败-------orderPriceCheck:" + orderPriceCheck);
                    return packagMsg(ResultCode.CHECK_FIND.getResp_code(), dataJson);
                }
                orderCount = goodsCount;
            }
            LOG.info("---开始校验商品---");
            List<TbOrderDetail> detailList = new ArrayList<>();
            Map<String, JSONArray> detailPropMap = new HashMap<>();//订单详情属性
            BigDecimal goodsPriceAllCheck = new BigDecimal("0.00");
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer goodsCount = goodsList.getJSONObject(i).getInteger("goods_count");
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                JSONArray detailPropArray = goodsList.getJSONObject(i).getJSONArray("detail_prop");
                String propCode = "";

                Map<String, Object> goods = goodsMap.get(goodsId);//商品信息
                if (goods == null) {
                    LOG.info("-----查询商品不存在----goodsId: " + goodsId);
                    return packagMsg(ResultCode.GOODSINFO_NOT_EXIST.getResp_code(), dataJson);
                }

                //设置订单详情
                TbOrderDetail orderDetail = new TbOrderDetail();
                String goodsName = goods.get("goods_name").toString();
                String goodsImg = goods.get("goods_img").toString();
                BigDecimal saleMoney = new BigDecimal(goods.get("now_money").toString());
                BigDecimal goodsTrueMoney = new BigDecimal(goods.get("true_money").toString());
                BigDecimal goodsPurchasePrice = new BigDecimal(goods.get("purchase_price").toString());

                String goodsSpec = "";
                String goodsSpecName = "";
                String goodsSpecNameStr = "";

                int isSingle = Integer.parseInt(goods.get("is_single").toString());
                BigDecimal goodsPrice = new BigDecimal(goods.get("now_money").toString());//商品销售价格
                int goodsNum = Integer.parseInt(goods.get("goods_num").toString());

                if (isSingle == 0 && (skuId == null || skuId == 0)) {
                    LOG.info("------商品信息有误-----goodsId: " + goodsId);
                    return packagMsg(ResultCode.GOODSINFO_NOT_EXIST.getResp_code(), dataJson);
                }
                //商品sku
                if (skuId != null && skuId > 0) {
                    Map<String, Object> goodsSku = goodsSkuMap.get(goodsId + "-" + skuId);
                    if (null == goodsSku) {
                        LOG.info("------商品sku信息有误-----goodsId: " + goodsId + "   skuId:" + skuId);
                        return packagMsg(ResultCode.GOODSINFO_NOT_EXIST.getResp_code(), dataJson);
                    }
                    goodsNum = Integer.parseInt(goodsSku.get("sku_num").toString());
                    propCode = goodsSku.get("prop_code").toString();
                    goodsPrice = new BigDecimal(goodsSku.get("now_money").toString());//sku销售价格
                    if (goodsCount > goodsNum) {
                        LOG.info("-------商品sku库存不足---------goodsId: " + goodsId + "   skuId:" + skuId + "  skuNum:" + goodsNum);
                        return packagMsg(ResultCode.GOODS_STORE_NOENOUGH.getResp_code(), dataJson);
                    }
                    //如果是sku则以sku为准
                    goodsImg = goodsSku.get("sku_img").toString();
                    saleMoney = new BigDecimal(goodsSku.get("now_money").toString());
                    goodsTrueMoney = new BigDecimal(goodsSku.get("true_money").toString());
                    goodsPurchasePrice = new BigDecimal(goodsSku.get("purchase_price").toString());


                    goodsSpec = goodsSku.get("prop_code").toString();
                    String array[] = goodsSpec.split(";");
                    //获取对应名称
                    goodsSpecName = propValueMap.get(array[0]).split("`")[1] + ";" + propValueMap.get(array[1]).split("`")[1] + ";" + propValueMap.get(array[2]).split("`")[1] + ";";
                    goodsSpecNameStr = propValueMap.get(array[0]).replace("`", ":") + ";" + propValueMap.get(array[1]).replace("`", ":") + ";" + propValueMap.get(array[2]).replace("`", ":") + ";";
                }
                //商品
                detailPropMap.put(goodsId + "-" + propCode + skuId, detailPropArray);
                if (goodsCount > goodsNum) {
                    LOG.info("-------商品库存不足---------goodsId: " + goodsId + "  goodsNum:" + goodsNum);
                    return packagMsg(ResultCode.GOODS_STORE_NOENOUGH.getResp_code(), dataJson);
                }

                //限购方式  限购方式: 1.每天一次2每月一次3.每年一次4不限购5只允许购买一次
                String limitBeginTime = String.valueOf(goods.get("limit_begin_time"));
                int limitWay = Integer.parseInt(goods.get("limit_way").toString());
                int limitNum = Integer.parseInt(goods.get("limit_num").toString());
                if (timeGoodsId == null || timeGoodsId == 0) {
                    orderCount = orderCount + goodsCount;
                } else {
                    //抢购商品（商品）  抢购商品销售价格=抢购商品价格
                    saleMoney = timePrice;
                }
                //计算商品总价
                goodsPriceAllCheck = goodsPriceAllCheck.add(goodsPrice.multiply(new BigDecimal(goodsCount)));

                //设置订单详情

                BigDecimal depositPrice = new BigDecimal(goods.get("deposit_money").toString());
                BigDecimal depositPriceAll = depositPrice.multiply(new BigDecimal(goodsCount));
                Integer isCoupon = Integer.parseInt(goods.get("coupon_id").toString());
                Integer cartId = goodsList.getJSONObject(i).getInteger("cart_id");

                String shopName = shop.getShopName();
                String outCode = "";
                //Integer getAddrId;
                //String getAddrName;
                //Timestamp getTime;
                //Integer backAddrId;
                //String backAddrName;
                //Timestamp backTime;
                Integer detailStatus = 0;//商品详情状态 0未取货1已取货2已取消3已完成4已退货5已退款

                Timestamp createtime = new Timestamp(new Date().getTime());
                String lastalterman = "";
                String remark = "";
                orderDetail.setGoodsId(goodsId);
                orderDetail.setShopId(shopId);
                orderDetail.setIsCoupon(isCoupon > 0 ? 1 : 0);
                orderDetail.setGoodsName(goodsName);
                orderDetail.setShopName(shopName);
                orderDetail.setGoodsCount(goodsCount);
                orderDetail.setDetailStatus(detailStatus);
                orderDetail.setCreatetime(createtime);
                orderDetail.setLastaltertime(createtime);
                orderDetail.setDepositPrice(depositPriceAll);//押金总和
                orderDetail.setLastalterman(lastalterman);
                orderDetail.setRemark(remark);
                orderDetail.setCartId(cartId);
                orderDetail.setTimePrice(timePrice);
                orderDetail.setOutCode(outCode);
                orderDetail.setTimeGoodsId(timeGoodsId == null ? 0 : timeGoodsId);
                orderDetail.setSaleMoney(saleMoney);
                orderDetail.setGoodsImg(goodsImg);
                orderDetail.setGoodsTrueMoney(goodsTrueMoney);
                orderDetail.setGoodsPurchasePrice(goodsPurchasePrice);
                orderDetail.setGoodsSpec(goodsSpec);
                orderDetail.setGoodsSpecName(goodsSpecName);
                orderDetail.setGoodsSpecNameStr(goodsSpecNameStr);
                detailList.add(orderDetail);
            }
            //检验价格
            if (timeGoodsId == null || timeGoodsId == 0) {
                if (goodsPriceAllCheck.compareTo(goodsPriceAll) != 0) {
                    LOG.info("--------商品总价格校验失败-------goodsPriceAllCheck:" + goodsPriceAllCheck);
                    return packagMsg(ResultCode.CHECK_FIND.getResp_code(), dataJson);
                }
                //订单实际价格
                realPriceCheck = goodsPriceAllCheck.add(freight);
                if (realPriceCheck.compareTo(realPrice) != 0) {
                    LOG.info("--------订单实际价格校验失败-------realPriceCheck:" + realPriceCheck);
                    return packagMsg(ResultCode.CHECK_FIND.getResp_code(), dataJson);
                }
                //订单支付价格
                orderPriceCheck = goodsPriceAllCheck.add(freight).subtract(discountsPrice);
                if (orderPriceCheck.compareTo(new BigDecimal(0.00)) < 0) {
                    LOG.info("--------支付价格小于0,自动转换为0------orderPriceCheck:" + orderPriceCheck);
                    orderPriceCheck = new BigDecimal("0.00");
                }
                if (orderPriceCheck.compareTo(orderPrice) != 0) {
                    LOG.info("--------订单支付价格校验失败-------orderPriceCheck:" + orderPriceCheck);
                    return packagMsg(ResultCode.CHECK_FIND.getResp_code(), dataJson);
                }
            }
            //生成订单
            TbOrder order = new TbOrder();
            String orderNo = CommonMethod.randomStr("");
            String orderFrom = "";
            BigDecimal advancePrice = new BigDecimal("0.00");
            BigDecimal needpayPrice = new BigDecimal("0.00");

            Integer advancePayWay = null;
            String advancePayCode = "";
            //Timestamp advancePayTime = null;
            Integer payWay = null;
            String payCode = "";
            //Timestamp payTime = null;
            String tradeChannel = "";

            Integer companyCode = null;
            String postNo = "";
            String userRemark = StringUtils.isEmpty(data.getString("user_remark")) ? "" : data.getString("user_remark");
            String serviceRemark = "";
            Integer orderStatus = orderPriceCheck.compareTo(new BigDecimal("0")) == 0 ? Constants.ORDER_STATUS_1 : Constants.ORDER_STATUS_0;//0待付款1已付款3已失败4待发货5待收货6已完成7已取消8已删除9退款中10已退款
            Timestamp orderTime = new Timestamp(new Date().getTime());
            //Timestamp postTime = null;
            Integer businessType = data.containsKey("business_type") ? data.getInteger("business_type") : 1;//1正常下单2抢购3预定4租赁
            String discountsRemark = "";
            String tradeNo = "";
            BigDecimal refundPrice = new BigDecimal("0.00");
            String refundRemark = "";
            String remark = "";
            String postPhone = "";
            String postMan = "";
            Integer statusOpration = 0;//订单状态操作标识 0用户1后台
            //Timestamp finishTime = null;
            //Timestamp lastaltertime = null;
            String lastalterman = "";
            Integer userReadMark = 1;//用户打开标记 0 已读 1未读
            order.setUserId(userId);
            order.setOrderNo(orderNo);
            order.setOrderCount(orderCount);
            order.setOrderPrice(orderPriceCheck);
            order.setRealPrice(realPriceCheck);
            order.setOrderFrom(orderFrom);
            order.setAdvancePrice(advancePrice);
            order.setNeedpayPrice(needpayPrice);
            order.setUserName(userName);
            order.setUserPhone(userPhone);
            order.setUserAddr(userAddrDetail);
            order.setUserAddrId(userAddrId);
            order.setAdvancePayWay(advancePayWay);
            order.setAdvancePayCode(advancePayCode);
            //order.setAdvancePayTime(advancePayTime);
            order.setPayWay(payWay);
            order.setPayCode(payCode);
            //order.setPayTime(payTime);
            order.setTradeChannel(tradeChannel);
            order.setPostPrice(freight);
            order.setPostWay(postWay);
            order.setCompanyCode(companyCode);
            order.setPostNo(postNo);
            order.setUserRemark(userRemark);
            order.setServiceRemark(serviceRemark);
            order.setOrderStatus(orderStatus);
            order.setOrderTime(orderTime);
            //order.setPostTime(postTime);
            order.setBusinessType(businessType);
            order.setDiscountsId(discountsId == null ? 0 : discountsId);
            order.setDiscountsPrice(discountsPrice == null ? new BigDecimal("0.00") : discountsPrice);
            order.setDiscountsRemark(discountsRemark);
            order.setTradeNo(tradeNo);
            order.setRefundPrice(refundPrice);
            order.setRefundRemark(refundRemark);
            order.setRemark(remark);
            order.setPostMan(postMan);
            order.setPostPhone(postPhone);
            order.setStatusOpration(statusOpration);
            order.setShopId(shopId);
            //order.setFinishTime(finishTime);
            order.setLastaltertime(orderTime);
            order.setLastalterman(lastalterman);
            order.setUserReadMark(userReadMark);
            int index = iOrderDao.createOrder(order, detailList, detailPropMap);
            if (index > 0) {
                //删除购物车
                if (StringUtils.isNotEmpty(cartIds)) {
                    iOrderDao.deleteShopCartByCartIds(userId, cartIds, 3);
                }
                //修改库存
                if (null != timeGoodsId && timeGoodsId > 0) {
                    Integer goodsCount = goodsList.getJSONObject(0).getInteger("goods_count");
                    LOG.info("----删除抢购商品库存----timeGoodsId:" + timeGoodsId + "  goodsCount:" + goodsCount);
                    iGoodsDao.subtractTimeGoodsStore(timeGoodsId, goodsCount);
                } else {
                    for (int i = 0; i < goodsList.size(); i++) {
                        Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                        Integer goodsCount = goodsList.getJSONObject(i).getInteger("goods_count");
                        Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                        if (null != skuId && skuId > 0) {
                            LOG.info("----删除商品sku库存----goodsId:" + goodsId + "  skuId:" + skuId + "  goodsCount:" + goodsCount);
                            iGoodsDao.subtractGoodsSkuStore(goodsId, skuId, goodsCount);
                        } else {
                            LOG.info("----删除商品库存----goodsId:" + goodsId + "  goodsCount:" + goodsCount);
                            iGoodsDao.subtractGoodsStore(goodsId, goodsCount);
                        }
                    }
                }
                dataJson.put("order_no", orderNo);
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            LOG.info("------订单创建失败-----------");
            return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }
}
