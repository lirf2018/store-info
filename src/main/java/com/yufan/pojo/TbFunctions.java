package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/12 11:36
 * 功能介绍:
 */
@Entity
@Table(name = "tb_functions", schema = "testlirf", catalog = "")
public class TbFunctions {
    private int functionId;
    private String functionCode;
    private String functionName;
    private Integer functionParentid;
    private Integer functionType;
    private Integer dataIndex;
    private String functionAction;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Integer shopId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "function_id", nullable = false)
    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    @Basic
    @Column(name = "function_code", nullable = true, length = 20)
    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    @Basic
    @Column(name = "function_name", nullable = true, length = 20)
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Basic
    @Column(name = "function_parentid", nullable = true)
    public Integer getFunctionParentid() {
        return functionParentid;
    }

    public void setFunctionParentid(Integer functionParentid) {
        this.functionParentid = functionParentid;
    }

    @Basic
    @Column(name = "function_type", nullable = true)
    public Integer getFunctionType() {
        return functionType;
    }

    public void setFunctionType(Integer functionType) {
        this.functionType = functionType;
    }

    @Basic
    @Column(name = "data_index", nullable = true)
    public Integer getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(Integer dataIndex) {
        this.dataIndex = dataIndex;
    }

    @Basic
    @Column(name = "function_action", nullable = true, length = 50)
    public String getFunctionAction() {
        return functionAction;
    }

    public void setFunctionAction(String functionAction) {
        this.functionAction = functionAction;
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

    @Basic
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbFunctions that = (TbFunctions) o;
        return functionId == that.functionId &&
                Objects.equals(functionCode, that.functionCode) &&
                Objects.equals(functionName, that.functionName) &&
                Objects.equals(functionParentid, that.functionParentid) &&
                Objects.equals(functionType, that.functionType) &&
                Objects.equals(dataIndex, that.dataIndex) &&
                Objects.equals(functionAction, that.functionAction) &&
                Objects.equals(createman, that.createman) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(functionId, functionCode, functionName, functionParentid, functionType, dataIndex, functionAction, createman, createtime, status, remark, shopId);
    }
}
