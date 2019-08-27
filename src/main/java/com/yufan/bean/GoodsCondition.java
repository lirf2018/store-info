package com.yufan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 10:37
 * 功能介绍:
 */
@Setter
@Getter
public class GoodsCondition {

    @JSONField(name = "curre_page")
    private Integer currePage;

    @JSONField(name = "page_size")
    private Integer pageSize;

    @JSONField(name = "goods_name")
    private String goodsName;

    @JSONField(name = "type")
    private String type; //type 查询类别 最新new, 最热 hot, 推荐 weight

    @JSONField(name = "level_ids")
    private String levelIds;

    @JSONField(name = "category_ids")
    private String categoryIds;
}
