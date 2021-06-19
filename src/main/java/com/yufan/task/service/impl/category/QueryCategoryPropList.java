package com.yufan.task.service.impl.category;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.yufan.common.bean.ReceiveJsonBean;
import com.yufan.utils.ResultCode;
import com.yufan.common.service.IResultOut;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.task.service.impl.Test;
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
 * 创建时间:  2019/8/26 17:08
 * 功能介绍:  查询类目
 */
@Service("query_category_prop_list")
public class QueryCategoryPropList implements IResultOut {

    private Logger LOG = Logger.getLogger(Test.class);

    @Autowired
    private ICategoryDao iCategoryDao;

    @Override
    public String getResult(ReceiveJsonBean receiveJsonBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject data = receiveJsonBean.getData();
        try {
            String categoryCodes = data.getString("category_codes");//类目编码(默认全部)
            String propCodes = data.getString("prop_codes");//属性编码(默认全部)

            //排除
            String exclusionCategoryCodes = data.getString("exclusion_category_codes");//排除的类目编码
            String exclusionPropCodes = data.getString("exclusion_prop_codes");//排除的属性编码

            Map<String, String> categoryCodeMap = new HashMap<>();
            if (StringUtils.isNotEmpty(categoryCodes)) {
                String str[] = categoryCodes.split(";");
                for (int i = 0; i < str.length; i++) {
                    categoryCodeMap.put(str[i].trim(), str[i].trim());
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

            //查询全部类目、属性和属性值
            List<Map<String, Object>> listAll = iCategoryDao.loadCategoryAllRelListMap(null, 1, 1, 1);
            List<Map<String, Object>> listCategory = new ArrayList<>();
            List<Map<String, Object>> listProp = new ArrayList<>();
            List<Map<String, Object>> listPropValue = new ArrayList<>();

            Map<String, String> distinctMap = new HashMap<>();//id-去重
            //分离数据
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
            //组合数据类目属性和类目属性值
            for (int j = 0; j < listProp.size(); j++) {
                String propId = listProp.get(j).get("prop_id").toString();
                List<Map<String, Object>> outValueList = new ArrayList<>();
                for (int k = 0; k < listPropValue.size(); k++) {
                    if (!propId.equals(listPropValue.get(k).get("prop_id").toString())) {
                        continue;
                    }
                    Map<String, Object> propValueMap = new HashMap<>();
                    propValueMap.put("value_id", Integer.parseInt(listPropValue.get(k).get("value_id").toString()));
                    propValueMap.put("value_name", listPropValue.get(k).get("value_name"));
                    propValueMap.put("value", listPropValue.get(k).get("value"));
                    outValueList.add(propValueMap);
                }
                listProp.get(j).put("value_list", outValueList);
            }
            //组合数据类目和类目属性
            for (int i = 0; i < listCategory.size(); i++) {
                String categoryId = listCategory.get(i).get("category_id").toString();
                List<Map<String, Object>> outPropList = new ArrayList<>();
                for (int j = 0; j < listProp.size(); j++) {
                    if (!categoryId.equals(listProp.get(j).get("category_id").toString())) {
                        continue;
                    }
                    Map<String, Object> propMap = new HashMap<>();
                    propMap.put("prop_id", Integer.parseInt(listProp.get(j).get("prop_id").toString()));
                    propMap.put("prop_name", listProp.get(j).get("prop_name"));
                    propMap.put("prop_code", listProp.get(j).get("prop_code"));
                    propMap.put("prop_web_img", listProp.get(j).get("prop_web_img"));
                    propMap.put("value_list", listProp.get(j).get("value_list"));
                    outPropList.add(propMap);
                }
                listCategory.get(i).put("prop_list", outPropList);
            }

            dataJson.put("category_list", listCategory);
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
