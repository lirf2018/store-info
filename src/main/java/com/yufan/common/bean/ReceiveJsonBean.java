package com.yufan.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JSON内容
 *
 * @author yink
 */
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

    private HttpServletRequest request;
    private HttpServletResponse response;

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

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
