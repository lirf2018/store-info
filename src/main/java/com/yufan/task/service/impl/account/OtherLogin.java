package com.yufan.task.service.impl.account;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbUserInfo;
import com.yufan.pojo.TbUserSns;
import com.yufan.task.dao.account.IAccountDao;
import com.yufan.task.dao.addr.IAddrDao;
import com.yufan.task.service.bean.UserSnsBean;
import com.yufan.task.service.impl.addr.AddUserAddr;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/2 21:26
 * 功能介绍: 第三登录（默认注册）
 */
@Service("other_login")
public class OtherLogin implements IResultOut {

    private Logger LOG = Logger.getLogger(AddUserAddr.class);

    @Autowired
    private IAccountDao iAccountDao;


    @Override
    @Transactional
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            String openId = userSnsBean.getUid();
            int snsType = userSnsBean.getSnsType();
            //查询用户信息
            List<Map<String, Object>> userInfoList = iAccountDao.queryUserInfo(openId, snsType);
            if (null == userInfoList || userInfoList.size() > 1) {
                LOG.info("-------查询用户数据异常-------");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (0 == userInfoList.size()) {
                //默认创建账户并绑定微信
                dataJson = registerOther(userSnsBean);
                if (null == dataJson) {
                    return packagMsg(ResultCode.FAIL.getResp_code(), new JSONObject());
                }
            } else {
                //已存在用户(返回用户信息)
                Map<String, Object> map = userInfoList.get(0);
                dataJson.put("user_id" , map.get("user_id"));
                dataJson.put("nick_name" , map.get("nick_name"));
                dataJson.put("user_mobile" , map.get("user_mobile"));
                dataJson.put("member_id" , map.get("member_id"));
                dataJson.put("money" , map.get("money"));
                dataJson.put("start_time" , map.get("start_time"));
                dataJson.put("end_time" , map.get("end_time"));
                dataJson.put("user_img" , Constants.IMG_WEB_URL + map.get("user_img"));
                dataJson.put("sns_img" , map.get("sns_img"));
                dataJson.put("is_use_img" , map.get("is_use_img"));
            }
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----" , e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    /**
     * 微信注册
     *
     * @param userSnsBean
     * @return
     */
    @Transactional
    public JSONObject registerOther(UserSnsBean userSnsBean) {
        try {
            //注册用户
            TbUserInfo userInfo = new TbUserInfo();
            userInfo.setUserState(1);
            userInfo.setLogCount(1);
            userInfo.setMemberId("");
            userInfo.setCreatetime(new Timestamp(new Date().getTime()));
            userInfo.setMoney(new BigDecimal("0"));
            iAccountDao.saveObj(userInfo);
            //绑定微信
            TbUserSns userSns = new TbUserSns();
            userSns.setUserId(userInfo.getUserId());
            userSns.setSnsType(userSnsBean.getSnsType());
            userSns.setUid(userSnsBean.getUid());
            userSns.setOpenkey(userSnsBean.getOpenkey());
            userSns.setSnsName(userSnsBean.getSnsName());
            userSns.setSnsAccount(userSnsBean.getSnsAccount());
            userSns.setSnsImg(userSnsBean.getSnsImg());
            userSns.setIsUseImg(userSnsBean.getIsUseImg());
            userSns.setCreatetime(new Timestamp(new Date().getTime()));
            userSns.setStatus(1);
            iAccountDao.saveObj(userSns);
            JSONObject dataJson = new JSONObject();
            dataJson.put("user_id" , userInfo.getUserId());
            dataJson.put("nick_name" , userInfo.getNickName());
            dataJson.put("user_mobile" , userInfo.getUserMobile());
            dataJson.put("member_id" , userInfo.getMemberId());
            dataJson.put("money" , userInfo.getMoney());
            dataJson.put("start_time" , userInfo.getStartTime());
            dataJson.put("end_time" , userInfo.getEndTime());
            dataJson.put("user_img" , userInfo.getUserImg());
            dataJson.put("sns_img" , userSnsBean.getSnsImg());
            dataJson.put("is_use_img" , userSnsBean.getIsUseImg());
            return dataJson;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            UserSnsBean userSnsBean = JSONObject.toJavaObject(data, UserSnsBean.class);
            String openId = userSnsBean.getUid();
            Integer snsType = userSnsBean.getSnsType();
            if (null == snsType || StringUtils.isEmpty(openId)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---" , e);
        }
        return false;
    }
}