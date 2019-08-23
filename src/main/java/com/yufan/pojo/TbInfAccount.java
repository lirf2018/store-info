package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 15:05
 * 功能介绍:
 */
@Entity
@Table(name = "tb_inf_account", schema = "testlirf", catalog = "")
public class TbInfAccount {
    private int id;
    private String sid;
    private String secretKey;
    private Integer status;
    private Timestamp createDate;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sid", nullable = true, length = 100)
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Basic
    @Column(name = "secret_key", nullable = true, length = 64)
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
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
    @Column(name = "create_date", nullable = true)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TbInfAccount that = (TbInfAccount) object;
        return id == that.id &&
                Objects.equals(sid, that.sid) &&
                Objects.equals(secretKey, that.secretKey) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sid, secretKey, status, createDate);
    }
}
