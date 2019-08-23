package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/22 16:52
 * 功能介绍:
 */
@Entity
@Table(name = "tb_role", schema = "testlirf", catalog = "")
public class TbRole {
    private Integer roleId;
    private String roleName;
    private Integer roleParentid;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id", nullable = false)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "role_name", nullable = true, length = 20)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "role_parentid", nullable = true)
    public Integer getRoleParentid() {
        return roleParentid;
    }

    public void setRoleParentid(Integer roleParentid) {
        this.roleParentid = roleParentid;
    }

    @Basic
    @Column(name = "createman", nullable = true, length = 100)
    public String getCreateman() {
        return createman;
    }

    public void setCreateman(String createman) {
        this.createman = createman;
    }

    @Basic
    @Column(name = "createtime", nullable = true)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
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
    @Column(name = "remark", nullable = true, length = 200)
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
        TbRole tbRole = (TbRole) o;
        return roleId == tbRole.roleId &&
                Objects.equals(roleName, tbRole.roleName) &&
                Objects.equals(roleParentid, tbRole.roleParentid) &&
                Objects.equals(createman, tbRole.createman) &&
                Objects.equals(createtime, tbRole.createtime) &&
                Objects.equals(status, tbRole.status) &&
                Objects.equals(remark, tbRole.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName, roleParentid, createman, createtime, status, remark);
    }
}
