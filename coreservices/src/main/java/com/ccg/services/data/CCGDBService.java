package com.ccg.services.data;

import java.util.List;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.ArticleMetaData;
import com.ccg.common.data.Category;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SubCategoryContent;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGArticleInfo;
import com.ccg.dataaccess.entity.CCGCategory;

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
	//////
	public List<CCGCategory> getAllCategories();
	public CCGArticle getCCGArticleById(Integer articleId);
	public List<CCGArticle> getAllCCGArticle();
	public void deleteArticle(int articleID);
	public void indexingArticle(Integer articleId);
	public void indexingAll();
	
	public CCGArticleInfo saveArticleInfo(CCGArticleInfo info);

}
