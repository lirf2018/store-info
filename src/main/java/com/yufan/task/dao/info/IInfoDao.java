package com.yufan.task.dao.info;

import com.yufan.pojo.TbInfo;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/17 22:22
 * 功能介绍:
 */
public interface IInfoDao {


    /**
     * 查询资讯列表
     *
     * @return
     */
    public List<Map<String, Object>> queryInfoList(Integer size);


    /**
     * 查询资讯详情
     *
     * @param id
     * @return
     */
    public TbInfo loadInfo(int id);

    /**
     * 更新阅读数
     *
     * @param id
     */
    public void updateReadCount(int id);


    public PageInfo loadInfoPage(Integer currePage, Integer pageSize);

}
