package com.yufan.common.dao.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.utils.PageInfo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作类
 */
@Repository
public class GeneralDaoImpl implements IGeneralDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(org.hibernate.Session.class);
    }

    @Override
    public PageInfo loadPageInfoSQLList(PageInfo pageInfo) {
        String sqlQuery = pageInfo.getSqlQuery();
        int currePage = pageInfo.getCurrePage();//查询页
        int pageSize = pageInfo.getPageSize();
//        System.out.println("------------------------>查询页:"+currePage);
        // 分页查询结果
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setFirstResult((currePage - 1) * pageSize);//从第一条记录开始  mysql是从0开始
        query.setMaxResults(pageSize);
        // 查询结果总条数
        int total = 0;
        String sqlCount = "SELECT COUNT(*) FROM (" + sqlQuery + ") AS COU";
        if (!StringUtils.isEmpty(pageInfo.getSqlCount())) {
            sqlCount = pageInfo.getSqlCount();
        }
        Query countQuery = entityManager.createNativeQuery(sqlCount);
        List listCount = countQuery.getResultList();
        total = listCount.size() > 0 ? Integer.parseInt(listCount.get(0).toString()) : 0;
        pageInfo.setRecordSum(total);
        List list = query.getResultList();
        pageInfo.setResultList(list);
        return account(pageInfo);
    }

    /**
     * 计算分页数据
     */
    private static PageInfo account(PageInfo pi) {
        /**
         * 计算分页导航信息
         */
        //计算总页数
        pi.setPageSum((pi.getRecordSum() % pi.getPageSize() == 0) ? pi.getRecordSum() / pi.getPageSize() : pi.getRecordSum() / pi.getPageSize() + 1);
        if (pi.getPageSum() == 0) pi.setPageSum(1);
        //计算当前页码数
        if (pi.getCurrePage() < 1) pi.setCurrePage(1);
        else if (pi.getCurrePage() > pi.getPageSum()) pi.setCurrePage(pi.getPageSum());
        //计算当前页的记录数量
        pi.setCurrePageRecord(pi.getRecordSum() - (pi.getPageSize() * (pi.getCurrePage() - 1)));
        if (pi.getCurrePageRecord() > pi.getPageSize()) pi.setCurrePageRecord(pi.getPageSize());
        if (pi.getCurrePageRecord() < 0) pi.setCurrePageRecord(0);
        pi.setFirstRecord(pi.getPageSize() * (pi.getCurrePage() - 1));
        //计算是否有下一页,上一页
        pi.setHasNext((pi.getCurrePage() < pi.getPageSum()) ? true : false);
        pi.setHasPrevious((pi.getCurrePage() > 1) ? true : false);
        if (pi.getRecordSum() == 0) {//如果没有记录
            pi.setResultList(new ArrayList());
        }
        return pi;
    }

    @Override
    public List<Map<String, Object>> getBySQLListMap(String sql, Object... values) {
        Session session = getSession();
        try {
            NativeQuery query = session.createSQLQuery(sql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    query.setParameter((i + 1), values[i]);
                }
            }
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        } finally {
//            session.close();
        }
    }

    @Override
    public PageInfo loadPageInfoSQLListMap(PageInfo pageInfo) {
        Session session = getSession();
        try {
            String sqlQuery = pageInfo.getSqlQuery();
            int currePage = pageInfo.getCurrePage();//查询页
            int pageSize = pageInfo.getPageSize();
            // 分页查询结果
            NativeQuery query = session.createNativeQuery(sqlQuery);
            query.setFirstResult((currePage - 1) * pageSize);//从第一条记录开始  mysql是从0开始
            query.setMaxResults(pageSize);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            // 查询结果总条数
            int total = 0;
            String sqlCount = "SELECT COUNT(*) FROM (" + sqlQuery + ") AS COU";
            if (!StringUtils.isEmpty(pageInfo.getSqlCount())) {
                sqlCount = pageInfo.getSqlCount();
            }
            NativeQuery countQuery = session.createNativeQuery(sqlCount);
            List listCount = countQuery.getResultList();
            total = listCount.size() > 0 ? Integer.parseInt(listCount.get(0).toString()) : 0;
            pageInfo.setRecordSum(total);
            List<Map<String, Object>> listMap = query.getResultList();
            pageInfo.setResultListMap(listMap);
            return account(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
//            session.close();
        }
    }

    @Override
    public int executeUpdateForSQL(String sql, Object... values) {
        Session session = getSession();
        NativeQuery query = session.createNativeQuery(sql);
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter((i + 1), values[i]);
            }
        }
        return query.executeUpdate();
    }

    @Override
    public <T> T queryUniqueByHql(String hql, Object... values) {
        Session session = getSession();
        org.hibernate.query.Query query = session.createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter((i + 1), values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    @Override
    public int save(Object entity) {
        Session session = getSession();
        return (int) session.save(entity);
    }

    @Override
    public void saveOrUpdate(Object entity) {
        Session session = getSession();
        session.saveOrUpdate(entity);
    }
}

