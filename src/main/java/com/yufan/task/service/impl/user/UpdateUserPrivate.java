package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.pojo.TbParam;
import com.yufan.utils.CacheData;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.task.service.impl.Test;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 用户预定,更新预定时间
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
            String dateType = GetYuDingDateList.getDateType();
            //
            List<String> checkDateList = GetYuDingDateList.getDateList(dateType);
            if (CollectionUtils.isEmpty(checkDateList)) {
                return packagMsg(ResultCode.DATE_NOT_EXIST.getResp_code(), dataJson);
            }
            boolean flag = false;
            for (int i = 0; i < checkDateList.size(); i++) {
                String strCheckDate = checkDateList.get(i);
                if (getDate.equals(strCheckDate)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return packagMsg(ResultCode.DATE_NOT_EXIST.getResp_code(), dataJson);
            }
            int index = getIndex(dateType, getDate);
            iUserDao.updateUserPrivate(userId, id, dateStr, getDate, index);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private int getIndex(String dateType, String getDate) {
        int index = 0;
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = CacheData.PARAMLIST.get(i);
            if ("date_list".equals(param.getParamCode()) && dateType.equals(param.getParamKey())) {
                //通过时间段获取下标
                String value2 = param.getParamValue2();//具体时间段
                String[] array = value2.split(";");
                int len = array.length;
                for (int j = 0; j < len; j++) {
                    if (getDate.indexOf(array[j]) >= 0) {
                        return j;
                    }
                }
            }
        }
        return index;
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