package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_exception", schema = "store_kc_db", catalog = "")
public class TbException {
    private int id;
    private String contents;
    private Byte status;
    private Integer relId;
    private Byte relType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "contents", nullable = true, length = 255)
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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
    @Column(name = "rel_id", nullable = true)
    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    @Basic
    @Column(name = "rel_type", nullable = true)
    public Byte getRelType() {
        return relType;
    }

    public void setRelType(Byte relType) {
        this.relType = relType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbException that = (TbException) o;
        return id == that.id &&
                Objects.equals(contents, that.contents) &&
                Objects.equals(status, that.status) &&
                Objects.equals(relId, that.relId) &&
                Objects.equals(relType, that.relType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, status, relId, relType);
    }
}
