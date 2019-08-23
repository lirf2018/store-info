package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/14 13:35
 * 功能介绍:
 */
@Entity
@Table(name = "tb_goods_attribute", schema = "testlirf", catalog = "")
public class TbGoodsAttribute {
    private int attrId;
    private Integer goodsId;
    private Integer propId;
    private Integer valueId;
    private Timestamp createtime;
    private Integer shopId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "attr_id", nullable = false)
    public int getAttrId() {
        return attrId;
    }

    public void setAttrId(int attrId) {
        this.attrId = attrId;
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
    @Column(name = "prop_id", nullable = true)
    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    @Basic
    @Column(name = "value_id", nullable = true)
    public Integer getValueId() {
        return valueId;
    }

    public void setValueId(Integer valueId) {
        this.valueId = valueId;
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
        TbGoodsAttribute that = (TbGoodsAttribute) o;
        return attrId == that.attrId &&
                Objects.equals(goodsId, that.goodsId) &&
                Objects.equals(propId, that.propId) &&
                Objects.equals(valueId, that.valueId) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attrId, goodsId, propId, valueId, createtime, shopId);
    }
}
