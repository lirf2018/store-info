package com.yufan.task.service.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.task.dao.coupon.ICouponDao;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/19
 */
@Service("change_qr_code")
public class ChangeQrCode implements IResultOut {

    private Logger LOG = Logger.getLogger(ChangeQrCode.class);

    @Autowired
    private ICouponDao iCouponDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String qrCode = data.getString("qrCode");
            String checkCode = data.getString("checkCode");
            TbCouponDownQr qr = iCouponDao.loadCouponDownQrByQrCode(qrCode.trim());
            if (qr == null) {
                return packagMsg(ResultCode.QR_NOT_EXIST.getResp_code(), dataJson);
            }
            if (StringUtils.isNotEmpty(qr.getCheckCode()) && !qr.getCheckCode().equals(checkCode)) {
                return packagMsg(ResultCode.QRCHECK_NOT_EXIST.getResp_code(), dataJson);
            }
            // 记录状态 0生成二维码1已下载2已兑换3生成检验中4已过期失效
            if (qr.getRecodeState() == 2) {
                return packagMsg(ResultCode.QRCODE_ISBEAN_USE.getResp_code(), dataJson);
            } else if (qr.getRecodeState() == 0 || qr.getRecodeState() == 3 || qr.getRecodeState() == 4) {
                return packagMsg(ResultCode.QRCODE_OUT_TIME.getResp_code(), dataJson);
            }

            // 二维码每次生成的码有效时间
            long outTime = qr.getQrCodeOuttime().getTime();
            long nowTime = System.currentTimeMillis();
            if (nowTime > outTime) {
                return packagMsg(ResultCode.QRCODE_CHANGE_OUT_TIME.getResp_code(), dataJson);
            }
            // 对固定使用时间的优惠券处理 过期方式：0按兑换过期天计算过期时间1指定过期时间2指定使用时间
            if (qr.getAppointType() == 2) {
                String now = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);
                String changeOutDate = DatetimeUtil.timeStamp2Date(qr.getChangeOutDate().getTime(), DatetimeUtil.DEFAULT_DATE_FORMAT);
                if (!now.equals(changeOutDate)) {
                    return packagMsg(ResultCode.ODE_CANTUSE_TIME.getResp_code(), dataJson);
                }
            }
            int index = iCouponDao.changeQrRecordStatus(qr.getId());
            if (index == 0) {
                LOG.info("--------更新记录数为0---------" + qrCode);
                return packagMsg(ResultCode.QRCODE_OUT_TIME.getResp_code(), dataJson);
            }
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
            String qrCode = data.getString("qrCode");
            String keyCome = receiveJsonBean.getKeyCome();
            if (StringUtils.isEmpty(keyCome)) {
                if (null == userId) {
                    return false;
                }
            }
            if (StringUtils.isEmpty(qrCode)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}