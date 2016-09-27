package com.shark.search4SVN.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuqinghua on 2016/9/27.
 */

@Component
@ConfigurationProperties(prefix = "search4SVN.solr", locations = "classpath:config/solr.properties")
public class SolrConnProperties {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
