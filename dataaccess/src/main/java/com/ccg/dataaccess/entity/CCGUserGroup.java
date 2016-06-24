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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@NamedQueries({
	 @NamedQuery(name="CCGUserGroup.findAll", query="select g from CCGUserGroup g"),
	 @NamedQuery(name="CCGUserGroup.countAll", query="select count(*) from CCGUserGroup")
	})
@Entity
@Table(name="ccgusergroup")
public class CCGUserGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccggroupID == null) ? 0 : ccggroupID.hashCode());
		return result;
	}

	@Id	
	@Column(name = "groupID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getCcggroupID() {
		return ccggroupID;
	}

	public void setCcggroupID(Integer ccggroupID) {
		this.ccggroupID = ccggroupID;
	}

	@Column(name = "groupname")
	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@Column(name = "createdTS")
	public Date getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}

	@Column(name = "lastupdate")
	public Date getLastupdateTS() {
		return lastupdateTS;
	}

	public void setLastupdateTS(Date lastupdateTS) {
		this.lastupdateTS = lastupdateTS;
	}

	 @ManyToOne(fetch=FetchType.LAZY )
	 @JoinColumn(name="ownerID",referencedColumnName="userID")
	public CCGUser getOwner() {
		return owner;
	}

	public void setOwner(CCGUser owner) {
		this.owner = owner;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CCGUserGroup other = (CCGUserGroup) obj;
		if (ccggroupID == null) {
			if (other.ccggroupID != null)
				return false;
		} else if (!ccggroupID.equals(other.ccggroupID))
			return false;
		return true;
	}

	private Integer ccggroupID;
	private String groupname;
	private Date createdTS;
	private Date lastupdateTS;
	
	private CCGUser owner;
	

}
