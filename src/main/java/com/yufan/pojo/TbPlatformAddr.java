package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/8 15:37
 * 功能介绍:
 */
@Entity
@Table(name = "tb_platform_addr", schema = "testlirf", catalog = "")
public class TbPlatformAddr {
    private int id;
    private String detailAddr;
    private String responsibleMan;
    private String responsiblePhone;
    private BigDecimal freight;
    private String sortChar;
    private String addrPrefix;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Integer addrSort;
    private Integer addrType;
    private String addrDesc;
    private String addrName;
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
    @Column(name = "detail_addr", nullable = true, length = 100)
    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    @Basic
    @Column(name = "responsible_man", nullable = true, length = 50)
    public String getResponsibleMan() {
        return responsibleMan;
    }

    public void setResponsibleMan(String responsibleMan) {
        this.responsibleMan = responsibleMan;
    }

    @Basic
    @Column(name = "responsible_phone", nullable = true, length = 18)
    public String getResponsiblePhone() {
        return responsiblePhone;
    }

    public void setResponsiblePhone(String responsiblePhone) {
        this.responsiblePhone = responsiblePhone;
    }

    @Basic
    @Column(name = "freight", nullable = true, precision = 2)
    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    @Basic
    @Column(name = "sort_char", nullable = true, length = 10)
    public String getSortChar() {
        return sortChar;
    }

    public void setSortChar(String sortChar) {
        this.sortChar = sortChar;
    }

    @Basic
    @Column(name = "addr_prefix", nullable = true, length = 50)
    public String getAddrPrefix() {
        return addrPrefix;
    }

    public void setAddrPrefix(String addrPrefix) {
        this.addrPrefix = addrPrefix;
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
    @Column(name = "addr_sort", nullable = true)
    public Integer getAddrSort() {
        return addrSort;
    }

    public void setAddrSort(Integer addrSort) {
        this.addrSort = addrSort;
    }

    @Basic
    @Column(name = "addr_type", nullable = true)
    public Integer getAddrType() {
        return addrType;
    }

    public void setAddrType(Integer addrType) {
        this.addrType = addrType;
    }

    @Basic
    @Column(name = "addr_desc", nullable = true, length = 500)
    public String getAddrDesc() {
        return addrDesc;
    }

    public void setAddrDesc(String addrDesc) {
        this.addrDesc = addrDesc;
    }

    @Basic
    @Column(name = "addr_name", nullable = true, length = 300)
    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
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
        TbPlatformAddr that = (TbPlatformAddr) o;
        return id == that.id &&
                Objects.equals(detailAddr, that.detailAddr) &&
                Objects.equals(responsibleMan, that.responsibleMan) &&
                Objects.equals(responsiblePhone, that.responsiblePhone) &&
                Objects.equals(freight, that.freight) &&
                Objects.equals(sortChar, that.sortChar) &&
                Objects.equals(addrPrefix, that.addrPrefix) &&
                Objects.equals(createman, that.createman) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(addrSort, that.addrSort) &&
                Objects.equals(addrType, that.addrType) &&
                Objects.equals(addrDesc, that.addrDesc) &&
                Objects.equals(addrName, that.addrName) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, detailAddr, responsibleMan, responsiblePhone, freight, sortChar, addrPrefix, createman, createtime, status, remark, addrSort, addrType, addrDesc, addrName, shopId);
    }
}
