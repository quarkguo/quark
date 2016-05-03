package com.ccg.article.services.data;

import java.util.ArrayList;
import java.util.List;

public class ArticleCategory {
	
	private Integer articleID;
	private List<Category> categories;
	
	public Integer getArticleID() {
		return articleID;
	}
	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}
	public List<Category> getCategories() {
		if(categories == null)
			categories = new ArrayList<Category>();
		
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
