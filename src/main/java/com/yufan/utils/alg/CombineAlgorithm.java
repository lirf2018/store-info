package com.yufan.utils.alg;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建人: lirf
 * 创建时间:  2018/11/19 17:21
 * 功能介绍: 组合算法 从M个数中取出N个数，无顺序
 */
public class CombineAlgorithm {
    /* 原M个数据数组 */
    private Object[] src;

    /* src数组的长度 */
    private int m;

    /* 需要获取N个数 */
    private int n;

    //临时变量，obj的行数
    private int objLineIndex;

    /* 存放结果的二维数组 */
    public Object[][] obj;

    public CombineAlgorithm(Object[] src, int getNum) throws Exception {
        if (src == null)
            throw new Exception("原数组为空.");
        if (src.length < getNum)
            throw new Exception("要取的数据比原数组个数还 大 .");
        this.src = src;
        m = src.length;
        n = getNum;

        /*  初始化  */
        objLineIndex = 0;
        obj = new Object[combination(m, n)][n];

        Object[] tmp = new Object[n];
        combine(src, 0, 0, n, tmp);
    }

    /**
     * <p>
     * 计算 C(m,n)个数 = (m!)/(n!*(m-n)!)
     * </p>
     * 从M个数中选N个数，函数返回有多少种选法 参数 m 必须大于等于 n m = 0; n = 0; retuan 1;
     *
     * @param m
     * @param n
     * @return
     * @since royoan 2014-6-13 下午8:25:33
     */
    public int combination(int m, int n) {
        if (m < n)
            return 0; // 如果总数小于取出的数，直接返回0

        int k = 1;
        int j = 1;
        // 该种算法约掉了分母的(m-n)!,这样分子相乘的个数就是有n个了
        for (int i = n; i >= 1; i--) {
            k = k * m;
            j = j * n;
            m--;
            n--;
        }
        return k / j;
    }

    /**
     * <p> 递归算法，把结果写到obj二维数组对象 </p>
     *
     * @param src
     * @param srcIndex
     * @param i
     * @param n
     * @param tmp
     * @since royoan 2014-6-15 上午11:22:24
     */
    private void combine(Object src[], int srcIndex, int i, int n, Object[] tmp) {
        int j;
        for (j = srcIndex; j < src.length - (n - 1); j++) {
            tmp[i] = src[j];
            if (n == 1) {
                //System.out.println(Arrays.toString(tmp));
                System.arraycopy(tmp, 0, obj[objLineIndex], 0, tmp.length);
                //obj[objLineIndex] = tmp;
                objLineIndex++;
            } else {
                n--;
                i++;
                combine(src, j + 1, i, n, tmp);
                n++;
                i--;
            }
        }

    }

    public Object[][] getResutl() {
        return obj;
    }

    /**
     * 组合结果
     *
     * @param a 下标
     * @param b 上标
     * @return
     */
    public static int c(int a, int b) {
        int da = 1;
        int xiao = 1;
        for (int i = 0; i < b; i++) {
            da = da * a;
            a--;
        }
        for (; b > 0; b--) {
            xiao = xiao * b;
        }
        return da / xiao;
    }


    /**
     * 用法实例
     *
     * @param args
     * @throws Exception
     * @since royoan 2014-6-15 下午8:21:05
     */
    public static void main(String[] args) throws Exception {
        String word = "bac4";
        int wordLen = word.length();
        String[] splitArr = new String[wordLen];//拆分成一个词
        for (int i = 0; i < wordLen; i++) {
            String w = word.substring(i, i + 1);
            splitArr[i] = w;
        }
        int ucount = 0;//总共的可能组合数
        for (int i = 1; i <= wordLen; i++) {
            ucount = ucount + c(wordLen, i);
        }

        String[] strCCount = new String[ucount];//所有可能的组合
        int strCCountIndex = 0;
        for (int j = 0; j < wordLen; j++) {
            CombineAlgorithm ca = new CombineAlgorithm(splitArr, j + 1);
            Object[][] c = ca.getResutl();
            for (int i = 0; i < c.length; i++) {
                String w = "";
                for (int k = 0; k < c[i].length; k++) {
                    w = w + c[i][k];
                }
//                System.out.println(w);
                strCCount[strCCountIndex] = w;
                strCCountIndex = strCCountIndex + 1;
            }
        }
        System.out.println("总共的可能组合数=" + ucount);
        System.out.println(JSONObject.toJSONString(strCCount));
    }


}
