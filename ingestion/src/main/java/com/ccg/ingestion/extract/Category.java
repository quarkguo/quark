package com.ccg.ingestion.extract;

import java.util.ArrayList;
import java.util.List;

public class Category {
	String title;
	String content;
	int startPosition;
	int endPosition;
	int startPage;
	int endPage;
	List<Category> subCategory;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	public int getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public List<Category> getSubCategory() {
		if(subCategory == null)
			subCategory = new ArrayList<Category>();
		return subCategory;
	}
	public void setSubCategory(List<Category> subCategory) {
		this.subCategory = subCategory;
	}
}
