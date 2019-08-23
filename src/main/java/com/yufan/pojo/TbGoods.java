package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/13 11:22
 * 功能介绍:
 */
@Entity
@Table(name = "tb_goods", schema = "testlirf", catalog = "")
public class TbGoods {
    private int goodsId;
    private String goodsName;
    private String title;
    private String goodsImg;
    private BigDecimal trueMoney;
    private BigDecimal nowMoney;
    private String intro;
    private Integer shopId;
    private Integer isYuding;
    private Integer getWay;
    private Integer isInvoice;
    private Integer isPutaway;
    private Integer dataIndex;
    private Integer categoryId;
    private Integer areaId;
    private Integer property;
    private Timestamp startTime;
    private Timestamp endTime;
    private String goodsCode;
    private String goodsUnit;
    private Integer isSingle;
    private Integer goodsNum;
    private Integer isReturn;
    private Integer couponId;
    private String createman;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private String lastalterman;
    private Integer status;
    private String remark;
    private Integer goodsType;
    private Integer isPayOnline;
    private String outCode;
    private BigDecimal depositMoney;
    private String peisongZcDesc;
    private String peisongPeiDesc;
    private BigDecimal purchasePrice;
    private Integer isTimeGoods;
    private Integer limitNum;
    private Integer limitWay;
    private Timestamp limitBeginTime;
    private Integer levelId;
    private BigDecimal advancePrice;
    private String barCode;
    private String barCodeShop;
    private Integer sellCount;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "goods_id", nullable = false)
    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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
    @Column(name = "title", nullable = true, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "goods_img", nullable = true, length = 200)
    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
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
    @Column(name = "intro", nullable = true, length = -1)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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
    @Column(name = "is_yuding", nullable = true)
    public Integer getIsYuding() {
        return isYuding;
    }

    public void setIsYuding(Integer isYuding) {
        this.isYuding = isYuding;
    }

    @Basic
    @Column(name = "get_way", nullable = true)
    public Integer getGetWay() {
        return getWay;
    }

    public void setGetWay(Integer getWay) {
        this.getWay = getWay;
    }

    @Basic
    @Column(name = "is_invoice", nullable = true)
    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    @Basic
    @Column(name = "is_putaway", nullable = true)
    public Integer getIsPutaway() {
        return isPutaway;
    }

    public void setIsPutaway(Integer isPutaway) {
        this.isPutaway = isPutaway;
    }

    @Basic
    @Column(name = "data_index", nullable = true)
    public Integer getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Integer dataIndex) {
        this.dataIndex = dataIndex;
    }

    @Basic
    @Column(name = "category_id", nullable = true)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "area_id", nullable = true)
    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    @Basic
    @Column(name = "property", nullable = true)
    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

    @Basic
    @Column(name = "start_time", nullable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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
    @Column(name = "goods_unit", nullable = true, length = 16)
    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    @Basic
    @Column(name = "is_single", nullable = true)
    public Integer getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(Integer isSingle) {
        this.isSingle = isSingle;
    }

    @Basic
    @Column(name = "goods_num", nullable = true)
    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Basic
    @Column(name = "is_return", nullable = true)
    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    @Basic
    @Column(name = "coupon_id", nullable = true)
    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    @Basic
    @Column(name = "createman", nullable = true, length = 100)
    public String getCreateman() {
        return createman;
    }

    public void setCreateman(String createman) {
        this.createman = createman;
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
    @Column(name = "lastalterman", nullable = true, length = 100)
    public String getLastalterman() {
        return lastalterman;
    }

    public void setLastalterman(String lastalterman) {
        this.lastalterman = lastalterman;
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
    @Column(name = "remark", nullable = true, length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "goods_type", nullable = true)
    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    @Basic
    @Column(name = "is_pay_online", nullable = true)
    public Integer getIsPayOnline() {
        return isPayOnline;
    }

    public void setIsPayOnline(Integer isPayOnline) {
        this.isPayOnline = isPayOnline;
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
    @Column(name = "deposit_money", nullable = true, precision = 2)
    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }

    @Basic
    @Column(name = "peisong_zc_desc", nullable = true, length = 500)
    public String getPeisongZcDesc() {
        return peisongZcDesc;
    }

    public void setPeisongZcDesc(String peisongZcDesc) {
        this.peisongZcDesc = peisongZcDesc;
    }

    @Basic
    @Column(name = "peisong_pei_desc", nullable = true, length = 500)
    public String getPeisongPeiDesc() {
        return peisongPeiDesc;
    }

    public void setPeisongPeiDesc(String peisongPeiDesc) {
        this.peisongPeiDesc = peisongPeiDesc;
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
    @Column(name = "is_time_goods", nullable = true)
    public Integer getIsTimeGoods() {
        return isTimeGoods;
    }

    public void setIsTimeGoods(Integer isTimeGoods) {
        this.isTimeGoods = isTimeGoods;
    }

    @Basic
    @Column(name = "limit_num", nullable = true)
    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    @Basic
    @Column(name = "limit_way", nullable = true)
    public Integer getLimitWay() {
        return limitWay;
    }

    public void setLimitWay(Integer limitWay) {
        this.limitWay = limitWay;
    }

    @Basic
    @Column(name = "limit_begin_time", nullable = true)
    public Timestamp getLimitBeginTime() {
        return limitBeginTime;
    }

    public void setLimitBeginTime(Timestamp limitBeginTime) {
        this.limitBeginTime = limitBeginTime;
    }

    @Basic
    @Column(name = "level_id", nullable = true)
    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
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
    @Column(name = "bar_code", nullable = true, length = 100)
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Basic
    @Column(name = "bar_code_shop", nullable = true, length = 100)
    public String getBarCodeShop() {
        return barCodeShop;
    }

    public void setBarCodeShop(String barCodeShop) {
        this.barCodeShop = barCodeShop;
    }

    @Basic
    @Column(name = "sell_count", nullable = true)
    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbGoods goods = (TbGoods) o;
        return goodsId == goods.goodsId &&
                Objects.equals(goodsName, goods.goodsName) &&
                Objects.equals(title, goods.title) &&
                Objects.equals(goodsImg, goods.goodsImg) &&
                Objects.equals(trueMoney, goods.trueMoney) &&
                Objects.equals(nowMoney, goods.nowMoney) &&
                Objects.equals(intro, goods.intro) &&
                Objects.equals(shopId, goods.shopId) &&
                Objects.equals(isYuding, goods.isYuding) &&
                Objects.equals(getWay, goods.getWay) &&
                Objects.equals(isInvoice, goods.isInvoice) &&
                Objects.equals(isPutaway, goods.isPutaway) &&
                Objects.equals(dataIndex, goods.dataIndex) &&
                Objects.equals(categoryId, goods.categoryId) &&
                Objects.equals(areaId, goods.areaId) &&
                Objects.equals(property, goods.property) &&
                Objects.equals(startTime, goods.startTime) &&
                Objects.equals(endTime, goods.endTime) &&
                Objects.equals(goodsCode, goods.goodsCode) &&
                Objects.equals(goodsUnit, goods.goodsUnit) &&
                Objects.equals(isSingle, goods.isSingle) &&
                Objects.equals(goodsNum, goods.goodsNum) &&
                Objects.equals(isReturn, goods.isReturn) &&
                Objects.equals(couponId, goods.couponId) &&
                Objects.equals(createman, goods.createman) &&
                Objects.equals(createtime, goods.createtime) &&
                Objects.equals(lastaltertime, goods.lastaltertime) &&
                Objects.equals(lastalterman, goods.lastalterman) &&
                Objects.equals(status, goods.status) &&
                Objects.equals(remark, goods.remark) &&
                Objects.equals(goodsType, goods.goodsType) &&
                Objects.equals(isPayOnline, goods.isPayOnline) &&
                Objects.equals(outCode, goods.outCode) &&
                Objects.equals(depositMoney, goods.depositMoney) &&
                Objects.equals(peisongZcDesc, goods.peisongZcDesc) &&
                Objects.equals(peisongPeiDesc, goods.peisongPeiDesc) &&
                Objects.equals(purchasePrice, goods.purchasePrice) &&
                Objects.equals(isTimeGoods, goods.isTimeGoods) &&
                Objects.equals(limitNum, goods.limitNum) &&
                Objects.equals(limitWay, goods.limitWay) &&
                Objects.equals(limitBeginTime, goods.limitBeginTime) &&
                Objects.equals(levelId, goods.levelId) &&
                Objects.equals(advancePrice, goods.advancePrice) &&
                Objects.equals(barCode, goods.barCode) &&
                Objects.equals(barCodeShop, goods.barCodeShop) &&
                Objects.equals(sellCount, goods.sellCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(goodsId, goodsName, title, goodsImg, trueMoney, nowMoney, intro, shopId, isYuding, getWay, isInvoice, isPutaway, dataIndex, categoryId, areaId, property, startTime, endTime, goodsCode, goodsUnit, isSingle, goodsNum, isReturn, couponId, createman, createtime, lastaltertime, lastalterman, status, remark, goodsType, isPayOnline, outCode, depositMoney, peisongZcDesc, peisongPeiDesc, purchasePrice, isTimeGoods, limitNum, limitWay, limitBeginTime, levelId, advancePrice, barCode, barCodeShop, sellCount);
    }
}
