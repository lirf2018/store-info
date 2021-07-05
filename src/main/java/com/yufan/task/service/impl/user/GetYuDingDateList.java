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

    private static Logger LOG = Logger.getLogger(GetYuDingDateList.class);

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String dateType = getDateType();
            dataJson.put("date_list", getDateList(dateType));
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    public static List<String> getDateList(String dateType) {
        try {
            if ("2".equals(dateType)) {
                // 2021-07-11【星期日】【上午】
                return getDateList2(dateType);
            } else {
                // 2021-07-11【星期日】
                return getDateList1(dateType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<String> getDateList2(String dateType) throws Exception {
        String defaultTime = "";
        Integer defaultDayCount = null;
        String shiJianDuan = "";
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = list.get(i);
            if ("date_list".equals(param.getParamCode()) && dateType.equals(param.getParamKey())) {
                String value = param.getParamValue();// 时间
                String value1 = param.getParamValue1();//范围数量
                String value2 = param.getParamValue2();//具体时间段
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) {
                    LOG.info("=============参数未配置====时间====和===范围数量=====value=" + value + "  value1=" + value1 + "  value2=" + value2);
                    return new ArrayList<>();
                }
                defaultTime = value;
                defaultDayCount = Integer.parseInt(value1);
                shiJianDuan = value2;
            }
        }
        if (StringUtils.isEmpty(defaultTime) || StringUtils.isEmpty(shiJianDuan) || null == defaultDayCount) {
            LOG.info("=============参数未配置====时间====和===范围数量==22222===");
            return new ArrayList<>();
        }
        String time = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_TIME);
        Date date = new Date();
        if (DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + defaultTime + ":00").getTime()
                <= DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + time + ":00").getTime()) {
            date = DatetimeUtil.addDays(date, 1);
        }
        List<String> listDate = new ArrayList<>();
        for (int i = 0; i < defaultDayCount; i++) {
            Date getDate = DatetimeUtil.addDays(date, i);
            String dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);
            int week = DatetimeUtil.getDayOfWeek(getDate);
            String[] valueArray = shiJianDuan.split(";");
            for (int j = 0; j < valueArray.length; j++) {
                listDate.add(dateStr + "【" + DatetimeUtil.getWeekName(week) + "】【" + valueArray[j] + "】");
            }
        }
        return listDate;
    }


    public static List<String> getDateList1(String dateType) throws Exception {
        String defaultTime = "";
        Integer defaultDayCount = null;
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = CacheData.PARAMLIST.get(i);
            if ("date_list".equals(param.getParamCode()) && dateType.equals(param.getParamKey())) {
                String value = param.getParamValue();// 时间
                String value1 = param.getParamValue1();//范围数量
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value1)) {
                    LOG.info("=============参数未配置====时间====和===范围数量=====value=" + value + "  value1=" + value1);
                    return new ArrayList<>();
                }
                defaultTime = value;
                defaultDayCount = Integer.parseInt(value1);
            }
        }
        if (StringUtils.isEmpty(defaultTime) || null == defaultDayCount) {
            LOG.info("=============参数未配置====时间====和===范围数量==22222===");
            return new ArrayList<>();
        }
        String time = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_TIME);
        Date date = new Date();
        if (DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + defaultTime + ":00").getTime()
                <= DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + time + ":00").getTime()) {
            date = DatetimeUtil.addDays(date, 1);
        }
        List<String> listDate = new ArrayList<>();
        for (int i = 0; i < defaultDayCount; i++) {
            Date getDate = DatetimeUtil.addDays(date, i);
            String dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);
            int week = DatetimeUtil.getDayOfWeek(getDate);
            listDate.add(dateStr + "【" + DatetimeUtil.getWeekName(week) + "】");
        }
        return listDate;
    }

    public static String getDateType() {
        try {
            List<TbParam> list = CacheData.PARAMLIST;
            for (int i = 0; i < list.size(); i++) {
                TbParam param = list.get(i);
                if ("date_list_flag".equals(param.getParamCode()) && "date_list_flag".equals(param.getParamKey())) {
                    return param.getParamValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
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