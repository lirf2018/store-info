package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/7 22:48
 * 功能介绍:
 */
@Entity
@Table(name = "tb_verification" , schema = "store-db" , catalog = "")
public class TbVerification {
    private int id;
    private Integer validType;
    private String validParam;
    private String validCode;
    private String validDesc;
    private Integer status;
    private Timestamp passTime;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private String userSnsUid;
    private Integer sendStatus;
    private String sendMsg;
    private Integer shopId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id" , nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "valid_type" , nullable = true)
    public Integer getValidType() {
        return validType;
    }

    public void setValidType(Integer validType) {
        this.validType = validType;
    }

    @Basic
    @Column(name = "valid_param" , nullable = true, length = 255)
    public String getValidParam() {
        return validParam;
    }

    public void setValidParam(String validParam) {
        this.validParam = validParam;
    }

    @Basic
    @Column(name = "valid_code" , nullable = true, length = 255)
    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    @Basic
    @Column(name = "valid_desc" , nullable = true, length = 255)
    public String getValidDesc() {
        return validDesc;
    }

    public void setValidDesc(String validDesc) {
        this.validDesc = validDesc;
    }

    @Basic
    @Column(name = "status" , nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "pass_time" , nullable = true)
    public Timestamp getPassTime() {
        return passTime;
    }

    public void setPassTime(Timestamp passTime) {
        this.passTime = passTime;
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
    @Column(name = "lastaltertime" , nullable = true)
    public Timestamp getLastaltertime() {
        return lastaltertime;
    }

    public void setLastaltertime(Timestamp lastaltertime) {
        this.lastaltertime = lastaltertime;
    }

    @Basic
    @Column(name = "user_sns_uid" , nullable = true, length = 255)
    public String getUserSnsUid() {
        return userSnsUid;
    }

    public void setUserSnsUid(String userSnsUid) {
        this.userSnsUid = userSnsUid;
    }

    @Basic
    @Column(name = "send_status" , nullable = true)
    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Basic
    @Column(name = "send_msg" , nullable = true, length = 1000)
    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    @Basic
    @Column(name = "shop_id" , nullable = true)
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
        TbVerification that = (TbVerification) o;
        return id == that.id &&
                Objects.equals(validType, that.validType) &&
                Objects.equals(validParam, that.validParam) &&
                Objects.equals(validCode, that.validCode) &&
                Objects.equals(validDesc, that.validDesc) &&
                Objects.equals(status, that.status) &&
                Objects.equals(passTime, that.passTime) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(lastaltertime, that.lastaltertime) &&
                Objects.equals(userSnsUid, that.userSnsUid) &&
                Objects.equals(sendStatus, that.sendStatus) &&
                Objects.equals(sendMsg, that.sendMsg) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, validType, validParam, validCode, validDesc, status, passTime, createtime, lastaltertime, userSnsUid, sendStatus, sendMsg, shopId);
    }
}
