package com.ccg.common.data;

import java.util.Date;

public class ArticleMetaData {
	private Integer articleId;
	private String title;
	private String type;
	private String author;
	private String company;
	private String acceptStatus;
	private Float praisalscore;
	
	private Date createDate;
	private Date lastUpdateDate;
	
	private boolean deleteArticle;

	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAcceptStatus() {
		return acceptStatus;
	}
	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public Float getPraisalscore() {
		return praisalscore;
	}
	public void setPraisalscore(Float praisalscore) {
		this.praisalscore = praisalscore;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public boolean isDeleteArticle() {
		return deleteArticle;
	}
	public void setDeleteArticle(boolean deleteArticle) {
		this.deleteArticle = deleteArticle;
	}	
	
	
}
