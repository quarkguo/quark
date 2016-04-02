package com.ccg.dataaccess.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ccgcategory")
public class CCGCategory implements Serializable {

	private static final long serialVersionUID = -8424915265747552093L;
	
	@Id	
	@Column(name = "categoryID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getCategoryID() {
		return categoryID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryID == null) ? 0 : categoryID.hashCode());
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
		CCGCategory other = (CCGCategory) obj;
		if (categoryID == null) {
			if (other.categoryID != null)
				return false;
		} else if (!categoryID.equals(other.categoryID))
			return false;
		return true;
	}
	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}
	
	@Column(name = "articleID")
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	@Column(name = "categorytitle")
	public String getCategorytitle() {
		return categorytitle;
	}
	public void setCategorytitle(String categorytitle) {
		this.categorytitle = categorytitle;
	}
	@Column(name = "categoryref")
	public String getCategoryref() {
		return categoryref;
	}
	public void setCategoryref(String categoryref) {
		this.categoryref = categoryref;
	}
	@Column(name = "categorycontent",columnDefinition="mediumtext")
	public String getCategorycontent() {
		return categorycontent;
	}
	public void setCategorycontent(String categorycontent) {
		this.categorycontent = categorycontent;
	}
	@Column(name = "categoryseq")
	public int getCategoryseq() {
		return categoryseq;
	}
	public void setCategoryseq(int categoryseq) {
		this.categoryseq = categoryseq;
	}
	private Integer categoryID;
	private Integer articleID;
	private String type;
	private int startposi;
	private int endposi;
	private String categorytitle;  // introduction etc
	private String categoryref; //  for example 1.
	private String categorycontent;
	private int categoryseq;
	
	// mappings
	private CCGArticle article;
	private List<CCGSubcategory> subcategorylist;

}
