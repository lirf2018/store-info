package com.yufan.pojo;

import javax.persistence.*;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 10:48
 * 功能介绍:
 */
@Entity
@Table(name = "tb_level_category_rel", schema = "testlirf", catalog = "")
public class TbLevelCategoryRel {
    private int id;
    private Integer levelId;
    private Integer categoryId;

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
    @Column(name = "level_id", nullable = true)
    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    @Basic
    @Column(name = "category_id", nullable = true)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbLevelCategoryRel that = (TbLevelCategoryRel) o;
        return id == that.id &&
                Objects.equals(levelId, that.levelId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, levelId, categoryId);
    }
}
