package com.yufan.task.service.impl.category;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.common.bean.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.category.ICategoryDao;
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
 * 创建时间:  2019/8/28 17:57
 * 功能介绍: 查询全部分类
 */
@Service("query_classify_all")
public class QueryAllClassifyList implements IResultOut {

    private Logger LOG = Logger.getLogger(QueryAllClassifyList.class);

    @Autowired
    private ICategoryDao iCategoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {

            String levelCodes = data.getString("level_codes");//一级分类编码(默认全部)
            String categoryCodes = data.getString("category_codes");//类目编码(默认全部)
            String propCodes = data.getString("prop_codes");//属性编码(默认全部)

            //排除
            String exclusionLevelCodes = data.getString("exclusion_level_codes");//排除的一级分类编码
            String exclusionCategoryCodes = data.getString("exclusion_category_codes");//排除的类目编码
            String exclusionPropCodes = data.getString("exclusion_prop_codes");//排除的属性编码

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
            Map<String, String> propCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(propCodes)) {
                String str[] = propCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    propCodeMap.put(str[i].trim(), str[i].trim());
                }
            }
            Map<String, String> exclusionPropCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(exclusionPropCodes)) {
                String str[] = exclusionPropCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    exclusionPropCodeMap.put(str[i].trim(), str[i].trim());
                }
            }

            //查询一级分类
            List<Map<String, Object>> listLevel = iCategoryDao.loadLevelListMap(Constants.DATA_STATUS_YX);
            List<Map<String, Object>> listLevelData = new ArrayList<>();
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
                    listLevelData.add(listLevel.get(i));
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
            List<Map<String, Object>> listLevelOut = new ArrayList<>();
            if (relMap.size() > 0) {
                //查询全部类目、属性和属性值
                List<Map<String, Object>> listAll = iCategoryDao.loadCategoryAllRelListMap(null, 1, 1, 1);
                List<Map<String, Object>> listCategory = new ArrayList<>();
                List<Map<String, Object>> listProp = new ArrayList<>();
                List<Map<String, Object>> listPropValue = new ArrayList<>();

                Map<String, String> distinctMap = new HashMap<>();//id-去重
                //分离数据类目、属性和属性值
                for (int i = 0; i < listAll.size(); i++) {
                    //类目
                    String categoryId = listAll.get(i).get("ca_category_id").toString();
                    String categoryName = listAll.get(i).get("ca_category_name").toString();
                    String categoryCode = listAll.get(i).get("ca_category_code").toString();
                    String caCategoryWebImg = listAll.get(i).get("ca_category_web_img").toString();

                    if (exclusionCategoryCodeMap.size() > 0 && exclusionCategoryCodeMap.get(categoryCode) != null) {
                        //排除的类目categoryCode
                        continue;
                    }
                    if (categoryCodeMap.size() > 0 && categoryCodeMap.get(categoryCode) == null) {
                        //查询具体categoryCode
                        continue;
                    }
                    if (distinctMap.get("category-" + categoryId) == null) {
                        Map<String, Object> categoryMap = new HashMap<>();
                        categoryMap.put("category_id", Integer.parseInt(categoryId));
                        categoryMap.put("category_name", categoryName);
                        categoryMap.put("category_code", categoryCode);
                        categoryMap.put("category_web_img", caCategoryWebImg);
                        categoryMap.put("prop_list", new ArrayList<>());
                        listCategory.add(categoryMap);
                        distinctMap.put("category-" + categoryId, categoryId);
                    }

                    //属性
                    String propId = listAll.get(i).get("it_prop_id") == null ? "" : listAll.get(i).get("it_prop_id").toString();
                    String propName = listAll.get(i).get("it_prop_name") == null ? "" : listAll.get(i).get("it_prop_name").toString();
                    String propWebImg = listAll.get(i).get("it_prop_web_img") == null ? "" : listAll.get(i).get("it_prop_web_img").toString();
                    String propCode = listAll.get(i).get("it_prop_code") == null ? "" : listAll.get(i).get("it_prop_code").toString();
                    if (exclusionPropCodeMap.size() > 0 && exclusionPropCodeMap.get(propCode) != null) {
                        //排除的propCode类目属性
                        continue;
                    }
                    if (propCodeMap.size() > 0 && propCodeMap.get(propCode) == null) {
                        //查询具体propCode类目属性
                        continue;
                    }
                    if (StringUtils.isNotEmpty(propId)) {
                        if (distinctMap.get("prop-" + propId) == null) {
                            Map<String, Object> propMap = new HashMap<>();
                            propMap.put("category_id", categoryId);
                            propMap.put("prop_id", propId);
                            propMap.put("prop_name", propName);
                            propMap.put("prop_code", propCode);
                            propMap.put("prop_web_img", propWebImg);
                            propMap.put("value_list", new ArrayList<>());
                            listProp.add(propMap);
                            distinctMap.put("prop-" + propId, propId);
                        }
                    }
                    //属性值
                    String valueId = listAll.get(i).get("pv_value_id") == null ? "" : listAll.get(i).get("pv_value_id").toString();
                    String valueName = listAll.get(i).get("pv_value_name") == null ? "" : listAll.get(i).get("pv_value_name").toString();
                    String value = listAll.get(i).get("pv_value") == null ? "" : listAll.get(i).get("pv_value").toString();
                    if (StringUtils.isNotEmpty(valueId)) {
                        if (distinctMap.get("value-" + valueId) == null) {
                            Map<String, Object> propValueMap = new HashMap<>();
                            propValueMap.put("value_id", valueId);
                            propValueMap.put("value_name", valueName);
                            propValueMap.put("prop_id", propId);
                            propValueMap.put("value", value);
                            listPropValue.add(propValueMap);
                            distinctMap.put("value-" + valueId, valueId);
                        }
                    }
                }
                //遍历一级分类
                for (int i = 0; i < listLevelData.size(); i++) {
                    String levelId = listLevelData.get(i).get("level_id").toString();
                    String levelCode = listLevelData.get(i).get("level_code").toString();
                    String levelName = listLevelData.get(i).get("level_name").toString();
                    Map<String, Object> levelMap = new HashMap<>();
                    levelMap.put("level_id", Integer.parseInt(levelId));
                    levelMap.put("level_code", levelCode);
                    levelMap.put("level_name", levelName);
                    List<Map<String, Object>> listCategoryOut = new ArrayList<>();
                    //遍历类目
                    for (int j = 0; j < listCategory.size(); j++) {
                        String categoryId = listCategory.get(j).get("category_id").toString();
                        String categoryCode = listCategory.get(j).get("category_code").toString();
                        String categoryName = listCategory.get(j).get("category_name").toString();
                        String categoryImg = listCategory.get(j).get("category_web_img").toString();
                        String word = levelId + "-" + categoryId;
                        //判断一级分类类目是否有关联
                        if (relMap.get(word) == null) {
                            //一级分类没有关联类目
                            continue;
                        }
                        Map<String, Object> categoryMap = new HashMap<>();
                        categoryMap.put("category_id", Integer.parseInt(categoryId));
                        categoryMap.put("category_name", categoryName);
                        categoryMap.put("category_code", categoryCode);
                        categoryMap.put("category_web_img", categoryImg);
                        List<Map<String, Object>> listCategoryPropOut = new ArrayList<>();
                        //遍历属性
                        for (int k = 0; k < listProp.size(); k++) {
                            String categoryId_ = listProp.get(k).get("category_id").toString();
                            String propId = listProp.get(k).get("prop_id").toString();
                            String propName = listProp.get(k).get("prop_name").toString();
                            String propCode = listProp.get(k).get("prop_code").toString();
                            String propWebImg = listProp.get(k).get("prop_web_img").toString();
                            if (!categoryId_.equals(categoryId)) {
                                continue;
                            }
                            Map<String, Object> propMap = new HashMap<>();
                            propMap.put("prop_id", propId);
                            propMap.put("prop_name", propName);
                            propMap.put("prop_code", propCode);
                            propMap.put("prop_web_img", propWebImg);
                            List<Map<String, Object>> listPropValueOut = new ArrayList<>();
                            //遍历属性值
                            for (int l = 0; l < listPropValue.size(); l++) {
                                String valueId = listPropValue.get(l).get("value_id").toString();
                                String valueName = listPropValue.get(l).get("value_name").toString();
                                String value = listPropValue.get(l).get("value").toString();
                                String propId_ = listPropValue.get(l).get("prop_id").toString();
                                if (!propId_.equals(propId)) {
                                    continue;
                                }
                                Map<String, Object> propValueMap = new HashMap<>();
                                propValueMap.put("value_id", Integer.parseInt(valueId));
                                propValueMap.put("value_name", valueName);
                                propValueMap.put("value", value);
                                listPropValueOut.add(propValueMap);
                            }
                            propMap.put("value_list", listPropValueOut);
                            listCategoryPropOut.add(propMap);
                        }
                        categoryMap.put("prop_list", listCategoryPropOut);
                        listCategoryOut.add(categoryMap);
                    }
                    levelMap.put("category_list", listCategoryOut);
                    listLevelOut.add(levelMap);
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