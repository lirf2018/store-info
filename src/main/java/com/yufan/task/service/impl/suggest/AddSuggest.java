package com.yufan.task.service.impl.suggest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbComplain;
import com.yufan.pojo.TbImg;
import com.yufan.task.dao.img.IImgDao;
import com.yufan.task.dao.suggest.ISuggestDao;
import com.yufan.task.service.impl.Test;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/9
 */
@Service("add_suggest")
public class AddSuggest implements IResultOut {

    private Logger LOG = Logger.getLogger(AddSuggest.class);

    @Autowired
    private ISuggestDao iSuggestDao;

    @Autowired
    private IImgDao iImgDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            TbComplain complain = JSONObject.toJavaObject(data, TbComplain.class);
            complain.setStatus(Constants.DATA_STATUS_YX);
            complain.setIsRead(0);
            complain.setCreatetime(new Timestamp(new Date().getTime()));
            complain.setShopId(0);
            complain.setAnswer("");
            iSuggestDao.addSuggest(complain);
            JSONArray array = data.getJSONArray("imgs");
            for (int i = 0; i < array.size(); i++) {
                int index = array.size() - i;
                TbImg img = new TbImg();
                img.setCreatetime(new Timestamp(new Date().getTime()));
                img.setImgClassify(Constants.IMG_CLASSIFY_SUGGEST);
                img.setImgSort(index);
                img.setImgUrl(array.getJSONObject(i).getString("imgPath"));
                img.setRelateId(complain.getId());
                img.setShopId(0);
                img.setImgType(Constants.IMG_TYPE_7);
                img.setStatus(Constants.DATA_STATUS_YX);
                iImgDao.saveImg(img);
            }
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