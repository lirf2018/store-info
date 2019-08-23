package com.yufan.common.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ServiceFactory implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public static IResultOut getService(String serviceName) {
        return (IResultOut) context.getBean(serviceName);
    }
}
