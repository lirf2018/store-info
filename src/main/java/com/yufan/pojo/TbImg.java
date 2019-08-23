package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/13 11:09
 * 功能介绍:
 */
@Entity
@Table(name = "tb_img", schema = "testlirf", catalog = "")
public class TbImg {
    private int imgId;
    private String imgUrl;
    private Integer imgType;
    private Integer imgClassify;
    private Timestamp createtime;
    private Integer relateId;
    private Integer imgSort;
    private Integer shopId;
    private Integer status;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "img_id", nullable = false)
    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    @Basic
    @Column(name = "img_url", nullable = true, length = 200)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Basic
    @Column(name = "img_type", nullable = true)
    public Integer getImgType() {
        return imgType;
    }

    public void setImgType(Integer imgType) {
        this.imgType = imgType;
    }

    @Basic
    @Column(name = "img_classify", nullable = true)
    public Integer getImgClassify() {
        return imgClassify;
    }

    public void setImgClassify(Integer imgClassify) {
        this.imgClassify = imgClassify;
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
    @Column(name = "relate_id", nullable = true)
    public Integer getRelateId() {
        return relateId;
    }

    public void setRelateId(Integer relateId) {
        this.relateId = relateId;
    }

    @Basic
    @Column(name = "img_sort", nullable = true)
    public Integer getImgSort() {
        return imgSort;
    }

    public void setImgSort(Integer imgSort) {
        this.imgSort = imgSort;
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
    @Column(name = "status", nullable = true)
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
        TbImg img = (TbImg) o;
        return imgId == img.imgId &&
                Objects.equals(imgUrl, img.imgUrl) &&
                Objects.equals(imgType, img.imgType) &&
                Objects.equals(imgClassify, img.imgClassify) &&
                Objects.equals(createtime, img.createtime) &&
                Objects.equals(relateId, img.relateId) &&
                Objects.equals(imgSort, img.imgSort) &&
                Objects.equals(shopId, img.shopId) &&
                Objects.equals(status, img.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(imgId, imgUrl, imgType, imgClassify, createtime, relateId, imgSort, shopId, status);
    }
}
