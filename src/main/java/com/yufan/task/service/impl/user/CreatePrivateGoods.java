package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.dao.common.ICommonDao;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbOrderDetailProperty;
import com.yufan.pojo.TbPrivateCustom;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.ResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 生成私人定制预约数据
 * @author: lirf
 * @time: 2021/6/24
 */
@Service("create_private_goods")
public class CreatePrivateGoods implements IResultOut {

    private Logger LOG = Logger.getLogger(CreatePrivateGoods.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private ICommonDao iCommonDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            Integer orderId = data.getInteger("orderId");
            List<Map<String, Object>> orderDetailList = iOrderDao.findOrderDetailPrivateGoodsList(orderId);
            if (CollectionUtils.isEmpty(orderDetailList)) {
                LOG.info("=====查询订单不存在或者不满足=====");
                return packagMsg(ResultCode.ORDER_STATUS_CANTNOT.getResp_code(), dataJson);
            }
            Integer userId_ = Integer.parseInt(orderDetailList.get(0).get("user_id").toString());
            if (null != userId && userId.intValue() != userId_) {
                LOG.info("=====查询订单用户id不一致=====");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            String orderNo = orderDetailList.get(0).get("order_no").toString();
            // 判断是否已生成
            List<TbPrivateCustom> list = iUserDao.loadPrivateCustom(orderNo);
            if (!CollectionUtils.isEmpty(list)) {
                LOG.info("-----------已生成数据,勿要重复生成------------");
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            // prop
            TbOrderDetailProperty property = new TbOrderDetailProperty();
            property.setOrderId(orderId);
            property.setPropertyType(0);
            property.setPropertyValue("1");//is_yuding=1 预约商品
            property.setPropertyKey("is_yuding");
            List<Map<String, Object>> propertyList = iOrderDao.findOrderDetailProp(property);
            if (CollectionUtils.isEmpty(propertyList)) {
                LOG.info("---------订单详情属性-propertyList不存在----------------");
                return packagMsg(ResultCode.ORDER_CONDITION_UNCREATE.getResp_code(), dataJson);
            }
            Map<String, String> detailConditionMap = new HashMap<>();
            for (int i = 0; i < propertyList.size(); i++) {
                String detailId = propertyList.get(i).get("detail_id").toString();
                detailConditionMap.put(detailId, detailId);
            }
            // 生成数据
            return createPrivateData(orderDetailList, detailConditionMap);

        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private synchronized String createPrivateData(List<Map<String, Object>> orderDetailList, Map<String, String> detailConditionMap) {
        JSONObject dataJson = new JSONObject();
        try {
            // 判断是否已生成
            String orderNo = orderDetailList.get(0).get("order_no").toString();
            List<TbPrivateCustom> list = iUserDao.loadPrivateCustom(orderNo);
            if (!CollectionUtils.isEmpty(list)) {
                LOG.info("-----createPrivateData------已生成数据,勿要重复生成------------");
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            for (int i = 0; i < orderDetailList.size(); i++) {
                String detailId = orderDetailList.get(i).get("detail_id").toString();
                if (detailConditionMap.get(detailId) == null) {
                    continue;
                }
                String getAddr = String.valueOf(orderDetailList.get(i).get("user_addr"));
                String payTime = String.valueOf(orderDetailList.get(i).get("pay_time"));
                Integer goodsId = Integer.parseInt(orderDetailList.get(i).get("goods_id").toString());
                Integer userId = Integer.parseInt(orderDetailList.get(i).get("user_id").toString());
                Integer skuId = Integer.parseInt(orderDetailList.get(i).get("sku_id").toString());
                Integer goodsCount = Integer.parseInt(orderDetailList.get(i).get("goods_count").toString());
                String goodsName = String.valueOf(orderDetailList.get(i).get("goods_name"));
                String goodsSpecName = String.valueOf(orderDetailList.get(i).get("goods_spec_name"));
                String goodsIntro = String.valueOf(orderDetailList.get(i).get("goods_intro"));
                Integer postWay = Integer.parseInt(orderDetailList.get(i).get("post_way").toString());
                for (int j = 0; j < goodsCount; j++) {
                    TbPrivateCustom custom = new TbPrivateCustom();
                    custom.setUserId(userId);
                    custom.setPayTime(new Timestamp(DatetimeUtil.convertStrToDate(payTime, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));
                    custom.setPrivateCode(orderNo);
                    custom.setRelType(0);// 关联类型 0 订单
                    custom.setCreateTime(now);
                    custom.setStatus(0);
                    custom.setYuyueCount(0);
                    custom.setContents(goodsIntro);
                    custom.setPostWay(postWay);
                    custom.setIsYuyue(0);
                    custom.setFlowStatus(0);
                    custom.setGoodsId(goodsId);
                    custom.setIndexSort(0);
                    custom.setGetAddr(getAddr);
                    String name = goodsName;
                    if (!StringUtils.isEmpty(goodsSpecName)) {
                        name = name + "【" + goodsSpecName + "】";
                    }
                    custom.setGoodsName(name);
                    custom.setSkuId(skuId);
                    iCommonDao.saveObj(custom);
                }
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer userId = data.getInteger("userId");
            Integer orderId = data.getInteger("orderId");
            if (null == orderId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}