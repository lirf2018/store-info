package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_goods_price_record", schema = "store_kc_db", catalog = "")
public class TbGoodsPriceRecord {
    private int goodsId;
    private BigDecimal incomePrice;
    private BigDecimal salePrice;
    private BigDecimal discountsPrice;
    private BigDecimal memberPrice;
    private String goodsName;
    private String goodsCode;
    private String goodsUnit;
    private Integer unitCount;
    private Timestamp createTime;
    private Byte status;

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
    @Column(name = "goods_name", nullable = true, length = 100)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbGoodsPriceRecord that = (TbGoodsPriceRecord) o;
        return goodsId == that.goodsId &&
                Objects.equals(incomePrice, that.incomePrice) &&
                Objects.equals(salePrice, that.salePrice) &&
                Objects.equals(discountsPrice, that.discountsPrice) &&
                Objects.equals(memberPrice, that.memberPrice) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsCode, that.goodsCode) &&
                Objects.equals(goodsUnit, that.goodsUnit) &&
                Objects.equals(unitCount, that.unitCount) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, incomePrice, salePrice, discountsPrice, memberPrice, goodsName, goodsCode, goodsUnit, unitCount, createTime, status);
    }
}
