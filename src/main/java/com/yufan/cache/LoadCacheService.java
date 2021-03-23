package com.yufan.cache;

import com.yufan.common.dao.param.IParamCodeJpaDao;
import com.yufan.pojo.TbParam;
import com.yufan.utils.CacheData;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/26 16:47
 * 功能介绍:
 */
@Component
public class LoadCacheService implements InitializingBean {

    private Logger LOG = Logger.getLogger(LoadCacheService.class);
    @Autowired
    private IParamCodeJpaDao iParamCodeJpaDao;

    ScheduledExecutorService  fixedThreadPool = Executors.newScheduledThreadPool(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        fixedThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                initParamList();
                initFileSavePath();
            }
        },0,20, TimeUnit.MINUTES);
    }
    /**
     * 初始化参数列表
     */
    private void initParamList() {
        LOG.info("-----开始初始华参数----");
        CacheData.PARAMLIST = iParamCodeJpaDao.loadTbParamCodeList(1);
        LOG.info("-----结束初始华参数----" + CacheData.PARAMLIST.size());
    }

    /**
     * 初始化文件本地保存路径和web访问路径
     */
    private void initFileSavePath() {
        LOG.info("-----开始初始化文件本地保存路径和web访问路径----");
        for (int i = 0; i < CacheData.PARAMLIST.size(); i++) {
            TbParam param = CacheData.PARAMLIST.get(i);
            if ("sys_code".equals(param.getParamCode()) && "img_web_path".equals(param.getParamKey())) {
                Constants.IMG_WEB_URL = param.getParamValue().endsWith("/") ? param.getParamValue() : param.getParamValue() + "/";
            }
        }
        LOG.info("--------Constants.IMG_WEB_URL------" + Constants.IMG_WEB_URL);
        LOG.info("-----结束初始化文件本地保存路径和web访问路径----");
    }

}
