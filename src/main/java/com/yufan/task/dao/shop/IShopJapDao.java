package com.yufan.task.dao.shop;

import com.yufan.pojo.TbShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/25 15:30
 * 功能介绍:
 */
@Repository
@Transactional
public interface IShopJapDao extends JpaRepository<TbShop, Integer> {

    @Query(value = "select * from tb_shop s where s.shop_id=?1",nativeQuery = true)
    TbShop findOne(int shopId);
}
