package com.ccg.services.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.Category;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SubCategory;
import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.dao.api.CCGCategoryDAO;
import com.ccg.dataaccess.dao.api.CCGContentDAO;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGCategory;
import com.ccg.dataaccess.entity.CCGContent;
import com.ccg.dataaccess.entity.CCGSubcategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("CCGDBService")
public class CCGDBSerivceImpl implements CCGDBService {

	@Autowired	
	private CCGArticleDAO articleDAO;
	
	@Autowired
	private CCGContentDAO contentDAO;
	
	@Autowired
	private CCGCategoryDAO categoryDAO;
	
	@Override
	public void saveArticle(CCGArticle article) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getArticleCouont() {
		// TODO Auto-generated method stub
		return articleDAO.countAll();
	}
	@Override
	public String getArticleListJson() {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tmp=new ArrayList<Map<String,Object>>();
		List<CCGArticle> res=articleDAO.findAll();	
		
		for(CCGArticle art:res)
		{
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("text","Doc"+art.getTitle());
			map.put("cls","file");
			map.put("leaf",new Boolean(true));
			tmp.add(map);
		}
		
		return toJson(tmp);
	}
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	@Override
	public List<ArticleBasicInfo> getArticleBasicInfo() {
		
		List<CCGArticle> articles = articleDAO.findAll();
		List<ArticleBasicInfo> infoList = new ArrayList<ArticleBasicInfo>();
		for(CCGArticle article : articles){
			ArticleBasicInfo info = new ArticleBasicInfo();
			info.setArticleID(article.getArticleID());
			info.setTitle(article.getTitle());
			info.setArticleType(article.getArticleType());
			infoList.add(info);
		}
		return infoList;
	}
	@Override
	@Transactional(readOnly=true)
	public ArticleContent getArticleContent(Integer articleId) {
		ArticleContent articleContent = new ArticleContent();
		CCGContent content = articleDAO.findById(articleId).getContent();
		articleContent.setContent(content.getContent());
		articleContent.setContentID(content.getContentID());
		articleContent.setContentTitle(content.getContentTitle());
		articleContent.setLength(content.getLength());
		articleContent.setFileName(content.getFilename());
		return articleContent;
	}
	@Override
	@Transactional(readOnly=true)
	public List<Category> getCategoryByArticleId(Integer articleId) {
		List<Category> catList = new ArrayList<Category>();
		List<CCGCategory> ccgCatList = articleDAO.findById(articleId).getCategorylist();
		for(CCGCategory ccgCat : ccgCatList){
			Category cat = new Category();
			cat.setCategoryID(ccgCat.getCategoryID());
			cat.setCategorytitle(ccgCat.getCategorytitle());
			cat.setCategoryseq(ccgCat.getCategoryseq());
			cat.setStartposi(ccgCat.getStartposi());
			cat.setEndposi(ccgCat.getEndposi());
			cat.setStartPage(ccgCat.getStartpage());
			cat.setEndPage(ccgCat.getEndpage());
			if(ccgCat.getSubcategorylist() != null 
					&& ccgCat.getSubcategorylist().size() != 0){
				//List<SubCategory> subCatList = new ArrayList<SubCategory>();
				List<CCGSubcategory> ccgSubList = ccgCat.getSubcategorylist();
				for(CCGSubcategory ccgSub : ccgSubList){
					SubCategory subCat = new SubCategory();
					subCat.setSubcategoryID(ccgSub.getSubcategoryID());
					subCat.setSubcategorytitle(ccgSub.getSubcategorytitle());
					subCat.setStartposi(ccgSub.getStartposi());
					subCat.setEndposi(ccgSub.getEndposi());
					subCat.setStartPage(ccgSub.getStartpage());
					subCat.setEndPage(ccgSub.getEndpage());
					cat.getSubCategories().add(subCat);
				}
			}
			catList.add(cat);
		}		
		return catList;
	}
	@Override
	public CategoryContent getCategoryContentById(Integer categoryId) {
		CCGCategory ccgCategory = categoryDAO.findById(categoryId);
		CategoryContent catContent = new CategoryContent();
		catContent.setCategoryID(categoryId);
		catContent.setCategorytitle(ccgCategory.getCategorytitle());
		catContent.setCategorycontent(ccgCategory.getCategorycontent());
		
		return catContent;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ArticleContent getContentById(Integer contentId) {
		ArticleContent articleContent = new ArticleContent();
		CCGContent content = contentDAO.findById(contentId);
		articleContent.setContent(content.getContent());
		articleContent.setContentID(content.getContentID());
		articleContent.setContentTitle(content.getContentTitle());
		articleContent.setLength(content.getLength());
		articleContent.setFileName(content.getFilename());
		return articleContent;
	}

}
