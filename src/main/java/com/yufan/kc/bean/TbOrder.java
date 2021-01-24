package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 22:16
 * 功能介绍:
 */
@Entity
@Table(name = "tb_order", schema = "store_kc_db", catalog = "")
public class TbOrder {
    private int orderId;
    private String orderNum;
    private Integer userId;
    private Integer goodsCount;
    private BigDecimal orderPrice;
    private BigDecimal realPrice;
    private BigDecimal discountsPrice;
    private String discountsRemark;
    private Byte payMethod;
    private Integer consumeCount;
    private Byte orderStatus;
    private Timestamp createTime;
    private String remark;

    @Id
    @Column(name = "order_id", nullable = false)
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
    @Column(name = "user_id", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "goods_count", nullable = true)
    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    @Basic
    @Column(name = "order_price", nullable = true, precision = 2)
    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Basic
    @Column(name = "real_price", nullable = true, precision = 2)
    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
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
    @Column(name = "discounts_remark", nullable = true, length = 100)
    public String getDiscountsRemark() {
        return discountsRemark;
    }

    public void setDiscountsRemark(String discountsRemark) {
        this.discountsRemark = discountsRemark;
    }

    @Basic
    @Column(name = "pay_method", nullable = true)
    public Byte getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Byte payMethod) {
        this.payMethod = payMethod;
    }

    @Basic
    @Column(name = "consume_count", nullable = true)
    public Integer getConsumeCount() {
        return consumeCount;
    }

    public void setConsumeCount(Integer consumeCount) {
        this.consumeCount = consumeCount;
    }

    @Basic
    @Column(name = "order_status", nullable = true)
    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
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
        TbOrder tbOrder = (TbOrder) o;
        return orderId == tbOrder.orderId &&
                Objects.equals(orderNum, tbOrder.orderNum) &&
                Objects.equals(userId, tbOrder.userId) &&
                Objects.equals(goodsCount, tbOrder.goodsCount) &&
                Objects.equals(orderPrice, tbOrder.orderPrice) &&
                Objects.equals(realPrice, tbOrder.realPrice) &&
                Objects.equals(discountsPrice, tbOrder.discountsPrice) &&
                Objects.equals(discountsRemark, tbOrder.discountsRemark) &&
                Objects.equals(payMethod, tbOrder.payMethod) &&
                Objects.equals(consumeCount, tbOrder.consumeCount) &&
                Objects.equals(orderStatus, tbOrder.orderStatus) &&
                Objects.equals(createTime, tbOrder.createTime) &&
                Objects.equals(remark, tbOrder.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderNum, userId, goodsCount, orderPrice, realPrice, discountsPrice, discountsRemark, payMethod, consumeCount, orderStatus, createTime, remark);
    }
}
