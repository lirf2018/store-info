package com.yufan.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/19 10:49
 * 功能介绍:
 */
@Entity
@Table(name = "tb_itemprops", schema = "testlirf", catalog = "")
public class TbItemprops {
    private int propId;
    private String propName;
    private Integer categoryId;
    private String outeId;
    private Integer isSales;
    private String showView;
    private String propImg;
    private String propCode;
    private Integer isShow;
    private Integer dataIndex;
    private Integer status;
    private String remark;
    private Integer shopId;
    private Integer categoryType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "prop_id", nullable = false)
    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    @Basic
    @Column(name = "prop_name", nullable = true, length = 50)
    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
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
    @Column(name = "oute_id", nullable = true, length = 50)
    public String getOuteId() {
        return outeId;
    }

    public void setOuteId(String outeId) {
        this.outeId = outeId;
    }

    @Basic
    @Column(name = "is_sales", nullable = true)
    public Integer getIsSales() {
        return isSales;
    }

    public void setIsSales(Integer isSales) {
        this.isSales = isSales;
    }

    @Basic
    @Column(name = "show_view", nullable = true, length = 20)
    public String getShowView() {
        return showView;
    }

    public void setShowView(String showView) {
        this.showView = showView;
    }

    @Basic
    @Column(name = "prop_img", nullable = true, length = 200)
    public String getPropImg() {
        return propImg;
    }

    public void setPropImg(String propImg) {
        this.propImg = propImg;
    }

    @Basic
    @Column(name = "prop_code", nullable = true, length = 100)
    public String getPropCode() {
        return propCode;
    }

    public void setPropCode(String propCode) {
        this.propCode = propCode;
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
    @Column(name = "data_index", nullable = true)
    public Integer getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Integer dataIndex) {
        this.dataIndex = dataIndex;
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
        TbItemprops that = (TbItemprops) o;
        return propId == that.propId &&
                Objects.equals(propName, that.propName) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(outeId, that.outeId) &&
                Objects.equals(isSales, that.isSales) &&
                Objects.equals(showView, that.showView) &&
                Objects.equals(propImg, that.propImg) &&
                Objects.equals(propCode, that.propCode) &&
                Objects.equals(isShow, that.isShow) &&
                Objects.equals(dataIndex, that.dataIndex) &&
                Objects.equals(status, that.status) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(shopId, that.shopId) &&
                Objects.equals(categoryType, that.categoryType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(propId, propName, categoryId, outeId, isSales, showView, propImg, propCode, isShow, dataIndex, status, remark, shopId, categoryType);
    }
}
