package com.shark.search4SVN.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by liuqinghua on 16-9-13.
 */
@Service
public class Search4SVNContext {
    private static ApplicationContext applicationContext;

    public static Object getBean(String name){
        if(applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name);
    }
    public static Object getBean(Class clazz){
        if(applicationContext == null){
             return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Search4SVNContext.applicationContext = applicationContext;
    }
}
