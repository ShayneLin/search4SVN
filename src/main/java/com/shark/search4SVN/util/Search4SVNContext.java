package com.shark.search4SVN.util;

import org.springframework.context.ApplicationContext;

/**
 * Created by liuqinghua on 16-9-13.
 */
public class Search4SVNContext {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

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
}
