package com.yufan.kc.timetask;

import com.yufan.kc.dao.timetask.TimeTaskDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/20 9:41
 * 功能介绍: 定时更新商品库存
 */
@Configuration
@EnableScheduling
public class UpdateGoodsStore {

    private Logger LOG = Logger.getLogger(UpdateGoodsStore.class);

    @Autowired
    private TimeTaskDao timeTaskDao;

//    @Scheduled(cron = "0 33 22 * * ?")
    public void updateGoodsStore() {
        long st = System.currentTimeMillis();
        timeTaskDao.updateGoodsStore(null);
        long et = System.currentTimeMillis();
        LOG.info("---更新商品库存--用时-----=" + (et - st) + " ");
    }

}
