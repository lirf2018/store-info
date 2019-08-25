package com.yufan.dao.img;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 13:09
 * 功能介绍:
 */
public interface IImgDao {
    /**
     * 查询关联图片
     *
     * @param imgType      1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
     * @param relateId     店铺标识/商品标识/卡券标识
     * @param classifyType 0.商品图片1.卡券图片2.店铺图片
     * @return
     */
    public List<Map<String, Object>> queryTableRelImg(Integer imgType, int relateId, Integer classifyType);

}
