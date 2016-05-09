package com.ccg.common.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Category{
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	Integer categoryID;
	@SerializedName("text") 
	String categorytitle;
	Integer startposi;
	Integer endposi;
	Integer categoryseq;
	String type;
	@SerializedName("children")
	List<SubCategory> subCategories;
	
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
	public List<SubCategory> getSubCategories() {
		if(subCategories == null)
			subCategories = new ArrayList<SubCategory>();
		return subCategories;
	}
	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}
}
