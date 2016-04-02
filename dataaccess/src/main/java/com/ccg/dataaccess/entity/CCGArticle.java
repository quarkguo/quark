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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NamedQueries({
 @NamedQuery(name="CCGArticle.findAll", query="select a from CCGArticle a"),
 @NamedQuery(name="CCGArticle.countAll", query="select count(*) from CCGArticle")
})
@Entity
@Table(name="ccgarticle")
public class CCGArticle implements Serializable{
	
	private static final long serialVersionUID = 5714871082795314674L;
	@Id	
	@Column(name = "articleID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
		CCGArticle other = (CCGArticle) obj;
		if (articleID == null) {
			if (other.articleID != null)
				return false;
		} else if (!articleID.equals(other.articleID))
			return false;
		return true;
	}
	
	@Column(name = "domain", nullable = false)
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Column(name = "subdomain", nullable = false)
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	
	@Column(name = "title", nullable = true)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "startposi")
	public int getStartPosi() {
		return startPosi;
	}
	public void setStartPosi(int startPosi) {
		this.startPosi = startPosi;
	}
	
	@Column(name = "endposi")
	public int getEndPosi() {
		return endPosi;
	}
	public void setEndPosi(int endPosi) {
		this.endPosi = endPosi;
	}
	
	@Column(name = "articleType")
	public String getArticleType() {
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	
	@Column(name = "rfpReference")
	public Integer getRfpReference() {
		return rfpReference;
	}
	public void setRfpReference(Integer rfpReference) {
		this.rfpReference = rfpReference;
	}
	
	private Integer articleID;
	private String domain;
	private String subdomain;
//	private Integer contentID;
	private String title;
	private int startPosi=-1;
	private int endPosi=-1;
	private String articleType;
	private Integer rfpReference;
	
	// mappings
	private CCGContent content;
	private List<CCGCategory> categorylist=new ArrayList<CCGCategory>();
	private CCGArticleMetadata metadata;

	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="articleID")
	public CCGArticleMetadata getMetadata() {
		return metadata;
	}
	public void setMetadata(CCGArticleMetadata metadata) {
		this.metadata = metadata;
	}
	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="contentID")
	public CCGContent getContent() {
		return content;
	}
	public void setContent(CCGContent content) {
		this.content = content;
	}
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL,mappedBy="article")
	public List<CCGCategory> getCategorylist() {
		return categorylist;
	}
	public void setCategorylist(List<CCGCategory> categorylist) {
		this.categorylist = categorylist;
	}
	
}
