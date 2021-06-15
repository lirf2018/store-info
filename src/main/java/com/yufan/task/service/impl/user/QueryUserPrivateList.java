package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 查询私人定制
 * @author: lirf
 * @time: 2021/6/6
 */
@Service("query_user_private")
public class QueryUserPrivateList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryUserPrivateList.class);

    @Autowired
    private IUserDao iUserDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            int findType = data.getInteger("findType");
            int currePage = data.getInteger("currePage");
            PageInfo page = iUserDao.loadUserPrivateList(userId, findType, null, currePage);

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

            Integer userId = data.getInteger("userId");
            Integer findType = data.getInteger("findType");
            if (null == userId || findType == null || (findType != 0 && findType != 1)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}