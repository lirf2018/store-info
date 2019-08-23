package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 14:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_shop", schema = "testlirf", catalog = "")
public class TbShop {
    private int shopId;
    private String shopName;
    private String shopLogo;
    private String shopTel1;
    private String shopTel2;
    private String shopLng;
    private String shopLat;
    private Integer weight;
    private String introduce;
    private String toway;
    private Integer adminId;
    private String address;
    private Integer areaId;
    private BigDecimal deposit;
    private Timestamp depositTime;
    private BigDecimal shopMoney;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Timestamp enterStartTime;
    private Timestamp enterEndTime;
    private Integer isOutShop;
    private String shopCode;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "shop_id", nullable = false)
    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Basic
    @Column(name = "shop_name", nullable = true, length = 40)
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Basic
    @Column(name = "shop_logo", nullable = true, length = 200)
    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    @Basic
    @Column(name = "shop_tel1", nullable = true, length = 20)
    public String getShopTel1() {
        return shopTel1;
    }

    public void setShopTel1(String shopTel1) {
        this.shopTel1 = shopTel1;
    }

    @Basic
    @Column(name = "shop_tel2", nullable = true, length = 20)
    public String getShopTel2() {
        return shopTel2;
    }

    public void setShopTel2(String shopTel2) {
        this.shopTel2 = shopTel2;
    }

    @Basic
    @Column(name = "shop_lng", nullable = true, length = 50)
    public String getShopLng() {
        return shopLng;
    }

    public void setShopLng(String shopLng) {
        this.shopLng = shopLng;
    }

    @Basic
    @Column(name = "shop_lat", nullable = true, length = 50)
    public String getShopLat() {
        return shopLat;
    }

    public void setShopLat(String shopLat) {
        this.shopLat = shopLat;
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
    @Column(name = "introduce", nullable = true, length = -1)
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Basic
    @Column(name = "toway", nullable = true, length = 300)
    public String getToway() {
        return toway;
    }

    public void setToway(String toway) {
        this.toway = toway;
    }

    @Basic
    @Column(name = "admin_id", nullable = true)
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    @Basic
    @Column(name = "address", nullable = true, length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    @Column(name = "deposit", nullable = true, precision = 2)
    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    @Basic
    @Column(name = "deposit_time", nullable = true)
    public Timestamp getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Timestamp depositTime) {
        this.depositTime = depositTime;
    }

    @Basic
    @Column(name = "shop_money", nullable = true, precision = 2)
    public BigDecimal getShopMoney() {
        return shopMoney;
    }

    public void setShopMoney(BigDecimal shopMoney) {
        this.shopMoney = shopMoney;
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
    @Column(name = "enter_start_time", nullable = true)
    public Timestamp getEnterStartTime() {
        return enterStartTime;
    }

    public void setEnterStartTime(Timestamp enterStartTime) {
        this.enterStartTime = enterStartTime;
    }

    @Basic
    @Column(name = "enter_end_time", nullable = true)
    public Timestamp getEnterEndTime() {
        return enterEndTime;
    }

    public void setEnterEndTime(Timestamp enterEndTime) {
        this.enterEndTime = enterEndTime;
    }

    @Basic
    @Column(name = "is_out_shop", nullable = true)
    public Integer getIsOutShop() {
        return isOutShop;
    }

    public void setIsOutShop(Integer isOutShop) {
        this.isOutShop = isOutShop;
    }

    @Basic
    @Column(name = "shop_code", nullable = true, length = 50)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbShop shop = (TbShop) o;
        return shopId == shop.shopId &&
                Objects.equals(shopName, shop.shopName) &&
                Objects.equals(shopLogo, shop.shopLogo) &&
                Objects.equals(shopTel1, shop.shopTel1) &&
                Objects.equals(shopTel2, shop.shopTel2) &&
                Objects.equals(shopLng, shop.shopLng) &&
                Objects.equals(shopLat, shop.shopLat) &&
                Objects.equals(weight, shop.weight) &&
                Objects.equals(introduce, shop.introduce) &&
                Objects.equals(toway, shop.toway) &&
                Objects.equals(adminId, shop.adminId) &&
                Objects.equals(address, shop.address) &&
                Objects.equals(areaId, shop.areaId) &&
                Objects.equals(deposit, shop.deposit) &&
                Objects.equals(depositTime, shop.depositTime) &&
                Objects.equals(shopMoney, shop.shopMoney) &&
                Objects.equals(createman, shop.createman) &&
                Objects.equals(createtime, shop.createtime) &&
                Objects.equals(status, shop.status) &&
                Objects.equals(remark, shop.remark) &&
                Objects.equals(enterStartTime, shop.enterStartTime) &&
                Objects.equals(enterEndTime, shop.enterEndTime) &&
                Objects.equals(isOutShop, shop.isOutShop) &&
                Objects.equals(shopCode, shop.shopCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(shopId, shopName, shopLogo, shopTel1, shopTel2, shopLng, shopLat, weight, introduce, toway, adminId, address, areaId, deposit, depositTime, shopMoney, createman, createtime, status, remark, enterStartTime, enterEndTime, isOutShop, shopCode);
    }
}
