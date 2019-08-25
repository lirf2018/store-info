package com.yufan.dao.category;

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

}
