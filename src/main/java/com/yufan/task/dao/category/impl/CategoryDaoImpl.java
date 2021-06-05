package com.yufan.task.dao.category.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbItemprops;
import com.yufan.task.dao.category.ICategoryDao;
import com.yufan.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 16:47
 * 功能介绍:
 */
@Transactional
@Repository
public class CategoryDaoImpl implements ICategoryDao {

    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public List<Map<String, Object>> queryCategoryRelListMap(int categoryId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_code,ca.category_name ");
        sql.append(" ,it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.is_sales as it_is_shell ");
        sql.append(" ,pv.value_id as pv_value_id,pv.value_name as pv_value_name,pv.`value` as pv_value ");
        sql.append(" from tb_category ca ");
        sql.append(" JOIN tb_itemprops it on it.category_id=ca.category_id and it.status=1 ");
        sql.append(" JOIN tb_props_value pv on pv.prop_id=it.prop_id and pv.`status`=1 ");
        sql.append(" where ca.category_id=").append(categoryId).append(" and ca.`status`=1 ");
        sql.append(" ORDER BY it.data_index DESC,it.prop_id desc,pv.data_index DESC,pv.value_id DESC ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    public List<Map<String, Object>> loadLevelCategoryData(Integer levelId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_code,ca.category_name,ca.data_index,CONCAT('").append(Constants.IMG_WEB_URL).append("',ca.category_img) as category_img,ca.`status` ");
        sql.append(" from tb_category ca ");
        if (null != levelId && levelId > 0) {
            sql.append(" left JOIN tb_level_category_rel rel on rel.category_id=ca.category_id  ");
        }
        sql.append(" where 1=1 ");
        if (null != levelId && levelId > 0) {
            sql.append(" and  rel.level_id=").append(levelId).append(" ");
        }
        if (null != status) {
            sql.append(" and ca.status =").append(status).append(" ");
        }

        sql.append(" ORDER BY ca.data_index desc,ca.category_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadCategoryListMap(Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT category_id,category_name,category_code,concat('" + Constants.IMG_WEB_URL + "',category_img) as category_img from tb_category where 1=1 ");
        if (null != status) {
            sql.append(" and `status` =1 ");
        }
        sql.append(" ORDER BY data_index desc,category_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadLevelListMap(Integer status, Integer categoryTyp) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT level_id,level_code,level_name,concat('" + Constants.IMG_WEB_URL + "',level_img) as level_img,category_type from tb_category_level where 1=1 ");
        if (null != status) {
            sql.append(" and `status` =1 ");
        }
        if (null != categoryTyp) {
            sql.append(" and `category_type` =").append(categoryTyp).append(" ");
        }
        sql.append(" ORDER BY data_index desc,level_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadGoodsAttr(int goodsId) {
        String sql = " SELECT attr_id,goods_id,prop_id,value_id from tb_goods_attribute where goods_id=? ";
        return iGeneralDao.getBySQLListMap(sql);
    }

    @Override
    public List<Map<String, Object>> loadCategoryAllRelListMap(Integer categoryId, Integer caStatus, Integer itStatus, Integer pvStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        //类目
        sql.append(" ca.category_id as ca_category_id,ca.category_name as ca_category_name,ca.is_parent as ca_is_parent,ca.data_index as ca_data_index,ca.oute_id as ca_oute_id,CONCAT('").append(Constants.IMG_WEB_URL).append("',ca.category_img) as ca_category_web_img,ca.category_img as ca_category_img,ca.category_code as ca_category_code,ca.is_show as ca_is_show,ca.status as ca_status,ca.remark as ca_remark ");
        //属性
        sql.append(" ,it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.category_id as it_category_id,it.oute_id as it_oute_id,it.is_sales as it_is_sales,it.show_view as it_show_view,CONCAT('").append(Constants.IMG_WEB_URL).append("',it.prop_img) as it_prop_web_img,it.prop_img as it_prop_img,it.prop_code as it_prop_code,it.is_show as it_is_show,it.data_index as it_data_index,it.status as it_status,it.remark as it_remark ");
        //属性值
        sql.append(" ,pv.value_id as pv_value_id,pv.prop_id as pv_prop_id,pv.value_name as pv_value_name,pv.category_id as pv_category_id,pv.oute_id as pv_oute_id,pv.value as pv_value,pv.data_index as pv_data_index,pv.status as pv_status,CONCAT('").append(Constants.IMG_WEB_URL).append("',pv.value_img) as pv_value_web_img,pv.value_img as  pv_value_img ");
        sql.append(" from tb_category ca ");
        sql.append(" LEFT JOIN tb_itemprops it on it.category_id=ca.category_id ");
        if (null != itStatus) {
            sql.append("  and it.`status`=").append(itStatus).append("  ");
        }
        sql.append(" LEFT JOIN tb_props_value pv on pv.category_id=ca.category_id ");
        if (pvStatus != null) {
            sql.append(" and pv.`status`=").append(pvStatus).append(" ");
        }
        sql.append(" where 1=1 ");
        if (null != caStatus) {
            sql.append(" and ca.`status`=").append(caStatus).append(" ");
        }
        if (null != categoryId) {
            sql.append(" and ca.category_id=").append(categoryId).append(" ");
        }
        sql.append(" ORDER BY ca.data_index desc,it.data_index desc,pv.data_index desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadCategoryItemRelListMap(Integer categoryId, Integer caStatus, Integer itStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        //类目
        sql.append(" ca.category_id as ca_category_id,ca.category_name as ca_category_name,ca.is_parent as ca_is_parent,ca.data_index as ca_data_index,ca.oute_id as ca_oute_id,CONCAT('").append(Constants.IMG_WEB_URL).append("',ca.category_img) as ca_category_web_img,ca.category_img as ca_category_img,ca.category_code as ca_category_code,ca.is_show as ca_is_show,ca.status as ca_status,ca.remark as ca_remark ");
        //属性
        sql.append(" ,it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.category_id as it_category_id,it.oute_id as it_oute_id,it.is_sales as it_is_sales,it.show_view as it_show_view,CONCAT('").append(Constants.IMG_WEB_URL).append("',it.prop_img) as it_prop_web_img,it.prop_img as it_prop_img,it.prop_code as it_prop_code,it.is_show as it_is_show,it.data_index as it_data_index,it.status as it_status,it.remark as it_remark ");
        sql.append(" from tb_category ca ");
        sql.append(" LEFT JOIN tb_itemprops it on it.category_id=ca.category_id ");
        if (null != itStatus) {
            sql.append("  and it.`status`=").append(itStatus).append("  ");
        }
        sql.append(" where 1=1 ");
        if (null != caStatus) {
            sql.append(" and ca.`status`=").append(caStatus).append(" ");
        }
        if (null != categoryId) {
            sql.append(" and ca.category_id=").append(categoryId).append(" ");
        }
        sql.append(" ORDER BY ca.data_index desc,it.data_index desc");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadItemAllRelListMap(Integer categoryId, Integer itStatus, Integer pvStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        //属性
        sql.append(" it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.category_id as it_category_id,it.oute_id as it_oute_id,it.is_sales as it_is_sales,it.show_view as it_show_view,CONCAT('").append(Constants.IMG_WEB_URL).append("',it.prop_img) as it_prop_web_img,it.prop_img as it_prop_img,it.prop_code as it_prop_code,it.is_show as it_is_show,it.data_index as it_data_index,it.status as it_status,it.remark as it_remark ");
        //属性值
        sql.append(" ,pv.value_id as pv_value_id,pv.prop_id as pv_prop_id,pv.value_name as pv_value_name,pv.category_id as pv_category_id,pv.oute_id as pv_oute_id,pv.value as pv_value,pv.data_index as pv_data_index,pv.status as pv_status,CONCAT('").append(Constants.IMG_WEB_URL).append("',pv.value_img) as pv_value_web_img,pv.value_img as pv_value_img ");
        sql.append(" from tb_itemprops it  ");
        sql.append(" LEFT JOIN tb_props_value pv on pv.prop_id=it.prop_id  ");
        if (pvStatus != null) {
            sql.append(" and pv.`status`=").append(pvStatus).append(" ");
        }
        sql.append(" where 1=1 ");
        if (null != categoryId) {
            sql.append(" and it.category_id=").append(categoryId).append(" ");
        }
        if (null != itStatus) {
            sql.append("  and it.`status`=").append(itStatus).append("  ");
        }
        sql.append(" ORDER BY it.data_index desc,pv.data_index desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadPropValueByCategoryId(int categoryId) {
        String sql = " SELECT prop_id,value_id,value_name from tb_props_value where status=1 and category_id=? ";
        return iGeneralDao.getBySQLListMap(sql, categoryId);
    }

    @Override
    public List<Map<String, Object>> loadItemPropListMap(Integer categoryId, Integer propId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT prop_id,prop_name,category_id,oute_id,is_sales,show_view,CONCAT('").append(Constants.IMG_WEB_URL).append("',prop_img) as prop_img,prop_code,is_show,data_index,`status`,remark,category_type from tb_itemprops ");
        sql.append(" where 1=1 ");
        if (null != categoryId && categoryId > 0) {
            sql.append(" and category_id=").append(categoryId).append(" ");
        }
        if (null != propId && propId > 0) {
            sql.append(" and prop_id=").append(propId).append(" ");
        }
        if (null != status) {
            sql.append(" and status=").append(status).append(" ");
        }
        sql.append(" ORDER BY data_index desc,prop_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadItemPropValueListMap(Integer categoryId, Integer propId, Integer valueId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT value_id, prop_id, value_name, category_id, oute_id, value, data_index,`status`,CONCAT('").append(Constants.IMG_WEB_URL).append("', value_img) ");
        sql.append(" as value_img from tb_props_value ");
        sql.append(" where 1 = 1 ");
        if (null != valueId && valueId > 0) {
            sql.append(" and value_id=").append(valueId).append(" ");
        }
        if (null != propId && propId > 0) {
            sql.append(" and prop_id=").append(propId).append(" ");
        }
        if (null != categoryId && categoryId > 0) {
            sql.append(" and category_id=").append(categoryId).append(" ");
        }
        if (null != status) {
            sql.append(" and status=").append(status).append(" ");
        }
        sql.append(" ORDER BY data_index desc, value_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadLeveCategoryRel(Integer levelId, Integer categoryId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT level_id,category_id from tb_level_category_rel where 1=1 ");
        if (null != levelId) {
            sql.append(" and level_id=").append(levelId).append(" ");
        }
        if (null != categoryId) {
            sql.append(" and category_id=").append(categoryId).append(" ");
        }
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadLeveCategoryRel(String levelIds, String categoryIds) {
        if(levelIds.endsWith(",")){
            levelIds = levelIds.substring(0,levelIds.length()-1);
        }
        if(null!=categoryIds && categoryIds.endsWith(",")){
            categoryIds = categoryIds.substring(0,categoryIds.length()-1);
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT level_id,category_id from tb_level_category_rel where 1=1 ");
        if (StringUtils.isNotEmpty(levelIds)) {
            sql.append(" and level_id in (").append(levelIds).append(") ");
        }
        if (StringUtils.isNotEmpty(categoryIds)) {
            sql.append(" and category_id in (").append(categoryIds).append(") ");
        }
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public TbCategory loadTbCategoryById(int categoryId) {
        String sql = " from TbCategory where categoryId=?1 ";
        return iGeneralDao.queryUniqueByHql(sql, categoryId);
    }


    @Override
    public TbItemprops loadTbItempropsById(int propId) {
        String sql = " from TbItemprops where propId=?1 ";
        return iGeneralDao.queryUniqueByHql(sql, propId);
    }

    @Override
    public Map<String, Object> loadCategoryMap(int categoryId) {
        String sql = " SELECT category_id,category_code,category_name,category_type,data_index,is_parent,oute_id,is_show,`status` from tb_category where category_id=? ";
        return iGeneralDao.getBySQLListMap(sql, categoryId).get(0);
    }

    @Override
    public List<Map<String, Object>> loadPropValueItem(String valueIds) {
        if(valueIds.endsWith(",")){
            valueIds = valueIds.substring(0,valueIds.length()-1);
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT pv.value_id,it.prop_name,pv.value_name ");
        sql.append(" from tb_props_value pv JOIN tb_itemprops it on it.prop_id = pv.prop_id ");
        sql.append(" where pv.`status`=1 and it.`status`=1 ");
        sql.append(" and pv.value_id in (").append(valueIds).append(") ");
        sql.append(" ORDER BY it.data_index DESC,it.prop_id desc,pv.data_index DESC,pv.value_id DESC ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> findMainMenu(int menuType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id as menuId, menu_name as menuName,menu_url as menuUrl,leve1_ids as leve1Ids,category_ids  as categoryIds, ");
        sql.append(" CONCAT('").append(Constants.IMG_WEB_URL).append("',menu_img) as menuImg,rel_type as relType ");
        sql.append(" from tb_page_menu m ");
        sql.append(" where menu_type = " + menuType + " and status=1 ");
        sql.append(" order by menu_sort desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
