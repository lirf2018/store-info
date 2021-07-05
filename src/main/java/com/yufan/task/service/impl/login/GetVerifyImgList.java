package com.yufan.task.service.impl.login;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.service.IResultOut;
import com.yufan.pojo.TbParam;
import com.yufan.task.dao.verify.IVerifyImgDao;
import com.yufan.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * @description: 获取校验图片
 * @author: lirf
 * @time: 2021/7/1
 */
@Service("get_verify_img_list")
public class GetVerifyImgList implements IResultOut {

    private Logger LOG = Logger.getLogger(GetVerifyImgList.class);

    private int Maxcount = 5000;// 默认循环次数

    @Autowired
    private IVerifyImgDao iVerifyImgDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            Map<String, Object> map = CommonMethod.getVerifyImgGroupCodeMap();
            if (map == null) {
                LOG.info("========verifyCode 获取失败=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            String verifyCode = map.get("verify_code").toString();
            String verifyTitle = map.get("verify_title").toString();
            Object backImgObj = map.get("back_img");
//            String backImg = "";
//            backImg = backImgObj == null || "".equals(backImgObj.toString().trim()) ? "vimgback.png" : map.get("back_img").toString();
            // 查询选中的一组校验图片
            List<Map<String, Object>> listChoseGroupImgList = iVerifyImgDao.queryVerifyImgListByCode(verifyCode);
            if (CollectionUtils.isEmpty(listChoseGroupImgList)) {
                LOG.info("========listChoseGroupImgList 没数据=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            // 查询其它不为相似的图片列表
            Integer similarType = Integer.parseInt(map.get("similar_type").toString());
            List<Map<String, Object>> listUnChoseGroupImgList = iVerifyImgDao.queryVerifyImgListUnInByCode(verifyCode, similarType);
            if (CollectionUtils.isEmpty(listUnChoseGroupImgList)) {
                LOG.info("========listUnChoseGroupImgList 没数据=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            // 判断是否能满足生成9张图片条件
            int canChoseCount = listChoseGroupImgList.size();// 能用于选择的图片总数
            int unDataCount = listUnChoseGroupImgList.size();// 用于混合的非选择图片
            //
            int needCount = 9;// 9宫选择图片
            int needChoseCount = getNeedChoseCount();// 必须要满足要选择的数量
            LOG.info("========needChoseCount =========" + needChoseCount);
            if (needChoseCount != 4 && needChoseCount != 5 && needChoseCount != 3) {
                LOG.info("========needChoseCount 不满足条件=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (canChoseCount < needChoseCount) {
                LOG.info("========listChoseGroupImgList 不满足要选择的数量=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            if (unDataCount < (needCount - needChoseCount)) {
                LOG.info("========listUnChoseGroupImgList 不满足要选择的数量=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }

            // 背景图片规则
            boolean defaultImgFlag = false;
            String defaultBackImg = "";
            List<TbParam> list = CacheData.PARAMLIST;
            for (int i = 0; i < list.size(); i++) {
                TbParam param = list.get(i);
                if ("default_verify_back_img".equals(param.getParamCode()) && StringUtils.isNotEmpty(param.getParamValue()) && StringUtils.isNotEmpty(param.getParamValue2())) {
                    defaultImgFlag = Boolean.valueOf(param.getParamValue2());
                    defaultBackImg = param.getParamValue();
                    break;
                }
            }
            //
            String backImg = "";
            backImg = backImgObj == null || "".equals(backImgObj.toString().trim()) ? defaultBackImg : backImgObj.toString();
            // 满足9张一组
            String uuidImgVerifyMark = UUID.randomUUID().toString().replace("-", "");
            List<Map<String, Object>> outList = generateVerifyImg(listChoseGroupImgList, listUnChoseGroupImgList, uuidImgVerifyMark, verifyCode, needCount, needChoseCount, backImg, defaultImgFlag);
            if (null == outList || outList.size() != 9) {
                LOG.info("========outList 没数据 或者数据不足9条=========");
                return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
            }
            dataJson.put("list", outList);
            dataJson.put("title", verifyTitle);
            dataJson.put("uuidImgVerifyMark", uuidImgVerifyMark);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
    }

    private int getNeedChoseCount() {
        try {
            Map<Integer, Integer> index = new HashMap<>();
            index.put(3, 3);
            index.put(4, 4);
            index.put(5, 5);

            int count = 0;
            Random r = new Random();
            while (true) {
                int n = r.nextInt(10);
                count++;
                if (index.get(n) != null) {
                    break;
                }
                if (count > Maxcount) {
                    break;
                }
            }
            LOG.info("=======count======getNeedChoseCount=========" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 5;
    }

    private List<Map<String, Object>> generateVerifyImg(List<Map<String, Object>> listChoseGroupImgList, List<Map<String, Object>> listUnChoseGroupImgList, String uuidImgVerifyMark,
                                                        String verifyCode, int needCount, int needChoseCount, String backImg, boolean defaultImgFlag) {
        String outDateTime = DatetimeUtil.addHourTime(DatetimeUtil.getNow(), 1, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING);
        List<Map<String, Object>> outList = new ArrayList<>();
        try {
            int canChoseCount = listChoseGroupImgList.size();
            Map<Integer, Integer> indexChoseMap = getNeedChoseIndexMap(canChoseCount, needChoseCount);// 返回要选择的图片下标
            // 从干扰图片中选择剩下的图片 （needCount-needChoseCount）
            int unNeedCount = needCount - needChoseCount;
            Map<Integer, Integer> indexUnChoseMap = getUnNeedChoseIndexMap(listUnChoseGroupImgList.size(), unNeedCount);// 返回不要选择的图片下标
            if (indexChoseMap == null || indexUnChoseMap == null) {
                LOG.info("===========indexChoseMap====或=======indexUnChoseMap============为空===========");
                return null;
            }
            // 组合数据 把要选择的和混合的组合 （随机生成要选择图片的下标）
            LOG.info("============开始组合图片==============");
            Map<Integer, Integer> index = new HashMap<>();
            Random r = new Random();
            int count = 0;
            while (true) {
                int n = r.nextInt(needCount);
                count++;
                index.put(n, n);
                if (index.size() == indexChoseMap.size()) {
                    break;
                }
                if (count > Maxcount) {
                    index.clear();
                    LOG.info("======未能生成有效组合，采用默认====index====");
                    for (int i = 0; i < indexChoseMap.size(); i++) {
                        int jiShu = i * 2 + 1;// 奇数
                        int ouShu = i * 2;// 偶数
                        if ((System.currentTimeMillis() / 1000) % 2 == 0) {
                            index.put(ouShu, ouShu);
                        } else {
                            index.put(jiShu, jiShu);
                        }
                    }
                    break;
                }
            }
            LOG.info("=======count======generateVerifyImg=========" + count);
            // 设置9宫格数据图片 0 到  needCount-1
            for (int i = 0; i < needCount; i++) {
                if (index.get(i) == null) {
                    // 放不要选择要图片
                    Integer key = null;
                    for (Map.Entry<Integer, Integer> map : indexUnChoseMap.entrySet()) {
                        key = map.getKey();
                        break;
                    }
                    Map<String, Object> keyMap = listUnChoseGroupImgList.get(key);
                    removeAndAddKey(keyMap, defaultImgFlag, backImg);
                    if (null != keyMap) {
                        outList.add(keyMap);
                        indexUnChoseMap.remove(key);
                    }
                    LOG.info("======放不要选择要图片=========" + count);
                } else {
                    // 放要选择的图片
                    Integer key = null;
                    for (Map.Entry<Integer, Integer> map : indexChoseMap.entrySet()) {
                        key = map.getKey();
                        break;
                    }
                    Map<String, Object> keyMap = listChoseGroupImgList.get(key);
                    removeAndAddKey(keyMap, defaultImgFlag, backImg);
                    if (null != keyMap) {
                        outList.add(keyMap);
                        indexChoseMap.remove(key);
                    }
                    LOG.info("======放要选择要图片=========" + count);
                }
            }
            CacheData.VERIFYIMGCODEMAP.put(uuidImgVerifyMark, verifyCode + ";" + needChoseCount + ";" + outDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outList;
    }

    private void removeAndAddKey(Map<String, Object> keyMap, boolean defaultImgFlag, String backImg) {
        keyMap.put("back_img", getBackImg(backImg, defaultImgFlag));
        keyMap.remove("verify_code");
        keyMap.remove("verify_img");
    }


    private String getBackImg(String backImg, boolean defaultImgFlag) {
        try {
            if (!defaultImgFlag) {
                return Constants.IMG_WEB_URL + backImg;
            }
            List<String> imgList = CacheData.VERIFYIMGCODEMAPIMGLIST;
            Random r = new Random();
            int index = r.nextInt(imgList.size());
            return Constants.IMG_WEB_URL + imgList.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return backImg;
    }

    private Map<Integer, Integer> getNeedChoseIndexMap(int canChoseCount, int needChoseCount) {
        try {
            // 从能选择图片的数据中随机获取 needChoseCount 条数据
            int count = 0;// 默认循环次数
            Map<Integer, Integer> indexChoseMap = new HashMap<>();
            Random r = new Random();
            while (true) {
                int n = r.nextInt(canChoseCount);
                count++;
                indexChoseMap.put(n, n);
                if (indexChoseMap.size() == needChoseCount) {
                    break;
                }
                if (count > Maxcount) {
                    indexChoseMap.clear();
                    LOG.info("======未能生成有效组合，采用默认====needChoseCount====");
                    for (int i = 0; i < needChoseCount; i++) {
                        indexChoseMap.put(i, i);
                    }
                    break;
                }
            }
            LOG.info("=======count======getNeedChoseIndexMap=========" + count);
            return indexChoseMap;
        } catch (Exception e) {
            LOG.error("========indexChoseMap====异常=========", e);
        }
        return null;
    }

    private Map<Integer, Integer> getUnNeedChoseIndexMap(int canChoseCount, int unNeedChoseCount) {
        try {
            // 从干扰选择图片的数据中随机获取 unNeedChoseCount 条数据
            int count = 0;// 默认循环次数
            Map<Integer, Integer> indexUnChoseMap = new HashMap<>();
            Random r = new Random();
            while (true) {
                int n = r.nextInt(canChoseCount);
                count++;
                indexUnChoseMap.put(n, n);
                if (indexUnChoseMap.size() == unNeedChoseCount) {
                    break;
                }
                if (count > Maxcount) {
                    indexUnChoseMap.clear();
                    LOG.info("======未能生成有效组合，采用默认===unNeedChoseCount=====");
                    for (int i = 0; i < unNeedChoseCount; i++) {
                        indexUnChoseMap.put(i, i);
                    }
                    break;
                }
            }
            LOG.info("=======count======getUnNeedChoseIndexMap=========" + count);
            return indexUnChoseMap;
        } catch (Exception e) {
            LOG.error("========indexUnChoseMap====异常=========", e);
        }
        return null;
    }

    @Override
    public boolean checkParam(ReceiveJsonBean receiveJsonBean) {
        JSONObject data = receiveJsonBean.getData();
        try {

            return true;
        } catch (Exception e) {
            LOG.error("----check-error---", e);
        }
        return false;
    }
}