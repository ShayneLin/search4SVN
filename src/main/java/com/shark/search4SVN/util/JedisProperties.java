package com.shark.search4SVN.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuqinghua on 2016/9/27.
 */

@Component
@ConfigurationProperties(prefix = "search4SVN.redis", locations = "classpath:config/redis.properties")
public class JedisProperties {
    private String url;
    private String db;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUrl() {
        return url;
    }

    public String getDb() {
        return db;
    }
}
