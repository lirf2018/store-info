package com.yufan.task.dao.user;

import com.yufan.pojo.TbUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 9:26
 * 功能介绍:
 */
@Repository
@Transactional
public interface IUserJpaDao extends JpaRepository<TbUserInfo, Integer> {
}
