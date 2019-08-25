package com.yufan.dao.goods;

import com.yufan.pojo.TbGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 12:01
 * 功能介绍:
 */
@Transactional
@Repository
public interface IGoodsJpaDao extends JpaRepository<TbGoods, Integer> {
}
