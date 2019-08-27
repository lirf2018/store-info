package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.param.IParamDao;
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
 * 创建时间:  2019/8/27 15:41
 * 功能介绍:
 */
@Service("query_order_list")
public class QueryOrderList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryOrderList.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IParamDao iParamDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            //查询订单状态
            List<Map<String, Object>> paramList = iParamDao.queryParamListMap("order_status", Constants.DATA_STATUS_YX);
            Map<String, String> statusName = new HashMap<>();
            for (int i = 0; i < paramList.size(); i++) {
                String key = paramList.get(i).get("param_key").toString();
                String value = paramList.get(i).get("param_value").toString();
                statusName.put(key, value);
            }


            Integer userId = data.getInteger("user_id");
            Integer status = data.getInteger("status");
            Integer currePage = data.getInteger("curre_page");
            Integer pageSize = data.getInteger("page_size");
            PageInfo page = iOrderDao.queryUserOrderList(currePage, pageSize, userId, status);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> orderData = page.getResultListMap();
            String orderIds = "";
            for (int i = 0; i < orderData.size(); i++) {
                String orderId = orderData.get(i).get("order_id").toString();
                orderIds = orderIds + orderId + ",";
            }
            if (StringUtils.isNotEmpty(orderIds)) {
                orderIds = orderIds.substring(0, orderIds.length() - 1);
                //查询订单详情
                List<Map<String, Object>> orderDetailList = iOrderDao.queryOrderDetailListmap(orderIds);
                for (int i = 0; i < orderData.size(); i++) {
                    Map<String, Object> mapOrder = new HashMap<>();
                    String orderId = orderData.get(i).get("order_id").toString();
                    String orderPrice = orderData.get(i).get("order_price").toString();
                    String orderCount = orderData.get(i).get("order_count").toString();
                    String orderStatus = orderData.get(i).get("order_status").toString();
                    String shopName = orderData.get(i).get("shop_name").toString();
                    String shopLogo = orderData.get(i).get("shop_logo").toString();
                    mapOrder.put("order_id", Integer.parseInt(orderId));
                    mapOrder.put("order_price", new BigDecimal(orderPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
                    mapOrder.put("order_count", Integer.parseInt(orderCount));
                    mapOrder.put("order_status", Integer.parseInt(orderStatus));
                    mapOrder.put("order_status_name", statusName.get(orderStatus));
                    mapOrder.put("shop_name", shopName);
                    mapOrder.put("shop_logo", shopLogo);
                    List<Map<String, Object>> detailList = new ArrayList<>();
                    for (int j = 0; j < orderDetailList.size(); j++) {
                        if (orderId.equals(orderDetailList.get(j).get("order_id").toString())) {
                            continue;
                        }
                        String goodsImg = orderDetailList.get(j).get("goods_img").toString();
                        String goodsId = orderDetailList.get(j).get("goods_id").toString();
                        String goodsName = orderDetailList.get(j).get("goods_name").toString();
                        String saleMoney = orderDetailList.get(j).get("sale_money").toString();
                        String goodsCount = orderDetailList.get(j).get("goods_count").toString();
                        String goodsSpecNameStr = orderDetailList.get(j).get("goods_spec_name_str").toString();
                        Map<String, Object> map = new HashMap<>();
                        map.put("goods_img", goodsImg);
                        map.put("goods_id", Integer.parseInt(goodsId));
                        map.put("goods_name", goodsName);
                        map.put("sale_money", new BigDecimal(saleMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
                        map.put("goods_count", Integer.parseInt(goodsCount));
                        map.put("goods_spec_name_str", goodsSpecNameStr);
                        detailList.add(map);
                    }
                    mapOrder.put("detail_list", detailList);
                    outList.add(mapOrder);
                }
            }
            dataJson.put("has_next", page.isHasNext());
            dataJson.put("curre_page", page.getCurrePage());
            dataJson.put("page_size", page.getPageSize());
            dataJson.put("order_list", outList);
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
            Integer userId = data.getInteger("user_id");
            if (null == userId) {
                return false;
            }
            Integer currePage = data.getInteger("curre_page");
            if (null == currePage) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
