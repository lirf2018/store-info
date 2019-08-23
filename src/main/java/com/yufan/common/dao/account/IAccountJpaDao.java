package com.yufan.common.dao.account;

import com.yufan.pojo.TbInfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/23 16:41
 * 功能介绍:
 */
@Repository
@Transactional
public interface IAccountJpaDao extends JpaRepository<TbInfAccount, Integer> {

    @Query("select t from TbInfAccount t where t.sid = :account and t.status = :status")
    TbInfAccount findInfAccountBySid(@Param("account") String account, @Param("status") Integer status);
}
