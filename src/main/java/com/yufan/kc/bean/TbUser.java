package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:11
 * 功能介绍:
 */
@Entity
@Table(name = "tb_user", schema = "store_kc_db", catalog = "")
public class TbUser {
    private int sysUserId;
    private String sysUserName;
    private String sysUserCode;
    private String sysUserPhone;
    private Byte sysUserType;
    private Byte sysStatus;
    private Byte loginType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "sys_user_id", nullable = false)
    public int getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(int sysUserId) {
        this.sysUserId = sysUserId;
    }

    @Basic
    @Column(name = "sys_user_name", nullable = true, length = 50)
    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    @Basic
    @Column(name = "sys_user_code", nullable = true, length = 30)
    public String getSysUserCode() {
        return sysUserCode;
    }

    public void setSysUserCode(String sysUserCode) {
        this.sysUserCode = sysUserCode;
    }

    @Basic
    @Column(name = "sys_user_phone", nullable = true, length = 30)
    public String getSysUserPhone() {
        return sysUserPhone;
    }

    public void setSysUserPhone(String sysUserPhone) {
        this.sysUserPhone = sysUserPhone;
    }

    @Basic
    @Column(name = "sys_user_type", nullable = true)
    public Byte getSysUserType() {
        return sysUserType;
    }

    public void setSysUserType(Byte sysUserType) {
        this.sysUserType = sysUserType;
    }

    @Basic
    @Column(name = "sys_status", nullable = true)
    public Byte getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(Byte sysStatus) {
        this.sysStatus = sysStatus;
    }

    @Basic
    @Column(name = "login_type", nullable = true)
    public Byte getLoginType() {
        return loginType;
    }

    public void setLoginType(Byte loginType) {
        this.loginType = loginType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbUser tbUser = (TbUser) o;
        return sysUserId == tbUser.sysUserId &&
                Objects.equals(sysUserName, tbUser.sysUserName) &&
                Objects.equals(sysUserCode, tbUser.sysUserCode) &&
                Objects.equals(sysUserPhone, tbUser.sysUserPhone) &&
                Objects.equals(sysUserType, tbUser.sysUserType) &&
                Objects.equals(sysStatus, tbUser.sysStatus) &&
                Objects.equals(loginType, tbUser.loginType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sysUserId, sysUserName, sysUserCode, sysUserPhone, sysUserType, sysStatus, loginType);
    }
}
