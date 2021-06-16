package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.pojo.TbShop;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/15
 */
@Service("query_qr_detail")
public class QueryQRDetail implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryQRDetail.class);

    @Autowired
    private ICouponDao iCouponDao;

    @Autowired
    private IShopJapDao shopJapDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer qrId = data.getInteger("qrId");
            TbCouponDownQr couponDownQr = iCouponDao.loadCouponDownQr(qrId);
            if (couponDownQr == null) {
                return packagMsg(ResultCode.COUPON_NOT_EXIST.getResp_code(), dataJson);
            }
            // 判断是否能领取并生成优惠券
            JSONObject checkResult = null;

            //
            dataJson.put("couponDownQr", couponDownQr);
            dataJson.put("imgPath", Constants.IMG_WEB_URL);
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

            Integer userId = data.getInteger("userId");
            Integer qrId = data.getInteger("qrId");
            if (null == userId || qrId == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}