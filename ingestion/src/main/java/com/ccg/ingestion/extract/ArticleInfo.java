package com.ccg.ingestion.extract;

import java.util.ArrayList;
import java.util.List;

public class ArticleInfo {
	String id;
	String title;
	String type="PDF"; // PDF, ...
	String content;
	String upperCaseContent=null;
	public String getUpperCaseContent() {
		if(upperCaseContent==null&&content!=null)
		{
			upperCaseContent=content.toUpperCase();
		}
		return upperCaseContent;
	}
	int pages;
	List<Category> categoryList ;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public List<Category> getCategoryList() {
		if(categoryList == null)
			categoryList = new ArrayList<Category>();
		return categoryList;
	}
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
}
