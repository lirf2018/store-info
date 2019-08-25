package com.yufan.dao.param;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 13:22
 * 功能介绍:
 */
public interface IParamDao {


    List<Map<String, Object>> queryParamListMap(Integer status);

    List<Map<String, Object>> queryParamListMap(String paramCode, Integer status);

    List<Map<String, Object>> queryParamListMap(String paramCode, String paramKey, Integer status);

}
