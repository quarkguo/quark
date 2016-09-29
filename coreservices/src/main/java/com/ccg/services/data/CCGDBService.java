package com.ccg.services.data;

import java.util.List;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.ArticleMetaData;
import com.ccg.common.data.WCategory;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SearchResult2;
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
	public List<WCategory> getCategoryByArticleId(Integer articleId);
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
	public void indexingArticle2(Integer articleId) throws Exception;
	public void indexingAll2() throws Exception;
	public CCGArticleInfo saveArticleInfo(CCGArticleInfo info);
	public List<WCategory> buildSearchCategory(List<SearchResult2> searchRes,String searchToken);
	public List<SearchResult2> filterDeletedResult( List<SearchResult2> searchResultList);
	public List<WCategory> getFlatCategory(int artileID,int page);
	public void indexMetadata(Integer articleId) throws Exception;
	public void indexMetadataAll() throws Exception;

}
