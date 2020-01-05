package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/5 14:07
 * 功能介绍:
 */
@Entity
@Table(name = "tb_order_refund", schema = "store-db", catalog = "")
public class TbOrderRefund {
    private int id;
    private String orderNo;
    private Integer detailId;
    private String partnerTradeNo;
    private Byte status;
    private String refundDesc;
    private Integer userId;
    private Byte payWay;
    private String refundAccount;
    private BigDecimal price;
    private Timestamp applyTime;
    private Timestamp finishTime;
    private Timestamp updateTime;
    private Byte applyReason;

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
    @Column(name = "order_no", nullable = true, length = 100)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "detail_id", nullable = true)
    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    @Basic
    @Column(name = "partner_trade_no", nullable = true, length = 100)
    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
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
    @Column(name = "refund_desc", nullable = true, length = 500)
    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
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
    @Column(name = "pay_way", nullable = true)
    public Byte getPayWay() {
        return payWay;
    }

    public void setPayWay(Byte payWay) {
        this.payWay = payWay;
    }

    @Basic
    @Column(name = "refund_account", nullable = true, length = 100)
    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAcount) {
        this.refundAccount = refundAcount;
    }

    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "apply_time", nullable = true)
    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
    }

    @Basic
    @Column(name = "finish_time", nullable = true)
    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "apply_reason", nullable = true)
    public Byte getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(Byte applyReason) {
        this.applyReason = applyReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderRefund that = (TbOrderRefund) o;
        return id == that.id &&
                Objects.equals(orderNo, that.orderNo) &&
                Objects.equals(detailId, that.detailId) &&
                Objects.equals(partnerTradeNo, that.partnerTradeNo) &&
                Objects.equals(status, that.status) &&
                Objects.equals(refundDesc, that.refundDesc) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(payWay, that.payWay) &&
                Objects.equals(refundAccount, that.refundAccount) &&
                Objects.equals(price, that.price) &&
                Objects.equals(applyTime, that.applyTime) &&
                Objects.equals(finishTime, that.finishTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(applyReason, that.applyReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNo, detailId, partnerTradeNo, status, refundDesc, userId, payWay, refundAccount, price, applyTime, finishTime, updateTime, applyReason);
    }
}
