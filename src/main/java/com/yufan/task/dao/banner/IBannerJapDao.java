package com.yufan.task.dao.banner;

import com.yufan.pojo.TbBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/26 14:51
 * 功能介绍:
 */
@Repository
@Transactional
public interface IBannerJapDao extends JpaRepository<TbBanner,Integer> {
}
