package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 22:15
 * 功能介绍:
 */
@Entity
@Table(name = "tb_goods", schema = "store_kc_db", catalog = "")
public class TbGoods {
    private int goodsId;
    private Integer calssifyId;
    private String goodsPy;
    private String goodsCode;
    private String shopCode;
    private String goodsName;
    private String goodsUnit;
    private Integer store;
    private Integer storeWarning;
    private Integer incomeId;
    private Integer unitCount;
    private BigDecimal salePrice;
    private BigDecimal memberPrice;
    private BigDecimal discountsPrice;
    private Timestamp discountsStartTime;
    private Timestamp discountsEndTime;
    private Byte status;
    private Timestamp createTime;
    private String createUserName;
    private String remark;

    @Id
    @Column(name = "goods_id", nullable = false)
    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "calssify_id", nullable = true)
    public Integer getCalssifyId() {
        return calssifyId;
    }

    public void setCalssifyId(Integer calssifyId) {
        this.calssifyId = calssifyId;
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
    @Column(name = "goods_unit", nullable = true, length = 10)
    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
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
    @Column(name = "income_id", nullable = true)
    public Integer getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Integer incomeId) {
        this.incomeId = incomeId;
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
    @Column(name = "remark", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbGoods tbGoods = (TbGoods) o;
        return goodsId == tbGoods.goodsId &&
                Objects.equals(calssifyId, tbGoods.calssifyId) &&
                Objects.equals(goodsPy, tbGoods.goodsPy) &&
                Objects.equals(goodsCode, tbGoods.goodsCode) &&
                Objects.equals(shopCode, tbGoods.shopCode) &&
                Objects.equals(goodsName, tbGoods.goodsName) &&
                Objects.equals(goodsUnit, tbGoods.goodsUnit) &&
                Objects.equals(store, tbGoods.store) &&
                Objects.equals(storeWarning, tbGoods.storeWarning) &&
                Objects.equals(incomeId, tbGoods.incomeId) &&
                Objects.equals(unitCount, tbGoods.unitCount) &&
                Objects.equals(salePrice, tbGoods.salePrice) &&
                Objects.equals(memberPrice, tbGoods.memberPrice) &&
                Objects.equals(discountsPrice, tbGoods.discountsPrice) &&
                Objects.equals(discountsStartTime, tbGoods.discountsStartTime) &&
                Objects.equals(discountsEndTime, tbGoods.discountsEndTime) &&
                Objects.equals(status, tbGoods.status) &&
                Objects.equals(createTime, tbGoods.createTime) &&
                Objects.equals(createUserName, tbGoods.createUserName) &&
                Objects.equals(remark, tbGoods.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, calssifyId, goodsPy, goodsCode, shopCode, goodsName, goodsUnit, store, storeWarning, incomeId, unitCount, salePrice, memberPrice, discountsPrice, discountsStartTime, discountsEndTime, status, createTime, createUserName, remark);
    }
}
