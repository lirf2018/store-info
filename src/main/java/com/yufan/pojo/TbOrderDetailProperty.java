package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 11:55
 * 功能介绍:
 */
@Entity
@Table(name = "tb_order_detail_property", schema = "testlirf", catalog = "")
public class TbOrderDetailProperty {
    private int propertyId;
    private Integer orderId;
    private Integer detailId;
    private String propertyKey;
    private String propertyValue;
    private String remark;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "property_id", nullable = false)
    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    @Basic
    @Column(name = "order_id", nullable = true)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "detail_id", nullable = true)
    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    @Basic
    @Column(name = "property_key", nullable = true, length = 50)
    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    @Basic
    @Column(name = "property_value", nullable = true, length = 255)
    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }


    @Basic
    @Column(name = "remark", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderDetailProperty that = (TbOrderDetailProperty) o;
        return propertyId == that.propertyId &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(detailId, that.detailId) &&
                Objects.equals(propertyKey, that.propertyKey) &&
                Objects.equals(propertyValue, that.propertyValue) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {

        return Objects.hash(propertyId, orderId, detailId, propertyKey, propertyValue, remark);
    }
}
