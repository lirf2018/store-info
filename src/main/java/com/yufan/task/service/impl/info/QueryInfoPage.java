package com.yufan.task.service.impl.info;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.activity.IActivityDao;
import com.yufan.task.dao.info.IInfoDao;
import com.yufan.task.service.impl.activity.QueryActivity;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2021/2/11 18:29
 * 功能介绍:
 */
@Service("query_info_page")
public class QueryInfoPage   implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryInfoPage.class);

    @Autowired
    private IInfoDao iInfoDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer currePage = data.getInteger("currePage");
            Integer pageSize = data.getInteger("pageSize");
            PageInfo page = iInfoDao.loadInfoPage(currePage,pageSize);

            dataJson.put("hasNext", page.isHasNext());
            dataJson.put("currePage", page.getCurrePage());
            dataJson.put("pageSize", page.getPageSize());
            dataJson.put("list", page.getResultListMap());
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


            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}