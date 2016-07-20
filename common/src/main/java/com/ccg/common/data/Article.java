package com.ccg.common.data;

import com.google.gson.annotations.SerializedName;

public class Article {
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
		Article other = (Article) obj;
		if (articleID == null) {
			if (other.articleID != null)
				return false;
		} else if (!articleID.equals(other.articleID))
			return false;
		return true;
	}
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
