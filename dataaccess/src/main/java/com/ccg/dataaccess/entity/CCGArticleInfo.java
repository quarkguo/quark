package com.ccg.dataaccess.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="ccgarticleinfo")
public class CCGArticleInfo implements Serializable {

	private static final long serialVersionUID = -2858227398319004554L;
	@Id	
	@Column(name = "articleID", unique = true)
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	
	@Column(name = "metadata")
	public String getMetaData() {
		return metaData;
	}
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}
	@Column(name = "createdTS")
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	@Column(name = "toc")
	public String getToc() {
		return toc;
	}
	public void setToc(String toc) {
		this.toc = toc;
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
		CCGArticleInfo other = (CCGArticleInfo) obj;
		if (articleID == null) {
			if (other.articleID != null)
				return false;
		} else if (!articleID.equals(other.articleID))
			return false;
		return true;
	}
	private Integer articleID;
	private String metaData;
	private Date createdTS;
	private String toc;  // here store the json object for 
}
