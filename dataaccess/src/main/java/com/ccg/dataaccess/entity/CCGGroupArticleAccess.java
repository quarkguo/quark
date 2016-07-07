package com.ccg.dataaccess.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="ccggrouparticleAccess")
public class CCGGroupArticleAccess implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer grouparticleaccessseq;
	private CCGArticle article;
	
	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="articleID")
	public CCGArticle getArticle() {
		return article;
	}

	public void setArticle(CCGArticle article) {
		this.article = article;
	}

	private Date createdTS;
	
	private CCGUserGroup group;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grouparticleaccessseq == null) ? 0 : grouparticleaccessseq.hashCode());
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
		CCGGroupArticleAccess other = (CCGGroupArticleAccess) obj;
		if (grouparticleaccessseq == null) {
			if (other.grouparticleaccessseq != null)
				return false;
		} else if (!grouparticleaccessseq.equals(other.grouparticleaccessseq))
			return false;
		return true;
	}

	@Id	
	@Column(name = "ccggrouparticleAccessseq", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getGrouparticleaccessseq() {
		return grouparticleaccessseq;
	}

	public void setGrouparticleaccessseq(Integer grouparticleaccessseq) {
		this.grouparticleaccessseq = grouparticleaccessseq;
	}

		@Column(name = "createdTS")
	public Date getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}

	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="groupID")
	public CCGUserGroup getGroup() {
		return group;
	}

	public void setGroup(CCGUserGroup group) {
		this.group = group;
	}
	
}
