package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.task.dao.shop.IShopJapDao;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
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
            Integer userId = data.getInteger("userId");
            String time = CommonMethod.qrChangeOutTime();
            iCouponDao.updateQrCodeChangeTime(userId, qrId, time);
            TbCouponDownQr qr = iCouponDao.loadCouponDownQrByQrId(qrId);
            if (qr == null) {
                return packagMsg(ResultCode.QR_NOT_EXIST.getResp_code(), dataJson);
            }
            if (userId != qr.getUserId().intValue()) {
                return packagMsg(ResultCode.QR_NOT_EXIST.getResp_code(), dataJson);
            }
            String changeOutDate = DatetimeUtil.timeStamp2Date(qr.getChangeOutDate().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT);
            // 对固定使用时间的优惠券处理 过期方式：0按兑换过期天计算过期时间1指定过期时间2指定使用时间
            int nowUseDate = 0;
            if (qr.getAppointType() == 2) {
                // 指定使用时间
                String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
                if (now.equals(changeOutDate)) {
                    nowUseDate = 1;
                }
            }
            // 记录状态 0生成二维码1已下载2已兑换3生成检验中4已过期失效
            dataJson.put("recodeStateName", "已失效");
            if (qr.getRecodeState() == 1) {
                long qrOutTime = qr.getChangeOutDate().getTime();
                // 过期日期
                String outDate = DatetimeUtil.timeStamp2Date(qrOutTime, DatetimeUtil.DEFAULT_DATE_FORMAT);
                String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
                if (DatetimeUtil.compareDate(now, outDate, DatetimeUtil.DEFAULT_DATE_FORMAT) < 1) {
                    dataJson.put("recodeStateName", "未使用");
                }
            } else if (qr.getRecodeState() == 2) {
                dataJson.put("recodeStateName", "已使用");
            }
            //
            dataJson.put("couponDownQr", qr);
            dataJson.put("changeOutDate", changeOutDate);
            dataJson.put("nowUseDate", nowUseDate);
            dataJson.put("time", time);
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