package com.yufan.task.service.impl.suggest;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.img.IImgDao;
import com.yufan.task.dao.suggest.ISuggestDao;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 查询建议和服务
 * @author: lirf
 * @time: 2021/5/9
 */
@Service("suggest_list")
public class FindSuggestList implements IResultOut {

    private Logger LOG = Logger.getLogger(FindSuggestList.class);

    @Autowired
    private ISuggestDao iSuggestDao;

    @Autowired
    private IImgDao iImgDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            Integer id = data.getInteger("id");

            List<Map<String, Object>> listImg = new ArrayList<>();
            if (null != id) {
                listImg = iImgDao.queryTableRelImg(null, id, Constants.IMG_CLASSIFY_SUGGEST);
            }
            List<Map<String, Object>> list = iSuggestDao.listSuggest(userId, id);
            dataJson.put("listImg", listImg);
            dataJson.put("listSuggest", list);
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
