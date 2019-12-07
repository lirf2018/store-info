package com.yufan.task.service.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/12/7 16:49
 * 功能介绍:
 */
@Getter
@Setter
public class UserSnsBean {


    @JSONField(name = "user_id")
    private Integer userId;

    @JSONField(name = "sns_type")
    private Integer snsType;//1、腾讯微博；2、新浪微博；3、人人网；4、微信；5、服务窗；6、一起沃；7、QQ;

    @JSONField(name = "uid")
    private String uid;//绑定后对应的绑定id,如微信openId

    @JSONField(name = "openkey")
    private String openkey;//只有腾讯有openkey

    @JSONField(name = "sns_name")
    private String snsName;//第三方账户名称


    @JSONField(name = "sns_account")
    private String snsAccount;//第三方账号

    @JSONField(name = "sns_img")
    private String snsImg;//第三方头像

}