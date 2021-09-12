package com.yufan.task.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.task.service.bean.GetSelectDateBean;
import com.yufan.utils.*;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbParam;
import org.apache.commons.collections.CollectionUtils;
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
            // 日期类型 1 预约日期 2 租赁日期
            String dateListFlag = data.getString("dateListFlag");
            // 根据日期类型，获取日期类型配置，日期类型格式配置，休息时间配置
            Map<String, TbParam> cfgMap = new HashMap<>();
            initGetDateListFlagCfg(dateListFlag, cfgMap);
            // 获取前端界面说明
            getDateListFlagDesc(dataJson, cfgMap);
            // 生成能选择的日期
            getDateList(dataJson, cfgMap);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 根据日期类型，获取日期类型配置，日期类型格式配置，休息时间配置
     * 说明：对应关系
     * 日期类型的(参数Key)与休息时间(参数Key)，一一对应
     * 日期类型(参数值)与日期类型格式(参数Key)，一一对应
     */
    public static void initGetDateListFlagCfg(String dateListFlag, Map<String, TbParam> map) {
        // 获取日期类型
        TbParam dateListFlagParam = CommonMethod.getParamConfigByParamCodeAndKey(Constants.DATE_LIST_FLAG_CODE, dateListFlag);
        if (null == dateListFlagParam) {
            LOG.info("------获取日期类型为空-----dateListFlag=" + dateListFlag);
            return;
        }
        map.put(Constants.DATE_LIST_FLAG_CODE, dateListFlagParam);
        // 根据 日期类型的(参数Key) dateListFlag 获取休息时间
        TbParam dateListRestParam = CommonMethod.getParamConfigByParamCodeAndKey(Constants.DATE_LIST_REST_CODE, dateListFlag);
        if (null == dateListRestParam) {
            LOG.info("------根据 日期类型的(参数Key) dateListFlag 获取休息时间为空-----paramKey=" + dateListFlagParam.getParamValue());
        }
        map.put(Constants.DATE_LIST_REST_CODE, dateListRestParam);
        // 根据日期类型(参数值)  获取日期类型格式
        TbParam dateListTypeParam = CommonMethod.getParamConfigByParamCodeAndKey(Constants.DATE_LIST_TYPE_CODE, dateListFlagParam.getParamValue());
        if (null == dateListTypeParam) {
            LOG.info("------根据日期类型(参数值)  获取日期类型格式为空-----paramKey=" + dateListFlagParam.getParamValue());
        }
        map.put(Constants.DATE_LIST_TYPE_CODE, dateListTypeParam);
    }

    /**
     * 用于前端预定界面显示说明时间段
     *
     * @param dataJson
     * @param cfgMap
     */
    private static void getDateListFlagDesc(JSONObject dataJson, Map<String, TbParam> cfgMap) {
        // 获取配置的日期类型格式key
        TbParam dateListFlagParam = cfgMap.get(Constants.DATE_LIST_FLAG_CODE);
        if (null == dateListFlagParam) {
            return;
        }
        String dateListFlag = dateListFlagParam.getParamKey();
        // 获取前端界面说明
        if (Constants.DATE_LIST_FLAG_2.equals(dateListFlag)) {
            // 前端预约界面 9:00至12:00;12:00至18:00;18:00至23:00     前端用于显示对应文字,目前用于预约界面显示对应工作时间段
            dataJson.putAll(getDateListFlagDesc2(cfgMap));
        }
    }

    /**
     * 用于前端预定界面显示说明时间段
     *
     * @param cfgMap
     * @return
     */
    private static Map<String, String> getDateListFlagDesc2(Map<String, TbParam> cfgMap) {
        Map<String, String> map = new HashMap<>();
        TbParam dateListTypeParam = cfgMap.get(Constants.DATE_LIST_TYPE_CODE);
        if (dateListTypeParam == null) {
            return map;
        }
        // 日期类型格式 得到时间段描述
        String value = dateListTypeParam.getParamValue();// 时间段   09:00 至 12:00;12:00 至 18:00;18:00 至 23:00
        if (StringUtils.isEmpty(value)) {
            return map;
        }
        String[] valueArray = value.replace("；", ";").split(";");
        if (valueArray.length > 0) {
            map.put("timeSW", valueArray[0]);
        }
        if (valueArray.length > 1) {
            map.put("timeXW", valueArray[1]);
        }
        if (valueArray.length > 2) {
            map.put("timeWS", valueArray[2]);
        }
        return map;
    }


    /**
     * 根据日期类型,生成日期
     *
     * @param cfgMap
     * @return
     */
    public static void getDateList(JSONObject dataJson, Map<String, TbParam> cfgMap) {
        try {
            List<GetSelectDateBean> result = new ArrayList<>();
            // 获取配置的日期类型格式key
            TbParam dateListFlagParam = cfgMap.get(Constants.DATE_LIST_FLAG_CODE);
            if (null == dateListFlagParam) {
                dataJson.put("date_list", result);
                return;
            }
            // init 休息时间
            Map<String, String> restTimeMap = new HashMap<>();
            initRestTime(cfgMap, restTimeMap);
            String dateListTypeKey = dateListFlagParam.getParamValue();
            if (Constants.DATE_LIST_TYPE_KEY_1.equals(dateListTypeKey)) {
                // fullDesc = 2021-07-11【星期日】
                result = getDateListTypeKey1(cfgMap, restTimeMap);
            } else if (Constants.DATE_LIST_TYPE_KEY_2.equals(dateListTypeKey)) {
                // fullDesc = 2021-07-11【星期日】【上午】
                result = getDateListTypeKey2(cfgMap, restTimeMap);
            } else if (Constants.DATE_LIST_TYPE_KEY_3.equals(dateListTypeKey)) {
                // fullDesc = 2021-07-11【星期日】
                result = getDateListTypeKey3(cfgMap, restTimeMap);
            }
            dataJson.put("date_list", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置说明：参数名称=休息时间	参数编码=date_list_rest	 参数Key=2	参数值=2021-08-24;2021-08-25;	参数值1=上午;下午
     * 参数Key=与配置的日期类型(参数Key一一对应)
     * 参数值=具体休息日期的连接
     * 参数值1=具体休息时段的连接
     * 特别说明：日期配置与时段配置分别对应
     * 获取休息时间
     */
    private static void initRestTime(Map<String, TbParam> cfgMap, Map<String, String> restTimeMap) {
        // 获取休息时间配置
        TbParam dateListRestParam = cfgMap.get(Constants.DATE_LIST_REST_CODE);
        if (dateListRestParam == null) {
            return;
        }
        // 校验是否配置正确
        String paramValue = dateListRestParam.getParamValue();//具体休息日期 格式 yyyy-MM-dd, 多个用;连接
        if (!StringUtils.isEmpty(paramValue)) {
            String[] datesArray = paramValue.replace("；", ";").split(";");
            for (int i = 0; i < datesArray.length; i++) {
                String date = datesArray[i];
                if (StringUtils.isEmpty(date)) {
                    continue;
                }
                date = date.trim();
                String timeInterval = dateListRestParam.getParamValue();//休息时段，格式：  上午;下午;晚上
                String dateKey = "";
                if (StringUtils.isEmpty(timeInterval)) {
                    dateKey = date;
                    restTimeMap.put(dateKey, dateKey);
                    continue;
                }
                String[] timeIntervalArray = timeInterval.replace("；", ";").split(";");
                for (int j = 0; j < timeIntervalArray.length; j++) {
                    if (StringUtils.isEmpty(timeIntervalArray[j])) {
                        continue;
                    }
                    dateKey = date + timeIntervalArray[j].trim();//yyyy-MM-dd上午
                    restTimeMap.put(dateKey, dateKey);
                }
            }
        }
    }


    /**
     * 生成日期类型格式key为1的日期数据 dateListType
     * 参数配置规则：参数名称=日期类型格式 参数编码=date_list_type	参数Key=1	参数值=13:00	参数值1=7
     * 参数值(时间比较,如果当前时间>参数值),则能选择的日期从下一天开始算
     * 参数值1=7，表示能选择的日期为其实日期再往后7天(包含休息日期)
     *
     * @param cfgMap
     * @return 2021-07-11【星期日】
     */
    private static List<GetSelectDateBean> getDateListTypeKey1(Map<String, TbParam> cfgMap, Map<String, String> restTimeMap) {
        List<GetSelectDateBean> resultList = new ArrayList<>();
        // 日期类型格式配置
        TbParam dateListTypeParam = cfgMap.get(Constants.DATE_LIST_TYPE_CODE);
        if (null == dateListTypeParam) {
            return resultList;
        }
        // 校验参数是否按规则配置正常
        try {
            String time = dateListTypeParam.getParamValue();// 时间 13:00
            String addDateStr = dateListTypeParam.getParamValue1();//范围数量 7
            if (StringUtils.isEmpty(time) || StringUtils.isEmpty(addDateStr)) {
                LOG.info("=============参数未配置====时间====和===范围数量=====time=" + time + "  addDateStr=" + addDateStr);
                return resultList;
            }
            String timeNow = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_TIME);
            Date date = new Date();
            if (DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + time + ":00").getTime()
                    <= DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + timeNow + ":00").getTime()) {
                date = DatetimeUtil.addDays(date, 1);
            }
            Integer addDate = Integer.parseInt(addDateStr);
            for (int i = 0; i < addDate; i++) {
                Date getDate = DatetimeUtil.addDays(date, i);
                String dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);// yyyy-MM-dd
                int week = DatetimeUtil.getDayOfWeek(getDate);
                String weekStr = DatetimeUtil.getWeekName(week);
                String fullDesc = dateStr + "【" + weekStr + "】";
                // 过滤休息时间
                if (restTimeMap.get(dateStr) != null) {
                    continue;
                }
                //
                GetSelectDateBean getSelectDateBean = new GetSelectDateBean();
                getSelectDateBean.setDate(dateStr);
                getSelectDateBean.setWeek(weekStr);
                getSelectDateBean.setFullDesc(fullDesc);
                resultList.add(getSelectDateBean);
            }
        } catch (Exception e) {
            LOG.error(e);
        }

        return resultList;
    }

    /**
     * 配置 参数名称=日期类型格式	参数编码=date_list_type	 参数Key=2	参数值=09:00 至 12:00;12:00 至 18:00;18:00 至 23:00	参数值1=7  参数值2=上午;下午;晚上
     * 参数值=配置为时间段，与 参数值2 对应，用于界面描述，具体时间段
     * 参数值1=7，表示能选择的日期为其实日期再往后7天(包含休息日期)
     * 参数值2=一天分为本个时间段
     * 生成日期类型格式key为2的日期数据 dateListType
     * 参数配置规则：
     *
     * @param cfgMap
     * @return 2021-07-11【星期日】【上午】
     */
    private static List<GetSelectDateBean> getDateListTypeKey2(Map<String, TbParam> cfgMap, Map<String, String> restTimeMap) {
        List<GetSelectDateBean> resultList = new ArrayList<>();
        // 日期类型格式配置
        TbParam dateListTypeParam = cfgMap.get(Constants.DATE_LIST_TYPE_CODE);
        if (null == dateListTypeParam) {
            return resultList;
        }
        // 校验参数是否按规则配置正常
        try {
            String paramValue = dateListTypeParam.getParamValue();// 09:00 至 12:00;12:00 至 18:00;18:00 至 23:00
            String paramValue1 = dateListTypeParam.getParamValue1();// 7
            String paramValue2 = dateListTypeParam.getParamValue2();// 上午;下午;晚上
            if (StringUtils.isEmpty(paramValue) || StringUtils.isEmpty(paramValue1) || StringUtils.isEmpty(paramValue2)) {
                return resultList;
            }
            String[] paramValueArray = paramValue.replace("；", ";").split(";");
            String[] paramValue2Array = paramValue2.replace("；", ";").split(";");
            if (paramValueArray.length != 3 || paramValueArray.length != paramValue2Array.length) {
                LOG.info("----配置数据paramValue和paramValue2不正确------");
                return resultList;
            }
            // 判断当前时间段,选择时间不能处理当前时间段内,只能从下一个时间段开始
            // 上午 9:00至12:00
            String t1_s = paramValueArray[0].split("至")[0];
            long t1_sl = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t1_s.trim() + ":00").getTime();
            String t1_e = paramValueArray[0].split("至")[1];
            long t1_el = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t1_e.trim() + ":00").getTime();
            // 下午 12:00至18:00
            String t2_s = paramValueArray[1].split("至")[0];
            long t2_sl = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t2_s.trim() + ":00").getTime();
            String t2_e = paramValueArray[1].split("至")[1];
            long t2_el = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT) + " " + t2_e.trim() + ":00").getTime();
            // 过滤时间,不能为当前时间段的时间和休息时间
            long nowTime = new Date().getTime();
            String dateStr = DatetimeUtil.getNow(DatetimeUtil.DEFAULT_DATE_FORMAT);// yyyy-MM-dd
            // 过滤当前时间段
            String dateKey = ""; //yyyy-MM-dd上午
            if (nowTime >= t1_sl && nowTime <= t1_el) {
                // 上午 9:00至12:00
                dateKey = dateStr + "" + "上午";
                restTimeMap.put(dateKey, dateKey);
            } else if (nowTime >= t2_sl && nowTime <= t2_el) {
                // 下午 12:00至18:00
                dateKey = dateStr + "" + "上午";
                restTimeMap.put(dateKey, dateKey);
                dateKey = dateStr + "" + "下午";
                restTimeMap.put(dateKey, dateKey);
            } else {
                // 晚上 18:00至23:00
                restTimeMap.put(dateStr, dateStr);
            }
            // 生成选择数据
            Date date = new Date(); // 当前日期
            int addDay = Integer.parseInt(paramValue1);
            for (int i = 0; i < addDay; i++) {
                Date getDate = DatetimeUtil.addDays(date, i);
                dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);// yyyy-MM-dd
                // 判断是当前日期是否休息
                if (restTimeMap.get(dateStr) != null) {
                    continue;
                }
                int week = DatetimeUtil.getDayOfWeek(getDate);
                String weekStr = DatetimeUtil.getWeekName(week);
                for (int j = 0; j < paramValue2Array.length; j++) {
                    String timeInterval = paramValue2Array[j];// 上午
                    if (StringUtils.isEmpty(timeInterval)) {
                        continue;
                    }
                    dateKey = dateStr + timeInterval.trim();
                    // 当前时间段不能选择
                    if (restTimeMap.get(dateKey) != null) {
                        continue;
                    }
                    //
                    String fullDesc = dateStr + "【" + weekStr + "】【" + timeInterval + "】";
                    //
                    GetSelectDateBean getSelectDateBean = new GetSelectDateBean();
                    getSelectDateBean.setDate(dateStr);
                    getSelectDateBean.setWeek(weekStr);
                    getSelectDateBean.setFullDesc(fullDesc);
                    resultList.add(getSelectDateBean);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }

        return resultList;
    }

    /**
     * 生成日期类型格式key为3的日期数据 dateListType
     * 参数配置规则：参数名称=日期类型格式 参数编码=date_list_type	参数Key=1	参数值=2	参数值1=7
     * 参数值=数字,表示计算开始日期,如果是0,表示从当天开始，1表示从下一天开始
     * 参数值1=7，表示能选择的日期为其实日期再往后7天(包含休息日期)
     *
     * @param cfgMap
     * @return 2021-07-11【星期日】
     */
    private static List<GetSelectDateBean> getDateListTypeKey3(Map<String, TbParam> cfgMap, Map<String, String> restTimeMap) {
        List<GetSelectDateBean> resultList = new ArrayList<>();
        // 日期类型格式配置
        TbParam dateListTypeParam = cfgMap.get(Constants.DATE_LIST_TYPE_CODE);
        if (null == dateListTypeParam) {
            return resultList;
        }
        // 校验参数是否按规则配置正常
        try {
            String startDayStr = dateListTypeParam.getParamValue();// 如果是0,表示从当天开始，1表示从下一天开始
            String addDateStr = dateListTypeParam.getParamValue1();//范围数量 7
            if (StringUtils.isEmpty(startDayStr) || StringUtils.isEmpty(addDateStr)) {
                LOG.info("=============参数未配置====时间====和===范围数量=====startDayStr=" + startDayStr + "  addDateStr=" + addDateStr);
                return resultList;
            }
            int startDay = Integer.parseInt(startDayStr);
            Date date = DatetimeUtil.addDays(new Date(), startDay);
            int addDate = Integer.parseInt(addDateStr);
            for (int i = 0; i < addDate; i++) {
                Date getDate = DatetimeUtil.addDays(date, i);
                String dateStr = DatetimeUtil.convertDateToStr(getDate, DatetimeUtil.DEFAULT_DATE_FORMAT);// yyyy-MM-dd
                int week = DatetimeUtil.getDayOfWeek(getDate);
                String weekStr = DatetimeUtil.getWeekName(week);
                String fullDesc = dateStr + "【" + weekStr + "】";
                // 过滤休息时间
                if (restTimeMap.get(dateStr) != null) {
                    continue;
                }
                //
                GetSelectDateBean getSelectDateBean = new GetSelectDateBean();
                getSelectDateBean.setDate(dateStr);
                getSelectDateBean.setWeek(weekStr);
                getSelectDateBean.setFullDesc(fullDesc);
                resultList.add(getSelectDateBean);
            }
        } catch (Exception e) {
            LOG.error(e);
        }

        return resultList;
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            Integer userId = data.getInteger("userId");
            String dateListFlag = data.getString("dateListFlag");
            if (null == userId || StringUtils.isEmpty(dateListFlag)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}