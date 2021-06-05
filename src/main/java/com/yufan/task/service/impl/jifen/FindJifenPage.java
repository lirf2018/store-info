package com.yufan.task.service.impl.jifen;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.jifen.IJifen;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/2
 */
@Service("find_jifen_page")
public class FindJifenPage implements IResultOut {

    private Logger LOG = Logger.getLogger(FindJifenPage.class);

    @Autowired
    private IJifen iJifen;

    @Autowired
    private IUserDao iUserDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            String taskIds = data.getString("taskIds");
            Integer inOut = data.getInteger("inOut");
            int currePage = data.getInteger("currePage");
            PageInfo page = iJifen.loadJifenListPage(userId, taskIds, inOut, null, currePage);

            //查询积分
            int jifen = iUserDao.userJifen(userId);

            dataJson.put("hasNext", page.isHasNext());
            dataJson.put("currePage", page.getCurrePage());
            dataJson.put("pageSize", page.getPageSize());
            dataJson.put("userJifen", jifen);
            dataJson.put("jifenList", page.getResultListMap());
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
            Integer currePage = data.getInteger("currePage");
            if (null == userId || currePage == null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
