package com.ccg.common.data;

import java.util.ArrayList;
import java.util.List;

public class ArticleCategory {
	
	private Integer articleID;
	private List<WCategory> categories;
	
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	public List<WCategory> getCategories() {
		if(categories == null)
			categories = new ArrayList<WCategory>();
		
		return categories;
	}
	public void setCategories(List<WCategory> categories) {
		this.categories = categories;
	}
}
