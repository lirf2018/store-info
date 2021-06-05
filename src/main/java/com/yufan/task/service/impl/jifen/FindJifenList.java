package com.yufan.task.service.impl.jifen;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.jifen.IJifen;
import com.yufan.task.dao.user.IUserDao;
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
@Service("find_jifen_list")
public class FindJifenList implements IResultOut {

    private Logger LOG = Logger.getLogger(FindJifenList.class);

    @Autowired
    private IJifen iJifen;

    @Autowired
    private IUserDao iUserDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("user_id");
            String taskIds = data.getString("task_ids");
            Integer inOut = data.getInteger("in_out");
            List<Map<String, Object>> list = iJifen.findJifenListMap(userId, taskIds, inOut);
            //查询积分
            int jifen = iUserDao.userJifen(userId);
            dataJson.put("user_jifen", jifen);
            dataJson.put("jifen_list", list);
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

            Integer userId = data.getInteger("user_id");
            if (null == userId) {
                return false;
            }

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }

}
