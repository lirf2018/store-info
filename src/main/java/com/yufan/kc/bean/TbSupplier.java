package com.yufan.kc.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:11
 * 功能介绍:
 */
@Entity
@Table(name = "tb_supplier", schema = "store_kc_db", catalog = "")
public class TbSupplier {
    private int supplierId;
    private String supplierName;
    private String supplierPhone;
    private String supplierAddr;
    private Timestamp createTime;
    private String createUserName;
    private String remark;
    private Byte status;
    private String supplierCode;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "supplier_id", nullable = false)
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "supplier_name", nullable = true, length = 50)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Basic
    @Column(name = "supplier_phone", nullable = true, length = 30)
    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    @Basic
    @Column(name = "supplier_addr", nullable = true, length = 100)
    public String getSupplierAddr() {
        return supplierAddr;
    }

    public void setSupplierAddr(String supplierAddr) {
        this.supplierAddr = supplierAddr;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "create_user_name", nullable = true, length = 20)
    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Basic
    @Column(name = "remark", nullable = false, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
    @Column(name = "supplier_code", nullable = true, length = 100)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbSupplier that = (TbSupplier) o;
        return supplierId == that.supplierId &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(supplierPhone, that.supplierPhone) &&
                Objects.equals(supplierAddr, that.supplierAddr) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(createUserName, that.createUserName) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(status, that.status) &&
                Objects.equals(supplierCode, that.supplierCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierId, supplierName, supplierPhone, supplierAddr, createTime, createUserName, remark, status, supplierCode);
    }
}
