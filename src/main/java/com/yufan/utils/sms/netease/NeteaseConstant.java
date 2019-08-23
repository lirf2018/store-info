package com.yufan.utils.sms.netease;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2017-11-29 9:31
 * 功能介绍: 网易常量参数
 */
public class NeteaseConstant {

    public final static Map<Integer, String> codeMap = new HashMap<Integer, String>();

    static {
        codeMap.put(201, "客户端版本不对，需升级sdk");
        codeMap.put(301, "被封禁");
        codeMap.put(302, "用户名或密码错误");
        codeMap.put(315, "IP限制");
        codeMap.put(403, "非法操作或没有权限");
        codeMap.put(404, "对象不存在");
        codeMap.put(405, "参数长度过长");
        codeMap.put(406, "对象只读");
        codeMap.put(408, "客户端请求超时");
        codeMap.put(413, "验证失败(短信服务)");
        codeMap.put(414, "参数错误");
        codeMap.put(415, "客户端网络问题");
        codeMap.put(416, "频率控制");
        codeMap.put(417, "重复操作");
        codeMap.put(418, "通道不可用(短信服务)");
        codeMap.put(419, "数量超过上限");
        codeMap.put(422, "账号被禁用");
        codeMap.put(431, "HTTP重复请求");
        codeMap.put(500, "服务器内部错误");
        codeMap.put(503, "服务器繁忙");
        codeMap.put(508, "消息撤回时间超限");
        codeMap.put(509, "无效协议");
        codeMap.put(514, "服务不可用");
        codeMap.put(998, "解包错误");
        codeMap.put(999, "打包错误");
        codeMap.put(801, "群人数达到上限");
        codeMap.put(802, "没有权限");
        codeMap.put(803, "群不存在");
        codeMap.put(804, "用户不在群");
        codeMap.put(805, "群类型不匹配");
        codeMap.put(806, "创建群数量达到限制");
        codeMap.put(807, "群成员状态错误");
        codeMap.put(808, "申请成功");
        codeMap.put(809, "已经在群内");
        codeMap.put(810, "邀请成功");
        codeMap.put(9102, "通道失效");
        codeMap.put(9103, "已经在他端对这个呼叫响应过了");
        codeMap.put(11001, "通话不可达，对方离线状态");
        codeMap.put(13001, "IM主连接状态异常");
        codeMap.put(13002, "聊天室状态异常");
        codeMap.put(13003, "账号在黑名单中,不允许进入聊天室");
        codeMap.put(13004, "在禁言列表中,不允许发言");
        codeMap.put(10431, "输入email不是邮箱");
        codeMap.put(10432, "输入mobile不是手机号码");
        codeMap.put(10433, "注册输入的两次密码不相同");
        codeMap.put(10434, "企业不存在");
        codeMap.put(10435, "登陆密码或帐号不对");
        codeMap.put(10436, "app不存在");
        codeMap.put(10437, "email已注册");
        codeMap.put(10438, "手机号已注册");
        codeMap.put(10441, "app名字已经存在");
    }
}
