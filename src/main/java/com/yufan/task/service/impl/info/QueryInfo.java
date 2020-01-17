package com.yufan.task.service.impl.info;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbInfo;
import com.yufan.task.dao.info.IInfoDao;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/17 22:30
 * 功能介绍:
 */
@Service("query_info")
public class QueryInfo implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryInfo.class);

    @Autowired
    private IInfoDao iInfoDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer id = data.getInteger("info_id");

            TbInfo info = iInfoDao.loadInfo(id);
            if (null == info) {
                LOG.info("------查询不存在------id: " + id);
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            //更新阅读数
            iInfoDao.updateReadCount(id);

            dataJson.put("info_id", id);
            dataJson.put("info_img", Constants.IMG_WEB_URL + info.getInfoImg());
            data.put("info_title", info.getInfoTitle());
            data.put("info_url", info.getInfoUrl());
            data.put("info_content", info.getInfoContent());
            data.put("read_count", info.getReadCount());
            data.put("create_time", DatetimeUtil.timeStamp2Date(info.getCreateTime().getTime() / 1000, "yyyy-MM-dd HH:mm:ss"));
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
            Integer id = data.getInteger("info_id");
            if (null == id) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}