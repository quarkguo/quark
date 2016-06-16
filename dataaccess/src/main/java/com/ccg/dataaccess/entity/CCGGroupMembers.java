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
@Table(name="ccggroupmembers")
public class CCGGroupMembers implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id	
	@Column(name = "ccggroupmemberseq", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getGroupmemberseq() {
		return groupmemberseq;
	}
	public void setGroupmemberseq(Integer groupmemberseq) {
		this.groupmemberseq = groupmemberseq;
	}
	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="groupID")
	public CCGUserGroup getGroup() {
		return group;
	}
	public void setGroup(CCGUserGroup group) {
		this.group = group;
	}
	
	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="userID",referencedColumnName="userID")
	public CCGUser getMember() {
		return member;
	}
	public void setMember(CCGUser member) {
		this.member = member;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupmemberseq == null) ? 0 : groupmemberseq.hashCode());
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
		CCGGroupMembers other = (CCGGroupMembers) obj;
		if (groupmemberseq == null) {
			if (other.groupmemberseq != null)
				return false;
		} else if (!groupmemberseq.equals(other.groupmemberseq))
			return false;
		return true;
	}


	private Integer groupmemberseq;
	private Date createdTS;
	
	@Column(name = "createdTS")	
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}


	private CCGUserGroup group;
	private CCGUser member;
}
