package com.ccg.common.data;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
	@SerializedName("categoryID") 
	private String categoryId;	
	private String categoryTitle;
	private String articleTitle;
	private String articleId;
	private float score;
	boolean leaf=true;
	@SerializedName("text") 
	private String indexText;
	public String getIndexText() {
		return "Article:["+articleId+"]--"+"Category:["+categoryId+"]";
	}
	public void setIndexText(String indexText) {
		//this.indexText = "Article:["+articleId+"]--"+"Category:["+categoryId+"]";
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.indexText = "Article:["+articleId+"]--"+"Category:["+categoryId+"]";
		this.categoryId = categoryId;
	}
	public String getCategoryTitle() {
		return categoryTitle;
	}
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.indexText = "Article:["+articleId+"]--"+"Category:["+categoryId+"]";
		this.articleId = articleId;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
}
