package com.yufan.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/6 9:51
 * 功能介绍: 系统编码参数
 */
public enum OutCode {


    fail("0", "操作失败"),
    success("1", "操作成功"),
    delete_fail("2", "删除失败"),
    delete_success("3", "删除成功"),
    use_success("4", "启用成功"),
    update_success("5", "修改成功"),
    add_success("6", "增加成功"),
    save_fail("7", "保存失败"),
    login_fail("8", "登录失败"),
    login_success("9", "登录成功"),
    account_or_passwd_fail("10", "账号或者密码有误"),
    error_passwd_key("11", "秘钥有误"),
    upload_success("12", "上传成功"),
    upload_fail("13", "上传失败"),
    upload_error_fail("14", "上传异常"),
    code_repeat("15", "编码已存在"),
    old_passwd_error("16","旧密码不正确"),
    is_deal_with("17","已处理"),
    is_not_deal_with("18","未处理"),
    is_on_sell("19","下架成功"),
    is_not_on_sell("20","上架成功"),
    cacel_success("21","取消成功"),
    setting_success("22","设置成功"),
    goods_exception_01("goods_exception_01","查询类目属性值数据异常"),
    goods_exception_02("goods_exception_02","sku数据异常");


    private String code;
    private String msg;


    OutCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getMsg(String code) {
        for (OutCode outCode : OutCode.values()) {
            if (code.equals(outCode.code)) {
                return outCode.msg;
            }
        }
        return "";
    }
}
