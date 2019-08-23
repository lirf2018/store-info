package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/12 11:47
 * 功能介绍:
 */
@Entity
@Table(name = "tb_banner", schema = "testlirf", catalog = "")
public class TbBanner {
    private int bannerId;
    private String bannerTitle;
    private String bannerName;
    private String bannerLink;
    private String bannerImg;
    private Integer dataIndex;
    private Integer channelId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer areaId;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private Integer shopId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "banner_id", nullable = false)
    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    @Basic
    @Column(name = "banner_title", nullable = true, length = 40)
    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    @Basic
    @Column(name = "banner_name", nullable = true, length = 40)
    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    @Basic
    @Column(name = "banner_link", nullable = true, length = 100)
    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    @Basic
    @Column(name = "banner_img", nullable = true, length = 200)
    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
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
    @Column(name = "channel_id", nullable = true)
    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    @Basic
    @Column(name = "start_time", nullable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "area_id", nullable = true)
    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
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
        TbBanner tbBanner = (TbBanner) o;
        return bannerId == tbBanner.bannerId &&
                Objects.equals(bannerTitle, tbBanner.bannerTitle) &&
                Objects.equals(bannerName, tbBanner.bannerName) &&
                Objects.equals(bannerLink, tbBanner.bannerLink) &&
                Objects.equals(bannerImg, tbBanner.bannerImg) &&
                Objects.equals(dataIndex, tbBanner.dataIndex) &&
                Objects.equals(channelId, tbBanner.channelId) &&
                Objects.equals(startTime, tbBanner.startTime) &&
                Objects.equals(endTime, tbBanner.endTime) &&
                Objects.equals(areaId, tbBanner.areaId) &&
                Objects.equals(createman, tbBanner.createman) &&
                Objects.equals(createtime, tbBanner.createtime) &&
                Objects.equals(status, tbBanner.status) &&
                Objects.equals(remark, tbBanner.remark) &&
                Objects.equals(shopId, tbBanner.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bannerId, bannerTitle, bannerName, bannerLink, bannerImg, dataIndex, channelId, startTime, endTime, areaId, createman, createtime, status, remark, shopId);
    }
}
