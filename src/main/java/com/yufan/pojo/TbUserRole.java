package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/3 16:26
 * 功能介绍:
 */
@Entity
@Table(name = "tb_user_role", schema = "testlirf", catalog = "")
public class TbUserRole {
    private int id;
    private Integer adminId;
    private Integer roleId;
    private Timestamp createtime;

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
    @Column(name = "admin_id", nullable = true)
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    @Basic
    @Column(name = "role_id", nullable = true)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "createtime", nullable = true)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbUserRole that = (TbUserRole) o;
        return id == that.id &&
                Objects.equals(adminId, that.adminId) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(createtime, that.createtime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, adminId, roleId, createtime);
    }
}
