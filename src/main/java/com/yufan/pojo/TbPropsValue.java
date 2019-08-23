package com.yufan.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:29
 * 功能介绍:
 */
@Entity
@Table(name = "tb_props_value", schema = "testlirf", catalog = "")
public class TbPropsValue {
    private int valueId;
    private Integer propId;
    private String valueName;
    private Integer categoryId;
    private String outeId;
    private String value;
    private Integer dataIndex;
    private Integer status;
    private String valueImg;
    private Integer shopId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "value_id", nullable = false)
    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    @Basic
    @Column(name = "prop_id", nullable = true)
    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    @Basic
    @Column(name = "value_name", nullable = true, length = 50)
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
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
    @Column(name = "value", nullable = true, length = 100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
    @Column(name = "value_img", nullable = true, length = 200)
    public String getValueImg() {
        return valueImg;
    }

    public void setValueImg(String valueImg) {
        this.valueImg = valueImg;
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
        TbPropsValue that = (TbPropsValue) o;
        return valueId == that.valueId &&
                Objects.equals(propId, that.propId) &&
                Objects.equals(valueName, that.valueName) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(outeId, that.outeId) &&
                Objects.equals(value, that.value) &&
                Objects.equals(dataIndex, that.dataIndex) &&
                Objects.equals(status, that.status) &&
                Objects.equals(valueImg, that.valueImg) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(valueId, propId, valueName, categoryId, outeId, value, dataIndex, status, valueImg, shopId);
    }
}
