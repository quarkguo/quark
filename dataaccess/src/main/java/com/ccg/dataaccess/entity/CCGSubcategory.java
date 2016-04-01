package com.ccg.dataaccess.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ccgsubcategory")
public class CCGSubcategory implements Serializable {

	private static final long serialVersionUID = -6716614221069232084L;
	
	@Id	
	@Column(name = "subcategoryID", unique = true)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getSubcategoryID() {
		return subcategoryID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subcategoryID == null) ? 0 : subcategoryID.hashCode());
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
		CCGSubcategory other = (CCGSubcategory) obj;
		if (subcategoryID == null) {
			if (other.subcategoryID != null)
				return false;
		} else if (!subcategoryID.equals(other.subcategoryID))
			return false;
		return true;
	}
	public void setSubcategoryID(Integer subcategoryID) {
		this.subcategoryID = subcategoryID;
	}
	@Column(name = "articleID")
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	@Column(name = "categoryID")
	public Integer getCateogryID() {
		return cateogryID;
	}
	public void setCateogryID(Integer cateogryID) {
		this.cateogryID = cateogryID;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "subcategorytitle")
	public String getSubcategorytitle() {
		return subcategorytitle;
	}
	public void setSubcategorytitle(String subcategorytitle) {
		this.subcategorytitle = subcategorytitle;
	}
	@Column(name = "startposi")
	public int getStartposi() {
		return startposi;
	}
	public void setStartposi(int startposi) {
		this.startposi = startposi;
	}
	@Column(name = "endposi")
	public int getEndposi() {
		return endposi;
	}
	public void setEndposi(int endposi) {
		this.endposi = endposi;
	}
	@Column(name = "createTS")
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	@Column(name = "lastupdateTS")
	public Date getLastupdateTS() {
		return lastupdateTS;
	}
	public void setLastupdateTS(Date lastupdateTS) {
		this.lastupdateTS = lastupdateTS;
	}
	private Integer subcategoryID;
	private Integer articleID;
	private Integer cateogryID;
	private String type;
	private String subcategorytitle;
	private int startposi;
	private int endposi;
	private Date createdTS;
	private Date lastupdateTS;
}
