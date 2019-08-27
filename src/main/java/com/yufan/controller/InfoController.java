package com.yufan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IBasicService;
import com.yufan.common.service.IResultOut;
import com.yufan.common.service.ServiceFactory;
import com.yufan.common.utils.VerifySign;
import com.yufan.pojo.TbInfAccount;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 14:44
 * 功能介绍: 接口入口
 */
@Controller
@RequestMapping(value = "/")
public class InfoController {

    /**
     * 接口请求说明
     * appsecret
     * 下面是参数   sid(应用ID)   appkey(接口密钥)  timestamp(10位秒级int值) sign数字签名
     * 数字签名如下(备注：放到map中的有系统参数sid,appkey,timestamp,和data中，如果包括数对象和数组，则不用加入签名中)
     * 1.把post请求的各参数先放到一个键值对集合里 ,如 req = array( "key2"=>"value2", "key1"=>"value1" );
     * 2.先对req的排序,以键的字母从小到大排序序排.得到req = array( "key1"=>"value1", "key2"=>"value2" );
     * 3.然后遍历req,以如下格式把req里的所有键值对给拼接成一个字符串
     * 格式:"keyLen-key:valueLen-value",多个键值对以分号分割,其中,key的字符串
     * 长度应固定为2位, 1要写成01.value的长度是4位 1写成0001.
     * 4.对req格式化求得字符串为 "04-key1:0006-value1;04-key2:0006-value2"
     * 5.把求得的字符串和appsecret进行拼接然后求其md5值(其中的appsecret第三方不需要传,由接口方提供给第三方,接口方通过传过来的系统参数查询对应分配的appsecret)
     * sign=md5(packData(req)+appsecret)=md5("04-key1:0006-value1;04-key2:0006-value212345")=2b65a10ed4154187f82880cfd841c09f
     * <p>
     * post请求 json 格式
     * {
     * req_type:"接口业务",
     * timestamp:10位时间,
     * sign:"数字签名",
     * data:{
     * key:value,
     * . . .
     * }
     * }
     */

    private static Logger log = Logger.getLogger(InfoController.class);

    @Autowired
    private IBasicService iBasicService;

    @RequestMapping(value = "service")
    public void enterService(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        PrintWriter pw = null;
        String message = null;
        try {
            pw = response.getWriter();
            message = request.getParameter("message");

            if (null == message || "".equals(message)) {
                message = readStreamParameter(request.getInputStream());
            }
//            log.info("接收参数密文:" + message);
            log.info("接收参数明文:" + message);
            JSONObject obj = JSONObject.parseObject(message);
            if (obj != null) {
                ReceiveJsonBean jsonHeaderBean = JSON.toJavaObject(obj, ReceiveJsonBean.class);
                jsonHeaderBean.setRequest(request);
                jsonHeaderBean.setResponse(response);
                //请求业务验证包括时间戳间隔(请求时间相隔不能大于30秒)sign
                Integer index = jsonHeaderBean.getCheckEmptyValue();
//                log.info("系统参数为空校验结果=" + index + "---->注 1:req_type为空 2:sid为空 3:data为空 4:timestamp为空 5:sign为空0:正常 ");
                if (null == index || index != 0) {
                    result = packagMsg(ResultCode.PARAM_ERROR.getResp_code(), null);
                } else {
                    long now = new Date().getTime() / 1000L;//秒//当前UTC时间戳
                    long betweenTime = Math.abs(now - jsonHeaderBean.getTimestamp());
                    if (betweenTime < 120) {//秒
                        //验证sign(根据账号sid查询密钥)
                        TbInfAccount account = iBasicService.loadTbInfAccount(jsonHeaderBean.getSid());
                        String appsecret = account.getSecretKey();
//                        log.info("appsecret="+appsecret);
                        //检验签名
                        if (VerifySign.checkSign(jsonHeaderBean, appsecret)) {
                            IResultOut resultOut = ServiceFactory.getService(jsonHeaderBean.getReq_type());
                            boolean flag = resultOut.checkParam(jsonHeaderBean);
                            if(!flag){
                                result = packagMsg(ResultCode.NEED_PARAM_ERROR.getResp_code(), null);
                            }else{
                                result = resultOut.getResult(jsonHeaderBean);
                            }
                        } else {
                            result = packagMsg(ResultCode.ERROR_SIGN.getResp_code(), null);
                        }
                    } else {
                        log.info("---->请求的时间差有误,时间差:now=" + now + "-" + jsonHeaderBean.getTimestamp() + "=" + betweenTime + "  s");
                        result = packagMsg(ResultCode.OUT_OF_TIME.getResp_code(), null);
                    }
                }
            } else {
                result = packagMsg(ResultCode.PARAM_ERROR.getResp_code(), null);
            }
            log.info("调用结果：" + result);
            pw.write(result);
            pw.flush();
            pw.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            result = packagMsg(ResultCode.PARAM_ERROR.getResp_code(), null);
            pw.write(result);
            pw.flush();
            pw.close();
        }
    }

    @RequestMapping(value = "test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        PrintWriter pw = null;
        String message = null;
        try {
            pw = response.getWriter();
            message = request.getParameter("message");

            if (null == message || "".equals(message)) {
                message = readStreamParameter(request.getInputStream());
            }
            log.info("接收参数:" + message);
            JSONObject obj = JSONObject.parseObject(message);
            if (obj != null) {
                ReceiveJsonBean jsonHeaderBean = JSON.toJavaObject(obj, ReceiveJsonBean.class);
                jsonHeaderBean.setRequest(request);
                jsonHeaderBean.setResponse(response);
                IResultOut resultOut = ServiceFactory.getService(jsonHeaderBean.getReq_type());
                //校验参数
                boolean flag = resultOut.checkParam(jsonHeaderBean);
                if(!flag){
                    result = packagMsg(ResultCode.NEED_PARAM_ERROR.getResp_code(), null);
                }else{
                    result = resultOut.getResult(jsonHeaderBean);
                }
            } else {
                result = packagMsg(ResultCode.PARAM_ERROR.getResp_code(), null);
            }
            log.info("调用结果：" + result);
            pw.write(result);
            pw.flush();
            pw.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            result = packagMsg(ResultCode.PARAM_ERROR.getResp_code(), null);
            pw.write(result);
            pw.flush();
            pw.close();
        }
    }


    /**
     * 从流中读取数据
     *
     * @param in
     * @return
     */
    public String readStreamParameter(ServletInputStream in) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }


}
