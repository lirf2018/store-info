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
@Table(name = "tb_kc_goods", schema = "store_kc_db", catalog = "")
public class TbKcGoods {
    private int goodsId;
    private String goodsPy;
    private String goodsCode;
    private String goodsName;
    private Integer store;
    private Integer storeWarning;
    private BigDecimal salePrice;
    private BigDecimal memberPrice;
    private BigDecimal discountsPrice;
    private Timestamp discountsStartTime;
    private Timestamp discountsEndTime;
    private Byte status;
    private Timestamp createTime;
    private String createUserName;
    private Timestamp lastUpdateTime;
    private String goodsUnit;
    private Integer unitCount;
    private Byte isDiscounts;
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "goods_id", nullable = false)
    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "goods_py", nullable = true, length = 30)
    public String getGoodsPy() {
        return goodsPy;
    }

    public void setGoodsPy(String goodsPy) {
        this.goodsPy = goodsPy;
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
    @Column(name = "goods_name", nullable = true, length = 100)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Basic
    @Column(name = "store", nullable = true)
    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    @Basic
    @Column(name = "store_warning", nullable = true)
    public Integer getStoreWarning() {
        return storeWarning;
    }

    public void setStoreWarning(Integer storeWarning) {
        this.storeWarning = storeWarning;
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
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
    @Column(name = "create_user_name", nullable = true, length = 50)
    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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
    @Column(name = "goods_unit", nullable = true, length = 20)
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
    @Column(name = "is_discounts", nullable = true)
    public Byte getIsDiscounts() {
        return isDiscounts;
    }

    public void setIsDiscounts(Byte isDiscounts) {
        this.isDiscounts = isDiscounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbKcGoods tbKcGoods = (TbKcGoods) o;
        return goodsId == tbKcGoods.goodsId &&
                Objects.equals(goodsPy, tbKcGoods.goodsPy) &&
                Objects.equals(goodsCode, tbKcGoods.goodsCode) &&
                Objects.equals(goodsName, tbKcGoods.goodsName) &&
                Objects.equals(store, tbKcGoods.store) &&
                Objects.equals(storeWarning, tbKcGoods.storeWarning) &&
                Objects.equals(salePrice, tbKcGoods.salePrice) &&
                Objects.equals(memberPrice, tbKcGoods.memberPrice) &&
                Objects.equals(discountsPrice, tbKcGoods.discountsPrice) &&
                Objects.equals(discountsStartTime, tbKcGoods.discountsStartTime) &&
                Objects.equals(discountsEndTime, tbKcGoods.discountsEndTime) &&
                Objects.equals(status, tbKcGoods.status) &&
                Objects.equals(createTime, tbKcGoods.createTime) &&
                Objects.equals(createUserName, tbKcGoods.createUserName) &&
                Objects.equals(lastUpdateTime, tbKcGoods.lastUpdateTime) &&
                Objects.equals(goodsUnit, tbKcGoods.goodsUnit) &&
                Objects.equals(unitCount, tbKcGoods.unitCount) &&
                Objects.equals(isDiscounts, tbKcGoods.isDiscounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, goodsPy, goodsCode, goodsName, store, storeWarning, salePrice, memberPrice, discountsPrice, discountsStartTime, discountsEndTime, status, createTime, createUserName, lastUpdateTime, goodsUnit, unitCount, isDiscounts);
    }
}
