package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_menu_role_rel", schema = "store_kc_db", catalog = "")
public class TbMenuRoleRel {
    private int id;
    private Integer menuId;
    private Integer roleId;

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
    @Column(name = "menu_id", nullable = true)
    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Basic
    @Column(name = "role_id", nullable = true)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbMenuRoleRel that = (TbMenuRoleRel) o;
        return id == that.id &&
                Objects.equals(menuId, that.menuId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, roleId);
    }
}
