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
@Table(name="ccguser")
public class CCGUser  implements Serializable{

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		CCGUser other = (CCGUser) obj;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
	private static final long serialVersionUID = 1L;

	private Integer userID;
	@Id	
	@Column(name = "userID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
	@Column(name = "useremail", nullable = false)
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	
	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "securitytoken")
	public String getSecurityToken() {
		return securityToken;
	}
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
	
	@Column(name = "createdTS")
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	
	@Column(name = "lastUpdateTS")
	public Date getLastUpdateTS() {
		return lastUpdateTS;
	}
	public void setLastUpdateTS(Date lastUpdateTS) {
		this.lastUpdateTS = lastUpdateTS;
	}
	
	@Column(name = "validated")
	public char getValidated() {
		return validated;
	}
	public void setValidated(char validated) {
		this.validated = validated;
	}
	private String useremail;
	private String password;
	private String securityToken;
	private Date createdTS;
	private Date lastUpdateTS;
	private char validated;
}
