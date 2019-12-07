package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/7 17:01
 * 功能介绍:
 */
@Entity
@Table(name = "tb_user_sns" , schema = "store-db" , catalog = "")
public class TbUserSns {
    private int snsId;
    private Integer userId;
    private Integer snsType;
    private String uid;
    private String openkey;
    private String snsName;
    private String snsAccount;
    private String snsImg;
    private Integer isUseImg;
    private Timestamp createtime;
    private Timestamp updateTime;
    private Integer status;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "sns_id" , nullable = false)
    public int getSnsId() {
        return snsId;
    }

    public void setSnsId(int snsId) {
        this.snsId = snsId;
    }

    @Basic
    @Column(name = "user_id" , nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "sns_type" , nullable = true)
    public Integer getSnsType() {
        return snsType;
    }

    public void setSnsType(Integer snsType) {
        this.snsType = snsType;
    }

    @Basic
    @Column(name = "uid" , nullable = true, length = 100)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "openkey" , nullable = true, length = 100)
    public String getOpenkey() {
        return openkey;
    }

    public void setOpenkey(String openkey) {
        this.openkey = openkey;
    }

    @Basic
    @Column(name = "sns_name" , nullable = true, length = 100)
    public String getSnsName() {
        return snsName;
    }

    public void setSnsName(String snsName) {
        this.snsName = snsName;
    }

    @Basic
    @Column(name = "sns_account" , nullable = true, length = 50)
    public String getSnsAccount() {
        return snsAccount;
    }

    public void setSnsAccount(String snsAccount) {
        this.snsAccount = snsAccount;
    }

    @Basic
    @Column(name = "sns_img" , nullable = true, length = 100)
    public String getSnsImg() {
        return snsImg;
    }

    public void setSnsImg(String snsImg) {
        this.snsImg = snsImg;
    }

    @Basic
    @Column(name = "is_use_img" , nullable = true)
    public Integer getIsUseImg() {
        return isUseImg;
    }

    public void setIsUseImg(Integer isUseImg) {
        this.isUseImg = isUseImg;
    }

    @Basic
    @Column(name = "createtime" , nullable = true)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "update_time" , nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "status" , nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbUserSns tbUserSns = (TbUserSns) o;
        return snsId == tbUserSns.snsId &&
                Objects.equals(userId, tbUserSns.userId) &&
                Objects.equals(snsType, tbUserSns.snsType) &&
                Objects.equals(uid, tbUserSns.uid) &&
                Objects.equals(openkey, tbUserSns.openkey) &&
                Objects.equals(snsName, tbUserSns.snsName) &&
                Objects.equals(snsAccount, tbUserSns.snsAccount) &&
                Objects.equals(snsImg, tbUserSns.snsImg) &&
                Objects.equals(isUseImg, tbUserSns.isUseImg) &&
                Objects.equals(createtime, tbUserSns.createtime) &&
                Objects.equals(updateTime, tbUserSns.updateTime) &&
                Objects.equals(status, tbUserSns.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snsId, userId, snsType, uid, openkey, snsName, snsAccount, snsImg, isUseImg, createtime, updateTime, status);
    }
}
