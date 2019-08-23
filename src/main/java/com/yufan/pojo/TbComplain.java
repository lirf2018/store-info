package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 9:28
 * 功能介绍:
 */
@Entity
@Table(name = "tb_complain", schema = "testlirf", catalog = "")
public class TbComplain {
    private int id;
    private Integer userId;
    private String information;
    private String contents;
    private Integer status;
    private Integer isRead;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private Integer shopId;
    private Integer complainType;
    private String answer;

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
    @Column(name = "user_id", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "information", nullable = true, length = 100)
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Basic
    @Column(name = "contents", nullable = true, length = 500)
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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
    @Column(name = "is_read", nullable = true)
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Basic
    @Column(name = "complain_type", nullable = true)
    public Integer getComplainType() {
        return complainType;
    }

    public void setComplainType(Integer complainType) {
        this.complainType = complainType;
    }

    @Basic
    @Column(name = "answer", nullable = true, length = 500)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbComplain complain = (TbComplain) o;
        return id == complain.id &&
                Objects.equals(userId, complain.userId) &&
                Objects.equals(information, complain.information) &&
                Objects.equals(contents, complain.contents) &&
                Objects.equals(status, complain.status) &&
                Objects.equals(isRead, complain.isRead) &&
                Objects.equals(createtime, complain.createtime) &&
                Objects.equals(lastaltertime, complain.lastaltertime) &&
                Objects.equals(shopId, complain.shopId) &&
                Objects.equals(complainType, complain.complainType) &&
                Objects.equals(answer, complain.answer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, information, contents, status, isRead, createtime, lastaltertime, shopId, complainType, answer);
    }
}
