package com.yufan.controller;

import com.yufan.cache.ILoadCacheService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @description:
 * @author: lirf
 * @time: 2021/7/3
 */
@Controller
@RequestMapping("/refresh/")
public class RefreshCacheController {

    private Logger LOG = Logger.getLogger(RefreshCacheController.class);

    @Autowired
    private ILoadCacheService iLoadCacheService;

    @RequestMapping("cache")
    public void refreshCache(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter pw = response.getWriter();
            iLoadCacheService.initCache();
            pw.write(1);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
