package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderAddr;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.task.service.bean.DetailData;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.*;
import com.yufan.task.dao.addr.IAddrDao;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.task.service.impl.IBusinessService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
@Service("create_order")
public class CreateOrder implements IResultOut {

    private Logger LOG = Logger.getLogger(CreateOrder.class);

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

    @Autowired
    private IBusinessService iBusinessService;


    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            Integer userAddrId = data.getInteger("user_addr_id");//用户收货地址
            Integer postWay = data.getInteger("post_way");
            Integer shopId = data.getInteger("shop_id");
            Integer businessType = data.getInteger("business_type");//1正常下单2抢购3预定4租赁
            BigDecimal orderPrice = data.getBigDecimal("order_price");//订单支付价格
            BigDecimal realPrice = data.getBigDecimal("real_price");//订单实际价格
            BigDecimal goodsPriceAll = data.getBigDecimal("goods_price_all");//商品总价
            BigDecimal depositMoneyAll = data.getBigDecimal("deposit_price_all");//押金总价
            BigDecimal discountsPriceAll = data.getBigDecimal("discounts_price_all");//优惠总价
            //
            BigDecimal advancePriceAll = data.getBigDecimal("advance_price_all");//预付款总价

            BigDecimal postPrice = data.getBigDecimal("post_price");//
            Integer couponId = data.getInteger("coupon_id");// 优惠券标识
            JSONArray goodsList = data.getJSONArray("goods_list");
            if (null == businessType || null == postWay || null == goodsList || goodsList.size() == 0 || userId == null || userAddrId == null
                    || shopId == null || shopId == 0 || null == orderPrice || realPrice == null || null == goodsPriceAll || depositMoneyAll == null
                    || discountsPriceAll == null || postPrice == null || couponId == null || null == advancePriceAll) {
                return false;
            }
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Integer buyCount = goodsList.getJSONObject(i).getInteger("buy_count");
                if (goodsId == null || goodsId == 0 || buyCount == null || buyCount == 0 || userAddrId == 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOG.error("----checkParam-error---", e);
        }
        return false;
    }

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            // 收货地址信息数据
            Integer userAddrId = data.getInteger("user_addr_id");//用户收货地址ID(如果是邮件则为用户收货地址标识，否则为平台地址标识)
            String addrDetail = data.getString("addr_detail");//用户送货上门的时候填的详细地址
            String userName = data.getString("user_name");//收货人
            String userPhone = data.getString("user_phone");//收货人电话
            Integer userSex = data.getInteger("user_sex");//
            BigDecimal postPrice = data.getBigDecimal("post_price");//
            Integer postWay = data.getInteger("post_way");//收货方式 1.邮寄4.自取5配送地址
            // 收货地址校验
            //地址处理（全国地址）
            BigDecimal freight = new BigDecimal("0.00");// 邮费
            String userAddrDetail = "";//收货完整地址
            if (Constants.POST_WAY_1 != postWay) {
                //自取或者配送地址
                if (userSex == null) {
                    LOG.info("--userSex-不能为空-");
                    return packagMsg(ResultCode.NET_ERROR.getResp_code(), dataJson);
                }
                TbPlatformAddr platformAddr = iAddrDao.loadPlatformAddr(userAddrId);
                if (null == platformAddr) {
                    LOG.info("----地址查询不存在----platformAddr:" + userAddrId);
                    return packagMsg(ResultCode.NET_ERROR.getResp_code(), dataJson);
                }
                //当取货方式为配送时使用
                userAddrDetail = addrDetail;//收货完整地址
                freight = platformAddr.getFreight();
            } else {
                //邮寄地址
                TbUserAddr userAddr = iAddrDao.loadUserAddrById(userAddrId);
                if (null == userAddr) {
                    LOG.info("----地址查询不存在----userAddrId:" + userAddrId);
                    return packagMsg(ResultCode.NET_ERROR.getResp_code(), dataJson);
                }
                userName = userAddr.getUserName();
                userPhone = userAddr.getUserPhone();
                userAddrDetail = userAddr.getAddrName();
                //查询运费
                //运费
                Map<Integer, BigDecimal> addrIdsFreightMap = new HashMap<>();
                //查询运费
                String addrIds = userAddr.getAreaIds();
                if (StringUtils.isNotEmpty(addrIds)) {
                    String regionIds = addrIds.replace("-", ",");
                    List<Map<String, Object>> listAddrs = iAddrDao.queryAddrByRegionIds(regionIds, Constants.USER_SNS_STATUS_1);
                    for (int i = 0; i < listAddrs.size(); i++) {
                        int regionId = Integer.parseInt(listAddrs.get(i).get("region_id").toString());
                        BigDecimal freight_ = new BigDecimal(listAddrs.get(i).get("freight").toString());
                        addrIdsFreightMap.put(regionId, freight_);
                    }
                }
                freight = iBusinessService.findPostPrice(addrIdsFreightMap, addrIds.split("-"));
            }
            LOG.info("-------查询运费为------" + freight);
            if (postPrice.compareTo(freight) != 0) {
                LOG.info("----运费查询不匹配----");
                return packagMsg(ResultCode.NET_ERROR.getResp_code(), dataJson);
            }
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(userAddrDetail)) {
                LOG.info("--userName和userPhone-不能为空-");
                return packagMsg(ResultCode.NET_ERROR.getResp_code(), dataJson);
            }
            OrderAddr orderAddr = new OrderAddr();
            orderAddr.setAddrId(userAddrId);
            orderAddr.setAddrDetail(userAddrDetail);
            orderAddr.setPhone(userPhone);
            orderAddr.setUserName(userName);
            orderAddr.setPostPrice(postPrice);
            orderAddr.setUserSex(userSex);
            orderAddr.setPostWay(postWay);
            return createOrder(data, orderAddr);
        } catch (Exception e) {
            LOG.error("-----error-----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Transactional
    public synchronized String createOrder(JSONObject data, OrderAddr orderAddr) {
        JSONObject dataJson = new JSONObject();
        try {
            Integer userId = data.getInteger("user_id");
            Integer shopId = data.getInteger("shop_id");
            String userRemark = StringUtils.isEmpty(data.getString("user_remark")) ? "" : data.getString("user_remark");
            // 订单信息数据
            BigDecimal orderPrice = data.getBigDecimal("order_price");//订单支付价格
            BigDecimal realPrice = data.getBigDecimal("real_price");//订单实际价格
            BigDecimal goodsPriceAll = data.getBigDecimal("goods_price_all");//商品总价
            BigDecimal depositMoneyAll = data.getBigDecimal("deposit_price_all");//押金总价
            BigDecimal discountsPriceAll = data.getBigDecimal("discounts_price_all");//优惠总价
            BigDecimal advancePriceAll = data.getBigDecimal("advance_price_all");//预付款总价(邮费+商品预付款总和)
            Integer couponId = data.getInteger("coupon_id");// 优惠券标识
            // 商品信息数据 "goods_list":{"goods_id":100, "time_goods_id":0,"buy_count":10,"sku_id":0}
            JSONArray goodsList = data.getJSONArray("goods_list");
            // 收货地址信息数据
            Integer userAddrId = orderAddr.getAddrId();//用户收货地址ID(如果是邮件则为用户收货地址标识，否则为平台地址标识)
            String userAddrDetail = orderAddr.getAddrDetail();//用户送货上门的时候填的详细地址
            String userName = orderAddr.getUserName();//收货人
            String userPhone = orderAddr.getPhone();//收货人电话
            Integer userSex = orderAddr.getUserSex();//
            Integer postWay = orderAddr.getPostWay();
            BigDecimal postPrice = orderAddr.getPostPrice();//邮费
            //查询db商品信息
            Map<Integer, Map<String, Object>> goodsMap = new HashMap<>();//商品
            Map<Integer, Map<String, Object>> goodsSkuMap = new HashMap<>();//商品sku
            Map<Integer, Map<String, Object>> timeGoodsMap = new HashMap<>();//抢购商品
            // 查询商品信息
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
            iBusinessService.findCheckGoodsInfo(goodsIds, skuIds, timeGoodsIds, goodsMap, goodsSkuMap, timeGoodsMap);
            LOG.info("---开始校验---");
            int resultCode = iBusinessService.checkGoodsInfo(goodsList, goodsMap, goodsSkuMap, timeGoodsMap, userId);
            if (resultCode != ResultCode.OK.getResp_code()) {
                return packagMsg(resultCode, dataJson);
            }
            //查询店铺
            TbShop shop = iShopJapDao.findOne(shopId);
            if (null == shop || shop.getStatus() != 1) {
                LOG.info("-----------店铺查询无效-----------shopId:" + shopId);
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            //
            List<DetailData> detailList = new ArrayList<>();
            // 校验订单价格  "goods_list":{"goods_id":100, "time_goods_id":0,"buy_count":10,"sku_id":0,"cart_id":22}
            BigDecimal dbGoodsAllPrice = BigDecimal.ZERO;// 商品总价格
            BigDecimal dbGoodsAllTruePrice = BigDecimal.ZERO;// 商品原总价格
            BigDecimal dbDepositAllMoney = BigDecimal.ZERO;// 押金总价格
            BigDecimal dbAdvancePriceAll = BigDecimal.ZERO; // (邮费+商品预付款总和) = 预付款
            // 商品优惠价格
            BigDecimal goodsDiscountsPrice = BigDecimal.ZERO;
            // 优惠券+商品优惠总额
            BigDecimal dbDiscountsPrice = BigDecimal.ZERO;// 优惠券总价格
            if (couponId != null && couponId > 0) {
                // 查询优惠券获取优惠价格 userId+couponId
            }
            String cartIds = "";
            int orderCount = 0;
            for (int i = 0; i < goodsList.size(); i++) {
                Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                Map<String, Object> goods = goodsMap.get(goodsId);
                BigDecimal dbGoodsTrueMoney = new BigDecimal(goods.get("true_money").toString());
                BigDecimal dbGoodsPurchasePrice = new BigDecimal(goods.get("purchase_price").toString());
                Integer couponId_ = Integer.parseInt(goods.get("coupon_id").toString());
                String goodsIntro = goods.get("intro") == null ? "" : goods.get("intro").toString();
                String isYuding = goods.get("is_yuding").toString();
                String dbGoodsImg = goods.get("goods_img").toString();
                String goodsSpec = "";
                String goodsSpecName = "";
                String goodsSpecNameStr = "";
                Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                Integer timeGoodsId = goodsList.getJSONObject(i).getInteger("time_goods_id");
                BigDecimal buyCount = new BigDecimal(goodsList.getJSONObject(i).getString("buy_count"));
                Integer cartId = goodsList.getJSONObject(i).getInteger("cart_id");
                if (null != cartId && cartId > 0) {
                    cartIds = cartIds + cartId + ",";
                }
                orderCount = orderCount + buyCount.intValue();
                //
                BigDecimal dbPrice = BigDecimal.ZERO;// 销售价格
                if (null != timeGoodsId && timeGoodsId > 0) {
                    // 获取抢购商品
                    Map<String, Object> timeGoods = timeGoodsMap.get(timeGoodsId);
                    dbPrice = new BigDecimal(timeGoods.get("time_price").toString());
                } else {
                    if (skuId != null && skuId > 0) {
                        // sku商品
                        Map<String, Object> goodsSku = goodsSkuMap.get(skuId);
                        dbPrice = new BigDecimal(goodsSku.get("now_money").toString());
                        dbGoodsImg = goodsSku.get("sku_img").toString();
                        dbGoodsImg = StringUtils.isEmpty(dbGoodsImg) ? goods.get("goods_img").toString() : dbGoodsImg;
                        dbGoodsTrueMoney = new BigDecimal(goodsSku.get("true_money").toString());
                        dbGoodsPurchasePrice = new BigDecimal(goodsSku.get("purchase_price").toString());
                        goodsSpec = goodsSku.get("prop_code").toString();
                        goodsSpecName = goodsSku.get("prop_code_name").toString();
                        goodsSpecNameStr = goodsSku.get("goods_spec_name_str").toString();
                    } else {
                        // 校验商品信息部分
                        dbPrice = new BigDecimal(goods.get("now_money").toString());
                    }
                }
                // 商品总价格
                dbGoodsAllPrice = dbGoodsAllPrice.add(dbPrice.multiply(buyCount));
                dbGoodsAllTruePrice = dbGoodsAllTruePrice.add(dbGoodsTrueMoney.multiply(buyCount));
                // 押金总价格
                BigDecimal depositMoney = new BigDecimal(goods.get("deposit_money").toString());
                dbDepositAllMoney = dbDepositAllMoney.add(depositMoney.multiply(buyCount));
                // 预付款
                BigDecimal advancePrice = new BigDecimal(goods.get("advance_price").toString());
                dbAdvancePriceAll = dbAdvancePriceAll.add(advancePrice.multiply(buyCount));// 预付款*数量 = 预付款总和
                // 订单详情
                String goodsName = goods.get("goods_name").toString();
                //
                TbOrderDetail orderDetail = new TbOrderDetail();
                orderDetail.setGoodsId(goodsId);
                orderDetail.setShopId(shopId);
                orderDetail.setIsCoupon(couponId_);
                orderDetail.setGoodsName(goodsName);
                orderDetail.setShopName(shop.getShopName());
                orderDetail.setGoodsCount(buyCount.intValue());
                orderDetail.setDetailStatus(0);
                orderDetail.setCreatetime(new Timestamp(new Date().getTime()));
                orderDetail.setLastaltertime(new Timestamp(new Date().getTime()));
                orderDetail.setDepositPrice(depositMoney.multiply(buyCount));//押金总和
                orderDetail.setLastalterman("");
                orderDetail.setRemark("");
                orderDetail.setCartId(cartId);
                orderDetail.setOutCode("");
                orderDetail.setTimePrice(dbPrice);
                orderDetail.setTimeGoodsId(timeGoodsId == null ? 0 : timeGoodsId);
                orderDetail.setSaleMoney(dbPrice);
                orderDetail.setGoodsImg(dbGoodsImg);
                orderDetail.setGoodsTrueMoney(dbGoodsTrueMoney);
                orderDetail.setGoodsPurchasePrice(dbGoodsPurchasePrice);
                orderDetail.setGoodsSpec(goodsSpec);
                orderDetail.setGoodsSpecName(goodsSpecName);
                orderDetail.setGoodsSpecNameStr(goodsSpecNameStr);
                orderDetail.setSkuId(skuId == null ? 0 : skuId);
                orderDetail.setGoodsIntro(goodsIntro);
                DetailData detailData = new DetailData();
                detailData.setDetail(orderDetail);
                // 添加详情属性
                List<TbOrderDetailProperty> propertyList = new ArrayList<>();
                TbOrderDetailProperty property1 = new TbOrderDetailProperty();
                property1.setPropertyKey("is_yuding");
                property1.setPropertyValue(isYuding);
                property1.setPropertyType(0);
                property1.setRemark("");
                propertyList.add(property1);
                //
                detailData.setProperties(propertyList);
                detailList.add(detailData);
            }
            // 商品优惠总额
            goodsDiscountsPrice = dbGoodsAllTruePrice.subtract(dbGoodsAllPrice);
            BigDecimal dbDiscountsPriceAll = dbDiscountsPrice.add(goodsDiscountsPrice);
            // (邮费+商品预付款总和) = 预付款
            // 校验订单价格（订单总价格）
            boolean flag = false;
            // 校验订单价格（订单优惠总价格）
            if (dbDiscountsPriceAll.compareTo(discountsPriceAll) != 0) {
                LOG.info("----- 校验订单价格（订单优惠总价格）不一致-------");
                flag = true;
            }
            BigDecimal dbOrderPriceAll = postPrice.add(dbGoodsAllTruePrice).add(dbDepositAllMoney);// 邮费+商品原总价格+押金总价格
            BigDecimal dbOrderRealPriceAll = dbOrderPriceAll.subtract(dbDiscountsPriceAll);

            BigDecimal needpayPrice = BigDecimal.ZERO;
            // price1 = 邮费+商品总价 实际支付
            BigDecimal price1 = postPrice.add(dbGoodsAllPrice);// 入账的总金额 = 邮费 + 商品现价总额
            // 优惠券优惠金额不能大于 price1
            if (dbDiscountsPrice.compareTo(price1) > 0) {
                LOG.info("------ 优惠券优惠金额不能大于 price1 --------");
                flag = true;
            }
            if (dbDiscountsPrice.compareTo(price1) >= 0) {
                // 免费(用户只需要付押金)
                dbOrderPriceAll = dbDepositAllMoney;
                dbOrderRealPriceAll = dbDepositAllMoney;
                dbAdvancePriceAll = dbDepositAllMoney;
            } else {
                if (dbOrderPriceAll.compareTo(orderPrice) != 0) {
                    LOG.info("----- 校验订单价格（订单总价格）不一致-------");
                    flag = true;
                }
                // 校验订单价格（订单实际支付价格）
                if (dbOrderRealPriceAll.compareTo(realPrice) != 0) {
                    LOG.info("----- 校验订单价格（订单实际支付价格）不一致-------");
                    flag = true;
                }
                // 校验订单价格（订单商品总价格）
                if (goodsPriceAll.compareTo(dbGoodsAllPrice) != 0) {
                    LOG.info("----- 校验订单价格（订单商品总价格）不一致-------");
                    flag = true;
                }
                // 校验订单价格（订单商品押金总价格）
                if (depositMoneyAll.compareTo(dbDepositAllMoney) != 0) {
                    LOG.info("----- 校验订单价格（订单商品押金总价格）不一致-------");
                    flag = true;
                }
                // 校验订单价格（订单预付款总价格）
                if (advancePriceAll.compareTo(dbAdvancePriceAll) != 0) {
                    LOG.info("----- 校验订单价格（订单预付款总价格）不一致-------");
                    flag = true;
                }
                if (flag) {
                    return packagMsg(ResultCode.ORDWE_PRICE_ERROR.getResp_code(), dataJson);
                }
                needpayPrice = dbOrderRealPriceAll.multiply(dbAdvancePriceAll);
            }
            if (needpayPrice.compareTo(BigDecimal.ZERO) < 0) {
                return packagMsg(ResultCode.ORDWE_PRICE_ERROR.getResp_code(), dataJson);
            }

            //
            if (StringUtils.isNotEmpty(cartIds)) {
                cartIds = cartIds.substring(0, cartIds.length() - 1);
            }
            //生成订单
            TbOrder order = new TbOrder();
            String orderNo = CommonMethod.randomStr("");
            String serviceRemark = "";
            //0待付款1已付款3已失败4待发货5待收货6已完成7已取消8已删除9退款中10已退款
            Integer orderStatus = dbOrderPriceAll.compareTo(new BigDecimal("0")) == 0 ? Constants.ORDER_STATUS_2 : Constants.ORDER_STATUS_0;

            Timestamp orderTime = new Timestamp(new Date().getTime());
            String discountsRemark = "";
            order.setUserId(userId);
            order.setOrderNo(orderNo);
            order.setOrderCount(orderCount);
            order.setOrderPrice(dbOrderPriceAll);
            order.setRealPrice(dbOrderRealPriceAll);
            order.setOrderFrom("");
            order.setAdvancePrice(dbAdvancePriceAll);
            order.setNeedpayPrice(needpayPrice);
            order.setUserName(userName);
            order.setUserPhone(userPhone);
            order.setUserAddr(userAddrDetail);
            order.setUserAddrId(userAddrId);
//            order.setAdvancePayWay(advancePayWay);
            order.setAdvancePayCode("");
            //order.setAdvancePayTime(advancePayTime);
            order.setPayCode("");
            //order.setPayTime(payTime);
            order.setTradeChannel("");
            order.setPostPrice(postPrice);
            order.setPostWay(postWay);
//            order.setCompanyCode("");
            order.setPostNo("");
            order.setUserRemark(userRemark);
            order.setServiceRemark(serviceRemark);
            order.setOrderStatus(orderStatus);
            order.setOrderTime(orderTime);
            //order.setPostTime(postTime);
            order.setBusinessType(getBusinessType(orderCount, goodsList, goodsMap));
            order.setDiscountsId(couponId);
            order.setDiscountsPrice(discountsPriceAll);
            order.setDiscountsRemark(discountsRemark);
            order.setTradeNo("");
            order.setRefundPrice(BigDecimal.ZERO);
            order.setRefundRemark("");
            order.setRemark("");
            order.setPostMan("");
            order.setPostPhone("");
            order.setStatusOpration(0);//订单状态操作标识 0用户1后台
            order.setShopId(shopId);
            //order.setFinishTime(finishTime);
            order.setLastaltertime(orderTime);
            order.setLastalterman("");
            order.setUserReadMark(1);//用户打开标记 0 已读 1未读
            order.setUserSex(userSex);
            int orderId = iOrderDao.createOrder(order, detailList, new HashMap<>());
            LOG.info("---------------orderId=" + orderId);
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            long t = System.currentTimeMillis();
            if (t % 2 == 0) {
                LOG.info("===============begin========模拟支付成功===============================");
                OrderPaySuccess addPrivateGoods = (OrderPaySuccess) applicationContext.getBean("order_pay_success");
                ReceiveJsonBean receiveJsonBean = new ReceiveJsonBean();
                receiveJsonBean.setUserId(userId);
                JSONObject data1 = new JSONObject();
                data1.put("userId", userId);
                data1.put("orderNo", orderNo);
                receiveJsonBean.setData(data1);
                addPrivateGoods.getResult(receiveJsonBean);
                LOG.info("============end===========模拟支付成功===============================");
            }
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            //测试
            if (orderId > 0) {
                //删除购物车
                if (StringUtils.isNotEmpty(cartIds)) {
                    iOrderDao.deleteShopCartByCartIds(userId, cartIds, 3);
                }

                for (int i = 0; i < goodsList.size(); i++) {
                    Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
                    BigDecimal buyCount = new BigDecimal(goodsList.getJSONObject(i).getString("buy_count"));
                    Integer skuId = goodsList.getJSONObject(i).getInteger("sku_id");
                    Integer timeGoodsId = goodsList.getJSONObject(i).getInteger("time_goods_id");
                    if (null != timeGoodsId && timeGoodsId > 0) {
                        LOG.info("----删除抢购商品库存----timeGoodsId:" + timeGoodsId + "  goodsCount:" + buyCount);
                        iGoodsDao.subtractTimeGoodsStore(timeGoodsId, buyCount.intValue());
                    }
                    if (null != skuId && skuId > 0) {
                        LOG.info("----删除商品sku库存----goodsId:" + goodsId + "  skuId:" + skuId + "  buyCount:" + buyCount);
                        iGoodsDao.subtractGoodsSkuStore(goodsId, skuId, buyCount.intValue());
                    }
                    LOG.info("----删除商品库存----goodsId:" + goodsId + "  goodsCount:" + buyCount);
                    iGoodsDao.subtractGoodsStore(goodsId, buyCount.intValue());
                }
                dataJson.put("order_no", orderNo);
                dataJson.put("order_id", order.getOrderId());
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            LOG.info("------订单创建失败-----------");
            return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private int getBusinessType(int orderCount, JSONArray goodsList, Map<Integer, Map<String, Object>> goodsMap) {
        Integer businessType = 1;//1正常下单2抢购3预定4租赁
        int yudingCount = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            Integer goodsId = goodsList.getJSONObject(i).getInteger("goods_id");
            Map<String, Object> goods = goodsMap.get(goodsId);
            Integer isYuding = Integer.parseInt(goods.get("is_yuding").toString());
            Integer buyCount = Integer.parseInt(goodsList.getJSONObject(i).getString("buy_count"));
            if (isYuding == 1) {
                yudingCount = yudingCount + buyCount;
            }
        }
        if (orderCount == yudingCount) {
            businessType = 3;
        }
        return businessType;
    }
}
