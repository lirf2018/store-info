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
@Table(name = "tb_discounts_config", schema = "store_kc_db", catalog = "")
public class TbDiscountsConfig {
    private int discountsId;
    private String discountsCode;
    private String discountsDesc;
    private Integer goodsId;
    private Byte discountsType;
    private BigDecimal discountsPrice;
    private BigDecimal discountsPercent;
    private BigDecimal enoughMoney;
    private BigDecimal subtractMoney;
    private Byte status;
    private Timestamp passTime;
    private Timestamp createTime;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "discounts_id", nullable = false)
    public int getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(int discountsId) {
        this.discountsId = discountsId;
    }

    @Basic
    @Column(name = "discounts_code", nullable = true, length = 50)
    public String getDiscountsCode() {
        return discountsCode;
    }

    public void setDiscountsCode(String discountsCode) {
        this.discountsCode = discountsCode;
    }

    @Basic
    @Column(name = "discounts_desc", nullable = true, length = 100)
    public String getDiscountsDesc() {
        return discountsDesc;
    }

    public void setDiscountsDesc(String discountsDesc) {
        this.discountsDesc = discountsDesc;
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
    @Column(name = "discounts_type", nullable = true)
    public Byte getDiscountsType() {
        return discountsType;
    }

    public void setDiscountsType(Byte discountsType) {
        this.discountsType = discountsType;
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
    @Column(name = "discounts_percent", nullable = true, precision = 2)
    public BigDecimal getDiscountsPercent() {
        return discountsPercent;
    }

    public void setDiscountsPercent(BigDecimal discountsPercent) {
        this.discountsPercent = discountsPercent;
    }

    @Basic
    @Column(name = "enough_money", nullable = true, precision = 2)
    public BigDecimal getEnoughMoney() {
        return enoughMoney;
    }

    public void setEnoughMoney(BigDecimal enoughMoney) {
        this.enoughMoney = enoughMoney;
    }

    @Basic
    @Column(name = "subtract_money", nullable = true, precision = 2)
    public BigDecimal getSubtractMoney() {
        return subtractMoney;
    }

    public void setSubtractMoney(BigDecimal subtractMoney) {
        this.subtractMoney = subtractMoney;
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
    @Column(name = "pass_time", nullable = true)
    public Timestamp getPassTime() {
        return passTime;
    }

    public void setPassTime(Timestamp passTime) {
        this.passTime = passTime;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbDiscountsConfig that = (TbDiscountsConfig) o;
        return discountsId == that.discountsId &&
                Objects.equals(discountsCode, that.discountsCode) &&
                Objects.equals(discountsDesc, that.discountsDesc) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(discountsType, that.discountsType) &&
                Objects.equals(discountsPrice, that.discountsPrice) &&
                Objects.equals(discountsPercent, that.discountsPercent) &&
                Objects.equals(enoughMoney, that.enoughMoney) &&
                Objects.equals(subtractMoney, that.subtractMoney) &&
                Objects.equals(status, that.status) &&
                Objects.equals(passTime, that.passTime) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountsId, discountsCode, discountsDesc, goodsId, discountsType, discountsPrice, discountsPercent, enoughMoney, subtractMoney, status, passTime, createTime);
    }
}
