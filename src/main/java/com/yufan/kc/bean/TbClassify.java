package com.yufan.kc.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/30 21:46
 * 功能介绍:
 */
@Entity
@Table(name = "tb_classify", schema = "store_kc_db", catalog = "")
public class TbClassify {
    private int classifyId;
    private String classifyName;
    private String classifyCode;
    private Integer classifySort;
    private Timestamp createTime;
    private String createUserName;
    private String remark;
    private Byte status;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "classify_id", nullable = false)
    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    @Basic
    @Column(name = "classify_name", nullable = true, length = 50)
    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    @Basic
    @Column(name = "classify_code", nullable = true, length = 50)
    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    @Basic
    @Column(name = "classify_sort", nullable = true)
    public Integer getClassifySort() {
        return classifySort;
    }

    public void setClassifySort(Integer classifySort) {
        this.classifySort = classifySort;
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
    @Column(name = "remark", nullable = true, length = 100)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbClassify that = (TbClassify) o;
        return classifyId == that.classifyId &&
                Objects.equals(classifyName, that.classifyName) &&
                Objects.equals(classifyCode, that.classifyCode) &&
                Objects.equals(classifySort, that.classifySort) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(createUserName, that.createUserName) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classifyId, classifyName, classifyCode, classifySort, createTime, createUserName, remark, status);
    }
}
