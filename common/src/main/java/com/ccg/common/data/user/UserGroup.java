package com.ccg.common.data.user;

import com.google.gson.annotations.SerializedName;

public class UserGroup {
	private Integer groupId;
	private Integer ownerId;
	@SerializedName("text")
	private String groupname;
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	
}
