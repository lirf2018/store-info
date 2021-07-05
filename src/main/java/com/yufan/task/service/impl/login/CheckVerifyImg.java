package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.verify.IVerifyImgDao;
import com.yufan.utils.CacheData;
import com.yufan.utils.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/7/3
 */
@Service("check_verify_img")
public class CheckVerifyImg implements IResultOut {

    private Logger LOG = Logger.getLogger(CheckVerifyImg.class);

    @Autowired
    private IVerifyImgDao iVerifyImgDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        String uuidMark = data.getString("uuidMark");
        try {
            String imgUuidStrs = data.getString("imgUuidStrs");

            if (imgUuidStrs.endsWith(",")) {
                imgUuidStrs = imgUuidStrs.substring(0, imgUuidStrs.length() - 1);
            }
            int size = imgUuidStrs.split(",").length;
            String uuids = imgUuidStrs.replace(",", "','");
            // verifyCode + ";" + needChoseCount + ";" + outDateTime
            String value = CacheData.VERIFYIMGCODEMAP.get(uuidMark);
            if (StringUtils.isEmpty(value)) {
                LOG.info("----------查询缓存不存在-----------" + imgUuidStrs);
                return packagMsg(ResultCode.PASS_FAIL_1.getResp_code(), dataJson);
            }
            String verifyCode = value.split(";")[0];
            int needChoseCount = Integer.parseInt(value.split(";")[1]);
            if (needChoseCount != size) {
                LOG.info("----------查询缓存不存在----needChoseCount != size-------");
                return packagMsg(ResultCode.PASS_FAIL_1.getResp_code(), dataJson);
            }
            // 查询图片数据
            List<Map<String, Object>> list = iVerifyImgDao.queryVerifyImgListByUuids(uuids, verifyCode);
            if (needChoseCount != list.size()) {
                LOG.info("----------必须选择项不满足条件-----------" + imgUuidStrs + "   value=" + value + "  list.size()=" + list.size());
                return packagMsg(ResultCode.PASS_FAIL_1.getResp_code(), dataJson);
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        } finally {
            CacheData.VERIFYIMGCODEMAP.remove(uuidMark);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            String uuidMark = data.getString("uuidMark");
            String imgUuidStrs = data.getString("imgUuidStrs");
            if (StringUtils.isEmpty(uuidMark) || StringUtils.isEmpty(imgUuidStrs)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}