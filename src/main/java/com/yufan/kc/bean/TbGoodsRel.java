package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_goods_rel", schema = "store_kc_db", catalog = "")
public class TbGoodsRel {
    private int id;
    private Integer goodsId;
    private Integer relGoodsId;

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
    @Column(name = "goods_id", nullable = true)
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "rel_goods_id", nullable = true)
    public Integer getRelGoodsId() {
        return relGoodsId;
    }

    public void setRelGoodsId(Integer relGoodsId) {
        this.relGoodsId = relGoodsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbGoodsRel that = (TbGoodsRel) o;
        return id == that.id &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(relGoodsId, that.relGoodsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goodsId, relGoodsId);
    }
}
