package com.yufan.task.dao.banner;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 14:50
 * 功能介绍:
 */
public interface IBannerDao {

    /**
     * 查询banner
     * @return
     */
    public List<Map<String,Object>> loadBannerListMap(int size);

}
