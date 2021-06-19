package com.yufan.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.yufan.utils.ResultCode;

public class ResponeUtil {


    /**
     * 组装返回参数
     *
     * @param reqCode
     * @param data
     * @return
     */
    public static String packagMsg(int reqCode, JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("resp_code", reqCode);
        json.put("resp_desc", ResultCode.getRespDesc(reqCode));
        if (null != data) {
            json.put("data", data);
        }

        return json.toString();
    }

    public static String packagMsg(int reqCode, String reqDesc, JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("resp_code", reqCode);
        json.put("resp_desc", reqDesc);
        if (null != data) {
            json.put("data", data);
        }

        return json.toString();
    }

    /**
     * @param obj
     * @return
     */
    public static String objToString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static int objToInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static double objToDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
