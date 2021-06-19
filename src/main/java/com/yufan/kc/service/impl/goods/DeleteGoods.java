package com.yufan.kc.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbKcGoods;
import com.yufan.kc.dao.goods.GoodsDao;
import com.yufan.kc.dao.store.StoreInOutDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/29 21:25
 * 功能介绍:
 */
@Service("kc_del_goods")
public class DeleteGoods implements IResultOut {

    private Logger LOG = Logger.getLogger(DeleteGoods.class);

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private StoreInOutDao storeInOutDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            // goodsIds
            String goodsIds = data.getString("goods_ids");
            //根据商品条形码更新商品对应入库为未匹配
            storeInOutDao.updateStoreMatching2(goodsIds, 0);
            goodsDao.deleteGoods(goodsIds);

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
            String goodsIds = data.getString("goods_ids");
            if(StringUtils.isEmpty(goodsIds)){
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }


}