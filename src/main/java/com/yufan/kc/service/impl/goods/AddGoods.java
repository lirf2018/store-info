package com.yufan.kc.service.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.kc.bean.TbKcGoods;
import com.yufan.kc.dao.goods.GoodsDao;
import com.yufan.kc.dao.store.StoreInOutDao;
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
@Service("kc_add_goods")
public class AddGoods implements IResultOut {

    private Logger LOG = Logger.getLogger(AddGoods.class);

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private StoreInOutDao storeInOutDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            TbKcGoods goods = JSONObject.toJavaObject(data, TbKcGoods.class);
            goods.setLastUpdateTime(new Timestamp(new Date().getTime()));
            goods.setStatus((byte) 2);
            // 查询是否已经存在商品
            if (goods.getGoodsId() == 0 && goodsDao.checkGoods(goods.getGoodsCode())) {
                storeInOutDao.updateStoreMatching(goods.getGoodsCode(), 1);
                return packagMsg(ResultCode.GOODS_IS_EXIST.getResp_code(), dataJson);
            }
            // 价格校验（销售价格>=会员价格; 销售价格>=促销价格）
            if (goods.getSalePrice().compareTo(goods.getDiscountsPrice()) < 0 || goods.getSalePrice().compareTo(goods.getMemberPrice()) < 0) {
                return packagMsg(ResultCode.PRICE_ERROR.getResp_code(), dataJson);
            }
            // 如果不是促销,则销售价格==促销价格
            if (goods.getIsDiscounts().intValue() == 0 && goods.getSalePrice().compareTo(goods.getDiscountsPrice()) != 0) {
                return packagMsg(ResultCode.PRICE_ERROR2.getResp_code(), dataJson);
            }
            if (goods.getIsDiscounts().intValue() == 0) {
                goods.setDiscountsStartTime(null);
                goods.setDiscountsEndTime(null);
            }
            if (goods.getGoodsId() == 0) {
                goods.setCreateTime(new Timestamp(new Date().getTime()));
                goodsDao.saveGoods(goods);
            } else {
                goodsDao.updateGoods(goods);
            }
            //根据商品条形码更新商品对应入库为匹配
            storeInOutDao.updateStoreMatching(goods.getGoodsCode(), 1);
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