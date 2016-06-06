package com.ccg.common.data;

public class SubCategoryContent {
	
	public Integer articleID;
	public Integer categoryID;
	private Integer subcategoryID;
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	public Integer getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}
	private String subcategorytitle;
	private String subcategorycontent;
	public Integer getSubcategoryID() {
		return subcategoryID;
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
	public String getSubcategorycontent() {
		return subcategorycontent;
	}
	public void setSubcategorycontent(String subcategorycontent) {
		this.subcategorycontent = subcategorycontent;
	}
}
