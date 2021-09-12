package com.yufan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/24 19:57
 * 功能介绍:
 */
@Data
public class UserCartOrderDetail {

    //    @JSONField(name = "cart_id")
    private int cartId;

    //    @JSONField(name = "user_id")
    private Integer userId;

    //    @JSONField(name = "goods_id")
    private Integer goodsId;

    //    @JSONField(name = "goods_name")
    private String goodsName;

    //    @JSONField(name = "goods_spec")
    private String goodsSpec;

    //    @JSONField(name = "goods_spec_name")
    private String goodsSpecName;

    //    @JSONField(name = "goods_count")
    private Integer goodsCount;

    //    @JSONField(name = "goods_price")
    private BigDecimal goodsPrice;

    //    @JSONField(name = "true_money")
    private BigDecimal trueMoney;

    //    @JSONField(name = "status")
    private Integer status;

    //    @JSONField(name = "goods_spec_name_str")
    private String goodsSpecNameStr;

    //    @JSONField(name = "cart_type")
    private Integer cartType;

    //    @JSONField(name = "goods_img")
    private String goodsImg;

    //    @JSONField(name = "is_single")
    private Integer isSingle;


    //    @JSONField(name = "sku_id")
    private Integer skuId;

    private Integer timeGoodsId;

    private Integer goodsType;
    
    private String rentPayTypeName;

}
