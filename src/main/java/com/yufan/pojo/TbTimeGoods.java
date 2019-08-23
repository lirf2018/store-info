package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 10:54
 * 功能介绍:
 */
@Entity
@Table(name = "tb_time_goods", schema = "testlirf", catalog = "")
public class TbTimeGoods {
    private int id;
    private Integer goodsId;
    private Integer goodsskuId;
    private Timestamp beginTime;
    private Timestamp endTime;
    private BigDecimal timePrice;
    private Integer goodsStore;
    private Integer limitNum;
    private Integer timeWay;
    private Integer weight;
    private Integer isMakeSure;
    private String createman;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private String lastalterman;
    private Integer status;
    private String remark;
    private Timestamp limitBeginTime;
    private Integer shopId;

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
    @Column(name = "goodssku_id", nullable = true)
    public Integer getGoodsskuId() {
        return goodsskuId;
    }

    public void setGoodsskuId(Integer goodsskuId) {
        this.goodsskuId = goodsskuId;
    }

    @Basic
    @Column(name = "begin_time", nullable = true)
    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
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
    @Column(name = "time_price", nullable = true, precision = 2)
    public BigDecimal getTimePrice() {
        return timePrice;
    }

    public void setTimePrice(BigDecimal timePrice) {
        this.timePrice = timePrice;
    }

    @Basic
    @Column(name = "goods_store", nullable = true)
    public Integer getGoodsStore() {
        return goodsStore;
    }

    public void setGoodsStore(Integer goodsStore) {
        this.goodsStore = goodsStore;
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
    @Column(name = "time_way", nullable = true)
    public Integer getTimeWay() {
        return timeWay;
    }

    public void setTimeWay(Integer timeWay) {
        this.timeWay = timeWay;
    }

    @Basic
    @Column(name = "weight", nullable = true)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "is_make_sure", nullable = true)
    public Integer getIsMakeSure() {
        return isMakeSure;
    }

    public void setIsMakeSure(Integer isMakeSure) {
        this.isMakeSure = isMakeSure;
    }

    @Basic
    @Column(name = "createman", nullable = true, length = 50)
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
    @Column(name = "lastalterman", nullable = true, length = 50)
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
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbTimeGoods that = (TbTimeGoods) o;
        return id == that.id &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(goodsskuId, that.goodsskuId) &&
                Objects.equals(beginTime, that.beginTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(timePrice, that.timePrice) &&
                Objects.equals(goodsStore, that.goodsStore) &&
                Objects.equals(limitNum, that.limitNum) &&
                Objects.equals(timeWay, that.timeWay) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(isMakeSure, that.isMakeSure) &&
                Objects.equals(createman, that.createman) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(lastaltertime, that.lastaltertime) &&
                Objects.equals(lastalterman, that.lastalterman) &&
                Objects.equals(status, that.status) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(limitBeginTime, that.limitBeginTime) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, goodsId, goodsskuId, beginTime, endTime, timePrice, goodsStore, limitNum, timeWay, weight, isMakeSure, createman, createtime, lastaltertime, lastalterman, status, remark, limitBeginTime, shopId);
    }
}
