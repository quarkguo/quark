package com.ccg.dataaccess.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	private String type;
	private int startposi;
	private int endposi;
	private String categorytitle;  // introduction etc
	private String categoryref; //  for example 1.
	private String categorycontent;
	private int categoryseq;
	
	@Column(name = "startpage")
	public Integer getStartpage() {
		return startpage;
	}
	public void setStartpage(Integer startpage) {
		this.startpage = startpage;
	}
	
	@Column(name = "endpage")
	public Integer getEndpage() {
		return endpage;
	}
	public void setEndpage(Integer endpage) {
		this.endpage = endpage;
	}

	private Integer startpage;
	private Integer endpage;
	
	// mappings
	private CCGArticle article;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="articleID")
	public CCGArticle getArticle() {
		return article;
	}
	public void setArticle(CCGArticle article) {
		this.article = article;
	}
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL,mappedBy="category")
	public List<CCGSubcategory> getSubcategorylist() {
		return subcategorylist;
	}
	public void setSubcategorylist(List<CCGSubcategory> subcategorylist) {
		this.subcategorylist = subcategorylist;
	}

	private List<CCGSubcategory> subcategorylist=new ArrayList<CCGSubcategory>();

}
