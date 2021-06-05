package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbUserSns;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.service.impl.Test;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 查询绑定列表
 * @author: lirf
 * @time: 2021/5/14
 */
@Service("find_sns_bang_list")
public class FindSnsBangList implements IResultOut {

    private Logger LOG = Logger.getLogger(FindSnsBangList.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            //查询用户信息
            TbUserInfo user = iAccountDao.loadUserInfo(userId);
            // 查询绑定列表
            List<TbUserSns> userSnsList = iAccountDao.findUserSnsList(userId);

            // 输出结果  用户snsType   1、腾讯微博；2、新浪微博；3、人人网；4、微信；5、服务窗；6、一起沃；7、QQ;8、绑定会员号；9、绑定手机号
            List<Map<String, Object>> bangMapList = new ArrayList<>();
            // 手机号
            Map<String, Object> mapPhone = new HashMap<>();
            mapPhone.put("phone", user.getUserMobile());
            mapPhone.put("snsType", Constants.USER_SNS_TYPE_9);
            bangMapList.add(mapPhone);
            // 会员号
            Map<String, Object> mapMemberNum = new HashMap<>();
            mapMemberNum.put("memberNum", user.getMemberId());
            mapMemberNum.put("snsType", Constants.USER_SNS_TYPE_8);
            bangMapList.add(mapMemberNum);
            // 处理数据
            for (int i = 0; i < userSnsList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                mapPhone.put("snsType", userSnsList.get(i).getSnsType());
                bangMapList.add(map);
            }
            dataJson.put("list", bangMapList);
            dataJson.put("cardImg", Constants.IMG_WEB_URL + "card.png");
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