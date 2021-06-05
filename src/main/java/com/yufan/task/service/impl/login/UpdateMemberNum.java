package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbMemberId;
import com.yufan.task.dao.account.IAccountDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description:
 * @author: lirf
 * @time: 2021/5/15
 */
@Service("update_member_num")
public class UpdateMemberNum implements IResultOut {

    private Logger LOG = Logger.getLogger(UpdateMemberNum.class);

    @Autowired
    private IAccountDao iAccountDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Integer userId = data.getInteger("userId");
            String type = data.getString("type");
            if ("add".equals(type)) {
                //绑定   检验是否已近被绑定
                String memberNum = data.getString("memberNum");
                if (iAccountDao.checkInviterNum(memberNum)) {
                    return packagMsg(ResultCode.MEMBERNUM_IS_BANG.getResp_code(), dataJson);
                }
                // 查询绑定号是否存在
                TbMemberId memberId = iAccountDao.loadMemberId(memberNum);
                if (null == memberId) {
                    return packagMsg(ResultCode.MEMBERNUM_NOT_EXIST.getResp_code(), dataJson);
                }
                iAccountDao.updateUserMemberNum(userId, memberNum);
            } else {
                // 解绑
                iAccountDao.updateUserMemberNum(userId, "");
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
            String type = data.getString("type");
            if (userId == null || userId == 0 || (!"add".equals(type) && !"del".equals(type))) {
                return false;
            }
            String memberNum = data.getString("memberNum");
            if ("add".equals(type) && StringUtils.isEmpty(memberNum)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}