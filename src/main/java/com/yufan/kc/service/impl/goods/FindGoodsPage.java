package com.yufan.kc.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ConditionCommon;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.dao.goods.GoodsDao;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/29 21:25
 * 功能介绍:
 */
@Service("kc_page_goods")
public class FindGoodsPage implements IResultOut {

    private Logger LOG = Logger.getLogger(FindGoodsPage.class);

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            ConditionCommon conditionCommon = JSONObject.toJavaObject(data, ConditionCommon.class);
            if(StringUtils.isNotEmpty(conditionCommon.getShopCodeDate())){
                Date shopCodeDate = DatetimeUtil.convertStrToDate(conditionCommon.getShopCodeDate());
                conditionCommon.setShopCodeDate(DatetimeUtil.convertDateToStr(shopCodeDate,"yyyyMMdd"));
            }
            PageInfo page = goodsDao.loadGoodsPage(conditionCommon);
            dataJson.put("goods_list", page.getResultListMap());
            dataJson.put("has_next", page.isHasNext());
            dataJson.put("total_num", page.getRecordSum());
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
