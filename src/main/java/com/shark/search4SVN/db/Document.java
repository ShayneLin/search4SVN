package com.shark.search4SVN.db;

import java.util.Date;

/**
 * @Author lcs
 * @Description 文档类，对应数据库表
 * @Date Created in 16:10 2018/6/27
 */
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
        this.entityFlag = entityFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
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
        this.description = description;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", entityFlag='" + entityFlag + '\'' +
                ", name='" + name + '\'' +
                ", docUrl='" + docUrl + '\'' +
                ", modifyTime=" + modifyTime +
                ", description='" + description + '\'' +
                '}';
    }
}
