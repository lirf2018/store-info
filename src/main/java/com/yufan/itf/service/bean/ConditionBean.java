package com.yufan.itf.service.bean;

import lombok.Data;

/**
 * @description:
 * @author: lirf
 * @time: 2021/9/4
 */
@Data
public class ConditionBean {
    private Integer currePage;
    private Integer pageSize;
    private Integer status;
    private String year;
    private String month;
    private Integer itemType;
}
