package com.yufan.kc.bean;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/10/27 23:10
 * 功能介绍:
 */
@Entity
@Table(name = "tb_menu", schema = "store_kc_db", catalog = "")
public class TbMenu {
    private int menuId;
    private String menuName;
    private String menuUrl;
    private Integer menuSort;
    private Byte isUsed;
    private Integer parentId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "menu_id", nullable = false)
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Basic
    @Column(name = "menu_name", nullable = true, length = 50)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "menu_url", nullable = true, length = 100)
    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @Basic
    @Column(name = "menu_sort", nullable = true)
    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    @Basic
    @Column(name = "is_used", nullable = true)
    public Byte getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Byte isUsed) {
        this.isUsed = isUsed;
    }

    @Basic
    @Column(name = "parent_id", nullable = true)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbMenu tbMenu = (TbMenu) o;
        return menuId == tbMenu.menuId &&
                Objects.equals(menuName, tbMenu.menuName) &&
                Objects.equals(menuUrl, tbMenu.menuUrl) &&
                Objects.equals(menuSort, tbMenu.menuSort) &&
                Objects.equals(isUsed, tbMenu.isUsed) &&
                Objects.equals(parentId, tbMenu.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, menuName, menuUrl, menuSort, isUsed, parentId);
    }
}
