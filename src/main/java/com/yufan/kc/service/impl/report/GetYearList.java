package com.yufan.kc.service.impl.report;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.utils.DatetimeUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2021/1/2 13:01
 * 功能介绍:
 */
@Service("year_list")
public class GetYearList implements IResultOut {

    private Logger LOG = Logger.getLogger(GetYearList.class);


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            List<String> yearList = new ArrayList<>();
            // 生成查询年，前后5年，10年
            String format = "yyyy";
            String nowYear = DatetimeUtil.getNow(format);
            for (int i = -5; i < 10; i++) {
                String year = DatetimeUtil.addYearTime(DatetimeUtil.getNow(format), i, format);
                yearList.add(year);
            }

            String defaultMonth = DatetimeUtil.getNow("yyyy-MM");
            dataJson.put("default_month", defaultMonth);
            dataJson.put("year_list", yearList);
            dataJson.put("report_year", nowYear);
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

    public static void main(String[] args) {

    }
}