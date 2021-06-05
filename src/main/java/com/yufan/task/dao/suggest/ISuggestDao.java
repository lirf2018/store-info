package com.yufan.task.dao.suggest;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/6
 */
public interface ISuggestDao {


    public List<Map<String, Object>> listSuggest(int userId, Integer id);


    public int addSuggest(Object obj);

}
