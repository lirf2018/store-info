package com.yufan.task.dao.jifen;

import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/2
 */
public interface IJifen {

    public List<Map<String, Object>> findJifenListMap(int userId, String taskIds, Integer inOut);


    public PageInfo loadJifenListPage(int userId, String taskIds, Integer inOut, Integer pageSize, int currePage);
}
