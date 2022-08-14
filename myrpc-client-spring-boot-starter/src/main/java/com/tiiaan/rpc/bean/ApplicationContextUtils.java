package com.tiiaan.rpc.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */


public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T autowire(T bean){
        applicationContext.getAutowireCapableBeanFactory()
                .autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, true);
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }


}
