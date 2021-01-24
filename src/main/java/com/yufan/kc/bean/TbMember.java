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
@Table(name = "tb_member", schema = "store_kc_db", catalog = "")
public class TbMember {
    private int memberId;
    private String memberNum;
    private String userPhone;
    private String loginPasswd;
    private Byte userStatus;
    private Timestamp createTime;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "member_id", nullable = false)
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Basic
    @Column(name = "member_num", nullable = true, length = 50)
    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
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
    @Column(name = "login_passwd", nullable = true, length = 100)
    public String getLoginPasswd() {
        return loginPasswd;
    }

    public void setLoginPasswd(String loginPasswd) {
        this.loginPasswd = loginPasswd;
    }

    @Basic
    @Column(name = "user_status", nullable = true)
    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbMember tbMember = (TbMember) o;
        return memberId == tbMember.memberId &&
                Objects.equals(memberNum, tbMember.memberNum) &&
                Objects.equals(userPhone, tbMember.userPhone) &&
                Objects.equals(loginPasswd, tbMember.loginPasswd) &&
                Objects.equals(userStatus, tbMember.userStatus) &&
                Objects.equals(createTime, tbMember.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, memberNum, userPhone, loginPasswd, userStatus, createTime);
    }
}
