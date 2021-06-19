package com.yufan.common.utils;

import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.HelpCommon;

/**
 * @功能名称 Sign 检验
 * @作者 lirongfan
 * @时间 2016年9月13日 下午3:55:15
 */
public class VerifySign {

    /**
     * 签名校验
     *
     * @param bean      json内容
     * @param appsecret 密钥
     * @return
     */
    public static boolean checkSign(ReceiveJsonBean bean, String appsecret) {
        //传过来的sign
        try {
            String getSign = bean.getSign();
            String sign = HelpCommon.generateSign(appsecret, bean.getTimestamp(), bean.getData());
            if (null != getSign && getSign.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
