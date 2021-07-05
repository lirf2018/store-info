package com.yufan.task.dao.verify.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.task.dao.verify.IVerifyImgDao;
import com.yufan.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/7/1
 */
@Transactional
@Repository
public class VerifyImgDaoImpl implements IVerifyImgDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryVerifyImgGroup() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select g.verify_code,g.similar_type,g.verify_title,g.back_img from tb_verify_img_group g JOIN tb_verify_img v on v.verify_code=g.verify_code where g.`status`=1 and v.`status`=1 ");
        sql.append(" GROUP BY g.verify_code ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> queryVerifyImgListByCode(String verifyCode) {
        String sql = " SELECT verify_code,verify_img,verify_type,word,img_uuid,CONCAT('" + Constants.IMG_WEB_URL + "',verify_img) as verify_img_path from tb_verify_img where status = 1 and verify_code=? ";
        return iGeneralDao.getBySQLListMap(sql, verifyCode);
    }

    @Override
    public List<Map<String, Object>> queryVerifyImgListByUuids(String uuids, String verifyCode) {
        String sql = " SELECT verify_code,img_uuid from tb_verify_img where status = 1 and img_uuid in ('" + uuids + "') and verify_code=? ";
        return iGeneralDao.getBySQLListMap(sql, verifyCode);
    }

    @Override
    public List<Map<String, Object>> queryVerifyImgListUnInByCode(String verifyCode, Integer similarType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT verify_code,verify_img,verify_type,word,img_uuid,CONCAT('" + Constants.IMG_WEB_URL + "',verify_img) as verify_img_path from tb_verify_img ");
        sql.append("  where status = 1  ");
        sql.append(" and verify_code in (select verify_code from tb_verify_img_group where `status`=1 and verify_code != '").append(verifyCode).append("' and similar_type !=").append(similarType).append(") ");
        sql.append(" order by id desc ");
        sql.append(" limit 3000 ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }
}
