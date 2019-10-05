package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/6 17:49
 * 功能介绍:
 */
@Entity
@Table(name = "tb_order", schema = "store-db", catalog = "")
public class TbOrder {
    private int orderId;
    private Integer userId;
    private String orderNo;
    private String orderFrom;
    private Integer orderCount;
    private BigDecimal orderPrice;
    private BigDecimal realPrice;
    private BigDecimal advancePrice;
    private BigDecimal needpayPrice;
    private String userName;
    private String userPhone;
    private String userAddr;
    private Integer userAddrId;
    private Integer advancePayWay;
    private String advancePayCode;
    private Timestamp advancePayTime;
    private Integer payWay;
    private String payCode;
    private Timestamp payTime;
    private String tradeChannel;
    private BigDecimal postPrice;
    private Integer postWay;
    private Integer companyCode;
    private String postNo;
    private String userRemark;
    private String serviceRemark;
    private Integer orderStatus;
    private Timestamp orderTime;
    private Timestamp postTime;
    private Integer businessType;
    private Integer discountsId;
    private BigDecimal discountsPrice;
    private String discountsRemark;
    private String tradeNo;
    private BigDecimal refundPrice;
    private String refundRemark;
    private String remark;
    private String postPhone;
    private String postMan;
    private Integer statusOpration;
    private Integer shopId;
    private Timestamp finishTime;
    private Timestamp lastaltertime;
    private String lastalterman;
    private Integer userReadMark;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
    @Column(name = "order_no", nullable = true, length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "order_from", nullable = true, length = 50)
    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    @Basic
    @Column(name = "order_count", nullable = true)
    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
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
    @Column(name = "advance_price", nullable = true, precision = 2)
    public BigDecimal getAdvancePrice() {
        return advancePrice;
    }

    public void setAdvancePrice(BigDecimal advancePrice) {
        this.advancePrice = advancePrice;
    }

    @Basic
    @Column(name = "needpay_price", nullable = true, precision = 2)
    public BigDecimal getNeedpayPrice() {
        return needpayPrice;
    }

    public void setNeedpayPrice(BigDecimal needpayPrice) {
        this.needpayPrice = needpayPrice;
    }

    @Basic
    @Column(name = "user_name", nullable = true, length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    @Column(name = "user_addr", nullable = true, length = 500)
    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    @Basic
    @Column(name = "user_addr_id", nullable = true)
    public Integer getUserAddrId() {
        return userAddrId;
    }

    public void setUserAddrId(Integer userAddrId) {
        this.userAddrId = userAddrId;
    }

    @Basic
    @Column(name = "advance_pay_way", nullable = true)
    public Integer getAdvancePayWay() {
        return advancePayWay;
    }

    public void setAdvancePayWay(Integer advancePayWay) {
        this.advancePayWay = advancePayWay;
    }

    @Basic
    @Column(name = "advance_pay_code", nullable = true, length = 100)
    public String getAdvancePayCode() {
        return advancePayCode;
    }

    public void setAdvancePayCode(String advancePayCode) {
        this.advancePayCode = advancePayCode;
    }

    @Basic
    @Column(name = "advance_pay_time", nullable = true)
    public Timestamp getAdvancePayTime() {
        return advancePayTime;
    }

    public void setAdvancePayTime(Timestamp advancePayTime) {
        this.advancePayTime = advancePayTime;
    }

    @Basic
    @Column(name = "pay_way", nullable = true)
    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    @Basic
    @Column(name = "pay_code", nullable = true, length = 100)
    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    @Basic
    @Column(name = "pay_time", nullable = true)
    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    @Basic
    @Column(name = "trade_channel", nullable = true, length = 50)
    public String getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(String tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    @Basic
    @Column(name = "post_price", nullable = true, precision = 2)
    public BigDecimal getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(BigDecimal postPrice) {
        this.postPrice = postPrice;
    }

    @Basic
    @Column(name = "post_way", nullable = true)
    public Integer getPostWay() {
        return postWay;
    }

    public void setPostWay(Integer postWay) {
        this.postWay = postWay;
    }

    @Basic
    @Column(name = "company_code", nullable = true)
    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    @Basic
    @Column(name = "post_no", nullable = true, length = 50)
    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    @Basic
    @Column(name = "user_remark", nullable = true, length = 300)
    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    @Basic
    @Column(name = "service_remark", nullable = true, length = 300)
    public String getServiceRemark() {
        return serviceRemark;
    }

    public void setServiceRemark(String serviceRemark) {
        this.serviceRemark = serviceRemark;
    }

    @Basic
    @Column(name = "order_status", nullable = true)
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Basic
    @Column(name = "order_time", nullable = true)
    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    @Basic
    @Column(name = "post_time", nullable = true)
    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    @Basic
    @Column(name = "business_type", nullable = true)
    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    @Basic
    @Column(name = "discounts_id", nullable = true)
    public Integer getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(Integer discountsId) {
        this.discountsId = discountsId;
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
    @Column(name = "discounts_remark", nullable = true, length = 200)
    public String getDiscountsRemark() {
        return discountsRemark;
    }

    public void setDiscountsRemark(String discountsRemark) {
        this.discountsRemark = discountsRemark;
    }

    @Basic
    @Column(name = "trade_no", nullable = true, length = 100)
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Basic
    @Column(name = "refund_price", nullable = true, precision = 2)
    public BigDecimal getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    @Basic
    @Column(name = "refund_remark", nullable = true, length = 500)
    public String getRefundRemark() {
        return refundRemark;
    }

    public void setRefundRemark(String refundRemark) {
        this.refundRemark = refundRemark;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 300)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "post_phone", nullable = true, length = 20)
    public String getPostPhone() {
        return postPhone;
    }

    public void setPostPhone(String postPhone) {
        this.postPhone = postPhone;
    }

    @Basic
    @Column(name = "post_man", nullable = true, length = 100)
    public String getPostMan() {
        return postMan;
    }

    public void setPostMan(String postMan) {
        this.postMan = postMan;
    }

    @Basic
    @Column(name = "status_opration", nullable = true)
    public Integer getStatusOpration() {
        return statusOpration;
    }

    public void setStatusOpration(Integer statusOpration) {
        this.statusOpration = statusOpration;
    }

    @Basic
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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
    @Column(name = "lastaltertime", nullable = true)
    public Timestamp getLastaltertime() {
        return lastaltertime;
    }

    public void setLastaltertime(Timestamp lastaltertime) {
        this.lastaltertime = lastaltertime;
    }

    @Basic
    @Column(name = "lastalterman", nullable = true, length = 20)
    public String getLastalterman() {
        return lastalterman;
    }

    public void setLastalterman(String lastalterman) {
        this.lastalterman = lastalterman;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrder order = (TbOrder) o;
        return orderId == order.orderId &&
                Objects.equals(userId, order.userId) &&
                Objects.equals(orderNo, order.orderNo) &&
                Objects.equals(orderFrom, order.orderFrom) &&
                Objects.equals(orderCount, order.orderCount) &&
                Objects.equals(orderPrice, order.orderPrice) &&
                Objects.equals(realPrice, order.realPrice) &&
                Objects.equals(advancePrice, order.advancePrice) &&
                Objects.equals(needpayPrice, order.needpayPrice) &&
                Objects.equals(userName, order.userName) &&
                Objects.equals(userPhone, order.userPhone) &&
                Objects.equals(userAddr, order.userAddr) &&
                Objects.equals(userAddrId, order.userAddrId) &&
                Objects.equals(advancePayWay, order.advancePayWay) &&
                Objects.equals(advancePayCode, order.advancePayCode) &&
                Objects.equals(advancePayTime, order.advancePayTime) &&
                Objects.equals(payWay, order.payWay) &&
                Objects.equals(payCode, order.payCode) &&
                Objects.equals(payTime, order.payTime) &&
                Objects.equals(tradeChannel, order.tradeChannel) &&
                Objects.equals(postPrice, order.postPrice) &&
                Objects.equals(postWay, order.postWay) &&
                Objects.equals(companyCode, order.companyCode) &&
                Objects.equals(postNo, order.postNo) &&
                Objects.equals(userRemark, order.userRemark) &&
                Objects.equals(serviceRemark, order.serviceRemark) &&
                Objects.equals(orderStatus, order.orderStatus) &&
                Objects.equals(orderTime, order.orderTime) &&
                Objects.equals(postTime, order.postTime) &&
                Objects.equals(businessType, order.businessType) &&
                Objects.equals(discountsId, order.discountsId) &&
                Objects.equals(discountsPrice, order.discountsPrice) &&
                Objects.equals(discountsRemark, order.discountsRemark) &&
                Objects.equals(tradeNo, order.tradeNo) &&
                Objects.equals(refundPrice, order.refundPrice) &&
                Objects.equals(refundRemark, order.refundRemark) &&
                Objects.equals(remark, order.remark) &&
                Objects.equals(postPhone, order.postPhone) &&
                Objects.equals(postMan, order.postMan) &&
                Objects.equals(statusOpration, order.statusOpration) &&
                Objects.equals(shopId, order.shopId) &&
                Objects.equals(finishTime, order.finishTime) &&
                Objects.equals(lastaltertime, order.lastaltertime) &&
                Objects.equals(lastalterman, order.lastalterman);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orderId, userId, orderNo, orderFrom, orderCount, orderPrice, realPrice, advancePrice, needpayPrice, userName, userPhone, userAddr, userAddrId, advancePayWay, advancePayCode, advancePayTime, payWay, payCode, payTime, tradeChannel, postPrice, postWay, companyCode, postNo, userRemark, serviceRemark, orderStatus, orderTime, postTime, businessType, discountsId, discountsPrice, discountsRemark, tradeNo, refundPrice, refundRemark, remark, postPhone, postMan, statusOpration, shopId, finishTime, lastaltertime, lastalterman);
    }

    @Basic
    @Column(name = "user_read_mark", nullable = true)
    public Integer getUserReadMark() {
        return userReadMark;
    }

    public void setUserReadMark(Integer userReadMark) {
        this.userReadMark = userReadMark;
    }
}
