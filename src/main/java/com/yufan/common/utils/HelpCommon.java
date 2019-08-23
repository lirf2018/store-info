package com.yufan.common.utils;

import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * @功能名称
 * @作者 lirongfan
 * @时间 2016年9月13日 下午4:00:45
 */
public class HelpCommon {

    private static Logger LOG = Logger.getLogger(HelpCommon.class);

    /**
     * 得到sign结合字符串
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getSign(MyMap map) {

        StringBuffer strs = new StringBuffer();
        map.sort();
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            int key_length = key.length();
            String key_length_ = "";
            if (key_length < 10) {
                key_length_ = "0" + key_length;
            } else {
                key_length_ = "" + key_length;
            }
            String value = map.get(key) + "";
            int value_length = value.length();
            String value_length_ = "";
            if (value_length < 10) {// 1位
                value_length_ = "000" + value_length;
            } else if (value_length > 9 && value_length < 100) {
                value_length_ = "00" + value_length;
            } else if (value_length > 99 && value_length < 1000) {
                value_length_ = "0" + value_length;
            } else {
                value_length_ = "" + value_length;
            }
            strs.append(key_length_);
            strs.append("-");
            strs.append(key);
            strs.append(":");
            strs.append(value_length_);
            strs.append("-");
            strs.append(value);
            strs.append(";");
        }
        String str = strs.substring(0, strs.length() - 1);
//        LOG.info("-------->得到sign结合字符串=" + str);
        return str;
    }

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
}
