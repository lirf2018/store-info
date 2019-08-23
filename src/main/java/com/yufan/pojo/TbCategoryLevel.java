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
@Table(name = "tb_category_level", schema = "testlirf", catalog = "")
public class TbCategoryLevel {
    private int levelId;
    private String levelCode;
    private String levelName;
    private String levelImg;
    private Integer dataIndex;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Integer shopId;
    private Integer categoryType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "level_id", nullable = false)
    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    @Basic
    @Column(name = "level_code", nullable = true, length = 50)
    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    @Basic
    @Column(name = "level_name", nullable = true, length = 100)
    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Basic
    @Column(name = "level_img", nullable = true, length = 200)
    public String getLevelImg() {
        return levelImg;
    }

    public void setLevelImg(String levelImg) {
        this.levelImg = levelImg;
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

    @Basic
    @Column(name = "category_type", nullable = true)
    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCategoryLevel that = (TbCategoryLevel) o;
        return levelId == that.levelId &&
                Objects.equals(levelCode, that.levelCode) &&
                Objects.equals(levelName, that.levelName) &&
                Objects.equals(levelImg, that.levelImg) &&
                Objects.equals(dataIndex, that.dataIndex) &&
                Objects.equals(createman, that.createman) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(shopId, that.shopId) &&
                Objects.equals(categoryType, that.categoryType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(levelId, levelCode, levelName, levelImg, dataIndex, createman, createtime, status, remark, shopId, categoryType);
    }
}
