package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.task.service.impl.Test;
import io.netty.util.internal.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/15
 */
@Service("find_my_user")
public class FindMyUser implements IResultOut {

    private Logger LOG = Logger.getLogger(FindMyUser.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Autowired
    private IUserDao iUserDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            TbUserInfo userInfo = iAccountDao.loadUserInfo(userId);
            String memberNum = userInfo.getMemberId();
            if(StringUtil.isNullOrEmpty(memberNum)){
                dataJson.put("list", new ArrayList<>());
                return packagMsg(ResultCode.OK.getResp_code(), dataJson);
            }
            List<TbUserInfo> list = iAccountDao.findUserInfoByMemberNumList(memberNum);
            //
            List<Map<String, Object>> outList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                String nickName = list.get(i).getNickName();
                String phone = list.get(i).getUserMobile();
                map.put("nickName", nickName);
                map.put("inviterJf", list.get(i).getInviterJf());
                map.put("phone", phone.substring(0, 3) + "***" + phone.substring(phone.length() - 4, phone.length()));
                outList.add(map);
            }
            //查询积分
            int jifen = iUserDao.userJifen(userId);

            dataJson.put("effectiveJifen", jifen);
            dataJson.put("list", outList);
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