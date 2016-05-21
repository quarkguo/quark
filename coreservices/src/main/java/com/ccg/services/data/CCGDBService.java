package com.ccg.services.data;

import java.util.List;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.ArticleMetaData;
import com.ccg.common.data.Category;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SubCategoryContent;
import com.ccg.dataaccess.entity.CCGArticle;

public interface CCGDBService {

	public void saveArticle(CCGArticle article);
	public int getArticleCouont();	
	public String getArticleListJson();
	
	public List<ArticleBasicInfo> getArticleBasicInfo();
	public ArticleContent getArticleContent(Integer articleId);
	public ArticleContent getContentById(Integer contentId);
	public List<Category> getCategoryByArticleId(Integer articleId);
	public CategoryContent getCategoryContentById(Integer categoryId);
	public SubCategoryContent getSubCategoryContentById(Integer subCategoryId);	
	public ArticleMetaData getArticleMetaDataByArticleId(Integer articleId);
	public void saveOrUpdateArticleMetaData(ArticleMetaData metadata);
}
