package com.ccg.common.data;

import java.sql.Date;

public class ArticleMetaData {
	private String title;
	private String type;
	private String author;
	private String company;
	private String acceptStatus;
	private float praisalscore;
	
	private Date date;
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getPraisalscore() {
		return praisalscore;
	}
	public void setPraisalscore(float praisalscore) {
		this.praisalscore = praisalscore;
	}		
}
