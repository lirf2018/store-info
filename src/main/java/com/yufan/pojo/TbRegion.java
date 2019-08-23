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
@Table(name = "tb_region", schema = "testlirf", catalog = "")
public class TbRegion {
    private int regionId;
    private String regionCode;
    private String regionName;
    private String regionShortname;
    private String parentId;
    private Integer regionLevel;
    private Integer regionOrder;
    private String regionNameEn;
    private String regionShortnameEn;
    private Integer regionType;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private BigDecimal freight;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "region_id", nullable = false)
    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    @Basic
    @Column(name = "region_code", nullable = true, length = 50)
    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @Basic
    @Column(name = "region_name", nullable = true, length = 50)
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Basic
    @Column(name = "region_shortname", nullable = true, length = 50)
    public String getRegionShortname() {
        return regionShortname;
    }

    public void setRegionShortname(String regionShortname) {
        this.regionShortname = regionShortname;
    }

    @Basic
    @Column(name = "parent_id", nullable = true, length = 50)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "region_level", nullable = true)
    public Integer getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(Integer regionLevel) {
        this.regionLevel = regionLevel;
    }

    @Basic
    @Column(name = "region_order", nullable = true)
    public Integer getRegionOrder() {
        return regionOrder;
    }

    public void setRegionOrder(Integer regionOrder) {
        this.regionOrder = regionOrder;
    }

    @Basic
    @Column(name = "region_name_en", nullable = true, length = 50)
    public String getRegionNameEn() {
        return regionNameEn;
    }

    public void setRegionNameEn(String regionNameEn) {
        this.regionNameEn = regionNameEn;
    }

    @Basic
    @Column(name = "region_shortname_en", nullable = true, length = 50)
    public String getRegionShortnameEn() {
        return regionShortnameEn;
    }

    public void setRegionShortnameEn(String regionShortnameEn) {
        this.regionShortnameEn = regionShortnameEn;
    }

    @Basic
    @Column(name = "region_type", nullable = true)
    public Integer getRegionType() {
        return regionType;
    }

    public void setRegionType(Integer regionType) {
        this.regionType = regionType;
    }

    @Basic
    @Column(name = "createman", nullable = true, length = 5011)
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
    @Column(name = "freight", nullable = true, precision = 2)
    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbRegion region = (TbRegion) o;
        return regionId == region.regionId &&
                Objects.equals(regionCode, region.regionCode) &&
                Objects.equals(regionName, region.regionName) &&
                Objects.equals(regionShortname, region.regionShortname) &&
                Objects.equals(parentId, region.parentId) &&
                Objects.equals(regionLevel, region.regionLevel) &&
                Objects.equals(regionOrder, region.regionOrder) &&
                Objects.equals(regionNameEn, region.regionNameEn) &&
                Objects.equals(regionShortnameEn, region.regionShortnameEn) &&
                Objects.equals(regionType, region.regionType) &&
                Objects.equals(createman, region.createman) &&
                Objects.equals(createtime, region.createtime) &&
                Objects.equals(status, region.status) &&
                Objects.equals(remark, region.remark) &&
                Objects.equals(freight, region.freight);
    }

    @Override
    public int hashCode() {

        return Objects.hash(regionId, regionCode, regionName, regionShortname, parentId, regionLevel, regionOrder, regionNameEn, regionShortnameEn, regionType, createman, createtime, status, remark, freight);
    }
}
