package com.yufan.kc.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/20 0:31
 * 功能介绍:
 */
@Entity
@Table(name = "tb_kc_order", schema = "store_kc_db", catalog = "")
public class TbKcOrder {
    private int orderId;
    private String orderNum;
    private Integer userId;
    private Integer goodsCount;
    private BigDecimal orderPrice;
    private BigDecimal realPrice;
    private BigDecimal discountsTicketPrice;
    private BigDecimal discountsMemberPrice;
    private BigDecimal discountsPrice;
    private String discountsRemark;
    private Byte payMethod;
    private Integer consumeCount;
    private Byte orderStatus;
    private Timestamp createTime;
    private String remark;
    private String userPhone;
    private String memberNo;
    private Byte orderSource;
    private Timestamp lastUpdateTime;
    private String serverName;
    private Integer personCount;
    private String tableName;
    private Timestamp payDate;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
    @Column(name = "discounts_ticket_price", nullable = true, precision = 2)
    public BigDecimal getDiscountsTicketPrice() {
        return discountsTicketPrice;
    }

    public void setDiscountsTicketPrice(BigDecimal discountsTicketPrice) {
        this.discountsTicketPrice = discountsTicketPrice;
    }

    @Basic
    @Column(name = "discounts_member_price", nullable = true, precision = 2)
    public BigDecimal getDiscountsMemberPrice() {
        return discountsMemberPrice;
    }

    public void setDiscountsMemberPrice(BigDecimal discountsMemberPrice) {
        this.discountsMemberPrice = discountsMemberPrice;
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

    @Basic
    @Column(name = "user_phone", nullable = true, length = 20)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Basic
    @Column(name = "member_no", nullable = true, length = 50)
    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    @Basic
    @Column(name = "order_source", nullable = true)
    public Byte getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Byte orderSource) {
        this.orderSource = orderSource;
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
    @Column(name = "server_name", nullable = true, length = 50)
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Basic
    @Column(name = "person_count", nullable = true)
    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    @Basic
    @Column(name = "table_name", nullable = true, length = 50)
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "pay_date", nullable = true)
    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbKcOrder order = (TbKcOrder) o;
        return orderId == order.orderId &&
                Objects.equals(orderNum, order.orderNum) &&
                Objects.equals(userId, order.userId) &&
                Objects.equals(goodsCount, order.goodsCount) &&
                Objects.equals(orderPrice, order.orderPrice) &&
                Objects.equals(realPrice, order.realPrice) &&
                Objects.equals(discountsTicketPrice, order.discountsTicketPrice) &&
                Objects.equals(discountsMemberPrice, order.discountsMemberPrice) &&
                Objects.equals(discountsPrice, order.discountsPrice) &&
                Objects.equals(discountsRemark, order.discountsRemark) &&
                Objects.equals(payMethod, order.payMethod) &&
                Objects.equals(consumeCount, order.consumeCount) &&
                Objects.equals(orderStatus, order.orderStatus) &&
                Objects.equals(createTime, order.createTime) &&
                Objects.equals(remark, order.remark) &&
                Objects.equals(userPhone, order.userPhone) &&
                Objects.equals(memberNo, order.memberNo) &&
                Objects.equals(orderSource, order.orderSource) &&
                Objects.equals(lastUpdateTime, order.lastUpdateTime) &&
                Objects.equals(serverName, order.serverName) &&
                Objects.equals(personCount, order.personCount) &&
                Objects.equals(tableName, order.tableName) &&
                Objects.equals(payDate, order.payDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderNum, userId, goodsCount, orderPrice, realPrice, discountsTicketPrice, discountsMemberPrice, discountsPrice, discountsRemark, payMethod, consumeCount, orderStatus, createTime, remark, userPhone, memberNo, orderSource, lastUpdateTime, serverName, personCount, tableName, payDate);
    }
}
