package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/31 0:07
 * 功能介绍:
 */
@Entity
@Table(name = "tb_store_inout", schema = "store_kc_db", catalog = "")
public class TbStoreInout {
    private int incomeId;
    private String supplierCode;
    private String goodsCode;
    private String shopCode;
    private String goodsName;
    private String goodsUnit;
    private BigDecimal incomePrice;
    private Integer unitCount;
    private Byte incomeType;
    private Timestamp makeDay;
    private Timestamp effectToTime;
    private Integer effectDay;
    private Byte status;
    private Timestamp createTime;
    private String createUserName;
    private Byte isMatching;
    private String remark;
    private String classifyCode;
    private Timestamp inTime;
    private Timestamp outTime;
    private Timestamp lastUpdateTime;
    private String shopCodeDate;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "income_id", nullable = false)
    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    @Basic
    @Column(name = "supplier_code", nullable = true, length = 50)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
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
    @Column(name = "income_price", nullable = true, precision = 2)
    public BigDecimal getIncomePrice() {
        return incomePrice;
    }

    public void setIncomePrice(BigDecimal incomePrice) {
        this.incomePrice = incomePrice;
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
    @Column(name = "income_type", nullable = true)
    public Byte getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(Byte incomeType) {
        this.incomeType = incomeType;
    }

    @Basic
    @Column(name = "make_day", nullable = true)
    public Timestamp getMakeDay() {
        return makeDay;
    }

    public void setMakeDay(Timestamp makeDay) {
        this.makeDay = makeDay;
    }

    @Basic
    @Column(name = "effect_to_time", nullable = true)
    public Timestamp getEffectToTime() {
        return effectToTime;
    }

    public void setEffectToTime(Timestamp effectToTime) {
        this.effectToTime = effectToTime;
    }

    @Basic
    @Column(name = "effect_day", nullable = true)
    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
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
    @Column(name = "is_matching", nullable = true)
    public Byte getIsMatching() {
        return isMatching;
    }

    public void setIsMatching(Byte isMatching) {
        this.isMatching = isMatching;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "classify_code", nullable = true, length = 50)
    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    @Basic
    @Column(name = "in_time", nullable = true)
    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    @Basic
    @Column(name = "out_time", nullable = true)
    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
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
    @Column(name = "shop_code_date", nullable = true, length = 8)
    public String getShopCodeDate() {
        return shopCodeDate;
    }

    public void setShopCodeDate(String shopCodeDate) {
        this.shopCodeDate = shopCodeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbStoreInout that = (TbStoreInout) o;
        return incomeId == that.incomeId &&
                Objects.equals(supplierCode, that.supplierCode) &&
                Objects.equals(goodsCode, that.goodsCode) &&
                Objects.equals(shopCode, that.shopCode) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsUnit, that.goodsUnit) &&
                Objects.equals(incomePrice, that.incomePrice) &&
                Objects.equals(unitCount, that.unitCount) &&
                Objects.equals(incomeType, that.incomeType) &&
                Objects.equals(makeDay, that.makeDay) &&
                Objects.equals(effectToTime, that.effectToTime) &&
                Objects.equals(effectDay, that.effectDay) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(createUserName, that.createUserName) &&
                Objects.equals(isMatching, that.isMatching) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(classifyCode, that.classifyCode) &&
                Objects.equals(inTime, that.inTime) &&
                Objects.equals(outTime, that.outTime) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                Objects.equals(shopCodeDate, that.shopCodeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(incomeId, supplierCode, goodsCode, shopCode, goodsName, goodsUnit, incomePrice, unitCount, incomeType, makeDay, effectToTime, effectDay, status, createTime, createUserName, isMatching, remark, classifyCode, inTime, outTime, lastUpdateTime, shopCodeDate);
    }
}
