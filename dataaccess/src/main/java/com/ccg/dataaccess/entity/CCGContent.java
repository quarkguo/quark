package com.ccg.dataaccess.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="ccgcontent")
public class CCGContent implements Serializable {
	
	private static final long serialVersionUID = -5124183600211806973L;
	
	@Id	
	@Column(name = "contentID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
		public Integer getContentID() {
		return contentID;
	}
	public void setContentID(Integer contentID) {
		this.contentID = contentID;
	}
	@Column(name = "contentTitle")
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	
	@Column(name = "length")
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	@Column(name = "filename")
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "Metatype")
	public String getMetatype() {
		return metatype;
	}
	public void setMetatype(String metatype) {
		this.metatype = metatype;
	}
	
	@Column(name = "content",columnDefinition="mediumtext")
		public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private Integer contentID;
	private String contentTitle;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contentID;
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
		CCGContent other = (CCGContent) obj;
		if (contentID != other.contentID)
			return false;
		return true;
	}
	private int length;
	private String filename;
	private String url;
	private String metatype;
	// this is the medium text lob
	private String content;
	
}
