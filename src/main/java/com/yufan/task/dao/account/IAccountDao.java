package com.yufan.task.dao.account;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/2 21:29
 * 功能介绍:
 */
public interface IAccountDao {

    /**
     * 校验openid 是否已绑定
     *
     * @param openId
     * @return
     */
    public boolean checkWXBang(String openId);

    /**
     * 根据uid查询用户登录信息
     *
     * @param uid
     * @return
     */
    public List<Map<String, Object>> queryUserInfo(String uid);
}
