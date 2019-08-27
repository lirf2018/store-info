package com.yufan.task.dao.activity;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 14:53
 * 功能介绍:
 */
public interface IActivityDao {


    /**
     * 查询banner
     * @return
     */
    public List<Map<String,Object>> loadActivityListMap(int size);

}
