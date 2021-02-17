package com.yufan.task.dao.activity;

import com.yufan.pojo.TbActivity;
import com.yufan.utils.PageInfo;

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


    public PageInfo loadActivityPage(Integer currePage,Integer pageSize);

    public TbActivity loadActivity(int id);

}
