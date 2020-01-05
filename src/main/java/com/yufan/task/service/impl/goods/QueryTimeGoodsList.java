package com.yufan.task.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.goods.IGoodsDao;
import com.yufan.task.dao.param.IParamDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
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
 * 创建时间:  2019/8/26 14:06
 * 功能介绍: 查询抢购商品列表
 */
@Service("query_time_goods_list")
public class QueryTimeGoodsList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryGoodsList.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private IParamDao iParamDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        try {
            boolean hasTimeGoods = false;
            //查询参数中是否设置了抢购开始结束时间time_goods_time
            List<Map<String, Object>> listParam = iParamDao.queryParamListMap("time_goods_time", "time_goods_time", 1);
            if (null != listParam && listParam.size() > 0) {
                try {
                    String nowTime = DatetimeUtil.getNow();
                    String format = "yyyy-MM-dd HH:mm:ss";
                    String setStime = listParam.get(0).get("param_value").toString();
                    String setEtime = listParam.get(0).get("param_value1").toString();
                    if (DatetimeUtil.compareDate(nowTime, setStime, format) > -1 && DatetimeUtil.compareDate(nowTime, setEtime, format) < 1) {
                        LOG.info("-------------参数抢购时间范围内------------------");
                        hasTimeGoods = true;
                    } else {
                        LOG.info("-------参数中是否设置了抢购开始结束时间---不在当前时间内-------");
                    }
                } catch (Exception e) {
                    LOG.error("----设置参数抢购开始结束时间异常---", e);
                }
            }
            GoodsCondition goodsCondition = JSONObject.toJavaObject(receiveJsonBean.getData(), GoodsCondition.class);
            if (!hasTimeGoods) {
                dataJson.put("has_next", false);
                dataJson.put("curre_page", goodsCondition.getCurrePage());
                dataJson.put("page_size", goodsCondition.getPageSize());
                dataJson.put("goods_list", new ArrayList<>());
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            PageInfo page = iGoodsDao.loadTimeGoodsList(goodsCondition);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> listData = page.getResultListMap();
            for (int i = 0; i < listData.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("goods_id", Integer.parseInt(listData.get(i).get("goods_id").toString()));
                map.put("title", listData.get(i).get("title"));
                map.put("goods_name", listData.get(i).get("goods_name"));
                map.put("true_money", new BigDecimal(listData.get(i).get("true_money").toString()));//商品原价格
                map.put("now_money", new BigDecimal(listData.get(i).get("time_price").toString()));//抢购价格
                map.put("goods_img", listData.get(i).get("goods_img"));
                map.put("sell_count", Integer.parseInt(listData.get(i).get("sell_count").toString()));
                map.put("is_single", Integer.parseInt(listData.get(i).get("is_single").toString()));//如果为非单品 页面价格应该显示为多少起
                outList.add(map);
            }

            dataJson.put("has_next", page.isHasNext());
            dataJson.put("curre_page", page.getCurrePage());
            dataJson.put("page_size", page.getPageSize());
            dataJson.put("goods_list", outList);
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


            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}