package com.yufan.task.service.impl.secondgoods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.secondgoods.ISecondGoodsDao;
import com.yufan.utils.Constants;
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
 * 创建时间:  2019/8/31 20:23
 * 功能介绍: 查询闲菜列表
 */
@Service("query_xc_list")
public class QuerySecondGoodsList implements IResultOut {

    private Logger LOG = Logger.getLogger(QuerySecondGoodsList.class);

    @Autowired
    private ISecondGoodsDao iSecondGoodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            GoodsCondition goodsCondition = JSONObject.toJavaObject(data, GoodsCondition.class);
            PageInfo page = iSecondGoodsDao.loadGoodsList(goodsCondition);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> listData = page.getResultListMap();
            for (int i = 0; i < listData.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("goods_id", Integer.parseInt(listData.get(i).get("id").toString()));
                map.put("goods_name", listData.get(i).get("goods_name"));
                map.put("goods_img", Constants.IMG_WEB_URL + listData.get(i).get("goods_img"));
                map.put("now_price", new BigDecimal(listData.get(i).get("now_price").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                map.put("read_num", Integer.parseInt(listData.get(i).get("read_num").toString()));
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
