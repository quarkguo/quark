package com.ccg.common.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleMetaData {
	private Integer articleId;
	private String title;
	private String type;
	private String author;
	private String company;
	private String acceptStatus;
	private Float praisalscore;
	private String description;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	public String toString(){

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");		
		StringBuffer sb = new StringBuffer();
		sb.append("title: " + title).append("\n");
		sb.append("type: " + type).append("\n");
		sb.append("author: " + author).append("\n");
		sb.append("company: " + company).append("\n");
		sb.append("accept status: " + acceptStatus).append("\n");
		sb.append("praisal score: " + praisalscore).append("\n");
		sb.append("description: " + description).append("\n");
		if(createDate != null){
			sb.append("create date: " + format.format(createDate)).append("\n");	
		}
		if(lastUpdateDate != null){
			sb.append("last update date: " + format.format(lastUpdateDate)).append("\n");
		}
		
		System.out.println(sb.toString());
		
		return sb.toString();
	}
	
	public String toHTML(){
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head></head><body>");
		sb.append("<h1>" + title + "</h1>");
		sb.append("<b>Description: </b><br />" + description + "<br />");
		sb.append("<b>Type: </b>" + type + "<br />");
		sb.append("<b>Author: </b>" + author + "<br />");
		sb.append("<b>Company: </b>" + company + "<br />");
		sb.append("<b>Accept Status: </b>" + acceptStatus + "<br />");
		sb.append("<b>Praisal Score: </b>" + praisalscore + "<br />");
		sb.append("</body></htmm>");
		return sb.toString();
	}
}
