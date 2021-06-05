package com.yufan.task.service.impl.shop;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbShop;
import com.yufan.task.dao.img.IImgDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/16
 */
@Service("find_shop_info")
public class FindShopInfo implements IResultOut {

    private Logger LOG = Logger.getLogger(FindShopInfo.class);

    @Autowired
    private IShopJapDao iShopJapDao;


    @Autowired
    private IImgDao iImgDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer shopId = data.getInteger("shopId");

            TbShop shop = iShopJapDao.findOne(shopId);
            // 查询图片
            List<Map<String, Object>> outImg = new ArrayList<>();
            //查询商品图片
            List<Map<String, Object>> imgListMap = iImgDao.queryTableRelImg(null, shopId, Constants.IMG_CLASSIFY_SHOP);
            for (int i = 0; i < imgListMap.size(); i++) {
                //1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
                String imgUrl = imgListMap.get(i).get("img_url").toString();
                JSONObject img = new JSONObject();
                img.put("imgUrl", StringUtils.isEmpty(imgUrl) ? "" : Constants.IMG_WEB_URL + imgUrl);
                outImg.add(img);
            }

            dataJson.put("shop", shop);
            dataJson.put("imgList", outImg);
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

            Integer shopId = data.getInteger("shopId");
            if (null == shopId) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}