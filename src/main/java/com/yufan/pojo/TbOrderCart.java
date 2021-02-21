package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @description:
 * @author: lirf
 * @time: 2021/2/19
 */
@Entity
@Table(name = "tb_order_cart", schema = "store_db", catalog = "")
public class TbOrderCart {
    private int cartId;
    private Integer userId;
    private Integer goodsId;
    private String goodsName;
    private String goodsImg;
    private String goodsSpec;
    private String goodsSpecName;
    private Integer goodsCount;
    private BigDecimal goodsPrice;
    private BigDecimal trueMoney;
    private Integer shopId;
    private String shopName;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Timestamp lastaltertime;
    private String goodsSpecNameStr;
    private Integer cartType;
    private Integer skuId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "cart_id", nullable = false)
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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
    @Column(name = "goods_img", nullable = true, length = 255)
    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    @Basic
    @Column(name = "goods_spec", nullable = true, length = 100)
    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    @Basic
    @Column(name = "goods_spec_name", nullable = true, length = 100)
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
    @Column(name = "goods_price", nullable = true, precision = 2)
    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    @Basic
    @Column(name = "true_money", nullable = true, precision = 2)
    public BigDecimal getTrueMoney() {
        return trueMoney;
    }

    public void setTrueMoney(BigDecimal trueMoney) {
        this.trueMoney = trueMoney;
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
    @Column(name = "shop_name", nullable = true, length = 255)
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    @Column(name = "lastaltertime", nullable = true)
    public Timestamp getLastaltertime() {
        return lastaltertime;
    }

    public void setLastaltertime(Timestamp lastaltertime) {
        this.lastaltertime = lastaltertime;
    }

    @Basic
    @Column(name = "goods_spec_name_str", nullable = true, length = 255)
    public String getGoodsSpecNameStr() {
        return goodsSpecNameStr;
    }

    public void setGoodsSpecNameStr(String goodsSpecNameStr) {
        this.goodsSpecNameStr = goodsSpecNameStr;
    }

    @Basic
    @Column(name = "cart_type", nullable = true)
    public Integer getCartType() {
        return cartType;
    }

    public void setCartType(Integer cartType) {
        this.cartType = cartType;
    }

    @Basic
    @Column(name = "sku_id", nullable = true)
    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderCart that = (TbOrderCart) o;
        return cartId == that.cartId &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsImg, that.goodsImg) &&
                Objects.equals(goodsSpec, that.goodsSpec) &&
                Objects.equals(goodsSpecName, that.goodsSpecName) &&
                Objects.equals(goodsCount, that.goodsCount) &&
                Objects.equals(goodsPrice, that.goodsPrice) &&
                Objects.equals(trueMoney, that.trueMoney) &&
                Objects.equals(shopId, that.shopId) &&
                Objects.equals(shopName, that.shopName) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(lastaltertime, that.lastaltertime) &&
                Objects.equals(goodsSpecNameStr, that.goodsSpecNameStr) &&
                Objects.equals(cartType, that.cartType) &&
                Objects.equals(skuId, that.skuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, userId, goodsId, goodsName, goodsImg, goodsSpec, goodsSpecName, goodsCount, goodsPrice, trueMoney, shopId, shopName, createtime, status, remark, lastaltertime, goodsSpecNameStr, cartType, skuId);
    }
}
