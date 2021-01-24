package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_check_in", schema = "store_kc_db", catalog = "")
public class TbCheckIn {
    private int id;
    private String goodsCode;
    private String unitType;
    private String shopCode;
    private Integer unitCount;
    private String orderNum;
    private Byte status;
    private Integer goodsSort;

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
    @Column(name = "goods_code", nullable = true, length = 50)
    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    @Basic
    @Column(name = "unit_type", nullable = true, length = 10)
    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    @Basic
    @Column(name = "shop_code", nullable = true, length = 50)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Basic
    @Column(name = "unit_count", nullable = true)
    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    @Basic
    @Column(name = "order_num", nullable = true, length = 30)
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
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
    @Column(name = "goods_sort", nullable = true)
    public Integer getGoodsSort() {
        return goodsSort;
    }

    public void setGoodsSort(Integer goodsSort) {
        this.goodsSort = goodsSort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCheckIn tbCheckIn = (TbCheckIn) o;
        return id == tbCheckIn.id &&
                Objects.equals(goodsCode, tbCheckIn.goodsCode) &&
                Objects.equals(unitType, tbCheckIn.unitType) &&
                Objects.equals(shopCode, tbCheckIn.shopCode) &&
                Objects.equals(unitCount, tbCheckIn.unitCount) &&
                Objects.equals(orderNum, tbCheckIn.orderNum) &&
                Objects.equals(status, tbCheckIn.status) &&
                Objects.equals(goodsSort, tbCheckIn.goodsSort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goodsCode, unitType, shopCode, unitCount, orderNum, status, goodsSort);
    }
}
