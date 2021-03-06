package com.yufan.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: lirf
 * @time: 2021/2/28
 */
@Data
public class OrderAddr {

    /**
     * 平台id或者用户收货地址id
     */
    private int addrId;

    private String userName;

    private String phone;

    private String addrDetail;

    private Integer userSex;

    private BigDecimal postPrice;

    private Integer postWay;


}
