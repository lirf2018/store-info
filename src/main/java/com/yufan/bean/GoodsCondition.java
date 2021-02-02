package com.yufan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 10:37
 * 功能介绍:
 */
@Data
public class GoodsCondition {

    @JSONField(name = "currePage")
    private Integer currePage;

    @JSONField(name = "pageSize")
    private Integer pageSize;

    @JSONField(name = "goodsName")
    private String goodsName;

    @JSONField(name = "searchType")
    private String searchType; //type 查询类别 最新new, 最热 hot, 推荐 weight

    @JSONField(name = "levelIds")
    private String levelIds;

    @JSONField(name = "categoryIds")
    private String categoryIds;

    @JSONField(name = "propId")
    private Integer propId;//属性

    @JSONField(name = "userId")
    private Integer userId;

    private Integer isSingle;

    private String from;
}
