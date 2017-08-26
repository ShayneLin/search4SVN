package com.shark.search4SVN.pojo;

import java.util.Date;

public class SVNDocument {
	private String docName; //文件名称，唯一属性，下次更新索引的话，以名字为标准更新（存储且检索）
	private String svnUrl; //SVN_URL 全路径，便于搜索（存储，但不检索）
	private String content; //文档内容（推荐:检索但不存储)
	private Date lastModifyTime; //上一次更新时间（存储且不检索）
	private String lastModifyAuthor; //最后修改的作者（存储且检索）
	private String revision;// SVN的 revision （存储且不检索）
	private String mimeType;//文件类型，（存储但不检索）
	
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getLastModifyAuthor() {
		return lastModifyAuthor;
	}
	public void setLastModifyAuthor(String lastModifyAuthor) {
		this.lastModifyAuthor = lastModifyAuthor;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType() {
		return mimeType;
	}

	@Override
	public String toString() {
		return "SVNDocument{" +
				"docName='" + docName + '\'' +
				", svnUrl='" + svnUrl + '\'' +
				", lastModifyTime=" + lastModifyTime +
				", lastModifyAuthor='" + lastModifyAuthor + '\'' +
				", revision='" + revision + '\'' +
				", mimeType='" + mimeType + '\'' +
				'}';
	}
}
