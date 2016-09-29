package com.ccg.dataaccess.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ccgarticlemetadata")
public class CCGArticleMetadata implements Serializable {

	private static final long serialVersionUID = 1825799943730662272L;
	@Id	
	@Column(name = "articleID", unique = true)
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articleID == null) ? 0 : articleID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CCGArticleMetadata other = (CCGArticleMetadata) obj;
		if (articleID == null) {
			if (other.articleID != null)
				return false;
		} else if (!articleID.equals(other.articleID))
			return false;
		return true;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "company")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	@Column(name = "author")
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	@Column(name = "createdTS")
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	@Column(name = "lastupdateTS")
	public Date getLastUpdateTS() {
		return lastUpdateTS;
	}
	public void setLastUpdateTS(Date lastUpdateTS) {
		this.lastUpdateTS = lastUpdateTS;
	}
	
	@Column(name = "acceptstatus")
	public String getAcceptStatus() {
		return acceptStatus;
	}
	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}
	@Column(name = "praisalscore")
	public float getPraisalscore() {
		return praisalscore;
	}
	public void setPraisalscore(float praisalscore) {
		this.praisalscore = praisalscore;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	private Integer articleID;
	private String type;
	private String title;
	private String company;
	private String Author;
	private Date createdTS;
	private Date lastUpdateTS;
	private String acceptStatus;
	private float praisalscore;
	private String description;
	
}
