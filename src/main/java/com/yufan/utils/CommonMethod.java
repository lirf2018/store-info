package com.yufan.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/6 10:09
 * 功能介绍:
 */
public class CommonMethod {

    /**
     * 处理结果参数
     * @param code
     * @return
     */
    public static JSONObject packagMsg(String code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", OutCode.getMsg(code));
        return json;
    }

}
