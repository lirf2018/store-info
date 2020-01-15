package com.yufan.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.MD5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            MyMap map = new MyMap();
            //系统参数
            map.put("sid", bean.getSid());
            map.put("secretKey", appsecret);
            map.put("timestamp", bean.getTimestamp());

            JSONObject json = bean.getData();
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //只取data第一层数据
                if (null != v && !(v instanceof JSONArray) && v.toString().indexOf("{") == -1 && v.toString().indexOf("[") == -1) {
                    map.put(k.toString(), v);
                }
            }
            String sign = MD5.enCodeStandard(HelpCommon.getSign(map) + appsecret);
            if (null != getSign && getSign.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
