package com.yufan.kc.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:11
 * 功能介绍:
 */
@Entity
@Table(name = "tb_table_log", schema = "store_kc_db", catalog = "")
public class TbTableLog {
    private int id;
    private Byte logType;
    private Timestamp logTime;
    private Byte logItem;
    private String tableName;

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
    @Column(name = "log_type", nullable = true)
    public Byte getLogType() {
        return logType;
    }

    public void setLogType(Byte logType) {
        this.logType = logType;
    }

    @Basic
    @Column(name = "log_time", nullable = true)
    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    @Basic
    @Column(name = "log_item", nullable = true)
    public Byte getLogItem() {
        return logItem;
    }

    public void setLogItem(Byte logItem) {
        this.logItem = logItem;
    }

    @Basic
    @Column(name = "table_name", nullable = true, length = 50)
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbTableLog that = (TbTableLog) o;
        return id == that.id &&
                Objects.equals(logType, that.logType) &&
                Objects.equals(logTime, that.logTime) &&
                Objects.equals(logItem, that.logItem) &&
                Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, logType, logTime, logItem, tableName);
    }
}
