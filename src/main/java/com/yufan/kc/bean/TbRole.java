package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:11
 * 功能介绍:
 */
@Entity
@Table(name = "tb_role", schema = "store_kc_db", catalog = "")
public class TbRole {
    private int roleId;
    private String roleName;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id", nullable = false)
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "role_name", nullable = true, length = 50)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbRole tbRole = (TbRole) o;
        return roleId == tbRole.roleId &&
                Objects.equals(roleName, tbRole.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName);
    }
}
