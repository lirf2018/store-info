package com.yufan.kc.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/2 22:15
 * 功能介绍:
 */
@Entity
@Table(name = "tb_calssify", schema = "store_kc_db", catalog = "")
public class TbCalssify {
    private int calssifyId;
    private String calssifyName;
    private String calssifyCode;
    private Integer calssifySort;
    private Timestamp createTime;
    private String createUserName;
    private String remark;

    @Id
    @Column(name = "calssify_id", nullable = false)
    public int getCalssifyId() {
        return calssifyId;
    }

    public void setCalssifyId(int calssifyId) {
        this.calssifyId = calssifyId;
    }

    @Basic
    @Column(name = "calssify_name", nullable = true, length = 50)
    public String getCalssifyName() {
        return calssifyName;
    }

    public void setCalssifyName(String calssifyName) {
        this.calssifyName = calssifyName;
    }

    @Basic
    @Column(name = "calssify_code", nullable = true, length = 50)
    public String getCalssifyCode() {
        return calssifyCode;
    }

    public void setCalssifyCode(String calssifyCode) {
        this.calssifyCode = calssifyCode;
    }

    @Basic
    @Column(name = "calssify_sort", nullable = true)
    public Integer getCalssifySort() {
        return calssifySort;
    }

    public void setCalssifySort(Integer calssifySort) {
        this.calssifySort = calssifySort;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCalssify that = (TbCalssify) o;
        return calssifyId == that.calssifyId &&
                Objects.equals(calssifyName, that.calssifyName) &&
                Objects.equals(calssifyCode, that.calssifyCode) &&
                Objects.equals(calssifySort, that.calssifySort) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(createUserName, that.createUserName) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calssifyId, calssifyName, calssifyCode, calssifySort, createTime, createUserName, remark);
    }
}
