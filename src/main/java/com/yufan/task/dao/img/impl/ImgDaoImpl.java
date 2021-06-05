package com.yufan.task.dao.img.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.img.IImgDao;
import com.yufan.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 13:09
 * 功能介绍:
 */
@Transactional
@Repository
public class ImgDaoImpl implements IImgDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryTableRelImg(Integer imgType, int relateId, Integer classifyType) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT img_id,img_url,img_type,img_classify,img_sort,CONCAT('" + Constants.IMG_WEB_URL + "',img_url) as img_url2 from tb_img ");
        sql.append(" where status=1 and relate_id=").append(relateId).append(" ");
        if (null != imgType) {
            sql.append(" and img_type=").append(imgType).append(" ");
        }
        if (null != classifyType) {
            sql.append(" and img_classify=").append(classifyType).append(" ");
        }
        sql.append(" ORDER BY img_sort desc,createtime DESC ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public int saveImg(Object img) {
        return iGeneralDao.save(img);
    }


}
