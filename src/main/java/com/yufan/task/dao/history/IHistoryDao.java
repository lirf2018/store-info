package com.yufan.task.dao.history;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/28 15:58
 * 功能介绍:
 */
public interface IHistoryDao {

    /**
     * 按排序查询历史记录
     *
     * @param size
     * @return
     */
    public List<Map<String, Object>> queryHistorySearchList(int size);

    /**
     * 查询用户的查询历史记录
     *
     * @param userId
     * @param size
     * @return
     */
    public List<Map<String, Object>> queryUserHistorySearchList(int userId, int size);

    /**
     * 删除用户的历史记录
     *
     * @param userId
     */
    public void deleteUserHistorySearch(int userId);
}
