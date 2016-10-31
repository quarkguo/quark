package com.ccg.common.data;

public class ArticleContent {
	
	private Integer contentID;
	private String contentTitle;
	private String fileName;
	private String url;
	private String metatype;
	private String content;
	private Integer length;
	
	public Integer getContentID() {
		return contentID;
	}
	public void setContentID(Integer contentID) {
		this.contentID = contentID;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMetatype() {
		return metatype;
	}
	public void setMetatype(String metatype) {
		this.metatype = metatype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
}
