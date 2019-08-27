package com.yufan.task.dao.activity;

import com.yufan.pojo.TbActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 14:54
 * 功能介绍:
 */
@Repository
@Transactional
public interface IActivityJpaDao extends JpaRepository<TbActivity,Integer> {
}
