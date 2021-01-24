package com.yufan.kc.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_member_save", schema = "store_kc_db", catalog = "")
public class TbMemberSave {
    private int id;
    private String saveCode;
    private String userPhone;
    private String userName;
    private String passWord;
    private Integer goodsId;
    private String goodsName;
    private Byte saveType;
    private Timestamp inTime;
    private Timestamp outTime;
    private String remark;

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
    @Column(name = "save_code", nullable = true, length = 30)
    public String getSaveCode() {
        return saveCode;
    }

    public void setSaveCode(String saveCode) {
        this.saveCode = saveCode;
    }

    @Basic
    @Column(name = "user_phone", nullable = true, length = 30)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Basic
    @Column(name = "user_name", nullable = true, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "pass_word", nullable = true, length = 100)
    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Basic
    @Column(name = "goods_id", nullable = true)
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "goods_name", nullable = true, length = 50)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Basic
    @Column(name = "save_type", nullable = true)
    public Byte getSaveType() {
        return saveType;
    }

    public void setSaveType(Byte saveType) {
        this.saveType = saveType;
    }

    @Basic
    @Column(name = "in_time", nullable = true)
    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    @Basic
    @Column(name = "out_time", nullable = true)
    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
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
        TbMemberSave that = (TbMemberSave) o;
        return id == that.id &&
                Objects.equals(saveCode, that.saveCode) &&
                Objects.equals(userPhone, that.userPhone) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(passWord, that.passWord) &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(saveType, that.saveType) &&
                Objects.equals(inTime, that.inTime) &&
                Objects.equals(outTime, that.outTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, saveCode, userPhone, userName, passWord, goodsId, goodsName, saveType, inTime, outTime, remark);
    }
}
