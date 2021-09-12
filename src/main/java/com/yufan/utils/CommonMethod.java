package com.yufan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yufan.pojo.TbParam;
import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/27 10:14
 * 功能介绍:
 */
public class CommonMethod {

    private static Logger LOG = Logger.getLogger(CommonMethod.class);

    /**
     * Unicode 转码
     *
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {

        char aChar;

        int len = theString.length();

        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {

            aChar = theString.charAt(x++);

            if (aChar == '\\') {

                aChar = theString.charAt(x++);

                if (aChar == 'u') {

                    // Read the xxxx

                    int value = 0;

                    for (int i = 0; i < 4; i++) {

                        aChar = theString.charAt(x++);

                        switch (aChar) {

                            case '0':

                            case '1':

                            case '2':

                            case '3':

                            case '4':

                            case '5':

                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';

                    else if (aChar == 'n')

                        aChar = '\n';

                    else if (aChar == 'f')

                        aChar = '\f';

                    outBuffer.append(aChar);

                }

            } else

                outBuffer.append(aChar);

        }
        return outBuffer.toString();
    }

    /**
     * 生成订单号，最长支持20位
     */
    public static synchronized String randomStr(String addWord) {
        String time = DatetimeUtil.getNow("yyyyMMddHHmmssSSS");
        Random random = new Random();
        int r = random.nextInt(899) + 100;//随机三位数
        return new StringBuffer().append(addWord).append(time).append(r).toString();
    }

    /**
     * 生成w随机数
     *
     * @param w 位数
     * @return
     */
    public static synchronized String randomNum(int w) {
        Random random = new Random();
        int s = 8;
        int multiple10 = 1;
        for (int i = 1; i < w; i++) {
            s = s * 10 + 9;
            multiple10 = multiple10 * 10;
        }
        int r = random.nextInt(s) + multiple10;//随机w位数
        String str = String.valueOf(r);
        if (str.length() > w) {
            str = str.substring(0, w);
        }
        return str;
    }

    /**
     * 检验手机验证码
     *
     * @return
     */
    public static boolean checkPhoneCode(int validType, String phone, String phoneCode, IAccountDao iAccountDao) {
        try {
            //校验手机验证码
            TbVerification verification = iAccountDao.loadVerification(validType, phone, phoneCode);
            if (null == verification) {
                LOG.info("---手机验证码无效---");
                return false;
            }
            //判断手机验证码是否过期
            long nowTime = DatetimeUtil.convertStrToDate(DatetimeUtil.getNow(), "yyyy-MM-dd HH:mm:ss").getTime();
            long passTime = verification.getPassTime().getTime();
            if (nowTime > passTime) {
                LOG.info("---手机验证码过期---");
                iAccountDao.deltVerificationStatus(verification.getId());
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> replaceSpecialChar(String parmas) {
        String separator = "；|，|;|, |,|; |(\\r\\n)|(\\n)";
        List<String> list = Arrays.asList(parmas.split(separator));
        if (list.size() == 0) {
            return list;
        } else {
            ArrayList<String> resultList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                if (!StringUtils.isEmpty((String) list.get(i))) {
                    resultList.add(list.get(i).trim());
                }
            }
            return resultList;
        }
    }

    public Map<String, Object> jsonToMap(JSONObject jsonObject) {
        return JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, Object>>() {
        });
    }


    /**
     * 正则匹配和替换1
     *
     * @param sql
     * @param paramMap
     * @return
     */
    public static String getSqlReplace1(String sql, Map<String, Object> paramMap) {
        // String sql = " select * from tb_a whre 1=1 ##and a.id=${id}## ##and a.name=${name}## limit 80"
        String reg = "##(.*?\\$\\{(.*?)\\}.*?)##";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(sql);

        while (matcher.find()) {
            String key = matcher.group(2);
            String con = matcher.group(1);
            if (!paramMap.containsKey(key)) {
                sql = sql.replaceFirst(reg, "");
                continue;
            }
            con = con.replaceAll("\\$\\{.*?\\}", (String) paramMap.get(key));
            sql = sql.replaceFirst(reg, con);
        }
        return sql;
    }


    /**
     * 正则匹配和替换1
     *
     * @param sql
     * @param paramMap
     * @return
     */
    public static Pattern pattern = Pattern.compile("##(.*?\\$\\{(.*?)\\}.*?)##");

    public static String getSqlReplace2(String sql, Map<String, Object> paramMap) {
        // String sql = " select * from tb_a whre 1=1 ##and a.id=${id}## ##and a.name=${name}## limit 80"

        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String key = matcher.group(2);
            String con = matcher.group(1);
            String con00 = matcher.group(0);
            if (!paramMap.containsKey(key)) {
                sql = sql.replaceFirst(matcher.group(0), "");
                continue;
            }
            con = con.replaceAll("\\$\\{.*?\\}", (String) paramMap.get(key));
            sql = sql.replaceFirst(matcher.group(0), con);
        }
        return sql;
    }

    /**
     * qr码兑换刷新有效时间
     *
     * @return
     */
    public static String qrChangeOutTime() {
        String time = DatetimeUtil.addMinuteTime(DatetimeUtil.getNow(), 10, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING);
        return time;
    }

    /**
     * 随机获取一组用于校验的图片
     *
     * @return
     */
    public static Map<String, Object> getVerifyImgGroupCodeMap() {
        try {
            int size = CacheData.VERIFYIMGGROUPLIST.size() - 1;
            if (size <= 0) {
                return null;
            }
            Random r = new Random();
            int num = r.nextInt(size);
            return CacheData.VERIFYIMGGROUPLIST.get(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取日期类型配置信息
    public static TbParam getParamConfigByParamCodeAndKey(String paramCode, String paramKey) {
        try {
            if (StringUtils.isEmpty(paramCode) || StringUtils.isEmpty(paramKey)) {
                LOG.info("获取日期类型配置信息条件不能为空");
                return null;
            }
            List<TbParam> list = CacheData.PARAMLIST;
            for (int i = 0; i < list.size(); i++) {
                TbParam param = list.get(i);
                if (!paramCode.equals(param.getParamCode())) {
                    continue;
                }
                if (!paramKey.equals(param.getParamKey())) {
                    continue;
                }
                return param;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
