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

import java.util.*;

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
            if ("2".equals(dateType)) {
                // 9:00至12:00;12:00至18:00;18:00至23:00
                dataJson.putAll(getDateList2Map(dateType));
            }
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

    /**
     * 固定配置
     * String value = param.getParamValue();// 时间段   9:00至12:00;12:00至18:00;18:00至23:00
     * String value1 = param.getParamValue1();//范围数量  7
     * String value2 = param.getParamValue2();//具体时间段  上午;下午;晚上
     *
     * @param dateType 2
     * @return
     * @throws Exception
     */
    public static List<String> getDateList2(String dateType) throws Exception {
        String timeDuang = "";// 具体时间 9:00至12:00;12:00至18:00;18:00至23:00
        Integer defaultDayCount = null;// 默认天数
        String shiJianDuan = ""; // 具体时间段  上午;下午;晚上
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = list.get(i);
            if ("date_list".equals(param.getParamCode()) && dateType.equals(param.getParamKey())) {
                String value = param.getParamValue();// 时间段   9:00至12:00;12:00至18:00;18:00至23:00
                String value1 = param.getParamValue1();//范围数量  7
                String value2 = param.getParamValue2();//具体时间段  上午;下午;晚上
                int len1 = value.split(";").length;
                int len2 = value2.split(";").length;
                if (len1 != 3 && len1 != len2 && StringUtils.isEmpty(value) || StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) {
                    LOG.info("=============参数未配置====时间====和===范围数量=====value=" + value + "  value1=" + value1 + "  value2=" + value2);
                    return new ArrayList<>();
                }
                defaultDayCount = Integer.parseInt(value1);
                shiJianDuan = value2;
                timeDuang = value;
                break;
            }
        }
        if (StringUtils.isEmpty(shiJianDuan) || null == defaultDayCount) {
            LOG.info("=============参数未配置====时间====和===范围数量==22222===");
            return new ArrayList<>();
        }
        String[] values = timeDuang.split(";");// 具体时间 9:00至12:00;12:00至18:00;18:00至23:00
        // 上午 9:00至12:00
        String t1_s = values[0].split("至")[0];
        long t1_sl = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t1_s.trim() + ":00").getTime();
        String t1_e = values[0].split("至")[1];
        long t1_el = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t1_e.trim() + ":00").getTime();
        // 下午 12:00至18:00
        String t2_s = values[1].split("至")[0];
        long t2_sl = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t2_s.trim() + ":00").getTime();
        String t2_e = values[1].split("至")[1];
        long t2_el = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t2_e.trim() + ":00").getTime();
        // 晚上 18:00至23:00
//        String t3_s = values[2].split("至")[0];
//        long t3_sl = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t3_s.trim() + ":00").getTime();
//        String t3_e = values[2].split("至")[1];
//        long t3_el = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t3_e.trim() + ":00").getTime();
        // 过滤时间,不能为当前时间段的时间和休息时间
        long nowTime = new Date().getTime();
        String dateStr = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);// yyyy-MM-dd
        Map<String, String> outTimeMap = new HashMap<>();
        if (nowTime >= t1_sl && nowTime <= t1_el) {
            // 上午 9:00至12:00
            outTimeMap.put(dateStr + "" + "上午", dateStr);
        } else if (nowTime >= t2_sl && nowTime <= t2_el) {
            // 下午 12:00至18:00
            outTimeMap.put(dateStr + "" + "上午", dateStr);
            outTimeMap.put(dateStr + "" + "下午", dateStr);

        } else {
            // 晚上 18:00至23:00
            outTimeMap.put(dateStr, dateStr);
        }
        // 休息时间
        restTime(outTimeMap);
        LOG.info("休息时间：" + JSONObject.toJSON(outTimeMap));

        Date date = new Date(); // 当前日期
        List<String> listDate = new ArrayList<>();
        for (int i = 0; i < defaultDayCount; i++) {
            Date getDate = DatetimeUtil.addDays(date, i);
            dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);// yyyy-MM-dd
            // 判断是否可选择,当前时间不可选择
            if (outTimeMap.get(dateStr) != null) {
                continue;
            }
            int week = DatetimeUtil.getDayOfWeek(getDate);
            String[] valueArray = shiJianDuan.split(";"); // 上午;下午;晚上
            for (int j = 0; j < valueArray.length; j++) {
                String key = dateStr + valueArray[j];
                // 当前时间段不能选择
                if (outTimeMap.get(key) != null) {
                    continue;
                }
                listDate.add(dateStr + "【" + DatetimeUtil.getWeekName(week) + "】【" + valueArray[j] + "】");
            }
        }
        return listDate;
    }

    /**
     * 休息时间
     */
    private static void restTime(Map<String, String> outTimeMap) {
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = list.get(i);
            if ("have_a_rest".equals(param.getParamCode())) {
                String dates = param.getParamValue();//具体休息日期 格式 yyyy-MM-dd, 多个用;连接
                String[] datesArray = dates.split(";");
                for (int j = 0; j < datesArray.length; j++) {
                    String date = datesArray[j];
                    if (null == date || StringUtils.isEmpty(date.trim())) {
                        continue;
                    }
                    String times = param.getParamValue1();//休息时段，格式：  上午;下午;晚上
                    if (null != times && StringUtils.isNotEmpty(times.trim())) {
                        String[] timesArray = times.split(";");
                        for (int k = 0; k < timesArray.length; k++) {
                            String key = date + timesArray[k];
                            outTimeMap.put(key, key);
                        }
                    } else {
                        outTimeMap.put(date, date);
                    }
                }
            }
        }
    }

    private Map<String, String> getDateList2Map(String dateType) {
        Map<String, String> map = new HashMap<>();
        List<TbParam> list = CacheData.PARAMLIST;
        for (int i = 0; i < list.size(); i++) {
            TbParam param = list.get(i);
            if ("date_list".equals(param.getParamCode()) && dateType.equals(param.getParamKey())) {
                String value = param.getParamValue();// 时间段   9:00至12:00;12:00至18:00;18:00至23:00
                String value1 = param.getParamValue1();//范围数量  7
                String value2 = param.getParamValue2();//具体时间段  上午;下午;晚上
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) {
                    LOG.info("=============参数未配置====时间====和===范围数量=====value=" + value + "  value1=" + value1 + "  value2=" + value2);
                    return new HashMap<>();
                }
                int len1 = value.split(";").length;
                int len2 = value2.split(";").length;
                if (len1 != len2) {
                    LOG.info("----------------------参数配置有误");
                    return new HashMap<>();
                }
                String[] valueArray = value.split(";");
                for (int j = 0; j < valueArray.length; j++) {
                    if (j == 0) {
                        map.put("timeSW", valueArray[j]);
                    } else if (j == 1) {
                        map.put("timeXW", valueArray[j]);
                    } else if (j == 2) {
                        map.put("timeWS", valueArray[j]);
                    }
                }
                return map;
            }
        }
        return map;
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