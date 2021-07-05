package com.yufan.cache;

import com.yufan.common.dao.param.IParamCodeJpaDao;
import com.yufan.pojo.TbParam;
import com.yufan.task.dao.verify.IVerifyImgDao;
import com.yufan.testRedis.ClearRedis;
import com.yufan.utils.CacheData;
import com.yufan.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/26 16:47
 * 功能介绍:
 */
@Component
public class LoadCacheService implements InitializingBean, ILoadCacheService {

    private Logger LOG = Logger.getLogger(LoadCacheService.class);

    @Autowired
    private IParamCodeJpaDao iParamCodeJpaDao;

    @Autowired
    private IVerifyImgDao iVerifyImgDao;

    ScheduledExecutorService fixedThreadPool = Executors.newScheduledThreadPool(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        fixedThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                initCache();
            }
        }, 0, 20, TimeUnit.MINUTES);
    }

    @Override
    public void initCache() {
        initParamList();
        initFileSavePath();
        initVerifyImgGroup();
        ClearRedis.getInstance().clearRedisMinutes();
    }

    /**
     * 初始化参数列表
     */
    private void initParamList() {
        LOG.info("-----开始初始化参数----");
        CacheData.PARAMLIST = iParamCodeJpaDao.loadTbParamCodeList(1);
        LOG.info("-----结束初始化参数----" + CacheData.PARAMLIST.size());
    }

    /**
     * 初始化文件本地保存路径和web访问路径
     */
    private void initFileSavePath() {
        LOG.info("-----开始初始化文件本地保存路径和web访问路径----");
        for (int i = 0; i < CacheData.PARAMLIST.size(); i++) {
            TbParam param = CacheData.PARAMLIST.get(i);
            if ("sys_code".equals(param.getParamCode()) && "img_save_root_path".equals(param.getParamKey())) {
                Constants.IMG_SAVE_ROOT_PATH = param.getParamValue().endsWith("\\") ? param.getParamValue() : param.getParamValue() + "\\";
            } else if ("sys_code".equals(param.getParamCode()) && "img_web_path".equals(param.getParamKey())) {
                Constants.IMG_WEB_URL = param.getParamValue().endsWith("/") ? param.getParamValue() : param.getParamValue() + "/";
            }
        }
        LOG.info("-------Constants.IMG_SAVE_ROOT_PATH-------" + Constants.IMG_SAVE_ROOT_PATH);
        LOG.info("--------Constants.IMG_WEB_URL------" + Constants.IMG_WEB_URL);
        LOG.info("-----结束初始化文件本地保存路径和web访问路径----");
    }

    private void initVerifyImgGroup() {
        LOG.info("-----开始初始化校验图片分组----");
        List<Map<String, Object>> list = iVerifyImgDao.queryVerifyImgGroup();
        CacheData.VERIFYIMGGROUPLIST = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object img = list.get(i).get("back_img");
            CacheData.VERIFYIMGGROUPLIST.add(list.get(i));
            if (null != img && !"".equals(img.toString())) {
                CacheData.VERIFYIMGCODEMAPIMGLIST.add(img.toString());
            }
        }
        LOG.info("-----结束初始化校验图片分组----" + CacheData.PARAMLIST.size());
    }

}
