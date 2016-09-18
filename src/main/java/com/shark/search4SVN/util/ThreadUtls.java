package com.shark.search4SVN.util;

import org.apache.log4j.Logger;

/**
 * Created by liuqinghua on 16-9-10.
 */
public class ThreadUtls {

    private static Logger logger = Logger.getLogger(ThreadUtls.class);

    public static void sleep(long num){
        try {
            Thread.currentThread().sleep(num);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
