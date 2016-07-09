package com.ccg.common.data.user;

import com.google.gson.annotations.SerializedName;

public class User {
	
	private Integer userID;
	@SerializedName("text")
	private String useremail;
	boolean leaf=true;
	String icon="images/usericon.png";
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	
}
