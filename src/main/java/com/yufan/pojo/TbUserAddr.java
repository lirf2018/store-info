package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/6 16:55
 * 功能介绍:
 */
@Entity
@Table(name = "tb_user_addr", schema = "store-db", catalog = "")
public class TbUserAddr {
    private int id;
    private String areaIds;
    private String areaName;
    private Integer userId;
    private String userPhone;
    private String userName;
    private String addrDetail;
    private Integer isDefault;
    private Integer status;
    private Timestamp createtime;
    private String addrName;
    private Integer addrType;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "area_ids", nullable = true, length = 200)
    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    @Basic
    @Column(name = "area_name", nullable = true, length = 300)
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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
    @Column(name = "user_phone", nullable = true, length = 15)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Basic
    @Column(name = "user_name", nullable = true, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "addr_detail", nullable = true, length = 300)
    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    @Basic
    @Column(name = "is_default", nullable = true)
    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
    @Column(name = "createtime", nullable = true)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "addr_name", nullable = true, length = 200)
    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    @Basic
    @Column(name = "addr_type", nullable = true)
    public Integer getAddrType() {
        return addrType;
    }

    public void setAddrType(Integer addrType) {
        this.addrType = addrType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbUserAddr that = (TbUserAddr) o;
        return id == that.id &&
                Objects.equals(areaIds, that.areaIds) &&
                Objects.equals(areaName, that.areaName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userPhone, that.userPhone) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(addrDetail, that.addrDetail) &&
                Objects.equals(isDefault, that.isDefault) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(addrName, that.addrName) &&
                Objects.equals(addrType, that.addrType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, areaIds, areaName, userId, userPhone, userName, addrDetail, isDefault, status, createtime, addrName, addrType);
    }
}
