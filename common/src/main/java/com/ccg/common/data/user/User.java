package com.ccg.common.data.user;

import com.google.gson.annotations.SerializedName;

public class User {
	
	private Integer id;
	@SerializedName("text")
	private String useremail;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	
}
