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
@Table(name = "tb_activity", schema = "testlirf", catalog = "")
public class TbActivity {
    private int activityId;
    private String activityTitle;
    private String activityName;
    private String activityLink;
    private String activityImg;
    private String activityIntro;
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
    @Column(name = "activity_id", nullable = false)
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Basic
    @Column(name = "activity_title", nullable = true, length = 40)
    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    @Basic
    @Column(name = "activity_name", nullable = true, length = 40)
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Basic
    @Column(name = "activity_link", nullable = true, length = 100)
    public String getActivityLink() {
        return activityLink;
    }

    public void setActivityLink(String activityLink) {
        this.activityLink = activityLink;
    }

    @Basic
    @Column(name = "activity_img", nullable = true, length = 200)
    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    @Basic
    @Column(name = "activity_intro", nullable = true, length = 200)
    public String getActivityIntro() {
        return activityIntro;
    }

    public void setActivityIntro(String activityIntro) {
        this.activityIntro = activityIntro;
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
        TbActivity activity = (TbActivity) o;
        return activityId == activity.activityId &&
                Objects.equals(activityTitle, activity.activityTitle) &&
                Objects.equals(activityName, activity.activityName) &&
                Objects.equals(activityLink, activity.activityLink) &&
                Objects.equals(activityImg, activity.activityImg) &&
                Objects.equals(activityIntro, activity.activityIntro) &&
                Objects.equals(dataIndex, activity.dataIndex) &&
                Objects.equals(channelId, activity.channelId) &&
                Objects.equals(startTime, activity.startTime) &&
                Objects.equals(endTime, activity.endTime) &&
                Objects.equals(areaId, activity.areaId) &&
                Objects.equals(createman, activity.createman) &&
                Objects.equals(createtime, activity.createtime) &&
                Objects.equals(status, activity.status) &&
                Objects.equals(remark, activity.remark) &&
                Objects.equals(shopId, activity.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(activityId, activityTitle, activityName, activityLink, activityImg, activityIntro, dataIndex, channelId, startTime, endTime, areaId, createman, createtime, status, remark, shopId);
    }
}
