package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/14 13:36
 * 功能介绍:
 */
@Entity
@Table(name = "tb_goods_sku", schema = "testlirf", catalog = "")
public class TbGoodsSku {
    private int skuId;
    private Integer goodsId;
    private String skuName;
    private BigDecimal trueMoney;
    private BigDecimal nowMoney;
    private String skuCode;
    private String propCode;
    private Integer skuNum;
    private String skuImg;
    private BigDecimal purchasePrice;
    private Integer shopId;
    private Integer sellCount;
    private Integer status;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "sku_id", nullable = false)
    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
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
    @Column(name = "sku_name", nullable = true, length = 50)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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
    @Column(name = "now_money", nullable = true, precision = 2)
    public BigDecimal getNowMoney() {
        return nowMoney;
    }

    public void setNowMoney(BigDecimal nowMoney) {
        this.nowMoney = nowMoney;
    }

    @Basic
    @Column(name = "sku_code", nullable = true, length = 50)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "prop_code", nullable = true, length = 50)
    public String getPropCode() {
        return propCode;
    }

    public void setPropCode(String propCode) {
        this.propCode = propCode;
    }

    @Basic
    @Column(name = "sku_num", nullable = true)
    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    @Basic
    @Column(name = "sku_img", nullable = true, length = 200)
    public String getSkuImg() {
        return skuImg;
    }

    public void setSkuImg(String skuImg) {
        this.skuImg = skuImg;
    }

    @Basic
    @Column(name = "purchase_price", nullable = true, precision = 2)
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
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
    @Column(name = "sell_count", nullable = true)
    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbGoodsSku goodsSku = (TbGoodsSku) o;
        return skuId == goodsSku.skuId &&
                Objects.equals(goodsId, goodsSku.goodsId) &&
                Objects.equals(skuName, goodsSku.skuName) &&
                Objects.equals(trueMoney, goodsSku.trueMoney) &&
                Objects.equals(nowMoney, goodsSku.nowMoney) &&
                Objects.equals(skuCode, goodsSku.skuCode) &&
                Objects.equals(propCode, goodsSku.propCode) &&
                Objects.equals(skuNum, goodsSku.skuNum) &&
                Objects.equals(skuImg, goodsSku.skuImg) &&
                Objects.equals(purchasePrice, goodsSku.purchasePrice) &&
                Objects.equals(shopId, goodsSku.shopId) &&
                Objects.equals(sellCount, goodsSku.sellCount) &&
                Objects.equals(status, goodsSku.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(skuId, goodsId, skuName, trueMoney, nowMoney, skuCode, propCode, skuNum, skuImg, purchasePrice, shopId, sellCount, status);
    }
}
