package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.order.IOrderDao;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.task.dao.user.IUserJpaDao;
import com.yufan.pojo.TbUserInfo;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 9:08
 * 功能介绍:
 */
@Service("query_user_center")
public class QueryUserCenter implements IResultOut {
    private Logger LOG = Logger.getLogger(QueryUserCenter.class);

    @Autowired
    private IUserJpaDao iUserJpaDao;

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IOrderDao iOrderDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer userId = data.getInteger("user_id");
            //查询用户信息
            TbUserInfo userInfo = iUserJpaDao.getOne(userId);
            if (null == userInfo) {
                return packagMsg(ResultCode.FAIL_USER_INVALIDATE.getResp_code(), dataJson);
            }

            //查询积分
            int jifen = iUserDao.userJifen(userId);
            //购物车
            int cartGoodsCount = iOrderDao.userCartCount(userId);

            //查询用户订单
            List<Map<String, Object>> userOrderListMap = iOrderDao.queryOrderPayAllListMap(userId);
            int c0 = 0;//0	待付款
            int c1 = 0;//1	已付款
            int c3 = 0;//3	已失败
            int c4 = 0;//4	待发货
            int c5 = 0; //5	待收货
            int c6 = 0; //6	已完成
            int c7 = 0;//7	已取消
            int c8 = 0; //8	已删除
            int c9 = 0; //9	退款中
            int c10 = 0;//10	已退款
            int c11 = 0;//11	处理中
            int c12 = 0;//12	还货中
            int c13 = 0;//13	已还货

            BigDecimal orderPriceAll = new BigDecimal(0);

            for (int i = 0; i < userOrderListMap.size(); i++) {
                int status = Integer.parseInt(userOrderListMap.get(i).get("order_status").toString());
                BigDecimal orderPrice = new BigDecimal(userOrderListMap.get(i).get("order_price").toString());
                switch (status) {
                    case 0:
                        c0++;
                        break;
                    case 1:
                        c1++;
                        break;
                    case 3:
                        c3++;
                        break;
                    case 4:
                        c4++;
                        break;
                    case 5:
                        c5++;
                        break;
                    case 6:
                        c6++;
                        orderPriceAll.add(orderPrice);
                        break;
                    case 7:
                        c7++;
                        break;
                    case 8:
                        c8++;
                        break;
                    case 9:
                        c9++;
                        break;
                    case 10:
                        c10++;
                        break;
                    case 11:
                        c11++;
                        break;
                    case 12:
                        c12++;
                        break;
                    case 13:
                        c13++;
                        break;
                }
            }
            dataJson.put("order_dfk", c0);
            dataJson.put("order_yfk", c1);
            dataJson.put("order_ysb", c3);
            dataJson.put("order_dfh", c4);
            dataJson.put("order_dsh", c5);
            dataJson.put("order_ywc", c6);
            dataJson.put("order_yqx", c7);
            dataJson.put("order_ysc", c8);
            dataJson.put("order_tkz", c9);
            dataJson.put("order_ytk", c10);
            dataJson.put("order_clz", c11);
            dataJson.put("order_hhz", c12);
            dataJson.put("order_yhh", c13);
            dataJson.put("order_price_all", orderPriceAll.setScale(2, BigDecimal.ROUND_HALF_UP));

            dataJson.put("user_jifen", jifen);
            dataJson.put("user_money", 0.00);//需要增加一张扣除详细表
            dataJson.put("user_img", StringUtils.isEmpty(userInfo.getUserImg()) ? "" : Constants.IMG_WEB_URL + userInfo.getUserImg());
            dataJson.put("member_id", userInfo.getMemberId());//会员卡号
            dataJson.put("nick_name", userInfo.getNickName());
            dataJson.put("cart_goods_count", cartGoodsCount);//购物车数量

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
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
