package com.yufan.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/3 13:52
 * 功能介绍:
 */
@Data
public class ConditionCommon {

    @JSONField(name = "curre_page")
    private Integer currePage;

    @JSONField(name = "page_size")
    private Integer pageSize;

    @JSONField(name = "classify_code")
    private String classifyCode;

    @JSONField(name = "classify_name")
    private String classifyName;

    @JSONField(name = "supplier_name")
    private String supplierName;

    @JSONField(name = "supplier_code")
    private String supplierCode;

    @JSONField(name = "supplier_addr")
    private String supplierAddr;

    @JSONField(name = "supplier_phone")
    private String supplierPhone;

    @JSONField(name = "param_name")
    private String paramName;

    @JSONField(name = "param_code")
    private String paramCode;

    @JSONField(name = "param_key")
    private String paramKey;

    @JSONField(name = "goods_code")
    private String goodsCode;

    @JSONField(name = "shop_code")
    private String shopCode;

    @JSONField(name = "goods_name")
    private String goodsName;

    @JSONField(name = "income_type")
    private Integer incomeType;

    @JSONField(name = "status")
    private Integer status;

    @JSONField(name = "is_matching")
    private Integer isMatching;

    @JSONField(name = "goods_unit")
    private String goodsUnit;

    @JSONField(name = "unit_count")
    private Integer unitCount;

    @JSONField(name = "shop_code_date")
    private String shopCodeDate;

    @JSONField(name = "search_type")
    private Integer searchType;

    /**
     * 促销
     */
    @JSONField(name = "is_discounts")
    private Integer isDiscounts;

    @JSONField(name = "goods_py")
    private String goodsPy;


    @JSONField(name = "order_no")
    private String orderNo;

    @JSONField(name = "user_phone")
    private String userPhone;

    @JSONField(name = "discounts_no")
    private String discountsNo;

    @JSONField(name = "table_name")
    private String tableName;

    @JSONField(name = "search_absolute")
    private Integer searchAbsolute;

    @JSONField(name = "pay_method")
    private Integer payMethod;

    @JSONField(name = "order_status")
    private Integer orderStatus;

    @JSONField(name = "report_type")
    private String reportType;

    @JSONField(name = "report_condition")
    private String reportCondition;

    @JSONField(name = "report_year")
    private String reportYear;
}
