package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.task.service.impl.Test;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/6
 */
@Service("update_user_private")
public class UpdateUserPrivate implements IResultOut {

    private Logger LOG = Logger.getLogger(UpdateUserPrivate.class);

    @Autowired
    private IUserDao iUserDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            Integer id = data.getInteger("id");
            String getDate = data.getString("get_date");
            String dateStr = getDate.split("【")[0];
            //
            List<String> checkDateList = GetYuDingDateList.getDateList();
            if (CollectionUtils.isEmpty(checkDateList)) {
                return packagMsg(ResultCode.DATE_NOT_EXIST.getResp_code(), dataJson);
            }
            boolean flag = false;
            for (int i = 0; i < checkDateList.size(); i++) {
                String strCheckDate = checkDateList.get(i);
                if (dateStr.equals(strCheckDate.split("【")[0])) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return packagMsg(ResultCode.DATE_NOT_EXIST.getResp_code(), dataJson);
            }
            iUserDao.updateUserPrivate(userId, id, dateStr);
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
            Integer id = data.getInteger("id");
            String getDate = data.getString("get_date");
            if (null == userId || id == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}