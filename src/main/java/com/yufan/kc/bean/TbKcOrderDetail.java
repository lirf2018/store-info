package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/11/16 23:34
 * 功能介绍:
 */
@Entity
@Table(name = "tb_kc_order_detail", schema = "store_kc_db", catalog = "")
public class TbKcOrderDetail {
    private int detailId;
    private Integer orderId;
    private Integer goodsId;
    private Integer buyCount;
    private String goodsCode;
    private String shopCode;
    private String goodsName;
    private BigDecimal salePriceTrue;
    private BigDecimal salePrice;
    private BigDecimal memberPrice;
    private BigDecimal discountsPrice;
    private Timestamp discountsStartTime;
    private Timestamp discountsEndTime;
    private String goodsUnitName;
    private Integer unitCount;
    private Byte isDiscounts;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;
    private Integer status;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "detail_id", nullable = false)
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    @Basic
    @Column(name = "order_id", nullable = true)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "goods_id", nullable = true)
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "buy_count", nullable = true)
    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
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
    @Column(name = "shop_code", nullable = true, length = 50)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Basic
    @Column(name = "goods_name", nullable = true, length = 100)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Basic
    @Column(name = "sale_price_true", nullable = true, precision = 2)
    public BigDecimal getSalePriceTrue() {
        return salePriceTrue;
    }

    public void setSalePriceTrue(BigDecimal salePriceTrue) {
        this.salePriceTrue = salePriceTrue;
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
    @Column(name = "member_price", nullable = true, precision = 2)
    public BigDecimal getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(BigDecimal memberPrice) {
        this.memberPrice = memberPrice;
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
    @Column(name = "discounts_start_time", nullable = true)
    public Timestamp getDiscountsStartTime() {
        return discountsStartTime;
    }

    public void setDiscountsStartTime(Timestamp discountsStartTime) {
        this.discountsStartTime = discountsStartTime;
    }

    @Basic
    @Column(name = "discounts_end_time", nullable = true)
    public Timestamp getDiscountsEndTime() {
        return discountsEndTime;
    }

    public void setDiscountsEndTime(Timestamp discountsEndTime) {
        this.discountsEndTime = discountsEndTime;
    }

    @Basic
    @Column(name = "goods_unit_name", nullable = true, length = 20)
    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
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
    @Column(name = "is_discounts", nullable = true)
    public Byte getIsDiscounts() {
        return isDiscounts;
    }

    public void setIsDiscounts(Byte isDiscounts) {
        this.isDiscounts = isDiscounts;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "last_update_time", nullable = true)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbKcOrderDetail that = (TbKcOrderDetail) o;
        return detailId == that.detailId &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(buyCount, that.buyCount) &&
                Objects.equals(goodsCode, that.goodsCode) &&
                Objects.equals(shopCode, that.shopCode) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(salePriceTrue, that.salePriceTrue) &&
                Objects.equals(salePrice, that.salePrice) &&
                Objects.equals(memberPrice, that.memberPrice) &&
                Objects.equals(discountsPrice, that.discountsPrice) &&
                Objects.equals(discountsStartTime, that.discountsStartTime) &&
                Objects.equals(discountsEndTime, that.discountsEndTime) &&
                Objects.equals(goodsUnitName, that.goodsUnitName) &&
                Objects.equals(unitCount, that.unitCount) &&
                Objects.equals(isDiscounts, that.isDiscounts) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detailId, orderId, goodsId, buyCount, goodsCode, shopCode, goodsName, salePriceTrue, salePrice, memberPrice, discountsPrice, discountsStartTime, discountsEndTime, goodsUnitName, unitCount, isDiscounts, createTime, lastUpdateTime);
    }
}
