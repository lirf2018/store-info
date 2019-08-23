package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/4/30 14:51
 * 功能介绍:
 */
@Entity
@Table(name = "tb_admin", schema = "testlirf", catalog = "")
public class TbAdmin {
    private Integer adminId;
    private String loginName;
    private String loginPassword;
    private String userName;
    private Date birthday;
    private String sex;
    private String photo;
    private String nickName;
    private String idcard;
    private String phone;
    private String email;
    private String qq;
    private Timestamp memberBegintime;
    private Timestamp memberEndtime;
    private Integer shopId;
    private Integer shopMenberType;
    private String passwordHide;
    private String createman;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private String lastalterman;
    private Timestamp lastlogintime;
    private Integer status;
    private String remark;
    private String wxOpenid;
    private Integer isMakeSure;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "admin_id", nullable = false)
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    @Basic
    @Column(name = "login_name", nullable = true, length = 50)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Basic
    @Column(name = "login_password", nullable = true, length = 64)
    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
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
    @Column(name = "birthday", nullable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "sex", nullable = true, length = 4)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "photo", nullable = true, length = 200)
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Basic
    @Column(name = "nick_name", nullable = true, length = 50)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Basic
    @Column(name = "idcard", nullable = true, length = 18)
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 12)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "qq", nullable = true, length = 15)
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Basic
    @Column(name = "member_begintime", nullable = true)
    public Timestamp getMemberBegintime() {
        return memberBegintime;
    }

    public void setMemberBegintime(Timestamp memberBegintime) {
        this.memberBegintime = memberBegintime;
    }

    @Basic
    @Column(name = "member_endtime", nullable = true)
    public Timestamp getMemberEndtime() {
        return memberEndtime;
    }

    public void setMemberEndtime(Timestamp memberEndtime) {
        this.memberEndtime = memberEndtime;
    }

    @Basic
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Basic
    @Column(name = "shop_menber_type", nullable = true)
    public Integer getShopMenberType() {
        return shopMenberType;
    }

    public void setShopMenberType(Integer shopMenberType) {
        this.shopMenberType = shopMenberType;
    }

    @Basic
    @Column(name = "password_hide", nullable = true, length = 50)
    public String getPasswordHide() {
        return passwordHide;
    }

    public void setPasswordHide(String passwordHide) {
        this.passwordHide = passwordHide;
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
    @Column(name = "lastaltertime", nullable = true)
    public Timestamp getLastaltertime() {
        return lastaltertime;
    }

    public void setLastaltertime(Timestamp lastaltertime) {
        this.lastaltertime = lastaltertime;
    }

    @Basic
    @Column(name = "lastalterman", nullable = true, length = 100)
    public String getLastalterman() {
        return lastalterman;
    }

    public void setLastalterman(String lastalterman) {
        this.lastalterman = lastalterman;
    }

    @Basic
    @Column(name = "lastlogintime", nullable = true)
    public Timestamp getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Timestamp lastlogintime) {
        this.lastlogintime = lastlogintime;
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
    @Column(name = "wx_openid", nullable = true, length = 300)
    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    @Basic
    @Column(name = "is_make_sure", nullable = true)
    public Integer getIsMakeSure() {
        return isMakeSure;
    }

    public void setIsMakeSure(Integer isMakeSure) {
        this.isMakeSure = isMakeSure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbAdmin tbAdmin = (TbAdmin) o;
        return adminId == tbAdmin.adminId &&
                Objects.equals(loginName, tbAdmin.loginName) &&
                Objects.equals(loginPassword, tbAdmin.loginPassword) &&
                Objects.equals(userName, tbAdmin.userName) &&
                Objects.equals(birthday, tbAdmin.birthday) &&
                Objects.equals(sex, tbAdmin.sex) &&
                Objects.equals(photo, tbAdmin.photo) &&
                Objects.equals(nickName, tbAdmin.nickName) &&
                Objects.equals(idcard, tbAdmin.idcard) &&
                Objects.equals(phone, tbAdmin.phone) &&
                Objects.equals(email, tbAdmin.email) &&
                Objects.equals(qq, tbAdmin.qq) &&
                Objects.equals(memberBegintime, tbAdmin.memberBegintime) &&
                Objects.equals(memberEndtime, tbAdmin.memberEndtime) &&
                Objects.equals(shopId, tbAdmin.shopId) &&
                Objects.equals(shopMenberType, tbAdmin.shopMenberType) &&
                Objects.equals(passwordHide, tbAdmin.passwordHide) &&
                Objects.equals(createman, tbAdmin.createman) &&
                Objects.equals(createtime, tbAdmin.createtime) &&
                Objects.equals(lastaltertime, tbAdmin.lastaltertime) &&
                Objects.equals(lastalterman, tbAdmin.lastalterman) &&
                Objects.equals(lastlogintime, tbAdmin.lastlogintime) &&
                Objects.equals(status, tbAdmin.status) &&
                Objects.equals(remark, tbAdmin.remark) &&
                Objects.equals(wxOpenid, tbAdmin.wxOpenid) &&
                Objects.equals(isMakeSure, tbAdmin.isMakeSure);
    }

    @Override
    public int hashCode() {

        return Objects.hash(adminId, loginName, loginPassword, userName, birthday, sex, photo, nickName, idcard, phone, email, qq, memberBegintime, memberEndtime, shopId, shopMenberType, passwordHide, createman, createtime, lastaltertime, lastalterman, lastlogintime, status, remark, wxOpenid, isMakeSure);
    }
}
