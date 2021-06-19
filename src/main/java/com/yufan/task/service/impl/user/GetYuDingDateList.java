package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbParam;
import com.yufan.utils.CacheData;
import com.yufan.utils.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 获取预定日期
 * @author: lirf
 * @time: 2021/6/6
 */
@Service("get_date_list")
public class GetYuDingDateList implements IResultOut {

    private Logger LOG = Logger.getLogger(GetYuDingDateList.class);

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            dataJson.put("date_list", getDateList());
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    public static List<String> getDateList() throws Exception {
        String defaultTime = "15:00";
        int dayCount = 7;
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = CacheData.PARAMLIST.get(i);
            if ("date_list".equals(param.getParamCode()) && "1".equals(param.getParamKey())) {
                defaultTime = param.getParamValue();
                dayCount = StringUtils.isNotEmpty(param.getParamValue1()) ? Integer.parseInt(param.getParamValue1()) : dayCount;
            }
        }
        String time = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_TIME);
        Date date = new Date();
        if (DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + defaultTime + ":00").getTime()
                <= DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + time + ":00").getTime()) {
            date = DatetimeUtil.addDays(date, 1);
        }
        List<String> listDate = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            Date getDate = DatetimeUtil.addDays(date, i);
            String dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);
            int week = DatetimeUtil.getDayOfWeek(getDate);
            listDate.add(dateStr + "【" + DatetimeUtil.getWeekName(week) + "】");
        }
        return listDate;
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer userId = data.getInteger("userId");
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