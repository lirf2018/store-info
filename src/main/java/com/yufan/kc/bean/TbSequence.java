package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/30 22:36
 * 功能介绍:
 */
@Entity
@Table(name = "tb_sequence", schema = "store_kc_db", catalog = "")
public class TbSequence {
    private int sequenceId;
    private int sequenceType;
    private int sequenceValue;

    @Id
    @Column(name = "sequence_id", nullable = false)
    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Basic
    @Column(name = "sequence_type", nullable = false)
    public int getSequenceType() {
        return sequenceType;
    }

    public void setSequenceType(int sequenceType) {
        this.sequenceType = sequenceType;
    }

    @Basic
    @Column(name = "sequence_value", nullable = false)
    public int getSequenceValue() {
        return sequenceValue;
    }

    public void setSequenceValue(int sequenceValue) {
        this.sequenceValue = sequenceValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbSequence that = (TbSequence) o;
        return sequenceId == that.sequenceId &&
                sequenceType == that.sequenceType &&
                sequenceValue == that.sequenceValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceId, sequenceType, sequenceValue);
    }
}
