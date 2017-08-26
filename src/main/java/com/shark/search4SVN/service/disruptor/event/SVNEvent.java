package com.shark.search4SVN.service.disruptor.event;

import com.shark.search4SVN.pojo.SVNDocument;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class SVNEvent {

    private Integer type; //1: SVN 目录， 2：SVN文件， 3。Solr 文档
    private String url; //1和2时使用
    private SVNDocument document; //3时使用

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SVNDocument getDocument() {
        return document;
    }

    public void setDocument(SVNDocument document) {
        this.document = document;
    }
}
