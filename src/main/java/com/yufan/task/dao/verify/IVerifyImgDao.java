package com.yufan.task.dao.verify;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/7/1
 */
public interface IVerifyImgDao {

    /**
     * 查询图形校验分组数
     *
     * @return
     */
    List<Map<String, Object>> queryVerifyImgGroup();

    /**
     * 查询校验图片组详情
     *
     * @return
     */
    List<Map<String, Object>> queryVerifyImgListByCode(String verifyCode);

    List<Map<String, Object>> queryVerifyImgListByUuids(String uuids, String verifyCode);

    /**
     * 查询校验图片组详情不包括校验码的图片
     *
     * @return
     */
    List<Map<String, Object>> queryVerifyImgListUnInByCode(String verifyCode, Integer similarType);

}
