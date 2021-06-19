package com.yufan.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JSON内容
 *
 * @author yink
 */
@Data
public class ReceiveJsonBean {

    /**
     * 请求对象
     */
    private String req_type;
    /**
     * 应用ID
     */
    @JSONField(name = "sid")
    private String sid;
    /**
     * 时间戳
     */
    @JSONField(name = "timestamp")
    private Long timestamp;
    /**
     * 校验的sign
     */
    @JSONField(name = "sign")
    private String sign;

    /**
     * 数据
     */
    private JSONObject data;

    // 其它参数
    private String keyCome;
    private Integer userId;

    private HttpServletRequest request;
    private HttpServletResponse response;


    /**
     * 验证系统参数非空
     *
     * @return
     */
    public Integer getCheckEmptyValue() {
        if (null == req_type || "".equals(req_type) || "null".equals(req_type)) {
            return 1;
        }
        if (null == sid || "".equals(sid) || "null".equals(sid)) {
            return 2;
        }
        if (null == timestamp || "".equals(timestamp) || "null".equals(timestamp)) {
            return 4;
        }
        if (null == sign || "".equals(sign) || "null".equals(sign)) {
            return 5;
        }
        if (null == data) {
            return 3;
        }
        return 0;
    }
}
