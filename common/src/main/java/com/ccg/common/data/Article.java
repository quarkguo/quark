package com.ccg.common.data;

import com.google.gson.annotations.SerializedName;

public class Article {
	private Integer articleID;
	@SerializedName("text") 
	private String articleTitle;
	private String icon="images/docs.jpg";
	private boolean leaf=true;
	
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
}
