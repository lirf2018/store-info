package com.yufan.task.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbOrderCart;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.goods.IGoodsJpaDao;
import com.yufan.task.service.impl.Test;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 10:44
 * 功能介绍: 增加购物车
 */
@Service("add_order_cart")
public class AddOrderCart implements IResultOut {

    private Logger LOG = Logger.getLogger(Test.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private IGoodsJpaDao iGoodsJpaDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer goodsId = data.getInteger("goods_id");
            Integer userId = data.getInteger("user_id");
            Integer goodsCount = data.getInteger("goods_count") == 0 ? 1 : data.getInteger("goods_count");
            String goodsSpec = data.getString("goods_spec") == null ? "" : data.getString("goods_spec");
            String goodsSpecName = data.getString("goods_spec_name") == null ? "" : data.getString("goods_spec_name");
            String goodsSpecNameStr = data.getString("goods_spec_str") == null ? "" : data.getString("goods_spec_name_str");
            BigDecimal goodsPrice = data.getBigDecimal("goods_price");//销售价格
            BigDecimal trueMoney = data.getBigDecimal("true_money");//商品原价
            TbGoods goods = iGoodsJpaDao.getOne(goodsId);
            boolean flag = false;
            //查询用户购物车
            List<Map<String, Object>> cartList = iGoodsDao.queryUserOrderCart(userId, goodsId);
            for (int i = 0; i < cartList.size(); i++) {
                //c.cart_id,c.goods_id,c.goods_spec,c.goods_count,c.goods_price,c.true_money
                int cartId = Integer.parseInt(cartList.get(i).get("cart_id").toString());
                int goodsCount_ = Integer.parseInt(cartList.get(i).get("goods_count").toString());
                String goodsSpec_ = cartList.get(i).get("goods_spec").toString();
                int count = goodsCount_ + goodsCount;
                //判断是规格商品还是单品
                if (goods.getIsSingle() == 0) {
                    //sku商品    //判断规格
                    if (goodsSpec_.equals(goodsSpec)) {
                        iGoodsDao.updateOrderCart(cartId, count, goodsSpec, goodsSpecName, goodsSpecNameStr, goodsPrice, trueMoney);
                        flag = true;
                    }
                    break;
                } else {
                    //单品
                    iGoodsDao.updateOrderCart(cartId, count, goodsSpec, goodsSpecName, goodsSpecNameStr, goodsPrice, trueMoney);
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                //新增加购物车
                TbOrderCart orderCart = new TbOrderCart();
                orderCart.setUserId(userId);
                orderCart.setGoodsId(goodsId);
                orderCart.setGoodsName(goods.getGoodsName());
                orderCart.setGoodsImg(goods.getGoodsImg());
                orderCart.setGoodsSpec(goodsSpec);
                orderCart.setGoodsSpecName(goodsSpecName);
                orderCart.setGoodsCount(goodsCount);
                orderCart.setGoodsPrice(goodsPrice);
                orderCart.setTrueMoney(goods.getTrueMoney());
                orderCart.setShopId(goods.getShopId());
                orderCart.setCreatetime(new Timestamp(new Date().getTime()));
                orderCart.setStatus(1);
                orderCart.setLastaltertime(new Timestamp(new Date().getTime()));
                orderCart.setGoodsSpecNameStr(goodsSpecNameStr);
                iGoodsDao.saveOrderCart(orderCart);
            }
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
            Integer userId = data.getInteger("user_id");
            Integer goodsCount = data.getInteger("goods_count") == 0 ? 1 : data.getInteger("goods_count");
            BigDecimal goodsPrice = data.getBigDecimal("goods_price");//销售价格
            BigDecimal trueMoney = data.getBigDecimal("true_money");
            if (null == goodsId || null == userId || null == goodsCount || null == goodsPrice || null == trueMoney) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
