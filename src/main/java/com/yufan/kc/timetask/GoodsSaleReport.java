package com.yufan.kc.timetask;

import com.yufan.kc.dao.timetask.TimeTaskDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/12/20 12:52
 * 功能介绍: 商品销售报表
 */
@Configuration
@EnableScheduling
public class GoodsSaleReport {
    private Logger LOG = Logger.getLogger(GoodsSaleReport.class);

    @Autowired
    private TimeTaskDao timeTaskDao;

//    @Scheduled(cron = "0 40 22 * * ?")
    public void goodsStoreInPriceAll() {
        long st = System.currentTimeMillis();
        timeTaskDao.deleteGoodsSaleReport(null);
        List<Map<String, Object>> monthList = timeTaskDao.findOrderPayDateMonth();
        for (int i = 0; i < monthList.size(); i++) {
            if (null == monthList.get(i).get("pay_month")) {
                continue;
            }
            String month = monthList.get(i).get("pay_month").toString();
            timeTaskDao.goodsSaleReport(month);
            timeTaskDao.goodsStoreInPriceAll(month);
        }
        long et = System.currentTimeMillis();
        LOG.info("---更新商品销售报表--用时-----=" + (et - st) + " ");
    }
}
