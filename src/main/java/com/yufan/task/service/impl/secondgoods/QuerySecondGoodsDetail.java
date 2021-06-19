package com.yufan.task.service.impl.secondgoods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.secondgoods.ISecondGoodsDao;
import com.yufan.task.dao.secondgoods.ISecondGoodsJpaDao;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/31 20:28
 * 功能介绍: 查询闲菜详情
 */
@Service("query_xc_detail")
public class QuerySecondGoodsDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QuerySecondGoodsDetail.class);

    @Autowired
    private ISecondGoodsJpaDao iSecondGoodsJpaDao;

    @Autowired
    private ISecondGoodsDao iSecondGoodsDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer goodsId = data.getInteger("id");
            TbSecondGoods secondGoods = iSecondGoodsJpaDao.findOne(goodsId);
            if (null == secondGoods || secondGoods.getStatus() != 1) {
                return packagMsg(ResultCode.GOODS_NOT_EXIST.getResp_code(), dataJson);
            }
            //更新访问数
            iSecondGoodsDao.UpdateSecondGoodsReadCount(goodsId);

            dataJson.put("goods_id", secondGoods.getGoodsId());
            dataJson.put("goods_name", secondGoods.getGoodsName());
            dataJson.put("goods_img", Constants.IMG_WEB_URL + secondGoods.getGoodsImg());
            dataJson.put("true_price", secondGoods.getTruePrice());
            dataJson.put("now_price", secondGoods.getNowPrice());
            dataJson.put("new_info", secondGoods.getNewInfo());
            dataJson.put("is_post", secondGoods.getIsPost());
            dataJson.put("about_price", secondGoods.getAboutPrice());
            dataJson.put("goods_info", secondGoods.getGoodsInfo());
            dataJson.put("goods_shop_code", secondGoods.getGoodsShopCode());
            String img1 = StringUtils.isEmpty(secondGoods.getImg1()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg1();
            String img2 = StringUtils.isEmpty(secondGoods.getImg2()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg2();
            String img3 = StringUtils.isEmpty(secondGoods.getImg3()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg3();
            String img4 = StringUtils.isEmpty(secondGoods.getImg4()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg4();
            dataJson.put("img1", img1);
            dataJson.put("img2", img2);
            dataJson.put("img3", img3);
            dataJson.put("img4", img4);
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
            Integer goodsId = data.getInteger("id");
            if (null == goodsId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
