package com.yufan.task.service.bean;

import com.yufan.pojo.TbOrderDetail;
import com.yufan.pojo.TbOrderDetailProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/25
 */
@Data
public class DetailData {
    private TbOrderDetail detail;
    private List<TbOrderDetailProperty> properties;
}
