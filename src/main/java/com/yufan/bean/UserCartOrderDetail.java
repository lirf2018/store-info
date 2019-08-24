package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:57
 * 功能介绍:
 */
@Setter
@Getter
public class UserCartOrderDetail {

    private int cartId;
    private Integer userId;
    private Integer goodsId;
    private String goodsName;
    private String goodsSpec;
    private String goodsSpecName;
    private Integer goodsCount;
    private BigDecimal goodsPrice;
    private BigDecimal trueMoney;
    private Integer status;
    private String goodsSpecNameStr;
    private Integer cartType;


}
