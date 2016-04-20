package com.ccg.services.data;

import com.ccg.dataaccess.entity.CCGArticle;

public interface CCGDBService {

	public void saveArticle(CCGArticle article);
	public int getArticleCouont();
}
