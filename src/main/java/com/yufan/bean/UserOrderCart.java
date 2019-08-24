package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:56
 * 功能介绍:
 */
@Getter
@Setter
public class UserOrderCart {

    private Integer shopId;
    private String shopName;
    private String shopLogo;

    private List<UserCartOrderDetail> cartDetailList;
}
