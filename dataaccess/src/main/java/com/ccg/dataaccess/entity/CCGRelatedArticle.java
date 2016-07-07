package com.ccg.dataaccess.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="ccgrelatedarticle")
public class CCGRelatedArticle implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relatedArticleSeq == null) ? 0 : relatedArticleSeq.hashCode());
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
		CCGRelatedArticle other = (CCGRelatedArticle) obj;
		if (relatedArticleSeq == null) {
			if (other.relatedArticleSeq != null)
				return false;
		} else if (!relatedArticleSeq.equals(other.relatedArticleSeq))
			return false;
		return true;
	}
	private static final long serialVersionUID = 2067461969604063306L;

	private Integer relatedArticleSeq;
	
	private CCGArticle baseArticle;
	
	private CCGArticle relatedArticle;
	
	private String relationType;
	private String relationTag;
	
	@Id	
	@Column(name = "relatedarticleseq", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getRelatedArticleSeq() {
		return relatedArticleSeq;
	}
	public void setRelatedArticleSeq(Integer relatedArticleSeq) {
		this.relatedArticleSeq = relatedArticleSeq;
	}
	
	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="basearticleID",referencedColumnName="articleID")
	public CCGArticle getBaseArticle() {
		return baseArticle;
	}
	public void setBaseArticle(CCGArticle baseArticle) {
		this.baseArticle = baseArticle;
	}
	
	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="relatedArticleID",referencedColumnName="articleID")
	public CCGArticle getRelatedArticle() {
		return relatedArticle;
	}
	public void setRelatedArticle(CCGArticle relatedArticle) {
		this.relatedArticle = relatedArticle;
	}
	@Column(name = "relationtype")
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	@Column(name = "relationtag")
	public String getRelationTag() {
		return relationTag;
	}
	public void setRelationTag(String relationTag) {
		this.relationTag = relationTag;
	}

}
