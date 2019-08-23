package com.yufan.utils.sms.netease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.utils.Constants;
import com.yufan.utils.RequestMethod;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2017-11-29 17:17
 * 功能介绍: 网易短信
 */
public class NeteaseUtils {

    private static final Logger LOG = Logger.getLogger(NeteaseUtils.class);

    private final static String APP_SECRET = Constants.APP_SECRET_WY;//开发者平台分配的appkey
    private final static String APP_KEY = Constants.APP_KEY_WY;//开发者平台分配的appkey

    private final static String SENDTEMPLATE_URL = Constants.SENDTEMPLATE_URL_WY;
    private final static String SENDCODE_URL = Constants.SENDCODE_URL_WY;


    /**
     * 发送短信/语音短信验证码
     */
    public static JSONObject sendcode(JSONObject inObj) {
        LOG.info("sendcode发送短信/语音短信验证码--->" + inObj);
        JSONObject out = new JSONObject();
        out.put("result", "-1");
        out.put("desc", "提交失败");
        try {
            String mobile = inObj.getString("mobile");//手机号码(必须)
            if (StringUtils.isEmpty(mobile)) {
                out.put("desc", "手机号码不能为空");
                return out;
            }
            String deviceId = inObj.getString("deviceId");//目标设备号，可选参数(可选)
            Integer templateid = inObj.getInteger("templateid");//模板编号(如不指定则使用配置的默认模版)(可选)
            Integer codelen = inObj.getIntValue("codelen");////验证码长度，范围4～10，默认为4(可选)
            String authCode = inObj.getString("authCode");//(可选)//客户自定义验证码，长度为4～10个数字   如果设置了该参数，则codeLen参数无效(可选)
            boolean needUp = inObj.getBooleanValue("needUp");//是否需要支持短信上行。true:需要，false:不需要  说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效(可选)
            if (codelen == 0) {
                codelen = 4;
            }

            // 设置请求的的参数，requestBody参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            /*
             * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
             * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
             * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
             */
            nvps.add(new BasicNameValuePair("mobile", mobile));
            nvps.add(new BasicNameValuePair("deviceId", deviceId));
            nvps.add(new BasicNameValuePair("templateid", String.valueOf(templateid)));
            nvps.add(new BasicNameValuePair("codeLen", String.valueOf(codelen)));
            nvps.add(new BasicNameValuePair("authCode", authCode));
            nvps.add(new BasicNameValuePair("needUp", String.valueOf(needUp)));

            //发送验证码
            String result = RequestMethod.httpPostNetease(nvps, SENDCODE_URL, APP_SECRET, APP_KEY);
            LOG.info("sendcode------>接收参数" + inObj + " 发送接口返回结果=" + result);
            if (StringUtils.isEmpty(result)) {
                LOG.info("sendcode-----null------->result=null");
                return out;
            }
            JSONObject objResult = JSONObject.parseObject(result);
            int code = objResult.getIntValue("code");
            if (null != objResult && code == 200) {
                out.put("result", "1");
                out.put("desc", "提交成功");
                return out;
            }
            String msg = objResult.getString("msg");
            out.put("desc", "提交失败," + NeteaseConstant.codeMap.get(code) + ":" + msg);
        } catch (Exception e) {
            LOG.info("sendcode--->" + e);
        }
        return out;
    }

    /**
     * 发送通知类和运营类短信
     */
    public static JSONObject sendtemplate(JSONObject inObj) {
        LOG.info("sendtemplate发送通知类和运营类短信--->" + inObj);
        JSONObject out = new JSONObject();
        out.put("result", "-1");
        out.put("desc", "提交失败");
        try {
            String templateid = inObj.getString("templateid");//模板编号(由客户顾问配置之后告知开发者)(必须)
            JSONArray mobiles = inObj.getJSONArray("mobiles");//接收者号码列表，JSONArray格式,如["186xxxxxxxx","186xxxxxxxx"]，限制接收者号码个数最多为100个
            JSONArray params = inObj.getJSONArray("params");//短信参数列表，用于依次填充模板，JSONArray格式，每个变量长度不能超过30字，如["xxx","yyy"];对于不包含变量的模板，不填此参数表示模板即短信全文内容(模板中若含变量则必须包含此参数)
            boolean needUp = inObj.getBooleanValue("needUp");//是否需要支持短信上行。true:需要，false:不需要  说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效(可选)
            if (StringUtils.isEmpty(templateid) || null == mobiles || mobiles.size() == 0 || mobiles.size() > 101) {
                LOG.info("sendtemplate入参有误");
                out.put("desc", "入参有误");
                return out;
            }
            if (null != params && params.size() > 0) {
                for (int i = 0; i < params.size(); i++) {
                    String param = params.getString(i);
                    if (param.length() > 31) {
                        LOG.info("sendtemplate入参有误");
                        out.put("desc", "params变量长度不能超过30字");
                        return out;
                    }
                }

            }
            // 设置请求的的参数，requestBody参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            /*
             * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
             * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
             * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
             */
            nvps.add(new BasicNameValuePair("templateid", String.valueOf(templateid)));
            nvps.add(new BasicNameValuePair("mobiles", mobiles.toString()));
            if (null != params && params.size() > 0) {
                nvps.add(new BasicNameValuePair("params", params.toString()));
            }
            nvps.add(new BasicNameValuePair("needUp", String.valueOf(needUp)));
            //发送验证码
            String result = RequestMethod.httpPostNetease(nvps, SENDTEMPLATE_URL, APP_SECRET, APP_KEY);
            out.put("sendMsg", result);
            LOG.info("sendtemplate------>接收参数" + inObj + " 发送接口返回结果=" + result);
            if (StringUtils.isEmpty(result)) {
                LOG.info("sendtemplate-----null------->result=null");
                return out;
            }
            JSONObject objResult = JSONObject.parseObject(result);
            int code = objResult.getIntValue("code");
            if (null != objResult && code == 200) {
                out.put("result", "1");
                out.put("desc", "提交成功");
                return out;
            }
            String msg = objResult.getString("msg");
            out.put("desc", "提交失败," + NeteaseConstant.codeMap.get(code) + ":" + msg);
        } catch (Exception e) {
            LOG.info("sendtemplate--->" + e);
        }
        return out;
    }


}
