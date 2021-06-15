package com.yufan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/15
 */
@Data
public class CouponCondition {

    @JSONField(name = "currePage")
    private Integer currePage;

    @JSONField(name = "pageSize")
    private Integer pageSize;

    private Integer show;

    private Integer couponId;

}
