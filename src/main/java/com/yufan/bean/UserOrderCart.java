package com.yufan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:56
 * 功能介绍:
 */
@Data
public class UserOrderCart {

//    @JSONField(name = "shop_id")
    private Integer shopId;

//    @JSONField(name = "shop_name")
    private String shopName;

//    @JSONField(name = "shop_logo")
    private String shopLogo;

    private boolean showCartEmptyFlag = true;// 默认没有商品

//    @JSONField(name = "cart_detail_list")
    private List<UserCartOrderDetail> cartDetailList;
}
