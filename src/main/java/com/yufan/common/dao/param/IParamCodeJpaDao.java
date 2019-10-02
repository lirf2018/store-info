package com.yufan.common.dao.param;

import com.yufan.pojo.TbParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/10/2 12:49
 * 功能介绍:
 */
@Transactional
@Repository
public interface IParamCodeJpaDao extends JpaRepository<TbParam, Integer> {

    @Query(value = "SELECT * from tb_param p where p.status=?1 ORDER BY p.param_id desc", nativeQuery = true)
    public List<TbParam> loadTbParamCodeList(int status);
}
