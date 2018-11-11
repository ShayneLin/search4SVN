package com.shark.search4SVN.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lcs
 * @description 获取容器中的Bean
 */
//初始化applicationContext
@Component
public class ApplicationContextUtil implements ApplicationContextAware, InitializingBean {
    private static ApplicationContext applicationContext;
    private static ApplicationContextUtil applicationContextUtil;

    private ApplicationContextUtil() {
    }

    public static ApplicationContextUtil getInstace() {

        if (applicationContextUtil == null) {
            synchronized (ApplicationContextUtil.class) {
                if (applicationContextUtil == null) {
                    applicationContextUtil = new ApplicationContextUtil();
                }
            }

        }
        return applicationContextUtil;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public Object getBean(String beanName) {
        return applicationContext != null ? applicationContext.getBean(beanName) : null;
    }


    public Object getBean(Class type) {
        return applicationContext.getBean(type);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        /*初始化单例*/
        ApplicationContextUtil.applicationContextUtil = this;
    }
}
