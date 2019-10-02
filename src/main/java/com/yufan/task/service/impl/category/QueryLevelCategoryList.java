package com.yufan.task.service.impl.category;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.service.impl.Test;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yufan.common.bean.ResponeUtil.packagMsg;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 17:07
 * 功能介绍: 查询一级分类
 */
@Service("query_level_category_list")
public class QueryLevelCategoryList implements IResultOut {

    private Logger LOG = Logger.getLogger(Test.class);

    @Autowired
    private ICategoryDao iCategoryDao;


    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            String levelCodes = data.getString("level_codes");//一级分类编码(默认全部)
            String categoryCodes = data.getString("category_codes");//类目编码(默认全部)

            //排除
            String exclusionLevelCodes = data.getString("exclusion_level_codes");//排除的一级分类编码
            String exclusionCategoryCodes = data.getString("exclusion_category_codes");//排除的类目编码

            Map<String, String> levelCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(levelCodes)) {
                String str[] = levelCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    levelCodeMap.put(str[i].trim(), str[i].trim());
                }
            }
            Map<String, String> categoryCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(categoryCodes)) {
                String str[] = categoryCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    categoryCodeMap.put(str[i].trim(), str[i].trim());
                }
            }
            Map<String, String> exclusionLevelCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(exclusionLevelCodes)) {
                String str[] = exclusionLevelCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    exclusionLevelCodeMap.put(str[i].trim(), str[i].trim());
                }
            }
            Map<String, String> exclusionCategoryCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(exclusionCategoryCodes)) {
                String str[] = exclusionCategoryCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    exclusionCategoryCodeMap.put(str[i].trim(), str[i].trim());
                }
            }

            List<Map<String, Object>> listLevel = iCategoryDao.loadLevelListMap(Constants.DATA_STATUS_YX);
            List<Map<String, Object>> listLevelOut = new ArrayList<>();
            String levelIds = "";
            for (int i = 0; i < listLevel.size(); i++) {
                String levelCode = listLevel.get(i).get("level_code").toString();
                String levelId = listLevel.get(i).get("level_id").toString();
                //排除
                if (exclusionLevelCodeMap.size() > 0 && exclusionLevelCodeMap.get(levelCode) != null) {
                    //有排除的levelCode
                    continue;
                }
                //查询具体levelCode
                if (levelCodeMap.size() > 0 && levelCodeMap.get(levelCode) == null) {
                    //判断levelCode包含(查询具体levelCode)
                    continue;
                } else {
                    levelIds = levelIds + levelId + ",";
                    listLevel.get(i).put("category_list", new ArrayList<>());
                    listLevelOut.add(listLevel.get(i));
                }
            }

            Map<String, String> relMap = new HashMap<>();
            if (StringUtils.isNotEmpty(levelIds)) {
                levelIds = levelIds.substring(0, levelIds.length() - 1);
                //查询一级分类关联类目标识
                List<Map<String, Object>> listRel = iCategoryDao.loadLeveCategoryRel(levelIds, null);
                for (int i = 0; i < listRel.size(); i++) {
                    String categoryId = listRel.get(i).get("category_id").toString();
                    String levelId = listRel.get(i).get("level_id").toString();
                    String word = levelId + "-" + categoryId;
                    relMap.put(word, word);
                }
            }

            if (relMap.size() > 0) {
                //查询类目列表
                List<Map<String, Object>> listCategory = iCategoryDao.loadCategoryListMap(Constants.DATA_STATUS_YX);
                for (int i = 0; i < listLevelOut.size(); i++) {
                    String levelId = listLevelOut.get(i).get("level_id").toString();
                    List<Map<String, Object>> listCategoryOut = new ArrayList<>();
                    for (int j = 0; j < listCategory.size(); j++) {
                        String categoryId = listCategory.get(j).get("category_id").toString();
                        String categoryCode = listCategory.get(j).get("category_code").toString();
                        String categoryName = listCategory.get(j).get("category_name").toString();
                        String categoryImg = listCategory.get(j).get("category_img").toString();
                        String word = levelId + "-" + categoryId;
                        //判断一级分类类目是否有关联
                        if (relMap.get(word) == null) {
                            continue;
                        }
                        //排除类目
                        if (exclusionCategoryCodeMap.size() > 0 && exclusionCategoryCodeMap.get(categoryCode) != null) {
                            //有排除的categoryCode
                            continue;
                        }
                        //查询具体categoryCode
                        if (categoryCodeMap.size() > 0 && categoryCodeMap.get(categoryCode) == null) {
                            continue;
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("category_name", categoryName);
                            map.put("category_id", Integer.parseInt(categoryId));
                            map.put("category_code", categoryCode);
                            map.put("category_img", categoryImg);
                            listCategoryOut.add(map);
                        }
                    }
                    listLevelOut.get(i).put("category_list", listCategoryOut);
                }

            }

            dataJson.put("level_list", listLevelOut);
            return packagMsg(ResultCode.OK.getResp_code(), dataJson);
        } catch (Exception e) {
            LOG.error("-------error----", e);
        }
        return packagMsg(ResultCode.FAIL.getResp_code(), dataJson);
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