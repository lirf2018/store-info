package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:11
 * 功能介绍:
 */
@Entity
@Table(name = "tb_sale_record", schema = "store_kc_db", catalog = "")
public class TbSaleRecord {
    private int id;
    private String goodsCode;
    private BigDecimal incomePrice;
    private BigDecimal salePrice;
    private BigDecimal discountsPrice;
    private BigDecimal memberPrice;
    private Byte saleCount;
    private String goodsUnit;
    private Integer unitCount;
    private BigDecimal realSalePrice;
    private BigDecimal discountPrice;
    private Timestamp saleTime;
    private String discountsRemark;
    private Byte status;
    private String orderNum;
    private String shopCode;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "goods_code", nullable = true, length = 50)
    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    @Basic
    @Column(name = "income_price", nullable = true, precision = 2)
    public BigDecimal getIncomePrice() {
        return incomePrice;
    }

    public void setIncomePrice(BigDecimal incomePrice) {
        this.incomePrice = incomePrice;
    }

    @Basic
    @Column(name = "sale_price", nullable = true, precision = 2)
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    @Basic
    @Column(name = "discounts_price", nullable = true, precision = 2)
    public BigDecimal getDiscountsPrice() {
        return discountsPrice;
    }

    public void setDiscountsPrice(BigDecimal discountsPrice) {
        this.discountsPrice = discountsPrice;
    }

    @Basic
    @Column(name = "member_price", nullable = true, precision = 2)
    public BigDecimal getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(BigDecimal memberPrice) {
        this.memberPrice = memberPrice;
    }

    @Basic
    @Column(name = "sale_count", nullable = true)
    public Byte getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Byte saleCount) {
        this.saleCount = saleCount;
    }

    @Basic
    @Column(name = "goods_unit", nullable = true, length = 10)
    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    @Basic
    @Column(name = "unit_count", nullable = true)
    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    @Basic
    @Column(name = "real_sale_price", nullable = true, precision = 2)
    public BigDecimal getRealSalePrice() {
        return realSalePrice;
    }

    public void setRealSalePrice(BigDecimal realSalePrice) {
        this.realSalePrice = realSalePrice;
    }

    @Basic
    @Column(name = "discount_price", nullable = true, precision = 2)
    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Basic
    @Column(name = "sale_time", nullable = true)
    public Timestamp getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Timestamp saleTime) {
        this.saleTime = saleTime;
    }

    @Basic
    @Column(name = "discounts_remark", nullable = true, length = 100)
    public String getDiscountsRemark() {
        return discountsRemark;
    }

    public void setDiscountsRemark(String discountsRemark) {
        this.discountsRemark = discountsRemark;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "order_num", nullable = true, length = 50)
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Basic
    @Column(name = "shop_code", nullable = true, length = 50)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbSaleRecord that = (TbSaleRecord) o;
        return id == that.id &&
                Objects.equals(goodsCode, that.goodsCode) &&
                Objects.equals(incomePrice, that.incomePrice) &&
                Objects.equals(salePrice, that.salePrice) &&
                Objects.equals(discountsPrice, that.discountsPrice) &&
                Objects.equals(memberPrice, that.memberPrice) &&
                Objects.equals(saleCount, that.saleCount) &&
                Objects.equals(goodsUnit, that.goodsUnit) &&
                Objects.equals(unitCount, that.unitCount) &&
                Objects.equals(realSalePrice, that.realSalePrice) &&
                Objects.equals(discountPrice, that.discountPrice) &&
                Objects.equals(saleTime, that.saleTime) &&
                Objects.equals(discountsRemark, that.discountsRemark) &&
                Objects.equals(status, that.status) &&
                Objects.equals(orderNum, that.orderNum) &&
                Objects.equals(shopCode, that.shopCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goodsCode, incomePrice, salePrice, discountsPrice, memberPrice, saleCount, goodsUnit, unitCount, realSalePrice, discountPrice, saleTime, discountsRemark, status, orderNum, shopCode);
    }
}
