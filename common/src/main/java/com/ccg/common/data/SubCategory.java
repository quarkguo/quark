package com.ccg.common.data;

public class SubCategory {
	
	Integer subcategoryID;
	Integer categoryID;
	String subcategorytitle;
	Integer startposi;
	Integer endposi;
	String type;
	
	public Integer getSubcategoryID() {
		return subcategoryID;
	}
	
	public Integer getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}

	public void setSubcategoryID(Integer subcategoryID) {
		this.subcategoryID = subcategoryID;
	}
	public String getSubcategorytitle() {
		return subcategorytitle;
	}
	public void setSubcategorytitle(String subcategorytitle) {
		this.subcategorytitle = subcategorytitle;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
