package com.yufan.kc.timetask;

import com.yufan.kc.dao.timetask.TimeTaskDao;
import com.yufan.utils.DatetimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/20 12:53
 * 功能介绍: 订单报表
 */
@Configuration
@EnableScheduling
public class OrderReport {

    private Logger LOG = Logger.getLogger(OrderReport.class);

    @Autowired
    private TimeTaskDao timeTaskDao;

//    @Scheduled(cron = "0 49 0 * * ?")
//    @Scheduled(cron = "0 33 22 * * ?")
    public void orderSaleReport() {
        long st = System.currentTimeMillis();
        String year = DatetimeUtil.getNow("yyyy");
        timeTaskDao.deleteAndInitOrderReport(year);
        timeTaskDao.orderSaleReport(year);
        timeTaskDao.goodsInPriceAllOrder(year);
        long et = System.currentTimeMillis();
        LOG.info("---更新订单报表--用时-----=" + (et - st) + " ");
    }
}
