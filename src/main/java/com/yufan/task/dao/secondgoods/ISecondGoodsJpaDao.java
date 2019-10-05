package com.yufan.task.dao.secondgoods;

import com.yufan.pojo.TbSecondGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/31 20:32
 * 功能介绍:
 */
@Transactional
@Repository
public interface ISecondGoodsJpaDao extends JpaRepository<TbSecondGoods, Integer> {

    @Query(value = "select * from tb_second_goods g where g.goods_id=?1", nativeQuery = true)
    TbSecondGoods findOne(int id);
}
