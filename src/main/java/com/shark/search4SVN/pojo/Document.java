package com.shark.search4SVN.pojo;

import java.util.Date;

public class Document {
    private Integer id;//主键
    private String entityFlag;//实体唯一标识
    private String name;//文档名字
    private String docUrl;//文档路径
    private Date modifyTime;//文档最近修改时间
    private String description;//描述，保留

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityFlag() {
        return entityFlag;
    }

    public void setEntityFlag(String entityFlag) {
        this.entityFlag = entityFlag == null ? null : entityFlag.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl == null ? null : docUrl.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}