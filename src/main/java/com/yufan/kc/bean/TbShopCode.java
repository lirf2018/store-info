package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/30 22:36
 * 功能介绍:
 */
@Entity
@Table(name = "tb_shop_code", schema = "store_kc_db", catalog = "")
public class TbShopCode {
    private int id;
    private String shopCode;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "shop_code", nullable = true, length = 50)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbShopCode that = (TbShopCode) o;
        return id == that.id &&
                Objects.equals(shopCode, that.shopCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopCode);
    }
}
