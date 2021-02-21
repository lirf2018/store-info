package com.yufan.utils;

import com.yufan.pojo.TbVerification;
import com.yufan.task.dao.account.IAccountDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

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

    public static  BigDecimal findPostPrice(Map<Integer, BigDecimal> addrIdsFreightMap, BigDecimal userAddrDefaulFreight, String[] addrIdsArray) {
        BigDecimal freight = new BigDecimal("0.00");
        //镇
        Integer freight4 = Integer.parseInt(addrIdsArray[3]);
        //县
        Integer freight3 = Integer.parseInt(addrIdsArray[2]);
        //市
        Integer freight2 = Integer.parseInt(addrIdsArray[1]);
        //省
        Integer freight1 = Integer.parseInt(addrIdsArray[0]);

        // 地址如果失效，则默认运费
        BigDecimal f4 = addrIdsFreightMap.get(freight4);
        BigDecimal f3 = addrIdsFreightMap.get(freight3);
        BigDecimal f2 = addrIdsFreightMap.get(freight2);
        BigDecimal f1 = addrIdsFreightMap.get(freight1);
        if (null == f1 || null == f2 || null == f3 || null == f4) {
            //默认运费
            LOG.info("-------默认运费------");
            freight = userAddrDefaulFreight;
        } else {
            // 取最大的一个
            BigDecimal maxfreight = f1;
            if (maxfreight.compareTo(f2) < 0) {
                maxfreight = f2;
            }
            if (maxfreight.compareTo(f3) < 0) {
                maxfreight = f3;
            }
            if (maxfreight.compareTo(f4) < 0) {
                maxfreight = f4;
            }
            freight = maxfreight;
        }
        return freight;
    }
}
