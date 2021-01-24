package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 22:16
 * 功能介绍:
 */
@Entity
@Table(name = "tb_order_detail", schema = "store_kc_db", catalog = "")
public class TbOrderDetail {
    private int detailId;
    private Integer orderId;
    private Integer goodsId;
    private BigDecimal salePrice;
    private Integer saleCount;

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
    @Column(name = "sale_price", nullable = true, precision = 2)
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    @Basic
    @Column(name = "sale_count", nullable = true)
    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderDetail that = (TbOrderDetail) o;
        return detailId == that.detailId &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(salePrice, that.salePrice) &&
                Objects.equals(saleCount, that.saleCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detailId, orderId, goodsId, salePrice, saleCount);
    }
}
