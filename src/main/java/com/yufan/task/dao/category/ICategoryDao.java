package com.yufan.task.dao.category;

import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbItemprops;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 16:47
 * 功能介绍:
 */
public interface ICategoryDao {

    /**
     * 查询商品销售属性和非销售属性
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> queryCategoryRelListMap(int categoryId);


    /**
     * 根据一级分类查询类目
     *
     * @param levelId
     * @return
     */
    public List<Map<String, Object>> loadLevelCategoryData(Integer levelId, Integer status);

    /**
     * 根据状态查询类目列表
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> loadCategoryListMap(Integer status);


    /**
     * 查询一级分类列表
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> loadLevelListMap(Integer status);


    /**
     * 查询商品属性列表
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> loadGoodsAttr(int goodsId);

    /**
     * 查询所有类目,属性,属性值
     *
     * @return
     */
    public List<Map<String, Object>> loadCategoryAllRelListMap(Integer categoryId, Integer caStatus, Integer itStatus, Integer pvStatus);

    /**
     * 查询所有类目,属性
     *
     * @return
     */
    public List<Map<String, Object>> loadCategoryItemRelListMap(Integer categoryId, Integer caStatus, Integer itStatus);


    /**
     * 查询所有属性,属性值
     *
     * @return
     */
    public List<Map<String, Object>> loadItemAllRelListMap(Integer categoryId, Integer itStatus, Integer pvStatus);

    /**
     * 根据类目标识查询属性值
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> loadPropValueByCategoryId(int categoryId);

    /**
     * 查询属性列表
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> loadItemPropListMap(Integer categoryId, Integer propId, Integer status);

    /**
     * 查询属性值列表
     *
     * @param categoryId
     * @param propId
     * @return
     */
    public List<Map<String, Object>> loadItemPropValueListMap(Integer categoryId, Integer propId, Integer valueId, Integer status);

    /**
     * 查询一级分类与类目关联表
     *
     * @param levelId
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> loadLeveCategoryRel(Integer levelId, Integer categoryId);
    public List<Map<String, Object>> loadLeveCategoryRel(String levelIds, String categoryIds);


    /**
     * 查询类目
     *
     * @param categoryId
     * @return
     */
    TbCategory loadTbCategoryById(int categoryId);


    /**
     * 查询类目属性
     *
     * @param propId
     * @return
     */
    TbItemprops loadTbItempropsById(int propId);


    Map<String,Object> loadCategoryMap(int categoryId);


}
