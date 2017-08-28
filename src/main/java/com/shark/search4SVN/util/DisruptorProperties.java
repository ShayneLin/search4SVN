package com.shark.search4SVN.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by qinghualiu on 2017/8/26.
 */
@Component
@ConfigurationProperties(prefix = "disruptor.rambuffer", locations = "classpath:config/disruptor.properties")
public class DisruptorProperties {
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
