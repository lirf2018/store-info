package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/12 11:36
 * 功能介绍:
 */
@Entity
@Table(name = "tb_category", schema = "testlirf", catalog = "")
public class TbCategory {
    private int categoryId;
    private String categoryName;
    private Integer isParent;
    private Integer dataIndex;
    private String outeId;
    private String categoryImg;
    private String categoryCode;
    private Integer isShow;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Integer shopId;
    private Integer categoryType;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "category_id", nullable = false)
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "category_name", nullable = true, length = 100)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Basic
    @Column(name = "is_parent", nullable = true)
    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
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
    @Column(name = "oute_id", nullable = true, length = 50)
    public String getOuteId() {
        return outeId;
    }

    public void setOuteId(String outeId) {
        this.outeId = outeId;
    }

    @Basic
    @Column(name = "category_img", nullable = true, length = 200)
    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    @Basic
    @Column(name = "category_code", nullable = true, length = 50)
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Basic
    @Column(name = "is_show", nullable = true)
    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Basic
    @Column(name = "category_type", nullable = true)
    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCategory category = (TbCategory) o;
        return categoryId == category.categoryId &&
                Objects.equals(categoryName, category.categoryName) &&
                Objects.equals(isParent, category.isParent) &&
                Objects.equals(dataIndex, category.dataIndex) &&
                Objects.equals(outeId, category.outeId) &&
                Objects.equals(categoryImg, category.categoryImg) &&
                Objects.equals(categoryCode, category.categoryCode) &&
                Objects.equals(isShow, category.isShow) &&
                Objects.equals(createman, category.createman) &&
                Objects.equals(createtime, category.createtime) &&
                Objects.equals(status, category.status) &&
                Objects.equals(remark, category.remark) &&
                Objects.equals(shopId, category.shopId) &&
                Objects.equals(categoryType, category.categoryType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(categoryId, categoryName, isParent, dataIndex, outeId, categoryImg, categoryCode, isShow, createman, createtime, status, remark, shopId, categoryType);
    }
}
