package com.yufan.task.service.bean;

import lombok.Data;

/**
 * @description: 下拉选择日期
 * @author: lirf
 * @time: 2021/9/12
 */
@Data
public class GetSelectDateBean {
    private String date; // 2021-08-08
    private String week;//星期日
    private String fullDesc;
    private String timeInterval;// 上午
}
