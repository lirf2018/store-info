package com.yufan.task.service.impl.category;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbCategory;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.dao.history.IHistoryDao;
import com.yufan.task.service.impl.history.QueryHistoryList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/19 12:19
 * 功能介绍: 查询分类
 */
@Service("query_category_detail")
public class QueryCategory implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryCategory.class);

    @Autowired
    private ICategoryDao iCategoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer categoryId = data.getInteger("category_id");

            TbCategory category = iCategoryDao.loadTbCategoryById(categoryId);
            dataJson.put("category_id", categoryId);
            dataJson.put("category_name", category.getCategoryName());
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer categoryId = data.getInteger("category_id");
            if (categoryId == null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}
