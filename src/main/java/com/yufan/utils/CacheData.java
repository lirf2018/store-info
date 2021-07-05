package com.yufan.utils;

import com.yufan.pojo.TbParam;

import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/26 16:46
 * 功能介绍:  缓存数据
 */
public class CacheData {


    /**
     * 参数列表
     */
    public static List<TbParam> PARAMLIST = new ArrayList<>();


    /**
     * 校验图片组
     */
    public static List<Map<String, Object>> VERIFYIMGGROUPLIST = new ArrayList<>();


    /**
     * 用于保存图片校验码 key = uuidMark, value = VerifyImgCode;图片数量;过期时间(过期时间只用于清楚缓存数据，不用于校验有效性)
     */
    public static Map<String, String> VERIFYIMGCODEMAP = new HashMap<>();

    /**
     * 背景图片列表
     */
    public static List<String> VERIFYIMGCODEMAPIMGLIST = new ArrayList<>();

}
