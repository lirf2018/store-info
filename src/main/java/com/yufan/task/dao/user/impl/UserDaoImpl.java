package com.yufan.task.dao.user.impl;

import com.yufan.common.dao.base.IGeneralDao;
import com.yufan.pojo.TbPrivateCustom;
import com.yufan.task.dao.user.IUserDao;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 9:10
 * 功能介绍:
 */
@Repository
@Transactional
public class UserDaoImpl implements IUserDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public Integer userJifen(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SUM(jifen) as jifen   from tb_jifen where user_id=").append(userId).append(" and `status`=1 and pass_time>=NOW() ");
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        int count = 0;
        if (null != list && list.size() > 0) {
            count = Integer.parseInt(null == list.get(0).get("jifen") ? "0" : list.get(0).get("jifen").toString());
        }
        return count;
    }

    @Override
    public PageInfo loadUserPrivateList(int userId, int findType, Integer pageSize, Integer currePage) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select pc.id,pc.user_id,DATE_FORMAT(pc.pay_time,'%Y-%m-%d %T') as pay_time,pc.private_code,DATE_FORMAT(pc.reservation_time,'%Y-%m-%d %T') as reservation_time, ");
        sql.append(" pc.status,pc.contents,DATE_FORMAT(pc.get_time,'%Y-%m-%d') as get_time,pc.post_way,pc.is_yuyue,u.user_mobile,p1.param_value as post_way_name ");
        sql.append("  ,pc.flow_status,if(DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(pc.get_time,'%Y-%m-%d'),if(pc.`status`=1,0,1),1) as out_time_flag ");
        sql.append(" ,if(DATE_FORMAT(NOW(),'%Y-%m-%d')=DATE_FORMAT(pc.get_time,'%Y-%m-%d'),if(pc.`status`=1,1,0),0) as get_time_flag ");
        sql.append(" ,p2.param_value as flow_status_name,DATE_FORMAT(pc.update_time,'%Y-%m-%d %T') as update_time ");
        sql.append(" ,pc.goods_id,pc.goods_name,pc.get_time_str,pc.get_addr ");
        sql.append(" from tb_private_custom pc JOIN tb_user_info u on u.user_id=pc.user_id ");
        sql.append(" LEFT JOIN tb_param p1 on p1.param_code='post_way' and p1.param_key=pc.post_way ");
        sql.append(" LEFT JOIN tb_param p2 on p2.param_code='prepare_flow_status' and p2.param_key=pc.flow_status ");
        sql.append(" where  pc.user_id=").append(userId).append(" ");
        if (findType == 0) {
            sql.append(" and pc.status in (0,1)  ");
            sql.append(" ORDER BY DATE_FORMAT(NOW(),'%Y-%m-%d')=DATE_FORMAT(pc.get_time,'%Y-%m-%d') desc,pc.get_time ,pc.is_yuyue desc,pc.index_sort ");
        } else {
            sql.append(" and pc.`status`=2 ");
            sql.append(" ORDER BY pc.update_time desc  ");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize == null ? 20 : pageSize);
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void cancelUserPrivate(int userId, int id) {
        String sql = " update tb_private_custom set reservation_time=null,status=0,get_time=null,is_yuyue=0,get_time_str='' where id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, id, userId);
    }

    @Override
    public void updateUserPrivate(int userId, int id, String getDate, String getDateStr, int index) {
        String sql = " update tb_private_custom set reservation_time=now(),status=1,get_time=?,is_yuyue=1,yuyue_count=yuyue_count+1,get_time_str=?,index_sort=? where id=? and user_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, getDate, getDateStr, index, id, userId);
    }

    @Override
    public List<TbPrivateCustom> loadPrivateCustom(String privateCode) {
        String hql = " from TbPrivateCustom where privateCode = ?1 ";
        return (List<TbPrivateCustom>) iGeneralDao.queryListByHql(hql, privateCode);
    }
}
