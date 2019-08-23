package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/9 17:44
 * 功能介绍:
 */
@Entity
@Table(name = "tb_order_detail", schema = "testlirf", catalog = "")
public class TbOrderDetail {
    private int detailId;
    private Integer orderId;
    private Integer goodsId;
    private String goodsName;
    private String goodsSpec;
    private String goodsSpecName;
    private Integer goodsCount;
    private BigDecimal saleMoney;
    private BigDecimal goodsTrueMoney;
    private BigDecimal goodsPurchasePrice;
    private BigDecimal timePrice;
    private BigDecimal depositPrice;
    private Integer shopId;
    private String shopName;
    private String outCode;
    private Integer getAddrId;
    private String getAddrName;
    private Timestamp getTime;
    private Integer backAddrId;
    private String backAddrName;
    private Timestamp backTime;
    private Integer detailStatus;
    private Integer isCoupon;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private String lastalterman;
    private String remark;
    private String goodsImg;
    private Integer cartId;
    private Integer timeGoodsId;
    private String goodsSpecNameStr;

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
    @Column(name = "goods_name", nullable = true, length = 255)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Basic
    @Column(name = "goods_spec", nullable = true, length = 255)
    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    @Basic
    @Column(name = "goods_spec_name", nullable = true, length = 255)
    public String getGoodsSpecName() {
        return goodsSpecName;
    }

    public void setGoodsSpecName(String goodsSpecName) {
        this.goodsSpecName = goodsSpecName;
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
    @Column(name = "sale_money", nullable = true, precision = 2)
    public BigDecimal getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(BigDecimal saleMoney) {
        this.saleMoney = saleMoney;
    }

    @Basic
    @Column(name = "goods_true_money", nullable = true, precision = 2)
    public BigDecimal getGoodsTrueMoney() {
        return goodsTrueMoney;
    }

    public void setGoodsTrueMoney(BigDecimal goodsTrueMoney) {
        this.goodsTrueMoney = goodsTrueMoney;
    }

    @Basic
    @Column(name = "goods_purchase_price", nullable = true, precision = 2)
    public BigDecimal getGoodsPurchasePrice() {
        return goodsPurchasePrice;
    }

    public void setGoodsPurchasePrice(BigDecimal goodsPurchasePrice) {
        this.goodsPurchasePrice = goodsPurchasePrice;
    }

    @Basic
    @Column(name = "time_price", nullable = true, precision = 2)
    public BigDecimal getTimePrice() {
        return timePrice;
    }

    public void setTimePrice(BigDecimal timePrice) {
        this.timePrice = timePrice;
    }

    @Basic
    @Column(name = "deposit_price", nullable = true, precision = 2)
    public BigDecimal getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(BigDecimal depositPrice) {
        this.depositPrice = depositPrice;
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
    @Column(name = "shop_name", nullable = true, length = 100)
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Basic
    @Column(name = "out_code", nullable = true, length = 30)
    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    @Basic
    @Column(name = "get_addr_id", nullable = true)
    public Integer getGetAddrId() {
        return getAddrId;
    }

    public void setGetAddrId(Integer getAddrId) {
        this.getAddrId = getAddrId;
    }

    @Basic
    @Column(name = "get_addr_name", nullable = true, length = 200)
    public String getGetAddrName() {
        return getAddrName;
    }

    public void setGetAddrName(String getAddrName) {
        this.getAddrName = getAddrName;
    }

    @Basic
    @Column(name = "get_time", nullable = true)
    public Timestamp getGetTime() {
        return getTime;
    }

    public void setGetTime(Timestamp getTime) {
        this.getTime = getTime;
    }

    @Basic
    @Column(name = "back_addr_id", nullable = true)
    public Integer getBackAddrId() {
        return backAddrId;
    }

    public void setBackAddrId(Integer backAddrId) {
        this.backAddrId = backAddrId;
    }

    @Basic
    @Column(name = "back_addr_name", nullable = true, length = 255)
    public String getBackAddrName() {
        return backAddrName;
    }

    public void setBackAddrName(String backAddrName) {
        this.backAddrName = backAddrName;
    }

    @Basic
    @Column(name = "back_time", nullable = true)
    public Timestamp getBackTime() {
        return backTime;
    }

    public void setBackTime(Timestamp backTime) {
        this.backTime = backTime;
    }

    @Basic
    @Column(name = "detail_status", nullable = true)
    public Integer getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }

    @Basic
    @Column(name = "is_coupon", nullable = true)
    public Integer getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(Integer isCoupon) {
        this.isCoupon = isCoupon;
    }

    @Basic
    @Column(name = "createtime", nullable = true)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
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
    @Column(name = "lastalterman", nullable = true, length = 50)
    public String getLastalterman() {
        return lastalterman;
    }

    public void setLastalterman(String lastalterman) {
        this.lastalterman = lastalterman;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "goods_img", nullable = true, length = 255)
    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    @Basic
    @Column(name = "cart_id", nullable = true)
    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Basic
    @Column(name = "time_goods_id", nullable = true)
    public Integer getTimeGoodsId() {
        return timeGoodsId;
    }

    public void setTimeGoodsId(Integer timeGoodsId) {
        this.timeGoodsId = timeGoodsId;
    }

    @Basic
    @Column(name = "goods_spec_name_str", nullable = true, length = 200)
    public String getGoodsSpecNameStr() {
        return goodsSpecNameStr;
    }

    public void setGoodsSpecNameStr(String goodsSpecNameStr) {
        this.goodsSpecNameStr = goodsSpecNameStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderDetail that = (TbOrderDetail) o;
        return detailId == that.detailId &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsSpec, that.goodsSpec) &&
                Objects.equals(goodsSpecName, that.goodsSpecName) &&
                Objects.equals(goodsCount, that.goodsCount) &&
                Objects.equals(saleMoney, that.saleMoney) &&
                Objects.equals(goodsTrueMoney, that.goodsTrueMoney) &&
                Objects.equals(goodsPurchasePrice, that.goodsPurchasePrice) &&
                Objects.equals(timePrice, that.timePrice) &&
                Objects.equals(depositPrice, that.depositPrice) &&
                Objects.equals(shopId, that.shopId) &&
                Objects.equals(shopName, that.shopName) &&
                Objects.equals(outCode, that.outCode) &&
                Objects.equals(getAddrId, that.getAddrId) &&
                Objects.equals(getAddrName, that.getAddrName) &&
                Objects.equals(getTime, that.getTime) &&
                Objects.equals(backAddrId, that.backAddrId) &&
                Objects.equals(backAddrName, that.backAddrName) &&
                Objects.equals(backTime, that.backTime) &&
                Objects.equals(detailStatus, that.detailStatus) &&
                Objects.equals(isCoupon, that.isCoupon) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(lastaltertime, that.lastaltertime) &&
                Objects.equals(lastalterman, that.lastalterman) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(goodsImg, that.goodsImg) &&
                Objects.equals(cartId, that.cartId) &&
                Objects.equals(timeGoodsId, that.timeGoodsId) &&
                Objects.equals(goodsSpecNameStr, that.goodsSpecNameStr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(detailId, orderId, goodsId, goodsName, goodsSpec, goodsSpecName, goodsCount, saleMoney, goodsTrueMoney, goodsPurchasePrice, timePrice, depositPrice, shopId, shopName, outCode, getAddrId, getAddrName, getTime, backAddrId, backAddrName, backTime, detailStatus, isCoupon, createtime, lastaltertime, lastalterman, remark, goodsImg, cartId, timeGoodsId, goodsSpecNameStr);
    }
}
