package com.yufan.utils.sms.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2018/7/31 15:28
 * 功能介绍: 阿里去短信
 */
public class AliyunSmsUtil {

    private Logger LOG = Logger.getLogger(AliyunSmsUtil.class);
    private static volatile AliyunSmsUtil aliyunSmsUtil;

    public static AliyunSmsUtil getInstence() {

        if (null == aliyunSmsUtil) {
            synchronized (AliyunSmsUtil.class) {
                if (null == aliyunSmsUtil) {
                    aliyunSmsUtil = new AliyunSmsUtil();
                }
            }
        }
        return aliyunSmsUtil;
    }

    /**
     * 发送短信
     *
     * @param phone
     * @param contends
     * @return
     */
    public JSONObject sendPhoneSms(String phone, String contends, String smsNumber, String sign, String smsModel) {
        LOG.info("sendPhoneSms发送短信验证码--->" + phone + " contends=" + contends + " smsNumber=" + smsNumber + " sign=" + sign + " smsModel=" + smsModel);
        try {
            JSONObject out = new JSONObject();
            out.put("result", "-1");
            out.put("desc", "提交失败");
            //产品名称:云通信短信API产品,开发者无需替换
            String product = "Dysmsapi";
            //产品域名,开发者无需替换
            String domain = "dysmsapi.aliyuncs.com";
            //TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
            String accessKeyId = Constants.ACCESSKEYID_ALIPAY;
            String accessKeySecret = Constants.ACCESSKEYSECRET_ALIPAY;

            Map<String, String> map = new HashMap<String, String>();
            map.put("product", product);
            map.put("domain", domain);
            map.put("accessKeyId", accessKeyId);
            map.put("accessKeySecret", accessKeySecret);
            map.put("phone", phone);
            map.put("contends", contends);
            map.put("smsNumber", smsNumber);
            map.put("sign", sign); //必填:短信签名-可在短信控制台中找到
            map.put("smsModel", smsModel);//必填:短信模板-可在短信控制台中找到
            SendSmsResponse sendSmsResponse = sendSms(map);
            if (null != sendSmsResponse) {
                LOG.info("----------------->result:" + JSONObject.toJSONString(sendSmsResponse));
                if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                    out.put("result", "1");
                    out.put("desc", "提交成功");
                } else {
                    out.put("msg", sendSmsResponse.getMessage());
                }
            }
            return out;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发短信
     *
     * @return
     * @throws ClientException
     */
    private SendSmsResponse sendSms(Map<String, String> map) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", map.get("accessKeyId"), map.get("accessKeySecret"));
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", map.get("product"), map.get("domain"));
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(map.get("phone"));
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(map.get("sign"));//"云通信"
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(map.get("smsModel"));//"SMS_1000000"
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(map.get("contends"));//"{\"name\":\"Tom\", \"code\":\"123\"}"
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(map.get("smsNumber"));

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }


    /**
     * 查明细
     *
     * @param map
     * @return
     * @throws ClientException
     */
    public QuerySendDetailsResponse querySendDetails(Map<String, String> map) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", map.get("accessKeyId"), map.get("accessKeySecret"));
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", map.get("product"), map.get("domain"));
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(map.get("phone"));//
        //可选-流水号
        request.setBizId(map.get("bizId"));
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public JSONObject test(String validCode){
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("desc", validCode);
        return result;
    }

    public static void main(String[] args) throws ClientException, InterruptedException {

//        //发短信
//        SendSmsResponse response = sendSms();
//        System.out.println("短信接口返回的数据----------------");
//        System.out.println("Code=" + response.getCode());
//        System.out.println("Message=" + response.getMessage());
//        System.out.println("RequestId=" + response.getRequestId());
//        System.out.println("BizId=" + response.getBizId());
//
//        Thread.sleep(3000L);
//
//        //查明细
//        if(response.getCode() != null && response.getCode().equals("OK")) {
//            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
//            System.out.println("短信明细查询接口返回数据----------------");
//            System.out.println("Code=" + querySendDetailsResponse.getCode());
//            System.out.println("Message=" + querySendDetailsResponse.getMessage());
//            int i = 0;
//            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
//            {
//                System.out.println("SmsSendDetailDTO["+i+"]:");
//                System.out.println("Content=" + smsSendDetailDTO.getContent());
//                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
//                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
//                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
//                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
//                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
//                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
//                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
//            }
//            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
//            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
//        }

    }

}



