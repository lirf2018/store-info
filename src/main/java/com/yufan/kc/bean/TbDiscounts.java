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
@Table(name = "tb_discounts", schema = "store_kc_db", catalog = "")
public class TbDiscounts {
    private int id;
    private Integer goodsId;
    private Timestamp discountsStartTime;
    private Timestamp discountsEndTime;
    private BigDecimal discountsPrice;
    private Timestamp createTime;
    private Byte status;

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
    @Column(name = "goods_id", nullable = true)
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
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
    @Column(name = "discounts_price", nullable = true, precision = 2)
    public BigDecimal getDiscountsPrice() {
        return discountsPrice;
    }

    public void setDiscountsPrice(BigDecimal discountsPrice) {
        this.discountsPrice = discountsPrice;
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
        TbDiscounts that = (TbDiscounts) o;
        return id == that.id &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(discountsStartTime, that.discountsStartTime) &&
                Objects.equals(discountsEndTime, that.discountsEndTime) &&
                Objects.equals(discountsPrice, that.discountsPrice) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goodsId, discountsStartTime, discountsEndTime, discountsPrice, createTime, status);
    }
}
