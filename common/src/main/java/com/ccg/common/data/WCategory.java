package com.ccg.common.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class WCategory{
	
	public boolean isLeaf() {
		return leaf;
	}
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	int searchCount;
	public int getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}
	String icon;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	Integer categoryID;
	String articleID;
	@SerializedName("text") 
	String categorytitle;
	Integer startposi;
	Integer endposi;
	Integer categoryseq;
	String type;
	
	Integer startPage;
	Integer endPage;
	
	@SerializedName("children")
	List<WCategory> subCategories;
	
	boolean leaf;
	
	public Integer getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}
	public String getCategorytitle() {
		return categorytitle;
	}
	public void setCategorytitle(String categorytitle) {
		this.categorytitle = categorytitle;
	}
	public Integer getStartposi() {
		return startposi;
	}
	public void setStartposi(Integer startposi) {
		this.startposi = startposi;
	}
	public Integer getEndposi() {
		return endposi;
	}
	public void setEndposi(Integer endposi) {
		this.endposi = endposi;
	}
	public Integer getCategoryseq() {
		return categoryseq;
	}
	public void setCategoryseq(Integer categoryseq) {
		this.categoryseq = categoryseq;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<WCategory> getSubCategories() {
		if(subCategories == null)
			subCategories = new ArrayList<WCategory>();
		return subCategories;
	}
	public void setSubCategories(List<WCategory> subCategories) {
		this.subCategories = subCategories;
	}
	public Integer getStartPage() {
		return startPage;
	}
	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}
	public Integer getEndPage() {
		return endPage;
	}
	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}
	
	
}
