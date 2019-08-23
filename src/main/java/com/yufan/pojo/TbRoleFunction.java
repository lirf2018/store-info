package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/3 16:41
 * 功能介绍:
 */
@Entity
@Table(name = "tb_role_function", schema = "testlirf", catalog = "")
public class TbRoleFunction {
    private int roleFunctionId;
    private Integer roleId;
    private Integer functionId;
    private Timestamp createtime;

    @Id
    @Column(name = "role_function_id", nullable = false)
    public int getRoleFunctionId() {
        return roleFunctionId;
    }

    public void setRoleFunctionId(int roleFunctionId) {
        this.roleFunctionId = roleFunctionId;
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
    @Column(name = "function_id", nullable = true)
    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
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
        TbRoleFunction that = (TbRoleFunction) o;
        return roleFunctionId == that.roleFunctionId &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(functionId, that.functionId) &&
                Objects.equals(createtime, that.createtime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(roleFunctionId, roleId, functionId, createtime);
    }
}
